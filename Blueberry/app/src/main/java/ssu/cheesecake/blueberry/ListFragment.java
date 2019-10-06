package ssu.cheesecake.blueberry;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ssu.cheesecake.blueberry.R;

import static android.os.Build.ID;

public class ListFragment extends Fragment{
    BitmapDrawable bitmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        final ScrollView scrollView = (ScrollView)root.findViewById(R.id.listScrollView);
        final LinearLayout linearLayout = (LinearLayout) root.findViewById(R.id.listLinearLayout);

        for (int i = 1; i < 10; i++) {
            String resName = "@drawable/sample" + (char)(i+'0');
            String packName = this.getActivity().getPackageName();
            int resId = getResources().getIdentifier(resName, "id", packName);
            ImageView imageView = new ImageView(this.getActivity());
            imageView.setImageResource(resId);
            linearLayout.addView(imageView);
            TextView textView = new TextView(this.getActivity());
            String name = "Full Name";
            String phone = "Phone Number";
            String text = "Name:" + name + "\nPhone: " + phone + '\n';
            textView.setText(text);
            textView.setTextSize(25);
            linearLayout.addView(textView);
        }
        DataObject tempObject = new DataObject("cpuman7@gmail.com", "Hansu", "Kim", "010-4537-9662", "cpuman7@gmail.com", "SoongSil Unv.");
        tempObject.postFirebaseDatabase();

        return root;
    }

}