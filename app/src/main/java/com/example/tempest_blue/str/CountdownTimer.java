package com.example.tempest_blue.str;

import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import static android.R.color.white;

public class CountdownTimer extends AppCompatActivity {
    private static final String FORMAT = "%02dh%02dm%02ds";
    private boolean reset = false;
    private boolean paused = false;
    private boolean counting = false;
    private long initialTime = 0;
    private long timeRemaining = 0;
    Deque<Integer> queue = new ArrayDeque<>(6);
    int zeroes = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);

        // Define toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= 23) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.colorPrimaryDark));
            myToolbar.setTitleTextColor(getColor(white));
        }
        // Get support action bar
        ActionBar actionBar = getSupportActionBar();
        // Enable the up (back) button
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageButton delete = (ImageButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes < 6) {
                        zeroes++;
                        queue.removeLast();
                        printClock();
                    }
                }
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(1);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(2);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(3);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(4);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(5);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button6 = (Button) findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(6);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(7);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(8);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button9 = (Button) findViewById(R.id.button9);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0) {
                        queue.add(9);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });

        Button button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counting == false) {
                    if (zeroes > 0 && zeroes != 6) {
                        queue.add(0);
                        zeroes--;
                        printClock();
                    }
                }
            }
        });


        final FloatingActionButton startFab = (FloatingActionButton) findViewById(R.id.startFab);
        final FloatingActionButton pauseFab = (FloatingActionButton) findViewById(R.id.pauseFab);
        final FloatingActionButton resetFab = (FloatingActionButton) findViewById(R.id.resetFab);
        final FloatingActionMenu menuFab = (FloatingActionMenu) findViewById(R.id.fabMenu);

        menuFab.setClosedOnTouchOutside(true);

        startFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initialTime > 0) {
                    if (startFab.getLabelText().equals("Start")) {
                        counting = true;
                        reset = false;
                        paused = false;
                        CountDownTimer cdtimer;
                        timeRemaining = initialTime;
                        long millisInFuture = initialTime;
                        long countdownInterval = 1000;
                        final TextView timer = (TextView) findViewById(R.id.timer);
                        startFab.setVisibility(View.GONE);
                        pauseFab.setVisibility(View.VISIBLE);
                        resetFab.setVisibility(View.VISIBLE);
                        cdtimer = new CountDownTimer(millisInFuture, countdownInterval) { // adjust the milli seconds here
                            public void onTick(long millisUntilFinished) {
                                if (paused || reset) {
                                    //If the user request to cancel or paused the
                                    //CountDownTimer we will cancel the current instance
                                    cancel();
                                } else {
                                    timer.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                    timeRemaining = millisUntilFinished;
                                }
                            }

                            public void onFinish() {
                                notifyFinish();
                                timer.setText("00h00m00s");
                                counting = false;
                                pauseFab.setVisibility(View.GONE);
                                resetFab.setVisibility(View.GONE);
                                startFab.setVisibility(View.VISIBLE);
                            }
                        }.start();
                    } else {
                        counting = true;
                        paused = false;
                        reset = false;
                        long millisInFuture = timeRemaining;
                        long countdownInterval = 1000;
                        final TextView timer = (TextView) findViewById(R.id.timer);
                        startFab.setVisibility(View.GONE);
                        pauseFab.setVisibility(View.VISIBLE);
                        resetFab.setVisibility(View.VISIBLE);
                        new CountDownTimer(millisInFuture, countdownInterval) {
                            public void onTick(long millisUntilFinished) {
                                if (paused || reset) {
                                    //If the user request to cancel or paused the
                                    //CountDownTimer we will cancel the current instance
                                    cancel();
                                } else {
                                    timer.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                                    timeRemaining = millisUntilFinished;
                                }
                            }

                            public void onFinish() {
                                notifyFinish();
                                timer.setText("00h00m00s");
                                counting = false;
                                pauseFab.setVisibility(View.GONE);
                                resetFab.setVisibility(View.GONE);
                                startFab.setLabelText("Start");
                                startFab.setVisibility(View.VISIBLE);
                            }
                        }.start();
                    }
                }
            }
        });

        pauseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paused = true;
                startFab.setLabelText("Resume");
                startFab.setVisibility(View.VISIBLE);
                pauseFab.setVisibility(View.GONE);
            }
        });

        resetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRemaining = initialTime;
                TextView timer = (TextView) findViewById(R.id.timer);
                timer.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toHours(initialTime),
                        TimeUnit.MILLISECONDS.toMinutes(initialTime) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(initialTime)),
                        TimeUnit.MILLISECONDS.toSeconds(initialTime) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(initialTime))));
                startFab.setLabelText("Start");
                startFab.setVisibility(View.VISIBLE);
                pauseFab.setVisibility(View.GONE);
                resetFab.setVisibility(View.GONE);
                reset = true;
                counting = false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the your_fab_buttons; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_countdown_timer,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    public void notifyFinish() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Time's up!");
        r.play();
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                r.stop();
                TextView timer = (TextView) findViewById(R.id.timer);
                timer.setText("" + String.format(FORMAT, TimeUnit.MILLISECONDS.toHours(initialTime),
                        TimeUnit.MILLISECONDS.toMinutes(initialTime) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(initialTime)),
                        TimeUnit.MILLISECONDS.toSeconds(initialTime) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(initialTime))));
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void printClock() {
        TextView timer = (TextView) findViewById(R.id.timer);
        String time = "";
        // Get input
        for (Iterator itr = queue.iterator(); itr.hasNext(); ) {
            time += itr.next();
        }
        // Pad to the left with zeroes
        while (time.length() < 6) {
            time = "0" + time;
        }
        // Save time
        initialTime = TimeUnit.HOURS.toMillis(Long.parseLong(time.substring(0,2)))
                + TimeUnit.MINUTES.toMillis(Long.parseLong(time.substring(2,4)))
                + TimeUnit.SECONDS.toMillis(Long.parseLong(time.substring(4,6)));
        timer.setText(time.substring(0,2) + "h" + time.substring(2,4) + "m" + time.substring(4,6) + "s");
    }
}
