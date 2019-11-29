package ssu.cheesecake.blueberry;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import ssu.cheesecake.blueberry.camera.OCRTask;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private EditActivity editActivity = this;

    private String imagePath;
    private ImageView imageView;

    public String name_finValue;
    public String phone_finValue;
    public String mail_finValue;
    public String company_finValue;

    /**
     * OCR
     */
    private boolean isOCRFailed = false;
    private AsyncTesseract asyncTesseract;
    private LoadToast loadToast;
    private OCRResult ocrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        imagePath = intent.getExtras().getString("imagePath");
        File imageFile = new File(imagePath);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        imageView = findViewById(R.id.business_card);
        imageView.setImageBitmap(bitmap);

        asyncTesseract = new AsyncTesseract();
        asyncTesseract.execute(bitmap);

        //TODO: Parse result string - name,phone,email ...
        //TODO: Insert parsing results into each spinners

        //버튼으로 주소록에 데이터를 전달함
        Button ToFinValue = findViewById(R.id.finishButton1);
        ToFinValue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //if 문으로 option에 따라 다르게 하도록 하기, option에도 listener 달아야함
        Intent insertIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        insertIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        insertIntent.putExtra(ContactsContract.Intents.Insert.NAME, name_finValue);
        insertIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, company_finValue);

        ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();

        ContentValues rawContactRow = new ContentValues();
        contactData.add(rawContactRow);

        ContentValues phoneRow = new ContentValues();
        phoneRow.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        );
        phoneRow.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_finValue);
        contactData.add(phoneRow);

        ContentValues emailRow = new ContentValues();
        emailRow.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
        );
        emailRow.put(ContactsContract.CommonDataKinds.Email.ADDRESS, mail_finValue);
        contactData.add(emailRow);

        insertIntent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
        startActivity(insertIntent);
    }

    public class AsyncTesseract extends AsyncTask<Bitmap, Integer, String> {

        TessBaseAPI tessBaseAPI;

        public AsyncTesseract() {
            super();
            OCRTask ocrTask = new OCRTask(editActivity);
            tessBaseAPI = ocrTask.getTessBaseAPI();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadToast = new LoadToast(editActivity);
            loadToast.setText("OCR Converting...");
            //TODO: center,, not 1000
            loadToast.setTranslationY(1000);
            loadToast.show();
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            tessBaseAPI.setImage(bitmaps[0]);
            return tessBaseAPI.getUTF8Text();
        }

        @Override
        protected void onCancelled() {
            loadToast.error();
            isOCRFailed = true;
            onDestroy();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String resultText) {
            loadToast.success();
//            tempTextView.setText(resultText);
            Log.i("OCR result",resultText);
            ocrResult = new OCRResult(resultText);
            HashMap<String,String> map = ocrResult.parsing();
            Log.i("OCR result",
                    "\nemail: "+map.get("email")
                            +"\nphone: "+map.get("phone")
                            +"\nname: "+map.get("name"));
            super.onPostExecute(resultText);
        }
    }

    @Override
    protected void onDestroy() {
        if(isOCRFailed == true){
            Toast.makeText(this,"Sorry, OCR Faild...",Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }
}
