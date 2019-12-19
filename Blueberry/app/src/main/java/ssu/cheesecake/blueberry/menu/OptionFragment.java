package ssu.cheesecake.blueberry.menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.FileNotFoundException;
import java.util.HashMap;

import io.realm.Realm;
import ssu.cheesecake.blueberry.LoginActivity;
import ssu.cheesecake.blueberry.MainActivity;
import ssu.cheesecake.blueberry.custom.BusinessCard;
import ssu.cheesecake.blueberry.util.FirebaseHelper;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.RealmController;

public class OptionFragment extends Fragment implements View.OnClickListener {
    Context context;

    Button backupBtn;
    Button restoreBtn;
    Button signOutBtn;
    com.google.android.material.switchmaterial.SwitchMaterial autoSave;
    RealmController realmController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.nowFragment = MainActivity.NowFragment.Option;
        View root = inflater.inflate(R.layout.fragment_option, container, false);
        context = this.getContext();

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(3).setChecked(true);

        backupBtn = root.findViewById(R.id.button_backup);
        restoreBtn = root.findViewById(R.id.button_restore);
        signOutBtn = root.findViewById(R.id.button_signOut);
        autoSave = root.findViewById(R.id.auto_save);
        backupBtn.setOnClickListener(this);
        restoreBtn.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
        Realm.init(this.getActivity());
        realmController = new RealmController(Realm.getDefaultInstance(), RealmController.WhichResult.List);
        if(realmController.getIsAutoSave().getIsAutoSave()){
            autoSave.setChecked(true);
        }
        else autoSave.setChecked(false);
        autoSave.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View button) {
        if (backupBtn.equals(button)) {
            final FirebaseHelper firebaseHelper = new FirebaseHelper(this.getContext());
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("백업");
            alert.setMessage("백업을 실행하시겠습니까?");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseHelper.Backup(realmController);
                        }
                    });
            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            alert.setIcon(getResources().getDrawable(R.drawable.button_icon_backup_black, null));
            alert.show();
        } else if (restoreBtn.equals(button)) {
            final FirebaseHelper firebaseHelper = new FirebaseHelper(this.getContext());
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle("복원");
            alert.setMessage("복원을 실행하시겠습니까? 현재 단말기에 저장된 정보는 모두 지워지고, 백업된 내용이 들어옵니다.");
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    firebaseHelper.Restore(realmController);
                }
            });
            alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            alert.setIcon(getResources().getDrawable(R.drawable.button_icon_restore_black, null));
            alert.show();
        } else if(signOutBtn.equals(button)){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("로그아웃");
            alert.setMessage("로그아웃하시겠습니까?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(context, LoginActivity.class));
                    getActivity().finish();
                }
            });
            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.setIcon(getResources().getDrawable(R.drawable.button_icon_signout, null));
            alert.show();
        }
        else if(autoSave.equals(button)){
            realmController.changeAutoSave();
        }
    }
}