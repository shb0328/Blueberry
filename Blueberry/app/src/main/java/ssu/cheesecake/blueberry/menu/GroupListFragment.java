package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

import io.realm.Realm;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.camera.SmartCropActivity;
import ssu.cheesecake.blueberry.util.RealmController;
import ssu.cheesecake.blueberry.util.RecyclerViewAdapter;

public class GroupListFragment extends Fragment {
    private Context context;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    private RealmController.WhichResult whichResult;

    RealmController realmController;
    private Realm mRealm;

    private View root;

    private String groupName;

    public GroupListFragment(String groupName) {
        this.groupName = groupName;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(1).setChecked(true);

        //MainActivity
        root = inflater.inflate(R.layout.fragment_group_list, container, false);

        //Realm
        whichResult = RealmController.WhichResult.Group;
        Realm.init(this.getActivity());
        mRealm = Realm.getDefaultInstance();
        realmController = new RealmController(mRealm, whichResult, groupName);

        //Recycler View
        recyclerView = root.findViewById(R.id.recyclerView_group_list);
        adapter = new RecyclerViewAdapter(context, realmController.getCards(), realmController);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //TouchEventListener 설정
        adapter.setTouchListener(context, this.getActivity(), recyclerView, whichResult);

        return root;
    }
}



