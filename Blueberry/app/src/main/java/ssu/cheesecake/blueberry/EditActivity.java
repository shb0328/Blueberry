package ssu.cheesecake.blueberry;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ListAdapter;

import ssu.cheesecake.blueberry.camera.OCRTask;

public class EditActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

//        Intent intent = getIntent();
//        imagePath = intent.getExtras().getString("imagePath");
//        File imageFile = new File(imagePath);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
//        imageView = findViewById(R.id.business_card);
//        imageView.setImageBitmap(bitmap);
//
//        asyncTesseract = new AsyncTesseract();
//        asyncTesseract.execute(bitmap);
//
//        //TODO: Parse result string - name,phone,email ...
//        //TODO: Insert parsing results into each spinners
//
//        //TODO 이전까지 건들지 말기 !!!!!!!~~~~~------------------------------------


        //앞으로 옮겨죠요 ...
        final EditSpinner editname;
        final EditSpinner editphone;
        final EditSpinner editcompany;
        final  EditSpinner editemail;
        final EditSpinner editgroup;


        String group_finValue;//일단 충돌 오류때문에 여기다가 해놓음하하


        editname=findViewById(R.id.edit_name);
        editphone=findViewById(R.id.edit_phone);
        editcompany=findViewById(R.id.edit_company);
        editemail=findViewById(R.id.edit_address);
        editgroup=findViewById(R.id.edit_group);


        ArrayAdapter name_adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.recheck_name));
        editname.setAdapter(name_adapter);

        ArrayAdapter phone_adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.recheck_phone));
        editphone.setAdapter(phone_adapter);

        ArrayAdapter company_adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.recheck_company));
        editcompany.setAdapter(company_adapter);

        ArrayAdapter email_adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.recheck_address));
        editemail.setAdapter(email_adapter);

        ArrayAdapter group_adapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.recheck_group));
        editemail.setAdapter(group_adapter);







        //버튼으로 주소록에 데이터를 전달함
        Button ToFinValue = findViewById(R.id.finishButton1);
        ToFinValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name_finValue=editname.getText().toString();
                phone_finValue=editphone.getText().toString();
                company_finValue= editcompany.getText().toString();
                mail_finValue=editemail.getText().toString();
//                group_finValue=editgroup.getText().toString();


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



    }});}

//    @Override
//    public void onClick(View view) {
//
//
//
//        Intent insertIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
//        insertIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//        insertIntent.putExtra(ContactsContract.Intents.Insert.NAME, name_finValue);
//        insertIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, company_finValue);
//
//        ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
//
//        ContentValues rawContactRow = new ContentValues();
//        contactData.add(rawContactRow);
//
//        ContentValues phoneRow = new ContentValues();
//        phoneRow.put(
//                ContactsContract.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
//        );
//        phoneRow.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_finValue);
//        contactData.add(phoneRow);
//
//        ContentValues emailRow = new ContentValues();
//        emailRow.put(
//                ContactsContract.Data.MIMETYPE,
//                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
//        );
//        emailRow.put(ContactsContract.CommonDataKinds.Email.ADDRESS, mail_finValue);
//        contactData.add(emailRow);
//
//        insertIntent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
//        startActivity(insertIntent);
//    }


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

//    private void showSoftInputPanel(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.showSoftInput(view, 0);
//        }
//    }

    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();

    }

}



