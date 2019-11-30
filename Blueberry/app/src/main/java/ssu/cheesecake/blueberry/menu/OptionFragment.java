package ssu.cheesecake.blueberry.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import io.realm.Realm;
import ssu.cheesecake.blueberry.FirebaseHelper;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.RealmController;

public class OptionFragment extends Fragment implements View.OnClickListener {
    Button backupBtn;
    Button restoreBtn;
    Button signOutBtn;
    Switch autoSave;
    RealmController realmController;


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
            FirebaseHelper firebaseHelper = new FirebaseHelper(this.getContext());
            firebaseHelper.Backup(realmController);
        } else if (restoreBtn.equals(button)) {
            FirebaseHelper firebaseHelper = new FirebaseHelper(this.getContext());
            firebaseHelper.Restore(realmController);
        } else if(signOutBtn.equals(button)){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("로그아웃");
            alert.setMessage("로그아웃하시겠습니까?");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    getActivity().finish();
                }
            });
            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
        else if(autoSave.equals(button)){
            realmController.changeAutoSave();
        }
    }
}