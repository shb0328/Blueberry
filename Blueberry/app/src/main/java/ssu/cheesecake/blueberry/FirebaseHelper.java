package ssu.cheesecake.blueberry;

import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.HashMap;
import java.util.Vector;

public class FirebaseHelper {
    private Context context;
    private String userId;
    private FirebaseUser user;
    private DatabaseReference myRef;
    LoadToast restoreLoadToast;

    public FirebaseHelper(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getDisplayName() + "_" + user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child(userId);
    }

    public FirebaseHelper(Context context){
        this.context = context;
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getDisplayName() + "_" + user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child(userId);
    }

    public void Backup(final RealmController realm){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("백업");
        alert.setMessage("백업을 실행하시겠습니까?");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Vector<BusinessCard> cards = realm.getCards();
                myRef.removeValue();
                int len = cards.size();
                for(int i = 0; i < len; i++) {
                    myRef.child(i+"").setValue(cards.get(i).toMap());
                }
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alert.show();
        return;
    }

    public void Restore(final RealmController realm){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("복원");
        alert.setMessage("복원을 실행하시겠습니까? 현재 단말기에 저장된 정보는 모두 지워지고, 백업된 내용이 들어옵니다.");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                restoreLoadToast = new LoadToast(context);
                restoreLoadToast.setText("Restore...");
                restoreLoadToast.setTranslationY(1000);
                restoreLoadToast.show();
                realm.initializeCards();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            HashMap<String, Object> map = (HashMap<String, Object>) postSnapshot.getValue();
                            BusinessCard card = new BusinessCard(map);
                            realm.addBusinessCard(card);
                        }
                        restoreLoadToast.success();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alert.show();

        return;
    }

}
