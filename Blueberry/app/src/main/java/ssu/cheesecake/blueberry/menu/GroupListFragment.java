package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import ssu.cheesecake.blueberry.MainActivity;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.RealmController;
import ssu.cheesecake.blueberry.util.RecyclerViewAdapter;

public class GroupListFragment extends Fragment implements View.OnClickListener {
    private Context context;

    private TextView groupNameTextView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ImageView backButton;

    private RealmController.WhichResult whichResult;

    RealmController realmController;
    private Realm mRealm;

    private View root;

    private String groupName;

    public GroupListFragment(String groupName) {
        this.groupName = groupName;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity.nowFragment = MainActivity.NowFragment.GroupList;
        context = this.getContext();
        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(1).setChecked(true);

        //MainActivity
        root = inflater.inflate(R.layout.fragment_group_list, container, false);

        groupNameTextView = root.findViewById(R.id.group_name_text);
        groupNameTextView.setText(groupName);
        groupNameTextView.setBackgroundColor(getResources().getColor(R.color.colorBlueBerry,getActivity().getTheme()));
        groupNameTextView.setTextColor(getResources().getColor(R.color.colorWhite,getActivity().getTheme()));

        //Realm
        whichResult = RealmController.WhichResult.Group;
        Realm.init(this.getActivity());
        mRealm = Realm.getDefaultInstance();
        realmController = new RealmController(mRealm, whichResult, groupName);


        //Recycler View
        recyclerView = root.findViewById(R.id.recyclerView_group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new RecyclerViewAdapter(context, realmController.getCards(), realmController);
        recyclerView.setAdapter(adapter);

        //TouchEventListener 설정
        adapter.setTouchListener(context, this.getActivity(), this, recyclerView, whichResult);
        backButton = root.findViewById(R.id.group_list_button_back);
        backButton.setBackgroundColor(getResources().getColor(R.color.colorBlueBerry,getActivity().getTheme()));
        backButton.setImageDrawable(getResources().getDrawable(R.drawable.back,getActivity().getTheme()));
//        backButton.setTextColor(getResources().getColor(R.color.colorWhite,getActivity().getTheme()));
        backButton.setOnClickListener(this);

        return root;
    }


    @Override
    public void onClick(View v) {
        if(v == backButton){
            MainActivity.navController.navigate(R.id.fragment_group);
            this.onDetach();
        }
    }

}



