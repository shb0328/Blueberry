package ssu.cheesecake.blueberry.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ssu.cheesecake.blueberry.R;

public class OptionFragment extends Fragment implements View.OnClickListener{
    Button backupBtn;
    Button restoreBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_option, container, false);

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(3).setChecked(true);

        backupBtn = root.findViewById(R.id.button_backup);
        restoreBtn = root.findViewById(R.id.button_restore);
        backupBtn.setOnClickListener(this);
        restoreBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View button) {
        if (backupBtn.equals(button)) {
        }
        else if (restoreBtn.equals(button)) {
        }
    }
}