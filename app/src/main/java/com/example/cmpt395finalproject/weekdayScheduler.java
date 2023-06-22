package com.example.cmpt395finalproject;

import static android.content.ContentValues.TAG;

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

public class weekdayScheduler extends AppCompatActivity {
    TextView date;
    ArrayList<String> EID1, FName1, LName1, Email1, trained1, EID2, FName2, LName2, Email2, trained2;
    ImageButton addMorningShift, addAfternoonShift;
    Button saveButton;
    RecyclerView MorningView, AfterNoonView;

    ShiftAdapter shiftAdapter, afternoonAdapter;
    ShiftDatabaseHelper DB;
    String DID, source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekday_scheduler);
        // pass values
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        String day = intent.getExtras().getString("day");
        source = intent.getExtras().getString("source");

        DB = new ShiftDatabaseHelper(this);
        // collect data
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
        FillMorning();
        FillAfternoon();
        // Set up recycler views
        shiftAdapter = new ShiftAdapter(this, EID1, FName1, LName1, Email1, trained1, true, DID, "AM", month, year);


        MorningView = findViewById(R.id.morningView);
        MorningView.setAdapter(shiftAdapter);
        MorningView.setLayoutManager(new LinearLayoutManager(this));

        afternoonAdapter = new ShiftAdapter(this, EID2, FName2, LName2, Email2, trained2, true, DID, "PM", month, year);


        AfterNoonView = findViewById(R.id.AfternoonView);
        AfterNoonView.setAdapter(afternoonAdapter);
        AfterNoonView.setLayoutManager(new LinearLayoutManager(this));




    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        String day = intent.getExtras().getString("day");
        source = intent.getExtras().getString("source");
        Log.d(TAG,"Day: " + day + ", Month: " + month + ", year: " + year);
        saveButton = findViewById(R.id.savebutton);
        addMorningShift = findViewById(R.id.AddMorningBtn);
        addAfternoonShift = findViewById(R.id.AddAfternoonBtn);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        String dayOfWeekString = sdf.format(calendar.getTime());
        date = findViewById(R.id.textView16);
        date.setText(month + "/" + day + "/" + year +" "+ dayOfWeekString);
        // add shifts
        addMorningShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillMorning();
                if (EID1.size() == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(weekdayScheduler.this);
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
                    Intent intent = new Intent(weekdayScheduler.this, ViewSchedules.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("shift", "M");
                    intent.putExtra("DID", DID);
                    intent.putExtra("dayofweek", String.valueOf(dayOfWeek));
                    intent.putExtra("slot", "AM");
                    intent.putExtra("source", source);
                    startActivity(intent);
                }
            }
        });
        addAfternoonShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillAfternoon();
                if (EID2.size() == 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(weekdayScheduler.this);
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
                    Intent intent = new Intent(weekdayScheduler.this, ViewSchedules.class);
                    intent.putExtra("year", year);
                    intent.putExtra("month", month);
                    intent.putExtra("day", day);
                    intent.putExtra("shift", "A");
                    intent.putExtra("DID", DID);
                    intent.putExtra("dayofweek", String.valueOf(dayOfWeek));
                    intent.putExtra("slot", "PM");
                    intent.putExtra("source", source);
                    startActivity(intent);
                }
            }
        });

        // back button using the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FillMorning();
                FillAfternoon();
                boolean TrainedOP = false;
                boolean TrainedCL = false;
                Intent intent = getIntent();
                String year = intent.getExtras().getString("year");
                String month = intent.getExtras().getString("month");
                DayDatabaseHelper db1 = new DayDatabaseHelper(weekdayScheduler.this);
                if (EID1.size() == 0 && EID2.size() == 0) {
                    db1.fillDay(DID, 0);
                    Log.d("NONE", "THERE IS NONE");
                    if (source.equals("MAIN")) {
                        intent = new Intent(weekdayScheduler.this, NewSchedule.class);
                        intent.putExtra("month", (Integer.parseInt(month)) - 1);
                        intent.putExtra("year", (Integer.parseInt(year) - 1900));
                        startActivity(intent);
                    } else {
                        intent = new Intent(weekdayScheduler.this, MainActivity.class);
                        startActivity(intent);
                    }
                    startActivity(intent);
                }else{
                    for (int i = 0; i < EID1.size(); i++) {
                        if (trained1.get(i).equals("O") || trained1.get(i).equals("B")) {
                            TrainedOP = true;
                        }
                    }
                    for (int i = 0; i < EID2.size(); i++) {
                        if (trained2.get(i).equals("C") || trained2.get(i).equals("B")) {
                            TrainedCL = true;
                        }
                    }

                    if (TrainedOP && TrainedCL && EID1.size() == 2 && EID2.size() == 2) {
                        db1.fillDay(DID, 2);
                        if (source.equals("MAIN")) {
                            intent = new Intent(weekdayScheduler.this, NewSchedule.class);
                            intent.putExtra("month", (Integer.parseInt(month)) - 1);
                            intent.putExtra("year", (Integer.parseInt(year) - 1900));
                            startActivity(intent);
                        } else {
                            intent = new Intent(weekdayScheduler.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }else if (!TrainedOP && !TrainedCL){
                        ErrorMessageMaker("You are missing an opener and closer. Go back anyway?");
                    }else if (!TrainedOP){
                        ErrorMessageMaker("You are missing an opener. Go back anyway?");
                    }else if (!TrainedCL){
                        ErrorMessageMaker("You are missing a closer. Go back anyway?");
                    }else if (EID1.size() <2 || EID2.size() <2){
                        ErrorMessageMaker("Not all shifts are filled in. Go back anyway?");
                    }
                }
            }
        });

    }

    //Stores morning values
void FillMorning(){
        EID1 = (ArrayList<String>) DB.returnShifts(DID, "AM");
    Log.d("WOW", "FillMorning: EID"+EID1.size());
    EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
    if (EID1.size() > 0){
        FName1 = new ArrayList<String>();
        LName1 = new ArrayList<String>();
        Email1 = new ArrayList<String>();
        trained1 = new ArrayList<String>();
        trained1 = (ArrayList<String>) DB.returnTraining(DID, "AM");
    }
    for (int i = 0; i < EID1.size(); i++){
            String[] IdInfo = dbE.getInfoByID(EID1.get(i));
            FName1.add(IdInfo[1]);
            LName1.add(IdInfo[2]);
            Email1.add(IdInfo[3]);
        }
}
//Stores afternoon values
void FillAfternoon(){
    EID2 = (ArrayList<String>) DB.returnShifts(DID, "PM");
    EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
    if (EID2.size() > 0){
        FName2 = new ArrayList<String>();
        LName2 = new ArrayList<String>();
        Email2 = new ArrayList<String>();
        trained2 = new ArrayList<String>();
        trained2 = (ArrayList<String>) DB.returnTraining(DID, "PM");
    }
    for (int i = 0; i < EID2.size(); i++){
        String[] IdInfo = dbE.getInfoByID(EID2.get(i));
        FName2.add(IdInfo[1]);
        LName2.add(IdInfo[2]);
        Email2.add(IdInfo[3]);
    }
}
// check if all criteria is met
void ErrorMessageMaker(String message){
    // popup warning message that not all shifts are filled and ask if the user wants to go back anyways
    DayDatabaseHelper DB = new DayDatabaseHelper(this);
    AlertDialog.Builder builder = new AlertDialog.Builder(weekdayScheduler.this);
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
                intent = new Intent(weekdayScheduler.this, NewSchedule.class);
                intent.putExtra("month", (Integer.parseInt(month)) - 1);
                intent.putExtra("year", (Integer.parseInt(year) - 1900));
                startActivity(intent);
            } else {
                intent = new Intent(weekdayScheduler.this, MainActivity.class);
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
        FillMorning();
        FillAfternoon();
        boolean TrainedOP = false;
        boolean TrainedCL = false;
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        DayDatabaseHelper db1 = new DayDatabaseHelper(weekdayScheduler.this);
        if (EID1.size() == 0 && EID2.size() == 0) {
            db1.fillDay(DID, 0);
            Log.d("NONE", "THERE IS NONE");
            if (source.equals("MAIN")) {
                intent = new Intent(weekdayScheduler.this, NewSchedule.class);
                intent.putExtra("month", (Integer.parseInt(month)) - 1);
                intent.putExtra("year", (Integer.parseInt(year) - 1900));
                startActivity(intent);
            } else {
                intent = new Intent(weekdayScheduler.this, MainActivity.class);
                startActivity(intent);
            }
            startActivity(intent);
        }else{
            for (int i = 0; i < EID1.size(); i++) {
                if (trained1.get(i).equals("O") || trained1.get(i).equals("B")) {
                    TrainedOP = true;
                }
                if (trained2.get(i).equals("C") || trained2.get(i).equals("B")) {
                    TrainedCL = true;
                }
            }

            if (TrainedOP && TrainedCL && EID1.size() == 2 && EID2.size() == 2) {
                db1.fillDay(DID, 2);
                if (source.equals("MAIN")) {
                    intent = new Intent(weekdayScheduler.this, NewSchedule.class);
                    intent.putExtra("month", (Integer.parseInt(month)) - 1);
                    intent.putExtra("year", (Integer.parseInt(year) - 1900));
                    startActivity(intent);
                } else {
                    intent = new Intent(weekdayScheduler.this, MainActivity.class);
                    startActivity(intent);
                }
            }else if (!TrainedOP && !TrainedCL){
                ErrorMessageMaker("You are missing an opener and closer. Go back anyway?");
            }else if (!TrainedOP){
                ErrorMessageMaker("You are missing an opener. Go back anyway?");
            }else if (!TrainedCL){
                ErrorMessageMaker("You are missing a closer. Go back anyway?");
            }else if (EID1.size() <2 || EID2.size() <2){
                ErrorMessageMaker("Not all shifts are filled in. Go back anyway?");
            }
        }
    }


}