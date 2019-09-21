package ssu.cheesecake.blueberry.ui.option;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import ssu.cheesecake.blueberry.R;

public class Optionragment extends Fragment {

    private OptionViewModel optionViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionViewModel =
                ViewModelProviders.of(this).get(OptionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_option, container, false);
        final TextView textView = root.findViewById(R.id.text_option);
        optionViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}