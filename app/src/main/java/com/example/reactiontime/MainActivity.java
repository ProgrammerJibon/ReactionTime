package com.example.reactiontime;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

import kotlin.Suppress;

@SuppressLint("All")
public class MainActivity extends AppCompatActivity {

    private Button startButton, reactionButton;
    private TextView resultText;
    private long startTime;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        reactionButton = findViewById(R.id.reactionButton);
        resultText = findViewById(R.id.resultText);

        startButton.setOnClickListener(v -> startReactionTest());
        reactionButton.setOnClickListener(v -> calculateReactionTime());
    }


    private void startReactionTest() {
        startButton.setVisibility(View.GONE);
        resultText.setText("Wait for the prompt...");

        int randomDelay = new Random().nextInt(2000) + 1000; // Between 1-3 seconds

        handler.postDelayed(() -> {
            startTime = System.currentTimeMillis();
            reactionButton.setVisibility(View.VISIBLE);
        }, randomDelay);
    }

    private void calculateReactionTime() {
        long reactionTime = System.currentTimeMillis() - startTime;
        resultText.setText("Reaction Time: " + reactionTime + " ms");

        reactionButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }
}
