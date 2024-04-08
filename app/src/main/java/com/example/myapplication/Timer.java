package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import Clocker.Clock;
import Dialogs.SetTimerFragment;
import Styles.Themes;

public class Timer extends AppCompatActivity {
    private final Clock clock = new Clock();
    private TextView timerDuration;
    private Button setTimerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        timerDuration = findViewById(R.id.timerDuration);
        setTimerButton = findViewById(R.id.setTimerButton);

        setTimerButton.setOnClickListener(v -> {
            SetTimerFragment setTimer = new SetTimerFragment();
            setTimer.show(getSupportFragmentManager(), "set timer");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        timerDuration.setText(clock.getTimeString());
        setTheme();
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.timer));
        Themes.setButtonTheme(this, setTimerButton);
    }
}