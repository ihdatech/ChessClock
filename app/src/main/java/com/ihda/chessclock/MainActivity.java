package com.ihda.chessclock;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.SystemClock;
import java.lang.Runnable;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {
    private Button pauseButton, resetButton;
    TextView textPlayerOne, playerOneTime, playerTwoTime;
    ColorStateList defaultTextColor, lightTextColor;

    CountDownTimer playerOneTimer, playerTwoTimer;

    int currentPlayer = 0, timeInMilliseconds = 0;
    long playerOneTimeLeft = 0L, playerTwoTimeLeft = 0L;
    boolean doubleBackToExitPressedOnce = false;

    private Handler customHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pauseButton = (Button) findViewById(R.id.pause_button);
        resetButton = (Button) findViewById(R.id.reset_button);
        textPlayerOne = (TextView) findViewById(R.id.player1_text);
        playerOneTime = (TextView) findViewById(R.id.player1_time);
        playerTwoTime = (TextView) findViewById(R.id.player2_time);
        playerOneTime.setText("03:00");
        playerTwoTime.setText("03:00");
        textPlayerOne.setRotation(180);
        playerOneTime.setRotation(180);
        defaultTextColor = playerOneTime.getTextColors();
        playerOneTime.setTextColor(Color.parseColor("#d8d1d1"));
        playerTwoTime.setTextColor(Color.parseColor("#d8d1d1"));
        lightTextColor = playerTwoTime.getTextColors();
        playerOneTime.setTextColor(defaultTextColor);
        playerTwoTime.setTextColor(defaultTextColor);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPlayer == 1) {
                    playerTwoTimer.cancel();
                } else if (currentPlayer == 2) {
                    playerOneTimer.cancel();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerOneTime.setTextColor(defaultTextColor);
                playerTwoTime.setTextColor(defaultTextColor);
                playerOneTime.setText("03:00");
                playerTwoTime.setText("03:00");
                timeInMilliseconds = 180000;
                if (currentPlayer == 1) {
                    playerTwoTimer.cancel();
                    currentPlayer = 0;
                } else if (currentPlayer == 2) {
                    playerOneTimer.cancel();
                    currentPlayer = 0;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void startTimerByPlayerOne(View v) {
        if (currentPlayer == 0){
            playerOneTime.setTextColor(lightTextColor);
            timeInMilliseconds = 180000;
            playerTwoTimer = new CountDownTimer(timeInMilliseconds, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    long milliseconds = (millisUntilFinished % 1000);
                    playerTwoTimeLeft = millisUntilFinished;
                    playerTwoTime.setText(String.format("%02d:%02d", minutes, seconds));
                    if (millisUntilFinished > 30000){
                        playerTwoTime.setTextColor(defaultTextColor);
                    } else if (millisUntilFinished < 30000){
                        playerTwoTime.setTextColor(Color.RED);
                    } else if (millisUntilFinished >= 10000){
                        playerTwoTime.setText(String.format("%02d:%02d", minutes, seconds));
                    } else if (millisUntilFinished < 10000){
                        playerTwoTime.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
                    }
                }
                public void onFinish(){
                    playerTwoTime.setTextColor(lightTextColor);
                    playerTwoTime.setText("Time's up!");
                    playerTwoTimer.cancel();
                }
            }.start();
            playerOneTimeLeft = timeInMilliseconds;
            currentPlayer = 1;
        } else if (currentPlayer == 2){
            playerOneTime.setTextColor(lightTextColor);
            playerOneTimer.cancel();
            playerTwoTimer = new CountDownTimer(playerOneTimeLeft, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    long milliseconds = (millisUntilFinished % 1000);
                    playerTwoTimeLeft = millisUntilFinished;
                    playerTwoTime.setText(String.format("%02d:%02d", minutes, seconds));
                    if (playerTwoTimeLeft > 30000){
                        playerTwoTime.setTextColor(defaultTextColor);
                    } else if (playerTwoTimeLeft < 30000){
                        playerTwoTime.setTextColor(Color.RED);
                    } else if (playerTwoTimeLeft >= 10000){
                        playerTwoTime.setText(String.format("%02d:%02d", minutes, seconds));
                    } else if (playerTwoTimeLeft < 10000){
                        playerTwoTime.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
                    }
                }
                public void onFinish(){
                    playerTwoTime.setTextColor(lightTextColor);
                    playerTwoTime.setText("Time's up!");
                    playerTwoTimer.cancel();
                }
            }.start();
            currentPlayer = 1;
        }
    }

    public void startTimerByPlayerTwo(View v) {
        if(currentPlayer == 0){
            playerTwoTime.setTextColor(lightTextColor);
            timeInMilliseconds = 180000;
            playerOneTimer = new CountDownTimer(timeInMilliseconds, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    long milliseconds = (millisUntilFinished % 1000);
                    playerOneTimeLeft = millisUntilFinished;
                    playerOneTime.setText(String.format("%02d:%02d", minutes, seconds));
                    if (playerOneTimeLeft > 30000){
                        playerOneTime.setTextColor(defaultTextColor);
                    } else if (playerOneTimeLeft < 30000){
                        playerOneTime.setTextColor(Color.RED);
                    } else if (playerOneTimeLeft >= 10000){
                        playerOneTime.setText(String.format("%02d:%02d", minutes, seconds));
                    } else if (playerOneTimeLeft < 10000){
                        playerOneTime.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
                    }
                }
                public void onFinish(){
                    playerOneTime.setTextColor(lightTextColor);
                    playerOneTime.setText("Time's up!");
                    playerOneTimer.cancel();
                }
            }.start();
            playerTwoTimeLeft = timeInMilliseconds;
            currentPlayer = 2;
        } else if (currentPlayer == 1){
            playerTwoTime.setTextColor(lightTextColor);
            playerTwoTimer.cancel();
            playerOneTimer = new CountDownTimer(playerOneTimeLeft, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = (millisUntilFinished / 1000) % 60;
                    long minutes = (millisUntilFinished / 1000) / 60;
                    long milliseconds = (millisUntilFinished % 1000);
                    playerOneTime.setText(String.format("%02d:%02d", minutes, seconds));
                    if (playerOneTimeLeft > 30000){
                        playerOneTime.setTextColor(defaultTextColor);
                        playerOneTimeLeft = millisUntilFinished;
                    } else if (playerOneTimeLeft < 30000){
                        playerOneTime.setTextColor(Color.RED);
                        playerOneTimeLeft = millisUntilFinished;
                    } else if (playerOneTimeLeft >= 10000){
                        playerOneTime.setText(String.format("%02d:%02d", minutes, seconds));
                        playerOneTimeLeft = millisUntilFinished;
                    } else if (playerOneTimeLeft < 10000){
                        playerOneTime.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
                        playerOneTimeLeft = millisUntilFinished;
                    }
                }
                public void onFinish(){
                    playerOneTime.setTextColor(lightTextColor);
                    playerOneTime.setText("Time's up!");
                    playerOneTimer.cancel();
                }
            }.start();
            currentPlayer = 2;
        }
    }
}