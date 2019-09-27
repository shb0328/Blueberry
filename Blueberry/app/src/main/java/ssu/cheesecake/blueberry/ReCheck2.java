package ssu.cheesecake.blueberry;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ReCheck2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check2);

        EditText editText1=findViewById(R.id.name_confirm);
        editText1.setText("The text1");

        EditText editText2=findViewById(R.id.phone_confirm);
        editText2.setText("The text2");

        EditText editText3=findViewById(R.id.mail_confirm);
        editText3.setText("The text3");

        EditText editText4=findViewById(R.id.company_confirm);
        editText4.setText("The text4");
    }


}
