package ssu.cheesecake.blueberry;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import me.pqpo.smartcropperlib.SmartCropper;
import me.pqpo.smartcropperlib.view.CropImageView;

public class EditPhotoActivity extends AppCompatActivity {

    private static final String TAG = "\n*****[ Blueberry : CameraFragment ]*****\n";

    private String path;
    private String fileName;
    private File mFile;

    CropImageView cropImageView;
    Button btnCancel;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        SmartCropper.buildImageDetector(this);

        Intent intent = getIntent();
        path = intent.getExtras().getString("path");
        fileName = intent.getExtras().getString("fileName");
        mFile = new File(path,fileName+"_edit");

        cropImageView = (CropImageView) findViewById(R.id.iv_crop);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (cropImageView.canRightCrop()) {
                    Bitmap crop = cropImageView.crop();
                    if (crop != null) {
                        saveImage(crop, mFile);
                        Intent intent1 = new Intent(EditPhotoActivity.this,ReCheck.class);
                        startActivity(intent1);
                    } else {
                        Log.d(TAG,"Bitmap crop is null...");
                    }
                    finish();
                } else {
                    Toast.makeText(EditPhotoActivity.this, "cannot crop correctly", Toast.LENGTH_SHORT).show();

                }
            }
        });

//        ContentResolver cr = getContentResolver();
//        Uri bmpUri = data.getData();
//        try {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(cr.openInputStream(bmpUri), new Rect(), options);
//            options.inJustDecodeBounds = false;
//            options.inSampleSize = calculateSampleSize(options);
//            selectedBitmap = BitmapFactory.decodeStream(cr.openInputStream(bmpUri), new Rect(), options);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        Bitmap selectedBitmap = null;
        selectedBitmap = BitmapFactory.decodeFile(path+fileName);

        if (selectedBitmap != null) {
            cropImageView.setImageToCrop(selectedBitmap);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    private void saveImage(Bitmap bitmap, File saveFile) {
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calculateSampleSize(BitmapFactory.Options options) {
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;
        int sampleSize = 1;
        int destHeight = 1000;
        int destWidth = 1000;
        if (outHeight > destHeight || outWidth > destHeight) {
            if (outHeight > outWidth) {
                sampleSize = outHeight / destHeight;
            } else {
                sampleSize = outWidth / destWidth;
            }
        }
        if (sampleSize < 1) {
            sampleSize = 1;
        }
        return sampleSize;
    }

}
