package ssu.cheesecake.blueberry.menu;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import ssu.cheesecake.blueberry.OnBackPressedListener;
import ssu.cheesecake.blueberry.R;

public class MainActivity extends AppCompatActivity{
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));
        MenuItem item;
        navView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch(id){
                            case R.id.navigation_button_list:
                                navController.navigate(R.id.fragment_list);
                                break;
                            case R.id.navigation_button_group:
                                navController.navigate(R.id.fragment_group);
                                break;
                            case R.id.navigation_button_favorite:
                                navController.navigate(R.id.fragment_favorite);
                                break;
                            case R.id.navigation_button_option:
                                navController.navigate(R.id.fragment_option);
                                break;
                        }
                        return true;
                    }
                }
        );

    }

    @Override
    public void onBackPressed() {
        Fragment navHostFragment = getSupportFragmentManager().getFragments().get(0);
        Fragment nowFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);
        if(nowFragment != null){
            if(nowFragment instanceof OnBackPressedListener){
                ((OnBackPressedListener)nowFragment).onBackPressed();
            }
        }
    }

}

