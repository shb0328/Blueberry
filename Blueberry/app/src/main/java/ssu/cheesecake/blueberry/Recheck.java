package ssu.cheesecake.blueberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Recheck extends AppCompatActivity{





        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            final String[] name={"1번", "2번"}; //이거는 머신러닝에서 값에 따라 달라지게 하려함
            final String[] phone_number={"010","0103"};//이거도 정수로 해야하나 문자인가 모르겠다
            final String[] company={"회사1","회사2"};
            final String[] address={"주소1", "주소2"};

            final Spinner spin1=findViewById(R.id.spinner1);
            final Spinner spin2=findViewById(R.id.spinner2);
            final Spinner spin3=findViewById(R.id.spinner3);
            final Spinner spin4=findViewById(R.id.spinner4);

            ArrayAdapter adapter1= new ArrayAdapter(getApplicationContext(),R.layout.spin, name);
            adapter1.setDropDownViewResource(R.layout.spin_dripdown);
            spin1.setAdapter(adapter1);

            ArrayAdapter adapter2= new ArrayAdapter(getApplicationContext(), R.layout.spin, phone_number);
            adapter2.setDropDownViewResource(R.layout.spin_dripdown);
            spin2.setAdapter(adapter2);

            ArrayAdapter adapter3= new ArrayAdapter(getApplicationContext(),R.layout.spin, company);
            adapter3.setDropDownViewResource(R.layout.spin_dripdown);
            spin3.setAdapter(adapter3);

            ArrayAdapter adapter4= new ArrayAdapter(getApplicationContext(),R.layout.spin,address);
            adapter4.setDropDownViewResource(R.layout.spin_dripdown);
            spin4.setAdapter(adapter4);

        }





        public void onClick(View view)
        {
            Intent intent= new Intent(this, ReCheck2.class);

        }//intent 자꾸 오류 나서 일단 두번째 페이지부터 만들겠음

}
