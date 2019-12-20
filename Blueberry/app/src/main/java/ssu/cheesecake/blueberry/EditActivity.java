package ssu.cheesecake.blueberry;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import ssu.cheesecake.blueberry.custom.BusinessCard;
import ssu.cheesecake.blueberry.util.EditSpinner;
import ssu.cheesecake.blueberry.util.RealmController;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    private Context app;

    private String imagePath;
    private String fileName;

    private Intent resultIntent;
    private BusinessCard card;
    private int position;

    private Realm mRealm;
    private RealmController realmController;

    private boolean isNew = false;

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

    private Button saveBtn;
    private Button backBtn;

    private int OnShowing;

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

        groupArray = new ArrayList<>();
        mRealm = Realm.getDefaultInstance();
        realmController = new RealmController(mRealm, RealmController.WhichResult.Group);
        for (int i = 0; i < realmController.getGroups().size(); i++) {
            groupArray.add(realmController.getGroups().get(i).getGroupName());
        }

        Intent intent = getIntent();
        if (intent.hasExtra("mode")) {
            String mode = intent.getStringExtra("mode");
        }
        String mode = intent.getStringExtra("mode");
        if (mode.equals("new")) {
            isNew = true;
        } else if (mode.equals("edit")) {
            isNew = false;
            position = intent.getIntExtra("position",-1);
            if(position == -1){
                Toast.makeText(this,"명함을 수정하는데 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
        if (isNew) {
            /**
             * new (from NameCropActivity)
             **/
            card = new BusinessCard();

            imagePath = intent.getStringExtra("path");
            fileName = intent.getStringExtra("fileName");
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            imageView.setImageBitmap(bitmap);

            card.setImageUrl(imagePath);

            nameArray = intent.getStringArrayListExtra("name");
            phoneArray = intent.getStringArrayListExtra("phone");
            emailArray = intent.getStringArrayListExtra("email");
            webSiteArray = intent.getStringArrayListExtra("webSite");
            companyArray = intent.getStringArrayListExtra("company");
            addressArray = intent.getStringArrayListExtra("address");
        } else {
            /**
             * edit (from MainActivity)
             **/

            card = intent.getParcelableExtra("card");

            //Image Loading
            //Local에 Image 저장할 경로 지정
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(card.getImageUrl(), options);
            imageView.setImageBitmap(bitmap);

            editName.setText(card.getName());
            editPhone.setText(card.getPhoneNumber());
            editEmail.setText(card.getEmail());
            editWebSite.setText(card.getWebSite());
            editCompany.setText(card.getCompany());
            editAddress.setText(card.getAddress());

            nameArray = new ArrayList<String>();
            nameArray.add(card.getName());
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

            groupArray.add(card.getGroup());
            groupArray.add("선택 안함");

        }//end of ArrayLists setting
        // triggered when dropdown popup window dismissed
        editName.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        editName.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                OnShowing = 1;
                // maybe you want to hide the soft input panel when the popup window is showing.
                hideSoftInputPanel();
            }
        });


        editPhone.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        editPhone.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {

                OnShowing = 2;
                hideSoftInputPanel();
            }
        });


        editEmail.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        editEmail.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                OnShowing = 3;
                hideSoftInputPanel();
            }
        });


        editWebSite.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        editWebSite.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                OnShowing = 4;
                hideSoftInputPanel();
            }
        });


        editCompany.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        editCompany.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                OnShowing = 5;
                hideSoftInputPanel();
            }
        });


        editAddress.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        editAddress.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {

                OnShowing = 6;
                hideSoftInputPanel();
            }
        });


        editGroup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        editGroup.setOnShowListener(new EditSpinner.OnShowListener() {
            @Override
            public void onShow() {
                OnShowing = 7;
                hideSoftInputPanel();
            }
        });

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
        editGroup.setEditable(false);

        resultIntent = new Intent(this, MainActivity.class);

        saveBtn = findViewById(R.id.finishButton1);
        backBtn = findViewById(R.id.edit_back_button);
        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == saveBtn) {
            name_finValue = editName.getText().toString();
            phone_finValue = editPhone.getText().toString();
            email_finValue = editEmail.getText().toString();
            webSite_finValue = editWebSite.getText().toString();
            company_finValue = editCompany.getText().toString();
            address_finValue = editAddress.getText().toString();
            group_finValue = editGroup.getText().toString();

            Realm.init(app);
            RealmController realmController = new RealmController(Realm.getDefaultInstance(), RealmController.WhichResult.List);

            if (!isNew) {
                BusinessCard editCard = new BusinessCard();
                editCard.Copy(card);
                editCard.setName(name_finValue);
                editCard.setPhoneNumber(phone_finValue);
                editCard.setEmail(email_finValue);
                editCard.setWebSite(webSite_finValue);
                editCard.setCompany(company_finValue);
                editCard.setAddress(address_finValue);
                editCard.setGroup(group_finValue);

                realmController.editBusinessCard(editCard, position);

                resultIntent.putExtra("activity", "Edit");
                resultIntent.putExtra("card", card);
                resultIntent.putExtra("position", position);
                setResult(RESULT_OK, resultIntent);
            }

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

            if (isNew) {
                card.setName(name_finValue);
                card.setPhoneNumber(phone_finValue);
                card.setCompany(company_finValue);
                card.setAddress(address_finValue);
                card.setEmail(email_finValue);
                card.setWebSite(webSite_finValue);
                card.setGroup(group_finValue);
                card.setFileName(fileName);
                realmController.addBusinessCard(card);
                startActivity(resultIntent);
            }

            if (isNew && realmController.getIsAutoSave().getIsAutoSave()) {
                startActivityForResult(insertIntent, RESULT_OK);
            }
            finish();
        }
        else if(view == backBtn)
            finish();
    }
    private void hideSoftInputPanel() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            if (OnShowing == 1) {
                imm.hideSoftInputFromWindow(editName.getWindowToken(), 0);
            } else if (OnShowing == 2) {
                imm.hideSoftInputFromWindow(editPhone.getWindowToken(), 0);
            } else if (OnShowing == 3) {
                imm.hideSoftInputFromWindow(editEmail.getWindowToken(), 0);
            } else if (OnShowing == 4) {
                imm.hideSoftInputFromWindow(editWebSite.getWindowToken(), 0);
            } else if (OnShowing == 5) {
                imm.hideSoftInputFromWindow(editCompany.getWindowToken(), 0);
            } else if (OnShowing == 6) {
                imm.hideSoftInputFromWindow(editAddress.getWindowToken(), 0);
            } else if (OnShowing == 7) {
                imm.hideSoftInputFromWindow(editGroup.getWindowToken(), 0);
            }

        }
    }
}



