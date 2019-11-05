package ssu.cheesecake.blueberry.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ssu.cheesecake.blueberry.OnBackPressedListener;
import ssu.cheesecake.blueberry.R;

public class OptionFragment extends Fragment implements OnBackPressedListener {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d("blueee", "startFragment");
        View root = inflater.inflate(R.layout.fragment_option, container, false);
        Log.d("blueee", "LoadXml");

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(3).setChecked(true);
        return root;
    }

    @Override
    public void onBackPressed() {
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(0).setChecked(true);
        navHostFragment.getChildFragmentManager().popBackStack();
    }
}