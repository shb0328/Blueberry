package ssu.cheesecake.blueberry.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.pqpo.smartcropperlib.SmartCropper;
import me.pqpo.smartcropperlib.view.CropImageView;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.EditActivity;

public class SmartCropActivity extends AppCompatActivity {

    private static final String TAG = "\n*****[ Blueberry : CameraFragment ]*****\n";
    private static final int REQUEST_CODE = 1020;

    private String path;
    private String fileName;
    private File mFile;

    CropImageView cropImageView;
    Button btnCancel;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(cameraIntent,REQUEST_CODE);
        
        setContentView(R.layout.activity_smartcrop);
        SmartCropper.buildImageDetector(this);

//        Intent receivedIntent = getIntent();
//        path = receivedIntent.getExtras().getString("path");
//        fileName = receivedIntent.getExtras().getString("fileName");
//        mFile = new File(path,fileName+"_edit");

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
                        Intent intent = new Intent(SmartCropActivity.this, EditActivity.class);
                        startActivity(intent);
                    } else {
                        Log.d(TAG,"Bitmap crop is null...");
                    }
                    finish();
                } else {
                    Toast.makeText(SmartCropActivity.this, "cannot crop correctly", Toast.LENGTH_SHORT).show();

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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Intent receivedIntent = getIntent();
        path = data.getExtras().getString("path");
        fileName = data.getExtras().getString("fileName");
        mFile = new File(path,fileName+"_edit");

        Bitmap selectedBitmap = null;
        selectedBitmap = BitmapFactory.decodeFile(path+fileName);

        if (selectedBitmap != null) {
            cropImageView.setImageToCrop(selectedBitmap);
        }else{
            Log.i(TAG,"SmartCropActivity - onActivityResult - selectedBitmap is null");
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
