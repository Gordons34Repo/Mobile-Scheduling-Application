package com.example.cmpt395finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class weekendScheduler extends AppCompatActivity {
    TextView date;
    ArrayList<String> EID, FName, LName, Email, trained;
    ImageButton addShift;
    Button backButton;
    RecyclerView ShiftView;

    ShiftAdapter shiftAdapter;
    ShiftDatabaseHelper DB;
    String DID, source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekend_scheduler);

        // pass values
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        String day = intent.getExtras().getString("day");
        source = intent.getExtras().getString("source");
        // collect data
        DB = new ShiftDatabaseHelper(this);
        MonthDatabaseHelper db = new MonthDatabaseHelper(this);
        DayDatabaseHelper dbD =new DayDatabaseHelper(this);
        String MID = db.findMonth(year, month);
        if (MID.equals("0")){
            MID = db.addMonth(month, year);
        }
        DID = dbD.findDay(MID, day);
        //check for repeat
        if (DID.equals("0")) {
            DID = dbD.addDay(Integer.valueOf(MID), Integer.valueOf(day));
        }
        Log.d("DID IS", "onCreate: "+DID);
        // fill up view
        EID = (ArrayList<String>) DB.returnShifts(DID, "F");
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        if (EID.size() > 0){
            FName = new ArrayList<String>();
            LName = new ArrayList<String>();
            Email = new ArrayList<String>();
            trained = new ArrayList<String>();
            trained = (ArrayList<String>) DB.returnTraining(DID, "F");
        }
        for (int i = 0; i < EID.size(); i++){
            String[] IdInfo = dbE.getInfoByID(EID.get(i));
            FName.add(IdInfo[1]);
            LName.add(IdInfo[2]);
            Email.add(IdInfo[3]);
        }
        // set up view
        shiftAdapter = new ShiftAdapter(this, EID, FName, LName, Email, trained, true, DID, "F", month, year);


        ShiftView = findViewById(R.id.RecyclerView);
        ShiftView.setAdapter(shiftAdapter);
        ShiftView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        String day = intent.getExtras().getString("day");
        backButton = findViewById(R.id.back2button);
        addShift = findViewById(R.id.addShiftBtn);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfWeekString = sdf.format(calendar.getTime());
        date = findViewById(R.id.textView7);
        date.setText(month + "/" + day + "/" + year +" "+ dayOfWeekString);
        // add shifts
        addShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EID = (ArrayList<String>) DB.returnShifts(DID, "F");
                if (EID.size() == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(weekendScheduler.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Two shifts already filled click on one to remove it");
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }else {
                    Intent intent = new Intent(weekendScheduler.this, ViewSchedules.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("shift", "F");
                    intent.putExtra("DID", DID);
                    intent.putExtra("dayofweek", String.valueOf(dayOfWeek));
                    intent.putExtra("slot", "F");
                    intent.putExtra("source", source);
                    startActivity(intent);
                }
            }
        });
        // back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fill in values to check if all requirments are met
                EID = (ArrayList<String>) DB.returnShifts(DID, "F");
                EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(weekendScheduler.this);
                if (EID.size() > 0){
                    FName = new ArrayList<String>();
                    LName = new ArrayList<String>();
                    Email = new ArrayList<String>();
                    trained = new ArrayList<String>();
                    trained = (ArrayList<String>) DB.returnTraining(DID, "F");
                }
                for (int i = 0; i < EID.size(); i++){
                    String[] IdInfo = dbE.getInfoByID(EID.get(i));
                    FName.add(IdInfo[1]);
                    LName.add(IdInfo[2]);
                    Email.add(IdInfo[3]);
                }
                // check if all requirments are met
                boolean TrainedOP = false;
                boolean TrainedCL = false;
                Intent intent = getIntent();
                String year = intent.getExtras().getString("year");
                String month = intent.getExtras().getString("month");
                DayDatabaseHelper db1 = new DayDatabaseHelper(weekendScheduler.this);
                if (EID.size() == 0) {
                    db1.fillDay(DID, 0);
                    if (source.equals("MAIN")) {
                        intent = new Intent(weekendScheduler.this, NewSchedule.class);
                        intent.putExtra("month", (Integer.parseInt(month)) - 1);
                        intent.putExtra("year", (Integer.parseInt(year) - 1900));
                        startActivity(intent);
                    } else {
                        intent = new Intent(weekendScheduler.this, MainActivity.class);
                        startActivity(intent);
                    }
                }else{
                    for (int i = 0; i < EID.size(); i++) {
                        if (trained.get(i).equals("O")) {
                            TrainedOP = true;
                        }
                        if (trained.get(i).equals("C")) {
                            TrainedCL = true;
                        }
                        if (trained.get(i).equals("B")) {
                            TrainedOP = true;
                            TrainedCL = true;
                        }
                        if (EID.size() <2){
                            ErrorMessageMaker("Not all shifts are filled in. Go back anyway?", source);
                        }
                    }
                    if (TrainedOP && TrainedCL && EID.size() == 2){
                        db1.fillDay(DID, 2);
                        if (source.equals("MAIN")) {
                            intent = new Intent(weekendScheduler.this, NewSchedule.class);
                            intent.putExtra("month", (Integer.parseInt(month)) - 1);
                            intent.putExtra("year", (Integer.parseInt(year) - 1900));
                            startActivity(intent);
                        } else {
                            intent = new Intent(weekendScheduler.this, MainActivity.class);
                            startActivity(intent);
                        }
                        startActivity(intent);
                    }else if (!TrainedOP && !TrainedCL){
                        ErrorMessageMaker("You are missing an opener and closer. Go back anyway?", source);
                    }else if (!TrainedOP){
                        ErrorMessageMaker("You are missing an opener. Go back anyway?", source);
                    }else if (!TrainedCL){
                        ErrorMessageMaker("You are missing a closer. Go back anyway?", source);
                    }
                }
            }
        });
    }

    void ErrorMessageMaker(String message, String source){
        // popup warning message that not all shifts are filled and ask if the user wants to go back anyways
        DayDatabaseHelper DB = new DayDatabaseHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(weekendScheduler.this);
        builder.setTitle("Warning");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = getIntent();
                String year = intent.getExtras().getString("year");
                String month = intent.getExtras().getString("month");
                DB.fillDay(DID, 1);
                if (source.equals("MAIN")) {
                    intent = new Intent(weekendScheduler.this, NewSchedule.class);
                    intent.putExtra("month", (Integer.parseInt(month)) - 1);
                    intent.putExtra("year", (Integer.parseInt(year) - 1900));
                    startActivity(intent);
                } else {
                    intent = new Intent(weekendScheduler.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        // fill in values to check if all requirments are met
        EID = (ArrayList<String>) DB.returnShifts(DID, "F");
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(weekendScheduler.this);
        if (EID.size() > 0){
            FName = new ArrayList<String>();
            LName = new ArrayList<String>();
            Email = new ArrayList<String>();
            trained = new ArrayList<String>();
            trained = (ArrayList<String>) DB.returnTraining(DID, "F");
        }
        for (int i = 0; i < EID.size(); i++){
            String[] IdInfo = dbE.getInfoByID(EID.get(i));
            FName.add(IdInfo[1]);
            LName.add(IdInfo[2]);
            Email.add(IdInfo[3]);
        }
        // check if all requirments are met
        boolean TrainedOP = false;
        boolean TrainedCL = false;
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        DayDatabaseHelper db1 = new DayDatabaseHelper(weekendScheduler.this);
        if (EID.size() == 0) {
            db1.fillDay(DID, 0);
            if (source.equals("MAIN")) {
                intent = new Intent(weekendScheduler.this, NewSchedule.class);
                intent.putExtra("month", (Integer.parseInt(month)) - 1);
                intent.putExtra("year", (Integer.parseInt(year) - 1900));
                startActivity(intent);
            } else {
                intent = new Intent(weekendScheduler.this, MainActivity.class);
                startActivity(intent);
            }
        }else if (EID.size() <2){
            ErrorMessageMaker("Not all shifts are filled in. Go back anyway?", source);
        }else{
            for (int i = 0; i < EID.size(); i++) {
                if (trained.get(i).equals("O")) {
                    TrainedOP = true;
                }
                if (trained.get(i).equals("C")) {
                    TrainedCL = true;
                }
                if (trained.get(i).equals("B")) {
                    TrainedOP = true;
                    TrainedCL = true;
                }
            }
            if (TrainedOP && TrainedCL){
                db1.fillDay(DID, 2);
                if (source.equals("MAIN")) {
                    intent = new Intent(weekendScheduler.this, NewSchedule.class);
                    intent.putExtra("month", (Integer.parseInt(month)) - 1);
                    intent.putExtra("year", (Integer.parseInt(year) - 1900));
                    startActivity(intent);
                } else {
                    intent = new Intent(weekendScheduler.this, MainActivity.class);
                    startActivity(intent);
                }
                startActivity(intent);
            }else if (!TrainedOP && !TrainedCL){
                ErrorMessageMaker("You are missing an opener and closer. Go back anyway?", source);
            }else if (!TrainedOP){
                ErrorMessageMaker("You are missing an opener. Go back anyway?", source);
            }else if (!TrainedCL){
                ErrorMessageMaker("You are missing a closer. Go back anyway?", source);
            }
        }
    }
}