package ssu.cheesecake.blueberry;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReCheck2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check2);

        EditText editText1=findViewById(R.id.name_confirm);
        editText1.setText(getResources().getString(R.string.recheck2_name1));

        TextView name_confirm2 = findViewById(R.id.name_confirm2);
        name_confirm2.setText(getResources().getString(R.string.recheck2_name2));

        EditText editText2=findViewById(R.id.phone_confirm);
        editText2.setText(getResources().getString(R.string.recheck2_phone1));

        TextView phone_confirm2= findViewById(R.id.phone_confirm2);
        phone_confirm2.setText(getResources().getString(R.string.recheck2_phone2));


        EditText editText3=findViewById(R.id.mail_confirm);
        editText3.setText(getResources().getString(R.string.recheck2_mail1));

        TextView mail_confirm2 = findViewById(R.id.mail_confirm2);
        mail_confirm2.setText(getResources().getString(R.string.recheck2_mail2));


        EditText editText4=findViewById(R.id.company_confirm);
        editText4.setText(getResources().getString(R.string.recheck2_company1));

        TextView company_confirm2 = findViewById(R.id.company_confirm2);
        company_confirm2.setText(getResources().getString(R.string.recheck2_company2));

    }


}
