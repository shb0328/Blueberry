package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ssu.cheesecake.blueberry.MainActivity;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.custom.BusinessCard;

public class DetailInfoFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private View root;

    private MainActivity.NowFragment nowFragment;

    private Button backButton;
    private ImageView imageView;
    private TextView nameView;
    private TextView numberView;
    private TextView emailView;
    private TextView companyView;
    private TextView addressView;
    private TextView groupView;
    private TextView timeView;
    private TextView pathView;

    private BusinessCard card;

    public DetailInfoFragment(BusinessCard card, MainActivity.NowFragment nowFragment) {
        this.card = card;
        this.nowFragment = nowFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.nowFragment = nowFragment;
        context = this.getContext();

        //MainActivity
        root = inflater.inflate(R.layout.fragment_detail_info, container, false);

        //TouchEventListener 설정
        backButton = root.findViewById(R.id.detail_info_button_back);
        backButton.setOnClickListener(this);

        imageView = root.findViewById(R.id.detail_info_image);
        nameView = root.findViewById(R.id.detail_info_name);
        numberView = root.findViewById(R.id.detail_info_phone_number);
        emailView = root.findViewById(R.id.detail_info_email);
        companyView = root.findViewById(R.id.detail_info_company);
        addressView = root.findViewById(R.id.detail_info_address);
        groupView = root.findViewById(R.id.detail_info_group);
        timeView = root.findViewById(R.id.detail_info_time);
        pathView = root.findViewById(R.id.detail_info_image_path);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(card.getImageUrl(), options);
        imageView.setImageBitmap(bitmap);
        nameView.setText(card.getName());
        numberView.setText(card.getPhoneNumber());
        emailView.setText(card.getEmail());
        companyView.setText(card.getCompany());
        addressView.setText(card.getAddress());
        groupView.setText(card.getGroup());
        timeView.setText(card.getTime());
        pathView.setText(card.getImageUrl());

        return root;
    }


    @Override
    public void onClick(View v) {
        if(v == backButton){
            if(nowFragment == MainActivity.NowFragment.DetailInfoFromFavorite)
                MainActivity.navController.navigate(R.id.fragment_favorite);
            else if(nowFragment == MainActivity.NowFragment.DetailInfoFromList)
                MainActivity.navController.navigate(R.id.fragment_list);
            else if(nowFragment == MainActivity.NowFragment.DetailInfoFromGroupList)
                MainActivity.navController.navigate(R.id.fragment_group);
            this.onDetach();
        }
    }

}



