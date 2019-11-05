package ssu.cheesecake.blueberry;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private String imagePath;
    private ImageView imageView;


    public String name_finValue;
    public String phone_finValue;
    public String mail_finValue;
    public String company_finValue;


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


        //스피너로 앞에서 받은 값 지정해주기
        final Spinner spinner_1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.recheck_name, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter1);

        //select 전에는 hint 부분이 나오도록 하기

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //직접 입력이라고 하기
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

        //EditText로 spinner에서 받은 값 수정 및 연락처로 옮길 값 받기
        EditText name_editText = findViewById(R.id.name_confirm);
        name_editText.setText(spinner_1.getSelectedItem().toString());
        name_finValue = name_editText.getText().toString();

        EditText phone_editText = findViewById(R.id.phone_confirm);
        phone_editText.setText(spinner_2.getSelectedItem().toString());
        phone_finValue = phone_editText.getText().toString();

        EditText mail_editText = findViewById(R.id.mail_confirm);
        mail_editText.setText(spinner_3.getSelectedItem().toString());
        mail_finValue = mail_editText.getText().toString();

        EditText company_editText = findViewById(R.id.company_confirm);
        company_editText.setText(spinner_4.getSelectedItem().toString());
        company_finValue = company_editText.getText().toString();


        //버튼으로 주소록에 데이터를 전달함
        Button ToFinValue = findViewById(R.id.finishButton1);
        ToFinValue.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //if 문으로 option에 따라 다르게 하도록 하기, option에도 listener 달아야함


        // Creates a new intent for sending to the device's contacts application
        Intent insertIntent = new Intent(ContactsContract.Intents.Insert.ACTION);

        // Sets the MIME type to the one expected by the insertion activity
        insertIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        // Sets the new contact name
        insertIntent.putExtra(ContactsContract.Intents.Insert.NAME, name_finValue);

        // Sets the new company
        insertIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, company_finValue);

        /*
         * Demonstrates adding data rows as an array list associated with the DATA key
         */

        // Defines an array list to contain the ContentValues objects for each row
        ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();


        /*
         * Defines the raw contact row
         */

// Sets up the row as a ContentValues object
        ContentValues rawContactRow = new ContentValues();

//// Adds the account type and name to the row
//            rawContactRow.put(ContactsContract.RawContacts.ACCOUNT_TYPE, selectedAccount.getType());
//            rawContactRow.put(ContactsContract.RawContacts.ACCOUNT_NAME, selectedAccount.getName());

// Adds the row to the array
        contactData.add(rawContactRow);

        /*
         * Sets up the phone number data row
         */

// Sets up the row as a ContentValues object
        ContentValues phoneRow = new ContentValues();

// Specifies the MIME type for this data row (all data rows must be marked by their type)
        phoneRow.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
        );

// Adds the phone number and its type to the row
        phoneRow.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_finValue);

// Adds the row to the array
        contactData.add(phoneRow);

        /*
         * Sets up the email data row
         */

// Sets up the row as a ContentValues object
        ContentValues emailRow = new ContentValues();

// Specifies the MIME type for this data row (all data rows must be marked by their type)
        emailRow.put(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
        );

// Adds the email address and its type to the row
        emailRow.put(ContactsContract.CommonDataKinds.Email.ADDRESS, mail_finValue);

// Adds the row to the array
        contactData.add(emailRow);

        /*
         * Adds the array to the intent's extras. It must be a parcelable object in order to
         * travel between processes. The device's contacts app expects its key to be
         * Intents.Insert.DATA
         */
        insertIntent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

// Send out the intent to start the device's contacts app in its add contact activity.
        startActivity(insertIntent);

    }


}
