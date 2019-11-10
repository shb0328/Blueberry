package ssu.cheesecake.blueberry.menu;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ssu.cheesecake.blueberry.R;

public class FavoriteFragment extends Fragment implements View.OnTouchListener {
    TextView textView;
    Animation item_menu_open;
    float initX;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(2).setChecked(true);

        return root;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            Log.d("EVENT!!", "Down!");
            initX = event.getRawX();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){
            Log.d("EVENT!!", "Move!");
            if(initX > event.getRawX()){
                Log.d("EVENT!!", "MoveIf!");
                textView.startAnimation(item_menu_open);
            }
        }
        return true;
    }
}