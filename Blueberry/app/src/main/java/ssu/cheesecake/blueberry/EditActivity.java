package ssu.cheesecake.blueberry;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import ssu.cheesecake.blueberry.custom.BusinessCard;
import ssu.cheesecake.blueberry.util.EditSpinner;
import ssu.cheesecake.blueberry.util.RealmController;

public class EditActivity extends AppCompatActivity {

    private Activity activity;

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

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        activity = this;

        Intent intent = getIntent();
        final BusinessCard card = intent.getParcelableExtra("card");
        //Image Loading
        imageView = findViewById(R.id.business_card);
        File imageFile = null;
        try {
            //Local에 Image 저장할 경로 지정
            File dir = new File(Environment.getExternalStorageDirectory() + "/photos");
            imageFile = new File(dir, card.getImageUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imageFile != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
            imageView.setImageBitmap(bitmap);
        }
        editname = findViewById(R.id.edit_name);
        editphone = findViewById(R.id.edit_phone);
        editcompany = findViewById(R.id.edit_company);
        editemail = findViewById(R.id.edit_address);
        editgroup = findViewById(R.id.edit_group);

        editname.setText(card.getKrName());
        editphone.setText(card.getPhoneNumber());
        editcompany.setText(card.getCompany());
        editemail.setText(card.getEmail());
        editgroup.setText(card.getGroup());

        ArrayList<String> nameArray = new ArrayList<String>();
        nameArray.add(card.getKrName());
        nameArray.add("직접 입력");
        ArrayList<String> phoneArray = new ArrayList<String>();
        phoneArray.add(card.getPhoneNumber());
        phoneArray.add("직접 입력");
        ArrayList<String> companyArray = new ArrayList<String>();
        companyArray.add(card.getCompany());
        companyArray.add("직접 입력");
        ArrayList<String> emailArray = new ArrayList<String>();
        emailArray.add(card.getEmail());
        emailArray.add("직접 입력");
        ArrayList<String> groupArray = new ArrayList<String>();
        groupArray.add(card.getGroup());
        groupArray.add("직접 입력");


        ArrayAdapter name_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nameArray);
        editname.setAdapter(name_adapter);

        ArrayAdapter phone_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, phoneArray);
        editphone.setAdapter(phone_adapter);

        ArrayAdapter company_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, companyArray);
        editcompany.setAdapter(company_adapter);

        ArrayAdapter email_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emailArray);
        editemail.setAdapter(email_adapter);

        ArrayAdapter group_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, groupArray);
        editemail.setAdapter(group_adapter);

        //버튼으로 주소록에 데이터를 전달함
        Button ToFinValue = findViewById(R.id.finishButton1);
        ToFinValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessCard editCard = new BusinessCard();
                editCard.Copy(card);
                editCard.setKrName(editname.getText().toString());
                editCard.setPhoneNumber(editphone.getText().toString());
                editCard.setCompany(editcompany.getText().toString());
                editCard.setEmail(editemail.getText().toString());
                editCard.setGroup(editgroup.getText().toString());

                Realm.init(activity);
                RealmController realmController = new RealmController(Realm.getDefaultInstance(), RealmController.WhichResult.List);
                realmController.editBusinessCard(editCard, position);

                name_finValue = editname.getText().toString();
                phone_finValue = editphone.getText().toString();
                company_finValue = editcompany.getText().toString();
                mail_finValue = editemail.getText().toString();
                group_finValue = editgroup.getText().toString();

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

                if(realmController.getIsAutoSave().getIsAutoSave()) {
                    startActivityForResult(insertIntent, RESULT_OK);
                }
                finish();
            }
        });
    }

}



