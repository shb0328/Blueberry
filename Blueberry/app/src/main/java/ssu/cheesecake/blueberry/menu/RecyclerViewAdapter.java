package ssu.cheesecake.blueberry.menu;

import android.app.Activity;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import java.io.File;
import java.util.List;

import ssu.cheesecake.blueberry.BusinessCard;
import ssu.cheesecake.blueberry.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder> {
    LayoutInflater inflater;
    List<BusinessCard> modelList;

    public RecyclerViewAdapter(Context context, List<BusinessCard> list) {
        inflater = LayoutInflater.from(context);
        modelList = list;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
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

        public MainViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            imageUrlTextView = itemView.findViewById(R.id.item_image_url);
            enNameTextView = itemView.findViewById(R.id.item_en_name);
            krNameTextView = itemView.findViewById(R.id.item_kr_name);
            phoneTextView = itemView.findViewById(R.id.item_phone_number);
            emailTextView = itemView.findViewById(R.id.item_email);
            companyTextView = itemView.findViewById(R.id.item_company);
            dateTextView = itemView.findViewById(R.id.item_date);
            imageUrlTextView = itemView.findViewById(R.id.item_image_url);
        }

        public void bindData(BusinessCard object) {
            //image 출력
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://blueberry-cheesecake-ssu.appspot.com/");
            StorageReference storageRef = storage.getReference();
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            //String path = user.getDisplayName() + "_" + user.getUid();
            String path = "sample";
            String fileName = object.getImageUrl();
            StorageReference pathReference = storageRef.child(path + "/" + fileName);

            //Image Loading
            File file = null;
            try {
                //Local에 Image 저장할 경로 지정
                File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
                file = new File(dir, fileName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //Local에 저장된 Image를 ImageView에 출력
            imageUrlTextView.setText(file.getAbsolutePath());
            imageView.setImageURI(Uri.parse(file.getAbsolutePath()));


            enNameTextView.setText(object.getEnName());
            krNameTextView.setText(object.getKrName());
            phoneTextView.setText(object.getPhoneNumber());
            emailTextView.setText(object.getEmail());
            companyTextView.setText(object.getCompany());
            dateTextView.setText(object.getTime());
        }
    }

    //RecyclerView에 TouchListener 설정 함수
     public static void setTouchListener(final Context context, Activity activity, RecyclerView recyclerView) {
        RecyclerTouchListener onTouchListener = new RecyclerTouchListener(activity, recyclerView);
        onTouchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        Toast toast = Toast.makeText(context, "RowClick!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                })
                .setSwipeOptionViews(R.id.item_button_favorite, R.id.item_button_edit, R.id.item_button_delete)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {

                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        if (viewID == R.id.item_button_favorite) {
                            Toast toast = Toast.makeText(context, "Favorite!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (viewID == R.id.item_button_edit) {
                            Toast toast = Toast.makeText(context, "Edit!", Toast.LENGTH_SHORT);
                            toast.show();
                        } else if (viewID == R.id.item_button_delete) {
                            Toast toast = Toast.makeText(context, "Delete!", Toast.LENGTH_SHORT);
                            toast.show();
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
                Log.d(TAG, "recyclerview: swipe&Refresh");

            }
        });
    }
}