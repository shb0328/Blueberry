package ssu.cheesecake.blueberry.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.RealmController;
import ssu.cheesecake.blueberry.util.RecyclerViewAdapter;

public class FavoriteFragment extends Fragment{
    private RealmController realmController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(2).setChecked(true);

        Realm.init(this.getActivity());
        Realm mRealm = Realm.getDefaultInstance();
        realmController = new RealmController(mRealm, RealmController.WhichResult.Favorite);

        //Recycler View
        recyclerView = root.findViewById(R.id.recyclerView_favorite);
        adapter = new RecyclerViewAdapter(this.getContext(), realmController.getCards(), realmController);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //TouchEventListener 설정
        adapter.setTouchListener(this.getContext(), this.getActivity(), recyclerView, RealmController.WhichResult.Favorite);


        return root;
    }

}