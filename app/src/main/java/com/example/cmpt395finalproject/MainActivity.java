package com.example.cmpt395finalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button viewSched;
    private Button ManColl;

    private Button createSched;
    private TextView dateDisplay;
    private TextView textView15;
    private TextView textView18;
    private Handler handler;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide(); // I dont want the action bar on main menu
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Assign XML buttons to variables. */
        createSched = findViewById((R.id.createSched));
        viewSched = findViewById((R.id.viewSched));
        ManColl = findViewById((R.id.ManColl));

        /* create the digital clock */
        textView15 = findViewById(R.id.textView15);
        handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                LocalTime currentTime = LocalTime.now();
                String time = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                textView15.setText(time);
                handler.postDelayed(this,1000);
            }
        });

        /* display date */
        textView18 = findViewById((R.id.textView18));
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String day = currentDate.format(dateFormat);
        textView18.setText(day);

        /*### The following are on-click listeners for the buttons. ###*/
        /* Goes to the create schedule page */
        createSched.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){openSchedule();}
        });
        /* Goes to the view schedule page */
        viewSched.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCreateSchedule();
            }
        });
        /* Goes to the manage colleagues page */
        ManColl.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openManColl();
            }
        });
        /* This does a popup to select a date before going to the schedule.*/

    }


    /* This helper function manages redirecting to the view schedule page. */
    public void openSchedule(){
        Date date = new Date();
        Intent intent = new Intent(this, NewSchedule.class);
        intent.putExtra("month", date.getMonth());
        intent.putExtra("year", date.getYear());
        startActivity(intent);
    }
    /* helper function to redirect to the create schedule page */
    /* This now doesn't directly redirect, the mDateSetListener does.*/
    public void openCreateSchedule(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1 || dayOfWeek == 7) {
            Intent intent = new Intent(MainActivity.this, weekendScheduler.class);
            intent.putExtra("day", String.valueOf(day));
            intent.putExtra("month", (String.valueOf(month + 1)));
            intent.putExtra("year", String.valueOf(year));
            intent.putExtra("source", "applesbees");
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, weekdayScheduler.class);
            intent.putExtra("day", String.valueOf(day));
            intent.putExtra("month", (String.valueOf(month + 1)));
            intent.putExtra("year", String.valueOf(year));
            intent.putExtra("source", "applesbees");
            startActivity(intent);
        }


    }

    /* Helper function to goto the manage colleagues page.*/
    public void openManColl(){
        Intent intent = new Intent(this, ViewColleagues.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}