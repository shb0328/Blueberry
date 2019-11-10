package ssu.cheesecake.blueberry.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ssu.cheesecake.blueberry.BusinessCard;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.camera.SmartCropActivity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener;
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener;

public class ListFragment extends Fragment implements View.OnClickListener, RecyclerTouchListener.RecyclerTouchListenerHelper {
    public static Context context;
    private static final int CAMREQUESTCODE = 1;
    private static final int GALLERYREQUESTCODE = 2;

    private DatabaseReference myRef;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private OnActivityTouchListener touchListener;

    private ArrayList<BusinessCard> list = new ArrayList<BusinessCard>();

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_add, fab_camera, fab_gallery;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getContext();
        //Navigation Menu bar Icon 변경
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        BottomNavigationView navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        Menu menu = navView.getMenu();
        menu.getItem(0).setChecked(true);

        //MainActivity
        root = inflater.inflate(R.layout.fragment_list, container, false);

        //Firebase
        FirebaseAuth firebaseAuth = null;
        FirebaseUser user = null;
        while (user == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
        }
        String path = user.getDisplayName() + "_" + user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(path);

        //Recycler View
        recyclerView = root.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(context, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //TouchEventListener 설정
        RecyclerViewAdapter.setTouchListener(context, this.getActivity(), recyclerView);

        //firebase에 변동이 있을 시
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        BusinessCard object = postSnapshot.getValue(BusinessCard.class);
                        list.add(object);
                        adapter = new RecyclerViewAdapter(context, list);
                        //mAdapter.setData(list);
                        recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button addBtn = root.findViewById(R.id.add_button);
        addBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                BusinessCard object = new BusinessCard("EnName", "한글이름", "010-1234-5678", "email@gmail.com", "Company", "sample1.jpg");
                object.postFirebaseDatabase();
            }
        });

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

    private List<BusinessCard> getData() {
        List<BusinessCard> list = new ArrayList<>();
        return list;
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
                break;
            case R.id.fab_gallery:
                anim();
                Intent intent2 = new Intent(this.getActivity(), SmartCropActivity.class);
                intent2.putExtra("key", GALLERYREQUESTCODE);
                startActivity(intent2);
                break;
        }
        Toast.makeText(this.getActivity(), "Hello!", Toast.LENGTH_LONG);
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



