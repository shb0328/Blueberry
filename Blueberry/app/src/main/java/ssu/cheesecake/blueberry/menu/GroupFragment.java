package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import io.realm.Realm;
import io.realm.RealmResults;
import ssu.cheesecake.blueberry.GroupAdapter;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.custom.CustomGroup;


public class GroupFragment extends Fragment {

    private Context context;

    private GridView groupView;
    private FloatingActionButton addGroupFab;
    private Realm realm;
    private ArrayList<CustomGroup> groupArrayList;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        groupView = root.findViewById(R.id.groupView);
        addGroupFab = root.findViewById(R.id.addGroupFab);

        context = getActivity();

        if(groupArrayList == null){
            groupArrayList = new ArrayList<>();
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<CustomGroup> results = realm.where(CustomGroup.class).findAll();
                    for(CustomGroup vo : results){
                        groupArrayList.add(vo);
                    }
                }
            });
        }

        //fab버튼 누르면 dialog 뜸
        addGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder groupBuild = new AlertDialog.Builder(getActivity());
                groupBuild.setTitle("그룹 추가");
                groupBuild.setMessage("그룹 이름을 입력하시오");

                final EditText editGroup = new EditText(getActivity());
                groupBuild.setView(editGroup);
                groupBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                CustomGroup vo = realm.createObject(CustomGroup.class);
                                vo.setGroupName(editGroup.getText().toString());
                                groupArrayList.add(vo);
                            }
                        });

                        GroupAdapter groupAdapter = new GroupAdapter(context,R.layout.group_item,groupArrayList);
                        groupView.setAdapter(groupAdapter);
                    }
                });

                groupBuild.setNegativeButton("No", null);
                groupBuild.show();
            }
        });

        GroupAdapter groupAdapter = new GroupAdapter(context,R.layout.group_item,groupArrayList);
        groupView.setAdapter(groupAdapter);

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(1).setChecked(true);
        return root;

    }


}

