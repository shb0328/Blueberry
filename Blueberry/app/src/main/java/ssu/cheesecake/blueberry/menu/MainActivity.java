package ssu.cheesecake.blueberry.menu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import ssu.cheesecake.blueberry.R;

public class MainActivity extends AppCompatActivity {
    NavController navController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));
        navView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        switch (id) {
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
        final Activity root = this;
        new AlertDialog.Builder(root)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_exit_title)
                .setMessage(R.string.dialog_exit_question)
                .setPositiveButton(R.string.dialog_exit_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        root.finish();
                    }

                })
                .setNegativeButton(R.string.dialog_exit_no, null)
                .show();
    }

}

