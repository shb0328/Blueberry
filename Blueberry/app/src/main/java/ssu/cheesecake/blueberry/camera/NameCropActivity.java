package ssu.cheesecake.blueberry.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import ssu.cheesecake.blueberry.R;

public class NameCropActivity extends AppCompatActivity {

    private String path = "";
    private String fileName = "";

    private Bitmap bitmap;

    private NameCropImageView nameCropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_crop);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        fileName = intent.getStringExtra("fileName");

        bitmap = BitmapFactory.decodeFile(path+fileName);

        nameCropImageView = findViewById(R.id.nameCropImageView);
        nameCropImageView.init(bitmap);


    }

}
