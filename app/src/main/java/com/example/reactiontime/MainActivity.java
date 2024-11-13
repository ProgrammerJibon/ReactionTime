package com.example.reactiontime;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("All")
public class MainActivity extends AppCompatActivity {

    private Button startButton, reactionButton;
    private TextView resultText;
    private long startTime;
    private RelativeLayout mainView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startButton = findViewById(R.id.startButton);
        reactionButton = findViewById(R.id.reactionButton);
        resultText = findViewById(R.id.resultText);
        mainView = findViewById(R.id.mainView);

        startButton.setOnClickListener(v -> startReactionTest());
        reactionButton.setOnClickListener(v -> calculateReactionTime());
    }


    private void startReactionTest() {
        startButton.setVisibility(View.GONE);
        resultText.setText("Wait for the prompt...");

        int randomDelay = new Random().nextInt(3000) + 2000;

        Runnable r = () -> {
            mainView.setOnTouchListener(null);
            startTime = System.currentTimeMillis();
            reactionButton.setVisibility(View.VISIBLE);
        };
        handler.postDelayed(r, randomDelay);

        mainView.setOnTouchListener((v, event) -> {
            mainView.setOnTouchListener(null);
            handler.removeCallbacks(r);
            reactionButton.setVisibility(View.GONE);
            resultText.setText("You're too early!");
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        startButton.setVisibility(View.VISIBLE);
                        resultText.setText("Try again...");
                    });
                }
            }, 2000);
            return true;
        });
    }

    private void calculateReactionTime() {
        mainView.setOnTouchListener(null);

        long reactionTime = System.currentTimeMillis() - startTime;
        resultText.setText("Reaction Time: " + reactionTime + " ms");

        reactionButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }
}
