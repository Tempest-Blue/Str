package com.example.tempest_blue.str;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import static android.R.color.white;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;

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

        // Define toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getColor(white));

        Context context = this;
        SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
        Boolean firstTime;
        firstTime = sharedPref.getBoolean("firstTime", false);
        EditText squatWeight = (EditText) findViewById(R.id.squatWeight);
        squatWeight.setText(Integer.toString(sharedPref.getInt("squatWeight", 45)));
        EditText benchWeight = (EditText) findViewById(R.id.benchWeight);
        benchWeight.setText(Integer.toString(sharedPref.getInt("benchWeight", 45)));
        EditText deadliftWeight = (EditText) findViewById(R.id.deadliftWeight);
        deadliftWeight.setText(Integer.toString(sharedPref.getInt("deadliftWeight", 45)));
        EditText militaryWeight = (EditText) findViewById(R.id.militaryWeight);
        militaryWeight.setText(Integer.toString(sharedPref.getInt("militaryWeight", 45)));
        if (!firstTime) {
            startActivity(new Intent(this, CalculateWeight.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the your_fab_buttons; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_map:
                startActivity(new Intent(this, FindGym.class));
                return true;
            case R.id.action_about:
                // Make text view and make link clickable
                final TextView message = new TextView(this);
                message.setPadding(75,0,0,0);
                final SpannableString messageText = new SpannableString(this.getText(R.string.action_about));
                Linkify.addLinks(messageText, Linkify.WEB_URLS);
                message.setText(messageText);
                message.setMovementMethod(LinkMovementMethod.getInstance());

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("About the developer").setView(message)
                        .setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
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

            // Set first time to false
            Context context = this;
            SharedPreferences sharedPref = getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("firstTime", true);
            editor.putInt("day",1);
            editor.apply();

            Intent intent = new Intent(this, CalculateWeight.class);
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        }
    }


}
