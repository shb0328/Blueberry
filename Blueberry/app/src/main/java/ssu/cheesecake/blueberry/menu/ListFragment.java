package ssu.cheesecake.blueberry.menu;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ssu.cheesecake.blueberry.BusinessCard;
import ssu.cheesecake.blueberry.EditActivity;
import ssu.cheesecake.blueberry.OnBackPressedListener;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.camera.SmartCropActivity;

import java.io.File;
import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_CANCELED;

public class ListFragment extends Fragment implements OnBackPressedListener, View.OnClickListener{

    private static final int CAMREQUESTCODE = 1;
    private static final int GALLERYREQUESTCODE = 2;

    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private ArrayList<BusinessCard> list = new ArrayList<BusinessCard>();

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab_add, fab_camera, fab_gallery;

    private View root;

        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        while(user == null) {
            firebaseAuth = FirebaseAuth.getInstance();
            user = firebaseAuth.getCurrentUser();
        }

        String path = user.getDisplayName() + "_" + user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(path);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    BusinessCard object = postSnapshot.getValue(BusinessCard.class);
                    list.add(object);
                    adapter.setData(list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button addBtn = root.findViewById(R.id.add_button);
        addBtn.setOnClickListener(new Button.OnClickListener(){
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

    //back button 누를 시 App 종료
    @Override
    public void onBackPressed() {
        Log.d("blueee", "List Fragment BackPressed!!");
        final Activity root = this.getActivity();
        new AlertDialog.Builder(root)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_exit_title)
                .setMessage(R.string.dialog_exit_question)
                .setPositiveButton(R.string.dialog_exit_yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        root.finish();
                    }

                })
                .setNegativeButton(R.string.dialog_exit_no, null)
                .show();
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
                intent.putExtra("key",CAMREQUESTCODE);
                startActivity(intent);
                break;
            case R.id.fab_gallery:
                anim();
                Intent intent2 = new Intent(this.getActivity(), SmartCropActivity.class);
                intent2.putExtra("key",GALLERYREQUESTCODE);
                startActivity(intent2);
                break;
        }
        Toast.makeText(this.getActivity(), "Hello!", Toast.LENGTH_LONG);
    }

    //Fab Button Animation
    public void anim(){
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



//RecyclerView Adapter Class
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<BusinessCard> mData = null;

    //RecyclerView Adapter Class 내에서 ViewHolder Class 선언
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView enNameTextView;
        TextView krNameTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView companyTextView;
        TextView dateTextView;
        TextView imageUrlTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.item_image);
            enNameTextView = (TextView)itemView.findViewById(R.id.item_en_name);
            krNameTextView = (TextView)itemView.findViewById(R.id.item_kr_name);
            phoneTextView = (TextView)itemView.findViewById(R.id.item_phone_number);
            emailTextView = (TextView)itemView.findViewById(R.id.item_email);
            companyTextView = (TextView)itemView.findViewById(R.id.item_company);
            dateTextView = (TextView)itemView.findViewById(R.id.item_date);
            imageUrlTextView = (TextView)itemView.findViewById(R.id.item_image_url);
        }
    }

    RecyclerViewAdapter(ArrayList<BusinessCard> list){
        mData = list;
    }

    public void setData(ArrayList<BusinessCard> Data) { this.mData = Data; }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.list_item, parent, false) ;
        RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://blueberry-cheesecake-ssu.appspot.com/");
        StorageReference storageRef = storage.getReference();
        BusinessCard object = mData.get(position);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        //String path = user.getDisplayName() + "_" + user.getUid();
        String path = "sample";
        String fileName = object.getImageUrl();
        StorageReference pathReference = storageRef.child(path + "/" + fileName);

        //Image Loading
        File file = null;
        try {
            //Local에 Image 저장할 경로 지정
            File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
            file = new File(dir, fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            /*
            //Firebase에서 image download해 ImageView에 출력
            final FileDownloadTask fileDownloadTask = pathReference.getFile(file);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    holder.imageUrlTextView.setText(file.getAbsolutePath());
                    holder.imageView.setImageURI(Uri.fromFile(file));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("fail","fail");
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                }
            });
        */
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Local에 저장된 Image를 ImageView에 출력
        holder.imageUrlTextView.setText(file.getAbsolutePath());
        holder.imageView.setImageURI(Uri.parse(file.getAbsolutePath()));

        //TextView 출력
        holder.enNameTextView.setText(object.getEnName());
        holder.krNameTextView.setText(object.getKrName());
        holder.phoneTextView.setText(object.getPhoneNumber());
        holder.emailTextView.setText(object.getEmail());
        holder.companyTextView.setText(object.getCompany());
        holder.dateTextView.setText(object.getTime());
    }

    // getItemCount() - 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return mData.size() ;
    }

}
