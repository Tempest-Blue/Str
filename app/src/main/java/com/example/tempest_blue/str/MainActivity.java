package com.example.tempest_blue.str;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.tempest_blue.str.R.style.Tempest;

public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }
    }

    // Validate that weights have been entered.
    public boolean validateInput(EditText weights) {
        if (weights.getText().toString().isEmpty()) {
            weights.setError("Input Required!");
            return false;
        }
        else if (Integer.parseInt(weights.getText().toString()) < 45) {
            weights.setError("Minimum weight >= 45!");
            return false;
        }
        return true;
    }

    // Begin Workout button onclick event
    public void beginWorkout(View view) {
        EditText squatWeight = (EditText) findViewById(R.id.squatWeight);
        EditText benchWeight = (EditText) findViewById(R.id.benchWeight);
        EditText deadliftWeight = (EditText) findViewById(R.id.deadliftWeight);
        EditText militaryWeight = (EditText) findViewById(R.id.militaryWeight);

        // Validate input and send to next activity
        if (validateInput(squatWeight) && validateInput(benchWeight) && validateInput(deadliftWeight) && validateInput(militaryWeight)) {
            Bundle extras = new Bundle();
            String value = squatWeight.getText().toString();
            int weight = Integer.parseInt(value);
            extras.putInt("SQUAT_WEIGHT", weight);

            value = benchWeight.getText().toString();
            weight = Integer.parseInt(value);
            extras.putInt("BENCH_WEIGHT", weight);

            value = deadliftWeight.getText().toString();
            weight = Integer.parseInt(value);
            extras.putInt("DEADLIFT_WEIGHT", weight);

            value = militaryWeight.getText().toString();
            weight = Integer.parseInt(value);
            extras.putInt("MILITARY_WEIGHT", weight);

            int day = 1;
            extras.putInt("DAY", day);

            Intent intent = new Intent(this, CalculateWeight.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }
}
