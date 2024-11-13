package com.example.reactiontime;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("all")
public class ColorTestGame extends AppCompatActivity {

    ArrayList<String> testLeft = new ArrayList<>();
    ArrayList<Long> testResultMs = new ArrayList<>();
    private RelativeLayout gameMenu, gamePlay;
    private Button btnStartTest, submitButton1, submitButton2, submitButton3, restartButton;
    private TextView resultTextBox, gameTestTitle;
    private CardView mainColorBoxToGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_response_time);

        // Finding all views by ID
        gameMenu = findViewById(R.id.gameMenu);
        gamePlay = findViewById(R.id.gamePlay);
        btnStartTest = findViewById(R.id.btnStartTest);
        submitButton1 = findViewById(R.id.submitButton1);
        submitButton2 = findViewById(R.id.submitButton2);
        submitButton3 = findViewById(R.id.submitButton3);
        restartButton = findViewById(R.id.restartButton);
        resultTextBox = findViewById(R.id.resultTextBox);
        gameTestTitle = findViewById(R.id.gameTestTitle);
        mainColorBoxToGuess = findViewById(R.id.mainColorBoxToGuess);


        //set click listenears
        restartGame();
        restartButton.setOnClickListener(v -> restartGame());


    }

    void restartGame() {
        // setDef
        gameMenu.setVisibility(View.VISIBLE);
        gamePlay.setVisibility(View.GONE);
        testLeft = getAllColors();
        testResultMs = new ArrayList<>();


        btnStartTest.setOnClickListener(v -> {
            gameTest();
        });
    }

    void gameTest() {
        int testCaseNumber = getAllColors().size() - testLeft.size() + 1;
        gameMenu.setVisibility(View.GONE);
        gamePlay.setVisibility(View.VISIBLE);

        String currentColor = testLeft.get(rand(1, testLeft.size()) - 1);
        testLeft.remove(currentColor);
        mainColorBoxToGuess.setCardBackgroundColor(Color.BLACK);
        blankSimulations();
        gameTestTitle.setText("Color Test " + String.valueOf(testCaseNumber));


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    long startMillis = CurrentMillis();

                    ArrayList<Button> buttons = simulateCurrentColor(currentColor);


                    buttons.get(0).setOnClickListener(v1 -> {
                        long responseTime = CurrentMillis() - startMillis;
                        testResultMs.add(responseTime);
                        gameTestTitle.setText("Response time: " + String.valueOf(responseTime) + "ms");
                        blankSimulations();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(() -> {
                                    if (testCaseNumber >= getAllColors().size()) {
                                        showTestResult();
                                    } else {
                                        gameTest();
                                    }
                                });
                            }
                        }, 2500);
                    });
                    buttons.get(1).setOnClickListener(v -> wrongAnswer());
                    buttons.get(2).setOnClickListener(v -> wrongAnswer());
                });
            }
        }, rand(1500, 3000));
    }

    void showTestResult() {
        String out = "<small>Response times:<br>";
        Long total = 0L, average = 0L, divider = Long.valueOf(testResultMs.size());
        for (int i = 0; i < testResultMs.size(); i++) {
            total += testResultMs.get(i);
            out += ("Test " + i + ": " + testResultMs.get(i) + "ms<br>");
        }
        average = total / divider;
        out = ("<br>Your average response time is<br><h1>" + average + "ms</h1>" + out + "</small>");
        gameTestTitle.setText(Html.fromHtml(out));
    }


    ArrayList<String> getAllColors() {
        ArrayList<String> newColorList = new ArrayList<>();
        newColorList.add("Red");
        newColorList.add("Green");
        newColorList.add("Blue");
        return newColorList;
    }

    ArrayList<Button> simulateCurrentColor(String currentColor) {
        ArrayList<Button> buttons = buttonArrayList();
        mainColorBoxToGuess.setCardBackgroundColor(getColorByString(currentColor));
        buttons.get(0).setText(currentColor);
        buttons.get(0).setBackgroundColor(getColorByString(getOutOfBoundColors(currentColor).get(1)));
        buttons.get(0).setVisibility(View.VISIBLE);
        buttons.get(1).setText(getOutOfBoundColors(currentColor).get(0));
        buttons.get(1).setBackgroundColor(getColorByString(currentColor));
        buttons.get(1).setVisibility(View.VISIBLE);
        buttons.get(2).setText(getOutOfBoundColors(currentColor).get(1));
        buttons.get(2).setBackgroundColor(getColorByString(getOutOfBoundColors(currentColor).get(0)));
        buttons.get(2).setVisibility(View.VISIBLE);
        return buttons;
    }

    ArrayList<String> getOutOfBoundColors(String excludeColor) {
        ArrayList<String> colors = new ArrayList<>();
        for (String color : getAllColors()) {
            if (!color.equalsIgnoreCase(excludeColor)) {
                colors.add(color);
            }
        }
        return colors;
    }


    public ArrayList<Button> buttonArrayList() {
        ArrayList<Button> buttonArrayList = new ArrayList<>();
        buttonArrayList.add(submitButton1);
        buttonArrayList.add(submitButton2);
        buttonArrayList.add(submitButton3);
        Collections.shuffle(buttonArrayList);
        return buttonArrayList;
    }

    void blankSimulations() {
        mainColorBoxToGuess.setCardBackgroundColor(Color.BLACK);
        for (Button button : buttonArrayList()) {
            button.setText("Wait");
            button.setBackgroundColor(Color.BLACK);
            button.setOnClickListener(null);
            button.setVisibility(View.GONE);
        }
    }

    void wrongAnswer() {
        gameTestTitle.setText("Wrong Answer\n\nPlease restart the game");
        blankSimulations();
    }

    ;

    int getColorByString(String colorName) {
        switch (colorName) {
            case "Red":
                return Color.RED;
            case "Green":
                return Color.GREEN;
            case "Blue":
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }


    long CurrentMillis() {
        return System.currentTimeMillis();
    }

    int rand(int from, int to) {
        Random random = new Random();
        return from + random.nextInt(to - from + 1);
    }
}
