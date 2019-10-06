package ssu.cheesecake.blueberry;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ssu.cheesecake.blueberry.R;

import static android.os.Build.ID;

public class ListFragment extends Fragment{
    BitmapDrawable bitmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference myRef;
    RecyclerView recyclerView;
    private ArrayList<DataObject> list = new ArrayList<DataObject>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("users");

        //MainActivity
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        DataObject tmp = null;
        for(int i = 0; i < 10; i++){
            tmp = new DataObject("cpuman7@gmail.com", "Hansu", "Kim", "010-4537-9662", "cpuman7@gmail.com", "SoongSil Unv.");
            //tmp.postFirebaseDatabase();
            list.add(tmp);
        }
        recyclerView = (RecyclerView)root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter);

        final Button addBtn = root.findViewById(R.id.add_button);
        addBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DataObject("cpuman7@gmail.com", "Hansu", "Kim", "010-4537-9662", "cpuman7@gmail.com", "SoongSil Unv.").postFirebaseDatabase();
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
        TextView nameTextView;
        TextView phoneTextView;
        TextView emailTextView;
        TextView companyTextView;
        TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_name);
            phoneTextView = itemView.findViewById(R.id.item_phone_number);
            emailTextView = itemView.findViewById(R.id.item_email);
            companyTextView = itemView.findViewById(R.id.item_company);
            dateTextView = itemView.findViewById(R.id.item_date);
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
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        DataObject object = mData.get(position);
        holder.nameTextView.setText(object.getFirstName() + object.getLastName()) ;
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
