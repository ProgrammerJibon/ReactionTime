package com.example.reactiontime;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        findViewById(R.id.easyTestLL).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        findViewById(R.id.hardTestLL).setOnClickListener(v -> {
            startActivity(new Intent(this, ColorTestGame.class));
        });
    }
}