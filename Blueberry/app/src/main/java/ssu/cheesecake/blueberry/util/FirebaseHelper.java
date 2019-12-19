package ssu.cheesecake.blueberry.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.custom.BusinessCard;

public class FirebaseHelper {
    private Context context;

    private static String TAG = "DEBUG!";

    private String path;

    private String userId;
    private FirebaseUser user;
    private DatabaseReference myRef;
    private FirebaseStorage storage;
    private StorageReference storageMyRef;

    private Vector<String> uploadFileList;
    private Vector<String> downloadFileList;
    private int uploadCount;
    private int downloadCount;

    private LoadToast backupLoadToast, restoreLoadToast;

    public FirebaseHelper() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getDisplayName() + "_" + user.getUid();
        myRef = FirebaseDatabase.getInstance().getReference().child(userId);
        storage = FirebaseStorage.getInstance("gs://blueberry-cheesecake-ssu.appspot.com");
        storageMyRef = storage.getReference().child(userId);
        init();
    }

    public FirebaseHelper(Context context) {
        this();
        this.context = context;
    }

    public void init() {
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/blueberry/";
        uploadFileList = new Vector<String>();
        downloadFileList = new Vector<String>();
        uploadCount = 0;
        downloadCount = 0;
    }

    public void Backup(final RealmController realm) {
        if (realm.getCards().size() > 0) {
            backupLoadToast = new LoadToast(context);
            backupLoadToast.setText("Backup...");
            backupLoadToast.setTranslationY(1000);
            backupLoadToast.show();
            Vector<BusinessCard> cards = realm.getCards();
            myRef.removeValue();
            realm.changeAllId();
            int len = cards.size();
            for (int i = 0; i < len; i++) {
                myRef.child(i + "").setValue(cards.get(i).toMap());
                uploadFileList.add(cards.get(i).getFileName());
            }
            addToUploadFileList();
        } else {
            Toast.makeText(context, "백업할 내용이 없습니다.", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    public void Restore(final RealmController realm) {
        restoreLoadToast = new LoadToast(context);
        restoreLoadToast.setText("Restore...");
        restoreLoadToast.setTranslationY(1000);
        restoreLoadToast.show();
        boolean isAutoSave = realm.getIsAutoSave().getIsAutoSave();
        realm.initializeCards();
        realm.initializeAutoSave(isAutoSave);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> map = (HashMap<String, Object>) postSnapshot.getValue();
                        BusinessCard card = new BusinessCard(map);
                        realm.addBusinessCard(card);
                        downloadFileList.add(card.getFileName());
                        try {
                            downloadImage(card.getFileName());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    restoreLoadToast.success();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return;
    }

    public void addToUploadFileList() {
        for (int i = 0; i < uploadFileList.size(); i++) {
            String fileName = uploadFileList.get(i);
            Uri file = Uri.fromFile(new File(path + '/' + fileName));
            UploadTask uploadTask = storageMyRef.child(fileName).putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    uploadCount++;
                    if (uploadCount == uploadFileList.size())
                        backupLoadToast.success();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadCount++;
                    if (uploadCount == uploadFileList.size())
                        backupLoadToast.success();
                }
            });
        }
        return;
    }

    public void downloadImage(String fileName) throws FileNotFoundException {
        StorageReference pathReference = storageMyRef.child(fileName);

        //Local Directory 없을 경우 생성
        File dir = new File(path);
        if (!dir.exists() && dir.mkdir()) {
            dir.setWritable(true);
        }

        //Local File 생성
        final File file = new File(path, fileName);
        if (!file.exists()) {
            pathReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    downloadCount++;
                    if (downloadCount == downloadFileList.size()) {
                        restoreLoadToast.success();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    downloadCount++;
                    if (downloadCount == downloadFileList.size()) {
                        restoreLoadToast.success();
                    }
                }
            });
            return;
        } else {
            downloadCount++;
            if (downloadCount == downloadFileList.size()) {
                restoreLoadToast.success();
            }
        }
    }

}
