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
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ssu.cheesecake.blueberry.R;


public class GroupFragment extends Fragment{


    String[] data={"group1", "group2", "group3", "group4"};
    Button[] myButton = new Button[data.length];

    GridLayout grid=null;


    private FloatingActionButton fab_add_2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_group, container, false);

        //gridlayout 설정
        grid=root.findViewById(R.id.GroupGrid_1);
        grid.setColumnCount(3);
        grid.setOrientation(GridLayout.HORIZONTAL);



//배열에 있는 버튼들 미리 만들어놓기
        for (int index = 0; index < data.length ; index++) {

            myButton[index] = new Button(getActivity()); //initialize the button here
            myButton[index].setText(data[index]);
            myButton[index].setHeight(100);
            myButton[index].setWidth(100);
            myButton[index].setTag(index);

            //버튼 누르면 fragment로 이동하도록 설정
            myButton[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
//gridlayout에 버튼 추가하기
           grid.addView(myButton[index]);
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

                        Button btn=new Button (getContext());
                        btn.setText(editGroup.getText());
                        grid.addView(btn);

                    }
                });

                groupBuild.setNegativeButton("No", null);
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

