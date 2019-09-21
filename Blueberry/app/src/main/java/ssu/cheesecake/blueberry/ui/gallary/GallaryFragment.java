package ssu.cheesecake.blueberry.ui.gallary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import ssu.cheesecake.blueberry.R;

public class GallaryFragment extends Fragment {

    private GallaryViewModel gallaryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gallaryViewModel =
                ViewModelProviders.of(this).get(GallaryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallary, container, false);
        final TextView textView = root.findViewById(R.id.text_gallary);
        gallaryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}