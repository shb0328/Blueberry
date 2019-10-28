package ssu.cheesecake.blueberry;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class ReCheck extends AppCompatActivity {
    private File cameraFile;
    private File galleryFile;
    private File file;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("blueee", "ReCheck!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check);

        Intent intent = getIntent();
        String imagePath = intent.getExtras().getString("imagePath");
        Log.d("blueee", imagePath);
        File imageFile = new File(imagePath);
        Log.d("blueee", imageFile.toString());
        //Uri photoUri = intent.getParcelableExtra("imageUri");
        //Log.d("blueee", photoUri.toString());
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        imageView = findViewById(R.id.business_card);
        imageView.setImageBitmap(bitmap);


        final Spinner spinner_1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.recheck_name, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter1);

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final Spinner spinner_2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.recheck_phone, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(adapter2);

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final Spinner spinner_3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.recheck_address, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_3.setAdapter(adapter3);

        spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final Spinner spinner_4 = findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.recheck_company, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_3.setAdapter(adapter4);

        spinner_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        Button toConfirm_page = findViewById(R.id.finishButton1);
        toConfirm_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toConfirm_intent = new Intent(ReCheck.this, ReCheck2.class);
                toConfirm_intent.putExtra("selectedValue1", spinner_1.getSelectedItem().toString());
                toConfirm_intent.putExtra("selectedValue2", spinner_2.getSelectedItem().toString());
                toConfirm_intent.putExtra("selectedValue3", spinner_3.getSelectedItem().toString());
                toConfirm_intent.putExtra("selectedValue4", spinner_4.getSelectedItem().toString());
                startActivity(toConfirm_intent);
                finish();
            }

        });

        //image 파일하고 값 받는 함수도 만들어야한다
        //cameraFile = (File)this.getIntent().getSerializableExtra("CameraFile");
        /*
        byte[] arr = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(arr, 0, arr.length);


        imageView = this.findViewById(R.id.business_card);
        imageView.setImageBitmap(image);
         */

    }
}
