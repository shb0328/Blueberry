package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import io.realm.Realm;
import ssu.cheesecake.blueberry.custom.BusinessCard;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.RealmController;
import ssu.cheesecake.blueberry.camera.SmartCropActivity;
import ssu.cheesecake.blueberry.util.RecyclerViewAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

public class ListFragment extends Fragment implements View.OnClickListener, RecyclerTouchListener.RecyclerTouchListenerHelper {
    public static Context context;
    private static final int CAMREQUESTCODE = 1;
    private static final int GALLERYREQUESTCODE = 2;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private OnActivityTouchListener touchListener;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_add, fab_camera, fab_gallery;

    private EditText editTextSearch;
    private Button buttonSearch;
    private String searchStr;
    private RealmController.WhichResult whichResult;

    private Realm mRealm;

    private View root;

    RealmController realmController;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(0).setChecked(true);

        //MainActivity
        root = inflater.inflate(R.layout.fragment_list, container, false);

        //Realm
        whichResult = RealmController.WhichResult.List;
        Realm.init(this.getActivity());
        mRealm = Realm.getDefaultInstance();
        realmController = new RealmController(mRealm, whichResult);

        editTextSearch = root.findViewById(R.id.edit_text_search);
        buttonSearch = root.findViewById(R.id.button_search);
        editTextSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    buttonSearch.callOnClick();
                    return true;
                }
                return false;
            }
        });
        buttonSearch.setFocusableInTouchMode(true);
        buttonSearch.requestFocus();
        searchStr = null;
        buttonSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchStr = editTextSearch.getText().toString();
                whichResult = RealmController.WhichResult.Search;
                realmController = new RealmController(mRealm, whichResult, searchStr);
                recyclerView.setAdapter(new RecyclerViewAdapter(context, realmController.getCards(), realmController));
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }
        });

        //Recycler View
        recyclerView = root.findViewById(R.id.recyclerView_list);
        adapter = new RecyclerViewAdapter(context, realmController.getCards(), realmController);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //TouchEventListener 설정
        RecyclerViewAdapter.setTouchListener(context, this.getActivity(), recyclerView);


        //Floating Action Button
        fab_open = AnimationUtils.loadAnimation(this.getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this.getContext(), R.anim.fab_close);

        fab_add = root.findViewById(R.id.fab_add);
        fab_camera = root.findViewById(R.id.fab_camera);
        fab_gallery = root.findViewById(R.id.fab_gallery);
        fab_add.setOnClickListener(this);
        fab_camera.setOnClickListener(this);
        fab_gallery.setOnClickListener(this);

        return root;
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    //Fab Button Click Listener
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab_add:
                anim();
                break;
            case R.id.fab_camera:
                anim();
                Intent intent = new Intent(this.getActivity(), SmartCropActivity.class);
                intent.putExtra("key", CAMREQUESTCODE);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.fab_gallery:
                anim();
                Intent intent2 = new Intent(this.getActivity(), SmartCropActivity.class);
                intent2.putExtra("key", GALLERYREQUESTCODE);
                startActivity(intent2);
                getActivity().finish();
                break;
        }
    }

    //Fab Button Animation
    public void anim() {
        if (isFabOpen) {
            fab_add.setImageResource(R.drawable.icon_add_simple);
            fab_camera.startAnimation(fab_close);
            fab_gallery.startAnimation(fab_close);
            fab_camera.setClickable(false);
            fab_gallery.setClickable(false);
            isFabOpen = false;
        } else {
            fab_add.setImageResource(R.drawable.icon_close_simple);
            fab_camera.startAnimation(fab_open);
            fab_gallery.startAnimation(fab_open);
            fab_camera.setClickable(true);
            fab_gallery.setClickable(true);
            isFabOpen = true;
        }
    }

}



