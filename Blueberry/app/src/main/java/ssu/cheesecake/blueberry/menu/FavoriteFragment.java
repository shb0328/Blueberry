package ssu.cheesecake.blueberry.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import ssu.cheesecake.blueberry.MainActivity;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.RealmController;
import ssu.cheesecake.blueberry.util.RecyclerViewAdapter;

public class FavoriteFragment extends Fragment{
    private RealmController realmController;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MainActivity.nowFragment = MainActivity.NowFragment.Favorite;
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
        adapter.setTouchListener(this.getContext(), this.getActivity(), this, recyclerView, RealmController.WhichResult.Favorite);


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            //EditActivity
            if (requestCode == 1004) {
                int position = intent.getIntExtra("position", -1);
                if (position != -1)
                    recyclerView.getAdapter().notifyItemChanged(position);
            }
        }
    }

}