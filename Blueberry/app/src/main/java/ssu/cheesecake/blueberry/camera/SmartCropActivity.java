package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.pqpo.smartcropperlib.SmartCropper;
import me.pqpo.smartcropperlib.view.CropImageView;
import ssu.cheesecake.blueberry.R;

public class SmartCropActivity extends AppCompatActivity {

    private Context activity = this;

    private static final int CAMREQUESTCODE = 1;
    private static final int GALLERYREQUESTCODE = 2;

    private static final String TAG = "\n*****[ Blueberry : CameraFragment ]*****\n";

    private String path;
    private String fileName;
    private File mFile;
    private LoadToast loadToast;

    CropImageView cropImageView;
    Button btnCancel;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (intent.getIntExtra("key", 0) == CAMREQUESTCODE) {
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivityForResult(cameraIntent, CAMREQUESTCODE);
        } else if (intent.getIntExtra("key", 0) == GALLERYREQUESTCODE) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK);
            galleryIntent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(galleryIntent, GALLERYREQUESTCODE);
        }

        setContentView(R.layout.activity_smartcrop);
        SmartCropper.buildImageDetector(this);

        cropImageView = findViewById(R.id.iv_crop);

        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
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
                        Intent intent = new Intent(SmartCropActivity.this, NameCropActivity.class);
                        intent.putExtra("imagePath",mFile.getPath());
                        startActivity(intent);
                        finish();

                    } else {
                        Log.d(TAG, "Bitmap crop is null...");
                    }
//                    finish();
                } else {
                    Toast.makeText(SmartCropActivity.this, "cannot crop correctly, please re crop", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMREQUESTCODE) {

                path = data.getExtras().getString("path");
                fileName = data.getExtras().getString("fileName");
                mFile = new File(path, fileName);
                String imagePath = path + fileName;
                Bitmap selectedBitmap = null;
                selectedBitmap = BitmapFactory.decodeFile(imagePath);

                if (selectedBitmap != null) {
                    selectedBitmap = rotateBitmap(selectedBitmap, imagePath);
                    cropImageView.setImageToCrop(selectedBitmap);
                } else {
                    Log.i(TAG, "SmartCropActivity - onActivityResult - camera - selectedBitmap is null");
                }

            } else if (requestCode == GALLERYREQUESTCODE) {
                Uri photoUri = data.getData();
                Cursor cursor = null;
                try {
                    // Uri 스키마를 content:/// 에서 file:/// 로  변경한다.
                    String[] proj = {MediaStore.Images.Media.DATA};
                    cursor = this.getContentResolver().query(photoUri, proj, null, null, null);
                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(column_index);

                    path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/blueberry/";
                    fileName = "blueberry_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
                    File dir = new File(path);
                    if (!dir.exists() && dir.mkdir()) {
                        dir.setWritable(true);
                    }
                    mFile = new File(path, fileName);

                    Bitmap originImage = BitmapFactory.decodeFile(imagePath);
                    saveImage(originImage, mFile);

                    Bitmap selectedBitmap = null;
                    selectedBitmap = BitmapFactory.decodeFile(mFile.getPath());

                    if (selectedBitmap != null) {
                        selectedBitmap = rotateBitmap(selectedBitmap, mFile.getPath());
                        cropImageView.setImageToCrop(selectedBitmap);
                    } else {
                        Log.i(TAG, "SmartCropActivity - onActivityResult - gallery - selectedBitmap is null");
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else {
                Toast.makeText(this, "requestCode is null", Toast.LENGTH_SHORT).show();
            }
        } else {//RESULT_CANCELED
            Toast.makeText(this, "result cancel", Toast.LENGTH_SHORT).show();
            onDestroy();
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap, String path) {
        Matrix matrix = new Matrix();

        try {
            ExifInterface ei = null;
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    matrix.postRotate(90);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    matrix.postRotate(180);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    matrix.postRotate(270);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                case ExifInterface.ORIENTATION_NORMAL:
//                default:
//                    return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
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
        } finally {
            activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(saveFile)));
        }
    }

}
