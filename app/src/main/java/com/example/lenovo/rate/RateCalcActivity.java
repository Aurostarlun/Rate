package com.example.lenovo.rate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class RateCalcActivity extends AppCompatActivity {

    float rate = 0f;
    String TAG = "rateCalcActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_calc);
        String title = getIntent().getStringExtra("title");
        rate  = getIntent().getFloatExtra("rate",0f);

        Log.i(TAG,"onCreate:title="+title);
        Log.i(TAG,"onCreate:rate="+rate);
        ((TextView)findViewById(R.id.title2)).setText(title);
        EditText inp2;
        inp2 = findViewById(R.id.inp2);
        inp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView show2 = RateCalcActivity.this.findViewById(R.id.show2);
                if (s.length()>0){
                    float val = Float.parseFloat(s.toString());
                    show2.setText(100/rate*val+"");
                }else {
                    show2.setText("");
                }
            }
        });
    }
}