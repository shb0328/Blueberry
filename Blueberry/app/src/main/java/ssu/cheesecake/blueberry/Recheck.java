package ssu.cheesecake.blueberry;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Recheck extends AppCompatActivity{


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            final String[] name={"1번", "2번"}; //이거는 머신러닝에서 값에 따라 달라지게 하려함
            final String[] phonenumber={"010","0103"};//이거도 정수로 해야하나 문자인가 모르겠다
            final String[] company={"회사1","회사2"};
            final String[] address={"주소1", "주소2"};

            final Spinner spin1=(Spinner) findViewById(R.id.spinner1);
            final Spinner spin2=(Spinner) findViewById(R.id.spinner2);
            final Spinner spin3=(Spinner) findViewById(R.id.spinner3);
            final Spinner spin4=(Spinner) findViewById(R.id.spinner4);

            ArrayAdapter adapter= new ArrayAdapter(){
                getApplicationContext(),
            }

        }

}
