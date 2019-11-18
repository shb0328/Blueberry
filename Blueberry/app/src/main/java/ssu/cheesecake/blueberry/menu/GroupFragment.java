package ssu.cheesecake.blueberry.menu;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ssu.cheesecake.blueberry.R;


public class GroupFragment extends Fragment{


    String[] data={"group1", "group2", "group3", "group4"};
    ArrayList<String> Group;

//    GridView gridView_group;
    private FloatingActionButton fab_add_2;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group, container, false);


        LinearLayout linear=new LinearLayout(getActivity());
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        linear.setOrientation(LinearLayout.VERTICAL);


//        gridView_group= root.findViewById(R.id.GroupGrid_1);


        for(int i=0;i<data.length;i++) {

            Button btn=new Button (getActivity());
            btn.setId(i+1);
            btn.setText("우왕");
            btn.setLayoutParams(params);

        }


        //fab버튼 누르면 dialog 뜸
        fab_add_2 = root.findViewById(R.id.fab_add_2);
        fab_add_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder groupBuild=new AlertDialog.Builder(getActivity());
                groupBuild.setTitle("그룹 추가");
                groupBuild.setMessage("그룹 이름을 선택하시오");

                final EditText editGroup=new EditText(getActivity());
                groupBuild.setView(editGroup);
                groupBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });
                groupBuild.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                groupBuild.show();
            }
        });


        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(1).setChecked(true);
        return root;


    }






}

