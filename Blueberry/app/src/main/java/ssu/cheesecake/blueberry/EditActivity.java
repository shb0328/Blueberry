package ssu.cheesecake.blueberry;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    private String imagePath;
    private ImageView imageView;

    public String name_finValue;
    public String phone_finValue;
    public String mail_finValue;
    public String company_finValue;
    private String group_finValue;//일단 충돌 오류때문에 여기다가 해놓음하하

    private EditSpinner editname;
    private EditSpinner editphone;
    private EditSpinner editcompany;
    private EditSpinner editemail;
    private EditSpinner editgroup;

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

    @Override
    protected void onDestroy() {
        Toast.makeText(this,"cancel...",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

}



