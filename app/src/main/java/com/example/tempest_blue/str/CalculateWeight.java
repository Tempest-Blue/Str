package com.example.tempest_blue.str;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.R.color.white;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CalculateWeight extends AppCompatActivity {

    int squatWeight, benchWeight, deadliftWeight, militaryWeight, day;
    int originalSquatWeight, originalBenchWeight, originalDeadliftWeight, originalMilitaryWeight;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_weight);

        // Define toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getColor(white));

        // Get support action bar
        ActionBar actionBar = getSupportActionBar();

        // Enable the up (back) button
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
        }

        Context context = this;
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();

        Boolean firstTime = sharedPref.getBoolean("firstTime", false);
        if (firstTime) {
            // Get Input
            Intent intent = getIntent();
            Bundle extras = intent.getExtras();
            originalSquatWeight = extras.getInt("SQUAT_WEIGHT");
            originalBenchWeight = extras.getInt("BENCH_WEIGHT");
            originalDeadliftWeight = extras.getInt("DEADLIFT_WEIGHT");
            originalMilitaryWeight = extras.getInt("MILITARY_WEIGHT");
            squatWeight = originalSquatWeight;
            benchWeight = originalBenchWeight;
            deadliftWeight = originalDeadliftWeight;
            militaryWeight = originalMilitaryWeight;
            Log.d("squatWeight",Integer.toString(originalSquatWeight));
            EditText editText = (EditText) findViewById(R.id.day);
            editText.setText("1");
            editor.putBoolean("firstTime", false);
            editor.commit();
        }

        else {
            // Restore day and inputs from save file
            originalSquatWeight = sharedPref.getInt("squatWeight",45);
            originalBenchWeight = sharedPref.getInt("benchWeight",45);
            originalDeadliftWeight = sharedPref.getInt("deadliftWeight",45);
            originalMilitaryWeight = sharedPref.getInt("militaryWeight",45);
            day = sharedPref.getInt("day", 1);
            squatWeight = originalSquatWeight + (5 * (day - 1));
            benchWeight = originalBenchWeight + (5 * ((day - 1) / 2));
            deadliftWeight = originalDeadliftWeight + (15 * ((day - 1) / 3));
            militaryWeight = originalMilitaryWeight + (5 * ((day - 1) / 2));
            EditText editText = (EditText) findViewById(R.id.day);
            editText.setText(Integer.toString(day));
        }

        // Set listener on day number
        final EditText day = (EditText) findViewById(R.id.day);
        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && s.length() < 10) {
                    if (Integer.parseInt(s.toString()) > 36500) {
                        day.setError("Wow! You've been working out for more than 100 years!");
                    }
                    if (s.length() > 6 && s.length() < 9) {
                        day.setError("Getting ahead of ourselves there aren't we?");
                    }
                    if (s.length() == 9) {
                        day.setError("Are you immortal?");
                    }
                    setWeight();
                    recordDay();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Call exercise function
        calculateWeights();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the your_fab_buttons; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculate_weights,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Context context = this;
                SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("firstTime", true);
                editor.commit();
//                NavUtils.navigateUpFromSameTask(this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.action_map:
                startActivity(new Intent(this, FindGym.class));
                return true;
            case R.id.action_timer:
                startActivity(new Intent(this, CountdownTimer.class));
                return true;
            case R.id.action_about:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage(R.string.action_about).setTitle("About the devleoper");
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Calculate weights for exercises
    public void calculateWeights() {
        // Save all weights first
        Context context = this;
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        EditText editText = (EditText) findViewById(R.id.day);
        day = Integer.parseInt(editText.getText().toString());
        editor.putInt("squatWeight", squatWeight - (5 * (day - 1)));
        editor.putInt("benchWeight", benchWeight - (5 * ((day - 1) / 2)));
        editor.putInt("deadliftWeight", deadliftWeight - (15 * ((day - 1) / 3)));
        editor.putInt("militaryWeight", militaryWeight - (5 * ((day - 1) / 2)));
        editor.commit();

        TextView textView;
        double splitWeight;
        // Squats done every day so do calculation no matter what.
        textView = (TextView) findViewById(R.id.firstExercise);
        textView.setText("Squat");
        textView = (TextView) findViewById(R.id.firstExerciseFirstSet);
        textView.setText(Integer.toString(45));
        textView = (TextView) findViewById(R.id.firstExerciseSecondSet);
        splitWeight = (5 * (Math.floor(Math.abs((squatWeight*.4)/5))));
        textView.setText(displayWeight(splitWeight));
        textView = (TextView) findViewById(R.id.firstExerciseThirdSet);
        splitWeight = 5 * (Math.floor(Math.abs((squatWeight*.6)/5)));
        textView.setText(displayWeight(splitWeight));
        textView = (TextView) findViewById(R.id.firstExerciseFourthSet);
        splitWeight = 5 * (Math.floor(Math.abs((squatWeight*.8)/5)));
        textView.setText(displayWeight(splitWeight));
        textView = (TextView) findViewById(R.id.firstExerciseFifthSet);
        textView.setText(displayWeight(squatWeight));

        if ((day % 6) == 1) { // Mondays : Squat, Bench, Chin-Ups
            textView = (TextView) findViewById(R.id.secondExercise);
            textView.setText("Bench");
            textView = (TextView) findViewById(R.id.secondExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.secondExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((benchWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((benchWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((benchWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFifthSet);
            textView.setText(displayWeight(benchWeight));

            setPullUps(TRUE);
        }

        else if ((day % 6) == 2) { // Wednesdays : Squat, Press, Deadlifts
            textView = (TextView) findViewById(R.id.secondExercise);
            textView.setText("Military");
            textView = (TextView) findViewById(R.id.secondExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.secondExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((militaryWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((militaryWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((militaryWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFifthSet);
            textView.setText(displayWeight(militaryWeight));

            setPullUps(FALSE);
            textView = (TextView) findViewById(R.id.thirdExercise);
            textView.setText("Deadlift");
            textView = (TextView) findViewById(R.id.thirdExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.thirdExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((deadliftWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.thirdExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((deadliftWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.thirdExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((deadliftWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.thirdExerciseFifthSet);
            textView.setText(displayWeight(deadliftWeight));
        }

        else if ((day % 6) == 3){ // Fridays: Squat, Bench, Pull-Ups
            textView = (TextView) findViewById(R.id.secondExercise);
            textView.setText("Bench");
            textView = (TextView) findViewById(R.id.secondExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.secondExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((benchWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((benchWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((benchWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFifthSet);
            textView.setText(displayWeight(benchWeight));

            setPullUps(TRUE);
        }
        else if ((day % 6) == 4) { // Mondays: Squat, Press, Chin-Ups
            textView = (TextView) findViewById(R.id.secondExercise);
            textView.setText("Military");
            textView = (TextView) findViewById(R.id.secondExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.secondExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((militaryWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((militaryWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((militaryWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFifthSet);
            textView.setText(displayWeight(militaryWeight));

            setPullUps(TRUE);
        }

        else if ((day % 6) == 5) { // Wednesdays: Squat, Bench, Deadlift
            textView = (TextView) findViewById(R.id.secondExercise);
            textView.setText("Bench");
            textView = (TextView) findViewById(R.id.secondExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.secondExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((benchWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((benchWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((benchWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFifthSet);
            textView.setText(displayWeight(benchWeight));

            setPullUps(FALSE);
            textView = (TextView) findViewById(R.id.thirdExercise);
            textView.setText("Deadlift");
            textView = (TextView) findViewById(R.id.thirdExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.thirdExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((deadliftWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.thirdExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((deadliftWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.thirdExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((deadliftWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.thirdExerciseFifthSet);
            textView.setText(displayWeight(deadliftWeight));
        }

        else if ((day % 6) == 0) { // Friday: Squat, Press, Pull-Ups
            textView = (TextView) findViewById(R.id.secondExercise);
            textView.setText("Military");
            textView = (TextView) findViewById(R.id.secondExerciseFirstSet);
            textView.setText(Integer.toString(45));
            textView = (TextView) findViewById(R.id.secondExerciseSecondSet);
            splitWeight = (5 * (Math.floor(Math.abs((militaryWeight * .4) / 5))));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseThirdSet);
            splitWeight = 5 * (Math.floor(Math.abs((militaryWeight * .6) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFourthSet);
            splitWeight = 5 * (Math.floor(Math.abs((militaryWeight * .8) / 5)));
            textView.setText(displayWeight(splitWeight));
            textView = (TextView) findViewById(R.id.secondExerciseFifthSet);
            textView.setText(displayWeight(militaryWeight));

            setPullUps(TRUE);
        }
    }

    public void addFirstExerciseWeight (View view) {
        squatWeight += 5;
        originalSquatWeight += 5;
        calculateWeights();
    }

    public void addSecondExerciseWeight (View view) {
        TextView textView = (TextView) findViewById(R.id.secondExercise);
        if (textView.getText().toString() == "Bench") {
            benchWeight += 5;
            originalBenchWeight += 5;
            calculateWeights();
        }
        else {
            militaryWeight += 5;
            originalMilitaryWeight += 5;
            calculateWeights();
        }
    }

    public void addThirdExerciseWeight (View view) {
        TextView textView = (TextView) findViewById(R.id.thirdExercise);
        if (textView.getText().toString() == "Deadlift") {
            deadliftWeight += 5;
            originalDeadliftWeight += 5;
            calculateWeights();
        }
    }

    public void subFirstExerciseWeight (View view) {
        if (!(originalSquatWeight - 5 < 45)) {
            squatWeight -= 5;
            originalSquatWeight -= 5;
            calculateWeights();
        }
    }

    public void subSecondExerciseWeight (View view) {
        TextView textView = (TextView) findViewById(R.id.secondExercise);
        if (textView.getText().toString() == "Bench") {
            if (!(originalBenchWeight - 5 < 45)) {
                benchWeight -= 5;
                originalBenchWeight -= 5;
                calculateWeights();
            }
        }
        else {
            if (!(originalMilitaryWeight - 5 < 45)) {
                militaryWeight -= 5;
                originalMilitaryWeight -= 5;
                calculateWeights();
            }
        }
    }

    public void subThirdExerciseWeight (View view) {
        TextView textView = (TextView) findViewById(R.id.thirdExercise);
        if (textView.getText().toString() == "Deadlift") {
            if (!(originalDeadliftWeight - 5 < 45)) {
                deadliftWeight -= 5;
                originalDeadliftWeight -= 5;
                calculateWeights();
            }
        }
    }

    // Save the day for next time the user opens app
    public void recordDay() {
        EditText editText = (EditText) findViewById(R.id.day);
        day = Integer.parseInt(editText.getText().toString());
        Context context = this;
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("day",day);
        editor.commit();
    }

    // User clicks right arrow button
    public void nextDay(View view) {
        day += 1;
        EditText editText = (EditText) findViewById(R.id.day);
        editText.setText(Integer.toString(day));
    }

    // User clicks left arrow button
    public void previousDay(View view) {
        if (day > 1) {
            day -= 1;
            EditText editText = (EditText) findViewById(R.id.day);
            editText.setText(Integer.toString(day));
        }
    }

    // User manually changing the day
    public void setWeight() {
        EditText editText = (EditText) findViewById(R.id.day);
        day = Integer.parseInt(editText.getText().toString());
        squatWeight = originalSquatWeight + (5 * (day - 1));
        benchWeight = originalBenchWeight + (5 * ((day - 1) / 2));
        deadliftWeight = originalDeadliftWeight + (15 * ((day - 1) / 3));
        militaryWeight = originalMilitaryWeight + (5 * ((day - 1) / 2));
        calculateWeights();
    }

    // If weight is too low do not add split weight
    public String displayWeight(double splitWeight) {
        if (splitWeight > 45 ) {
            return (String)(((int)(splitWeight) + " | " + ((splitWeight - 45)/2)));
        }
        else {
            return (Integer.toString((int)(splitWeight)));
        }
    }

    // Changing the template based on whether it is pull-ups, chin-ups, or dead lifts
    public void setPullUps(boolean condition) {
        if (condition) {
            TextView textView;
            textView = (TextView) findViewById(R.id.thirdExercise);
            if ((day % 6) == 1 || (day % 6) == 4)
                textView.setText("Chin-Ups");
            else
                textView.setText("Pull-Ups");
            ImageButton imageButton = (ImageButton) findViewById(R.id.thirdExerciseAdd);
            imageButton.setVisibility(View.GONE);
            imageButton = (ImageButton) findViewById(R.id.thirdExerciseSub);
            imageButton.setVisibility(View.GONE);
            textView = (TextView) findViewById(R.id.thirdExerciseOne);
            textView.setText("3 Sets to failure");
            textView = (TextView) findViewById(R.id.thirdExerciseTwo);
            textView.setText("1st Set");
            textView = (TextView) findViewById(R.id.thirdExerciseThree);
            textView.setText("2nd Set");
            textView = (TextView) findViewById(R.id.thirdExerciseFour);
            textView.setText("3rd Set");
            textView = (TextView) findViewById(R.id.thirdExerciseFive);
            textView.setText("");
            textView = (TextView) findViewById(R.id.thirdExerciseFirstSet);
            textView.setText("");
            textView = (TextView) findViewById(R.id.thirdExerciseSecondSet);
            textView.setText("");
            textView = (TextView) findViewById(R.id.thirdExerciseThirdSet);
            textView.setText("");
            textView = (TextView) findViewById(R.id.thirdExerciseFourthSet);
            textView.setText("");
            textView = (TextView) findViewById(R.id.thirdExerciseFifthSet);
            textView.setText("");
        }
        else {
            TextView textView;
            ImageButton imageButton = (ImageButton) findViewById(R.id.thirdExerciseAdd);
            imageButton.setVisibility(View.VISIBLE);
            imageButton = (ImageButton) findViewById(R.id.thirdExerciseSub);
            imageButton.setVisibility(View.VISIBLE);
            textView = (TextView) findViewById(R.id.thirdExercise);
            textView.setText("");
            textView = (TextView) findViewById(R.id.thirdExerciseOne);
            textView.setText("2 x 5");
            textView = (TextView) findViewById(R.id.thirdExerciseTwo);
            textView.setText("1 x 5");
            textView = (TextView) findViewById(R.id.thirdExerciseThree);
            textView.setText("1 x 3");
            textView = (TextView) findViewById(R.id.thirdExerciseFour);
            textView.setText("1 x 2");
            textView = (TextView) findViewById(R.id.thirdExerciseFive);
            textView.setText("3 x 5");
        }
    }
}
