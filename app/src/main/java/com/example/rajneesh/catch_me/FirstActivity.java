package com.example.rajneesh.catch_me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.ResultSet;

public class FirstActivity extends AppCompatActivity {

    TextView textView_score, textView_highscore;
    public static String SHARE_PREF = "sharedPref";

    private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        initialization();
        loadHighscore();


    }

    private void initialization() {

        textView_score = findViewById(R.id.text_view_score);
        textView_highscore = findViewById(R.id.text_view_highscore);

    }

    public void newGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == -1) {
                int score = data.getIntExtra("score", 0);
                textView_score.setText("Score :" + score);
                textView_score.setVisibility(View.VISIBLE);
                if(score > highscore)
                {
                    updateHighscore(score);
                }

            }
        }


    }

    private void loadHighscore()
    {
        SharedPreferences pref = getSharedPreferences(SHARE_PREF,MODE_PRIVATE);
        highscore = pref.getInt("highscore",0);
        textView_highscore.setText("Highscore : "+highscore);
    }

    public void updateHighscore( int score)
    {
        highscore = score;
        textView_highscore.setText("Highscore : "+highscore);
        SharedPreferences pref = getSharedPreferences(SHARE_PREF,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("highscore",highscore);
        editor.apply();
    }


}
