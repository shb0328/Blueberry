package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import io.realm.Realm;
import ssu.cheesecake.blueberry.GroupAdapter;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.RealmController;


public class GroupFragment extends Fragment {

    private Context context;

    private GridView groupView;
    private FloatingActionButton addGroupFab;
    private Realm realm;

    private RealmController realmController;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group, container, false);
        groupView = root.findViewById(R.id.groupView);
        addGroupFab = root.findViewById(R.id.addGroupFab);

        context = getActivity();

        realm = Realm.getDefaultInstance();
        realmController = new RealmController(realm, RealmController.WhichResult.Group);

        //fab버튼 누르면 dialog 뜸
        addGroupFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder groupAddBuilder = new AlertDialog.Builder(getActivity());
                groupAddBuilder.setTitle("그룹 추가");
                groupAddBuilder.setMessage("그룹 이름을 입력하시오");

                final EditText editGroup = new EditText(getActivity());
                groupAddBuilder.setView(editGroup);
                groupAddBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String groupName = editGroup.getText().toString();
                        if(groupName.length() < 2){
                            Toast.makeText(context,"그룹 이름은  2글자 이상만 가능합니다.",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean isSuccess = realmController.addCustomGroup(groupName);
                        if(isSuccess) {
                            GroupAdapter groupAdapter = new GroupAdapter(context,R.layout.group_item,realmController.getGroups(),new OnGroupDeleteListener());
                            groupView.setAdapter(groupAdapter);
                            return;
                        }
                        Toast.makeText(context,"이미 존재하는 그룹 이름 입니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                groupAddBuilder.setNegativeButton("No", null);
                groupAddBuilder.show();
            }
        });

        GroupAdapter groupAdapter = new GroupAdapter(context,R.layout.group_item,realmController.getGroups(),new OnGroupDeleteListener());
        groupView.setAdapter(groupAdapter);

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(1).setChecked(true);
        return root;

    }


    class OnGroupDeleteListener implements View.OnTouchListener{
        private long touchDownTime, touchUpTime;

        @Override
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                touchDownTime = motionEvent.getDownTime();
                view.getParent().requestDisallowInterceptTouchEvent(true);
                Log.i("time", "down:" + touchDownTime);
            }
            if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                touchUpTime = motionEvent.getEventTime();
                Log.i("time", "up" + touchUpTime);
                if (touchUpTime - touchDownTime > 1000) {
                    touchDownTime = 0;
                    touchUpTime = 0;
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                    builder.setTitle("그룹 삭제");
                    builder.setMessage("그룹을 삭제하시겠습니까?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            realmController.deleteCustomGroup(((TextView)view).getText().toString());
                            GroupAdapter groupAdapter = new GroupAdapter(context,R.layout.group_item,realmController.getGroups(),new OnGroupDeleteListener());
                            groupView.setAdapter(groupAdapter);
                        }
                    });
                    builder.setNegativeButton("No", null);
                    builder.show();
                }else{
                    Toast.makeText(context,"onClicked!!!",Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        }

    }


}

