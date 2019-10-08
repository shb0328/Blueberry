package ssu.cheesecake.blueberry;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import ssu.cheesecake.blueberry.R;

import static android.os.Build.ID;

public class ListFragment extends Fragment{
    BitmapDrawable bitmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    RecyclerView recyclerView;
    private ArrayList<DataObject> list = new ArrayList<DataObject>();
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        int i = 0;

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

        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    DataObject object = postSnapshot.getValue(DataObject.class);
                    list.add(object);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
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
                DataObject object = new DataObject("EnName", "한글이름", "010-1234-5678", "email@gmail.com", "Company", "sample1.jpg");
                object.postFirebaseDatabase();
            }
        });

        return root;
    }

}

//RecyclerView Adapter Class
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<DataObject> mData = null;

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

    RecyclerViewAdapter(ArrayList<DataObject> list){
        mData = list;
    }

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
        DataObject object = mData.get(position);
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
