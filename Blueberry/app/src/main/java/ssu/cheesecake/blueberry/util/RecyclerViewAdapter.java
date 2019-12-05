package ssu.cheesecake.blueberry.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.io.File;
import java.util.Vector;

import ssu.cheesecake.blueberry.custom.BusinessCard;
import ssu.cheesecake.blueberry.EditActivity;
import ssu.cheesecake.blueberry.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder> {
    LayoutInflater inflater;
    Vector<BusinessCard> cards;
    Context context;
    static RealmController realmController;

    public Vector<BusinessCard> getCards(){ return cards;}

    public RecyclerViewAdapter(Context context, Vector<BusinessCard> cards, RealmController realmController) {
        this.inflater = LayoutInflater.from(context);
        this.cards = cards;
        this.realmController = realmController;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(cards.get(position));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView imageUrlTextView;
        TextView enNameTextView;
        TextView krNameTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView companyTextView;
        TextView dateTextView;
        ImageView favoriteBtnImage;

        public MainViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            enNameTextView = itemView.findViewById(R.id.item_en_name);
            krNameTextView = itemView.findViewById(R.id.item_kr_name);
            phoneTextView = itemView.findViewById(R.id.item_phone_number);
            emailTextView = itemView.findViewById(R.id.item_email);
            companyTextView = itemView.findViewById(R.id.item_company);
            dateTextView = itemView.findViewById(R.id.item_date);
            favoriteBtnImage = itemView.findViewById(R.id.item_button_favorite_imageview);
            context = itemView.getContext();
        }

        public void bindData(BusinessCard object) {
            //Image Loading

            File imageFile = null;
            try {
                //Local에 Image 저장할 경로 지정
                imageFile = new File(object.getImageUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imageFile != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                imageView.setImageBitmap(bitmap);
            }

            //Local에 저장된 Image를 ImageView에 출력
//            imageUrlTextView.setText(file.getAbsolutePath());
//            imageView.setImageURI(Uri.parse(file.getAbsolutePath()));

            enNameTextView.setText(object.getEnName());
            krNameTextView.setText(object.getKrName());
            phoneTextView.setText(object.getPhoneNumber());
            emailTextView.setText(object.getEmail());
            companyTextView.setText(object.getCompany());
            dateTextView.setText(object.getTime());

            if(object.getIsFavorite()){
                favoriteBtnImage.setColorFilter(Color.argb(255, 255, 255, 0));
            }
        }
    }

    //RecyclerView에 TouchListener 설정 함수
     public static void setTouchListener(final Context context, final Activity activity, final RecyclerView recyclerView) {
        final RecyclerTouchListener onTouchListener = new RecyclerTouchListener(activity, recyclerView);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.item_button_favorite, R.id.item_button_edit, R.id.item_button_delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {

                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        BusinessCard card = ((RecyclerViewAdapter)(recyclerView.getAdapter())).getCards().get(position);
                        if (viewID == R.id.item_button_favorite) {
                            if(realmController != null){
                                realmController.changeIsFavorite(card);
                                recyclerView.getAdapter().notifyItemChanged(position);
                            }
                        } else if (viewID == R.id.item_button_edit) {
                            Intent intent = new Intent(context, EditActivity.class);
                            intent.putExtra("mode", "edit");
                            intent.putExtra("card", card);
                            activity.startActivity(intent);
                        } else if (viewID == R.id.item_button_delete) {
                            if(realmController != null){
                                realmController.deleteBusinessCard(card);
                                recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(onTouchListener);
        return;
    }

    public static void SetRefresh(final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //새로 고침할 작업 나중에 추가하기
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
