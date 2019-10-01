package ssu.cheesecake.blueberry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import android.app.Fragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class OptionActivity extends AppCompatActivity{
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        androidx.fragment.app.Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));
        MenuItem item;
        navView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch(id){
                            case R.id.navigation_list:
                                navController.navigate(R.id.navigation_list);
                                break;
                            case R.id.navigation_camera:
                                Intent intent = new Intent(OptionActivity.this, CameraActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.navigation_gallery:
                                navController.navigate(R.id.navigation_gallery);
                                break;
                            case R.id.navigation_option:
                                navController.navigate(R.id.navigation_option);
                                break;
                        }
                        return true;
                    }
                }
        );

    }

}
