package ssu.cheesecake.blueberry;

import android.content.ContentValues;
import android.content.Context;
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

    private Context app;

    private String imagePath;

    private BusinessCard card;
    private int position;

    //data
    private ArrayList<String> nameArray;
    private ArrayList<String> phoneArray;
    private ArrayList<String> emailArray;
    private ArrayList<String> webSiteArray;
    private ArrayList<String> companyArray;
    private ArrayList<String> addressArray;
    private ArrayList<String> groupArray;

    //spinners and adapters
    private ImageView imageView;
    private EditSpinner editName;
    private EditSpinner editPhone;
    private EditSpinner editEmail;
    private EditSpinner editWebSite;
    private EditSpinner editCompany;
    private EditSpinner editAddress;
    private EditSpinner editGroup;

    private ArrayAdapter name_adapter;
    private ArrayAdapter phone_adapter;
    private ArrayAdapter email_adapter;
    private ArrayAdapter webSite_adapter;
    private ArrayAdapter company_adapter;
    private ArrayAdapter address_adapter;
    private ArrayAdapter group_adapter;

    //cheesed data
    private String name_finValue;
    private String phone_finValue;
    private String email_finValue;
    private String webSite_finValue;
    private String company_finValue;
    private String address_finValue;
    private String group_finValue;//일단 충돌 오류때문에 여기다가 해놓음하하

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        app = this;

        imageView = findViewById(R.id.business_card);
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        editEmail = findViewById(R.id.edit_email);
        editWebSite = findViewById(R.id.edit_webSite);
        editCompany = findViewById(R.id.edit_company);
        editAddress = findViewById(R.id.edit_address);
        editGroup = findViewById(R.id.edit_group);

        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");

        if (mode.equals("new")) {
            /**
             * new (from NameCropActivity)
             **/
            card = new BusinessCard();

            imagePath = intent.getStringExtra("path");
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            imageView.setImageBitmap(bitmap);

            nameArray = intent.getStringArrayListExtra("name");
            phoneArray = intent.getStringArrayListExtra("phone");
            emailArray = intent.getStringArrayListExtra("email");
            webSiteArray = intent.getStringArrayListExtra("webSite");
            companyArray = intent.getStringArrayListExtra("company");
            addressArray = intent.getStringArrayListExtra("address");
            groupArray = new ArrayList<>();
        }
        else if (mode.equals("edit")) {
            /**
             * edit (from MainActivity)
             **/

            card = intent.getParcelableExtra("card");

            //Image Loading
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

            editName.setText(card.getKrName());
            editPhone.setText(card.getPhoneNumber());
            editEmail.setText(card.getEmail());
            editWebSite.setText(card.getWebSite());
            editCompany.setText(card.getCompany());
            editAddress.setText(card.getAddress());
            editGroup.setText(card.getGroup());

            nameArray = new ArrayList<String>();
            nameArray.add(card.getKrName());
            nameArray.add("직접 입력");

            phoneArray = new ArrayList<String>();
            phoneArray.add(card.getPhoneNumber());
            phoneArray.add("직접 입력");

            emailArray = new ArrayList<String>();
            emailArray.add(card.getEmail());
            emailArray.add("직접 입력");

            webSiteArray = new ArrayList<String>();
            webSiteArray.add(card.getWebSite());
            webSiteArray.add("직접 입력");

            companyArray = new ArrayList<String>();
            companyArray.add(card.getCompany());
            companyArray.add("직접 입력");

            addressArray = new ArrayList<String>();
            addressArray.add(card.getAddress());
            addressArray.add("직접 입력");

            groupArray = new ArrayList<String>();
            groupArray.add(card.getGroup());
            groupArray.add("직접 입력");

        }//end of ArrayLists setting

        name_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nameArray);
        editName.setAdapter(name_adapter);

        phone_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, phoneArray);
        editPhone.setAdapter(phone_adapter);

        email_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, emailArray);
        editEmail.setAdapter(email_adapter);

        webSite_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, webSiteArray);
        editWebSite.setAdapter(webSite_adapter);

        company_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, companyArray);
        editCompany.setAdapter(company_adapter);

        address_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, addressArray);
        editAddress.setAdapter(address_adapter);

        group_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, groupArray);
        editGroup.setAdapter(group_adapter);


        /**
         *         Save
         *         *****************************************************
         *         TODO:받아라 김한수
         *         *****************************************************
         */
        Button ToFinValue = findViewById(R.id.finishButton1);
        ToFinValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BusinessCard editCard = new BusinessCard();
                editCard.Copy(card);
                editCard.setKrName(editName.getText().toString());
                editCard.setPhoneNumber(editPhone.getText().toString());
                editCard.setEmail(editEmail.getText().toString());
                editCard.setWebSite(editWebSite.getText().toString());
                editCard.setCompany(editCompany.getText().toString());
                editCard.setAddress(editAddress.getText().toString());
                editCard.setGroup(editGroup.getText().toString());

                Realm.init(app);
                RealmController realmController = new RealmController(Realm.getDefaultInstance(), RealmController.WhichResult.List);
                realmController.editBusinessCard(editCard, position);

                name_finValue = editName.getText().toString();
                phone_finValue = editPhone.getText().toString();
                email_finValue = editEmail.getText().toString();
                webSite_finValue = editWebSite.getText().toString();
                company_finValue = editCompany.getText().toString();
                address_finValue = editAddress.getText().toString();
                group_finValue = editGroup.getText().toString();

                Intent insertIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
                insertIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                insertIntent.putExtra(ContactsContract.Intents.Insert.NAME, name_finValue);
                insertIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, company_finValue);
                insertIntent.putExtra(ContactsContract.Intents.Insert.COMPANY, address_finValue);

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
                emailRow.put(ContactsContract.CommonDataKinds.Email.ADDRESS, email_finValue);
                contactData.add(emailRow);
                insertIntent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

                ContentValues webRow = new ContentValues();
                webRow.put(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                );
                webRow.put(ContactsContract.CommonDataKinds.Website.URL, webSite_finValue);
                contactData.add(webRow);
                insertIntent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);

                if (realmController.getIsAutoSave().getIsAutoSave()) {
                    startActivityForResult(insertIntent, RESULT_OK);
                }
                finish();
            }
        });
    }

}



