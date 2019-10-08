package ssu.cheesecake.blueberry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReCheck2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check2);

        Intent fromConfirm_intent=getIntent();

        String selectedValue1=fromConfirm_intent.getExtras().getString("selectedValue1");//null값 설정 나중에 다시 해주기
        EditText editText1=findViewById(R.id.name_confirm);
        editText1.setText(selectedValue1);
        String finValue1=editText1.getText().toString();//최종적으로 전화부에 보낼 값



        TextView name_confirm2 = findViewById(R.id.name_confirm2);
        name_confirm2.setText(getResources().getString(R.string.recheck2_name2));

        String selectedValue2=fromConfirm_intent.getExtras().getString("selectedValue2");
        EditText editText2=findViewById(R.id.phone_confirm);
        editText2.setText(selectedValue2);
        String finValue2=editText1.getText().toString();

        TextView phone_confirm2= findViewById(R.id.phone_confirm2);
        phone_confirm2.setText(getResources().getString(R.string.recheck2_phone2));//전화부에 보낼 값인데 전화번호 string으로 설정해도 되는지는 모르겠다

        String selectedValue3=fromConfirm_intent.getExtras().getString("selectedValue3");
        EditText editText3=findViewById(R.id.mail_confirm);
        editText3.setText(selectedValue3);
        String finValue3=editText1.getText().toString();

        TextView mail_confirm2 = findViewById(R.id.mail_confirm2);
        mail_confirm2.setText(getResources().getString(R.string.recheck2_mail2));


        String selectedValue4=fromConfirm_intent.getExtras().getString("selectedValue4");
        EditText editText4=findViewById(R.id.company_confirm);
        editText4.setText(selectedValue4);
        String finValue4=editText1.getText().toString();

        TextView company_confirm2 = findViewById(R.id.company_confirm2);
        company_confirm2.setText(getResources().getString(R.string.recheck2_company2));

    }


}
