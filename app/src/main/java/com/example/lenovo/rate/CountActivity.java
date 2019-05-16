package com.example.lenovo.rate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class CountActivity extends AppCompatActivity {
    TextView score;
    TextView score2;
    String TAG = "CountActivity:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        score = findViewById(R.id.score);
        score2 = findViewById(R.id.score2);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.score)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.score2)).getText().toString();

        Log.i(TAG,"onSaveInstanceState:");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");

        Log.i(TAG,"onRestoreInstanceState:");
        ((TextView)findViewById(R.id.score)).setText(scorea);
        ((TextView)findViewById(R.id.score2)).setText(scoreb);
    }

    public void btn_add1(View btn)
    {
        if(btn.getId()==R.id.btn_1)
        {showScore(1);}
        else{showScore2(1);}
    }

    public void btn_add2(View btn)
    {
        if(btn.getId()==R.id.btn_2)
        {showScore(2);}
        else{showScore2(2);}
    }

    public void btn_add3(View btn)
    {
        if(btn.getId()==R.id.btn_3)
        {showScore(3);}
        else{showScore2(3);}
    }

    public void btn_Reset(View btn)
    {
        score.setText("0");
        score2.setText("0");
    }

    private void showScore(int inc)
    {
        Log.i("show", "inc=" + inc);
        String oldScore = (String) score.getText();
        int newscore = Integer.parseInt(oldScore) + inc;
        score.setText("" + newscore);
    }

    private void showScore2(int inc)
    {
        Log.i("show", "inc=" + inc);
        String oldScore = (String) score2.getText();
        int newscore2 = Integer.parseInt(oldScore) + inc;
        score2.setText("" + newscore2);
    }
}