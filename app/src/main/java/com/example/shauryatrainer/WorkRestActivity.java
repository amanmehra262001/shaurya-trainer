package com.example.shauryatrainer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WorkRestActivity extends AppCompatActivity {

    private TextView setsLeft;
    private TextView timeMinutes;
    private TextView timeSeconds;
    private TextView currentPhase;
    private LinearLayout layout;
    private Button holdToExitButton;

    private int setsNumber;
    private int workMinutes;
    private int workSeconds;
    private int restMinutes;
    private int restSeconds;

    private CountDownTimer timer;
    private boolean isPreparing = true;
    private boolean isWorkPhase = false;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable exitRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(WorkRestActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_rest);

        // Initialize views
        setsLeft = findViewById(R.id.sets_left);
        timeMinutes = findViewById(R.id.time_minutes);
        timeSeconds = findViewById(R.id.time_seconds);
        currentPhase = findViewById(R.id.current_phase);
        layout = findViewById(R.id.linearLayout); // Updated id
        holdToExitButton = findViewById(R.id.hold_to_exit);

        // Retrieve data from the intent
        Intent intent = getIntent();
        setsNumber = Integer.parseInt(intent.getStringExtra("setsNumber"));
        workMinutes = Integer.parseInt(intent.getStringExtra("workMinutes"));
        workSeconds = Integer.parseInt(intent.getStringExtra("workSeconds"));
        restMinutes = Integer.parseInt(intent.getStringExtra("restMinutes"));
        restSeconds = Integer.parseInt(intent.getStringExtra("restSeconds"));

        // Hold button to exit
        holdToExitButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.postDelayed(exitRunnable, 1000);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(exitRunnable);
                        return true;
                }
                return false;
            }
        });

        // Start the prepare timer
        startPrepareTimer();
    }

    private void startPrepareTimer() {
        currentPhase.setText("PREPARE");
        setsLeft.setText(String.format("%02d", setsNumber));
        timer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // You can update the UI here if needed
                int remainingSeconds = (int) (millisUntilFinished / 1000);
                int mins = remainingSeconds / 60;
                int secs = remainingSeconds % 60;
                timeMinutes.setText(String.format("%02d", mins));
                timeSeconds.setText(String.format("%02d", secs+1));
            }

            @Override
            public void onFinish() {
                layout.setBackgroundColor(getResources().getColor(R.color.work_color));
                startWorkCycle();
            }
        }.start();
    }

    private void startWorkCycle() {
        isPreparing = false;
        isWorkPhase = true;
        currentPhase.setText("WORK");
        startTimer(workMinutes, workSeconds, true);
    }

    private void startRestCycle() {
        layout.setBackgroundColor(getResources().getColor(R.color.rest_color));
        currentPhase.setText("REST");
        startTimer(restMinutes, restSeconds, false);
    }

    private void startTimer(int minutes, int seconds, boolean isWork) {
        int totalSeconds = minutes * 60 + seconds;

        timer = new CountDownTimer(totalSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int remainingSeconds = (int) (millisUntilFinished / 1000);
                int mins = remainingSeconds / 60;
                int secs = remainingSeconds % 60;
                timeMinutes.setText(String.format("%02d", mins));
                timeSeconds.setText(String.format("%02d", secs+1));
            }

            @Override
            public void onFinish() {
                if (isWork) {
                        layout.setBackgroundColor(getResources().getColor(R.color.rest_color));
                        startRestCycle();
                } else {
                    setsNumber--;
                    if (setsNumber > 0) {
                        setsLeft.setText(String.valueOf(setsNumber));
                        // Switch back to work phase after rest
                        layout.setBackgroundColor(getResources().getColor(R.color.work_color));
                        startWorkCycle();
                    } else {
                        // If sets are done, finish activity
                        finish();
                    }
                }
            }
        }.start();
    }
}
