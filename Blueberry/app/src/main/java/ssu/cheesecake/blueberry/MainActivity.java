package ssu.cheesecake.blueberry;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import io.realm.Realm;
import ssu.cheesecake.blueberry.util.RealmController;

public class MainActivity extends AppCompatActivity {
    public enum NowFragment{List, Group, Favorite, Option, DetailInfoFromList, DetailInfoFromFavorite, DetailInfoFromGroupList, GroupList}

    public static NavController navController;
    public static NowFragment nowFragment = NowFragment.List;

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

        //주소록 AutoSave Realm에서 읽어옴
        //TODO:잠시주석
        Realm.init(this);
        initRealm();
    }

    //앱 설치 후 첫 로그인 시 초기화 함수
    public void initRealm(){
        RealmController realmController = new RealmController(Realm.getDefaultInstance(), RealmController.WhichResult.List);
        realmController.initializeAutoSave(true);
        realmController.initializeGroup();

    }

    @Override
    public void onBackPressed() {
        final Activity root = this;
        if(nowFragment == NowFragment.GroupList){
            navController.navigate(R.id.fragment_group);
        }
        else if(nowFragment == NowFragment.DetailInfoFromList){
            navController.navigate(R.id.fragment_list);
        }
        else if(nowFragment == NowFragment.DetailInfoFromFavorite){
            navController.navigate(R.id.fragment_favorite);
        }
        else if(nowFragment == NowFragment.DetailInfoFromGroupList){
            navController.navigate(R.id.fragment_group);
        }
        else{
            new AlertDialog.Builder(root)
                    .setIcon(getResources().getDrawable(R.drawable.icon_warning, null))
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

}

