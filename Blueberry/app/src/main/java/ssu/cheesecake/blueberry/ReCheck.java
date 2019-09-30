package ssu.cheesecake.blueberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReCheck extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check);

        Button toConfirm_page=findViewById(R.id.finishButton1);
        toConfirm_page.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toConfirm_intent=new Intent(ReCheck.this,ReCheck2.class);
                startActivity(toConfirm_intent);
                finish();
            }

        });
    }
}
