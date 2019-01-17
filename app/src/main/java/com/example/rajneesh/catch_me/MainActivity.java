package com.example.rajneesh.catch_me;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView[][] image = new ImageView[3][3];
    ImageView temp_imageView;
    ImageView clickedImage;
    TextView textViewScore,textViewMiss;
    TextView textView_gameOver;
    Button buttonStart;

    CountDownTimer countDownTimer;
    
    private long countdown_time = 1000;
    private int miss=0;
    private int score=0;
    private int red_row=0;
    private int red_column=0;
    private int GREEN_IMG_RESOURCE= R.drawable.green;
    private int RED_IMG_RESOURCE = R.drawable.heart;
    private int BREAK_HEART = R.drawable.broken_heart;
    private boolean catched = false;
    private boolean gameOver = true;
    private long systemTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

    }

    private void initialization()
    {
        textViewMiss = (TextView)findViewById(R.id.text_view_miss);
        textViewScore= (TextView)findViewById(R.id.text_view_score);
        buttonStart = (Button)findViewById(R.id.start_button);
        textView_gameOver = (TextView)findViewById(R.id.gameOver);

        for(int i=0; i<3 ; i++)
        {
            for(int j=0; j<3 ;j++)
            {
                String image_id = "image"+i+j;
                int id = getResources().getIdentifier(image_id,"id",getPackageName());

                image[i][j] = (ImageView) findViewById(id);
                image[i][j].setOnClickListener(this);
            }
        }


    }


    @Override
    public void onClick(View view) {

        if(!gameOver){

            clickedImage = (ImageView) findViewById(view.getId());

            if(clickedImage.getId() == image[red_row][red_column].getId())
            {
                clickedImage.setImageResource(BREAK_HEART);
                catched = true;
                score++;
                decreaseCountdown();
                updateScore();

            }



        }



    }

    private void decreaseCountdown() {
        if(countdown_time > 800)
        {
            countdown_time -= 20;
        }
        else if(countdown_time > 600)
        {
            countdown_time -= 7;
        }
        else if(countdown_time > 550)
        {
            countdown_time -= 5;
        }
        else if (countdown_time > 500)
        {
            countdown_time -= 3;
        }
        else if(countdown_time > 450)
        {
            countdown_time -= 2;
        }
        else countdown_time--;
    }

    private void updateScore() {
        textViewScore.setText("Score : "+score);
    }

    public void start_game(View view) {

        gameOver = false;

        if(buttonStart.getText().toString().equals("Finish"))
        {
            finish();
        }
        else {
            nextChance();
            buttonStart.setVisibility(View.INVISIBLE);
            buttonStart.setEnabled(false);
        }


    }

    private void countdownStart() {
        countDownTimer = new CountDownTimer(countdown_time,1) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                    if(!catched)
                    {
                        miss++;
                        updateMiss();
                        if(miss >=5 )
                        {
                            gameOver();

                        }
                    }

                        nextChance();


            }
        }.start();
    }

    private void gameOver() {

        gameOver = true;
        if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }
        Intent intent = new Intent();
        intent.putExtra("score",score);
        setResult(RESULT_OK,intent);

        textView_gameOver.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.VISIBLE);
        buttonStart.setEnabled(true);
        buttonStart.setText("Finish");


    }

    private void updateMiss() {
        textViewMiss.setText("Miss : "+miss);
    }

    private void nextChance() {

        if(!gameOver){
            if(countDownTimer != null)
            {
                countDownTimer.cancel();
            }
            catched = false;
            countdownStart();
            refreshImages();
        }

    }

    private void refreshImages() {

        for (int i=0 ; i<3 ; i++)
        {
            for (int j= 0 ; j<3 ; j++)
            {
                image[i][j].setImageResource(GREEN_IMG_RESOURCE);
                image[i][j].setVisibility(View.INVISIBLE);
            }
        }
        temp_imageView = getRandomImageView(image);

        temp_imageView.setImageResource(RED_IMG_RESOURCE);
        temp_imageView.setVisibility(View.VISIBLE);
    }

    private ImageView getRandomImageView(ImageView[][] image) {

        red_row = new Random().nextInt(3);
        red_column = new Random().nextInt(3);
        return image[red_row][red_column];
    }

    @Override
    protected void onDestroy() {
        if(countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if(systemTime+2000 > System.currentTimeMillis())
        {
            finishGame();
        }
        else
        {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        systemTime = System.currentTimeMillis();

    }

    private void finishGame() {
        gameOver = true;
        if(countDownTimer != null)
        {
            countDownTimer.cancel();
        }
        Intent intent = new Intent();
        intent.putExtra("score",score);
        setResult(RESULT_OK,intent);

        textView_gameOver.setVisibility(View.VISIBLE);
        buttonStart.setVisibility(View.VISIBLE);
        buttonStart.setEnabled(true);
        buttonStart.setText("Finish");


        finish();
    }
}
