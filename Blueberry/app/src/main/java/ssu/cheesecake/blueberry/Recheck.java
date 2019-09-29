package ssu.cheesecake.blueberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Recheck extends AppCompatActivity {

    public ImageView Recheck_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Recheck_imageView = findViewById(R.id.business_card_confirm);
        Recheck_imageView.setImageResource(R.drawable.testcard);


        Spinner spinner1 = findViewById(R.id.spinner1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinner2 = findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinner3 = findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Spinner spinner4 = findViewById(R.id.spinner4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        TextView textView1 = findViewById(R.id.textView1);
        textView1.setText(getResources().getString(R.string.recheck_name));

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(getResources().getString(R.string.recheck_phone));

        TextView textView3 = findViewById(R.id.textView3);
        textView3.setText(getResources().getString(R.string.recheck_company));

        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText(getResources().getString(R.string.recheck_email));


        Button finishButton1 = findViewById(R.id.finishButton1);
        finishButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToR2 = new Intent(Recheck.this, ReCheck2.class);
                startActivity(intentToR2);
                finish();
            }
        });


    }


}
