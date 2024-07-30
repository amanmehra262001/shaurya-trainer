package com.example.shauryatrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton setsMinusButton;
    private ImageButton setsAddButton;
    private ImageButton workMinusButton;
    private ImageButton workAddButton;
    private ImageButton restMinusButton;
    private ImageButton restAddButton;
    private EditText setsNumber;
    private EditText workMinutes;
    private EditText workSeconds;
    private EditText restMinutes;
    private EditText restSeconds;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting references
        setsMinusButton = findViewById(R.id.sets_minus);
        setsAddButton = findViewById(R.id.sets_add);
        workMinusButton = findViewById(R.id.work_minus);
        workAddButton = findViewById(R.id.work_add);
        restMinusButton = findViewById(R.id.rest_minus);
        restAddButton = findViewById(R.id.rest_add);
        setsNumber = findViewById(R.id.sets_number);
        workMinutes = findViewById(R.id.work_minutes);
        workSeconds = findViewById(R.id.work_seconds);
        restMinutes = findViewById(R.id.rest_minutes);
        restSeconds = findViewById(R.id.rest_seconds);
        actionButton = findViewById(R.id.floatingActionButton);

        initialSetup();

        // Set up the OnClickListeners
        setsAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSets(1);
            }
        });
        setsMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSets(-1);
            }
        });
        workAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWorkTime(10);
            }
        });
        workMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWorkTime(-10);
            }
        });
        restAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRestTime(10);
            }
        });
        restMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRestTime(-10);
            }
        });
        // Set up click listener for floating action button
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchWorkRestActivity();
            }
        });
    }

    protected void initialSetup(){
        setsNumber.setText("00");
        workMinutes.setText("00");
        workSeconds.setText("00");
        restMinutes.setText("00");
        restSeconds.setText("00");
    }

    protected void updateSets(int increment){
        String currentText = setsNumber.getText().toString();
        int currentNumber;

        // Convert to integer, handling empty strings
        try {
            currentNumber = Integer.parseInt(currentText);
        } catch (NumberFormatException e) {
            currentNumber = 0; // Default to 0 if parsing fails
        }

        // Increment the number
        currentNumber+=increment;
        if(currentNumber<0) return;

        // Set the updated value back to the EditText
        setsNumber.setText(String.format("%02d", currentNumber));
    }

    // Method to update the work minutes and seconds by 10
    private void updateWorkTime(int increment) {
        // Increment seconds
        String workSecondsText = workSeconds.getText().toString();
        int seconds;
        try {
            seconds = Integer.parseInt(workSecondsText);
        } catch (NumberFormatException e) {
            seconds = 0;
        }
        seconds += increment;

        // Handle overflow for seconds
        if (seconds >= 60) {
            // Increment minutes
            String workMinutesText = workMinutes.getText().toString();
            int minutes;
            try {
                minutes = Integer.parseInt(workMinutesText);
            } catch (NumberFormatException e) {
                minutes = 0;
            }
            minutes += 1;
            workMinutes.setText(String.format("%02d", minutes));
            seconds = seconds % 60; // Keep only the remainder
            workSeconds.setText(String.format("%02d", seconds));
            return;
        }
        if(seconds<0){
            String workMinutesText = workMinutes.getText().toString();
            int minutes;
            try {
                minutes = Integer.parseInt(workMinutesText);
            } catch (NumberFormatException e) {
                minutes = 0;
            }
            minutes -= 1;
            if(minutes<0) return;
            workMinutes.setText(String.format("%02d", minutes));
            seconds += 60; // Keep only the remainder
            workSeconds.setText(String.format("%02d", seconds));
            return;
        }
        workSeconds.setText(String.format("%02d", seconds));
    }

    // Method to update the rest minutes and seconds by 10
    private void updateRestTime(int increment) {
        // Increment seconds
        String restSecondsText = restSeconds.getText().toString();
        int seconds;
        try {
            seconds = Integer.parseInt(restSecondsText);
        } catch (NumberFormatException e) {
            seconds = 0;
        }
        seconds += increment;

        // Handle overflow for seconds
        if (seconds >= 60) {
            // Increment minutes
            String restMinutesText = restMinutes.getText().toString();
            int minutes;
            try {
                minutes = Integer.parseInt(restMinutesText);
            } catch (NumberFormatException e) {
                minutes = 0;
            }
            minutes += 1;
            restMinutes.setText(String.format("%02d", minutes));
            seconds = seconds % 60; // Keep only the remainder
            restSeconds.setText(String.format("%02d", seconds));
            return;
        }
        if(seconds<0){
            String restMinutesText = restMinutes.getText().toString();
            int minutes;
            try {
                minutes = Integer.parseInt(restMinutesText);
            } catch (NumberFormatException e) {
                minutes = 0;
            }
            minutes -= 1;
            if (minutes<0) return;
            restMinutes.setText(String.format("%02d", minutes));
            seconds += 60; // Keep only the remainder
            restSeconds.setText(String.format("%02d", seconds));
            return;
        }
        restSeconds.setText(String.format("%02d", seconds));
    }

    private void launchWorkRestActivity() {
        Intent intent = new Intent(MainActivity.this, WorkRestActivity.class);
        intent.putExtra("setsNumber", setsNumber.getText().toString());
        intent.putExtra("workMinutes", workMinutes.getText().toString());
        intent.putExtra("workSeconds", workSeconds.getText().toString());
        intent.putExtra("restMinutes", restMinutes.getText().toString());
        intent.putExtra("restSeconds", restSeconds.getText().toString());
        startActivity(intent);
    }
}