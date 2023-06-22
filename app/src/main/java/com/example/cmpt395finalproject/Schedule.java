package com.example.cmpt395finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class Schedule extends AppCompatActivity {

    private Button back, saveSched, exportBtn;
    private CalendarView cal;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* DO NOT TOUCH ANYTHING IN HERE. IT WAS A MASSIVE PAIN IN THE ASS TO IMPLEMENT. */
        /* Grab the passed variables */
        Intent intent = getIntent();
        String year = intent.getExtras().getString("year");
        String month = intent.getExtras().getString("month");
        // init database
        MonthDatabaseHelper db = new MonthDatabaseHelper(this);
        DayDatabaseHelper dbD =new DayDatabaseHelper(this);
        // grabvalue
        String MID = db.findMonth(year, month);
        // check if in database
        if (MID.equals("0")){
            MID = db.addMonth(month, year);
        }else {
            //global or not you DECIDE
            // Completed days with no problems
            String[] DayComplete = dbD.DaysFilled(MID).toArray(new String[0]);
            for (int i = 0; i < DayComplete.length; i++) {
                Log.d("HODY NEIGHTER", "DayComplete"+DayComplete[i]);
            }
            // Incomplete filled in days
            String[] DaysFilledIncomplete = dbD.DaysFilled(MID).toArray(new String[0]);
            Log.d("TESTCASE", "onCreate: "+DaysFilledIncomplete.length);
        }

        getSupportActionBar().hide(); // I don't want the action bar on main menu
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        back = findViewById((R.id.back));
        saveSched = findViewById((R.id.saveSched));
        cal = findViewById((R.id.calendarView));
        //exportBtn = findViewById((R.id.exportBtn));

        /* I use data binding here to set the cal month and year within a try statement. */
        String minDate = month + "/01/" + year; // This is the first day in month
        /* Get the amount of days in the month. */
        Calendar calendar = new GregorianCalendar(Integer.parseInt(year),
                Integer.parseInt(month)-1, 1);
        int numOfDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        String maxDate = month + "/" + numOfDays + "/" + year; // This is the last day in month.

        /* ESPECIALLY DONT TOUCH THIS ONE. THE STRING IS CRUCIAL */
        /* Does some magic shit to convert a date in mm/dd/yyyy to milli since epoch */
        /* This is done so when you select date, it creates a cal for that date. */
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = df.parse(minDate);
            long millis = date.getTime();
            cal.setMinDate(millis);

            date = df.parse(maxDate);
            millis = date.getTime();
            cal.setMaxDate(millis);

            date = df.parse("02/01/2020");
            millis = date.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                Log.d("Schedule", "onSelectDayChange: date: " + date);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Integer.valueOf(year), Integer.valueOf(month) - 1, i2);
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                if (dayOfWeek==1 || dayOfWeek ==7){
                    Intent intent1 = new Intent(Schedule.this, weekendScheduler.class);
                    intent1.putExtra("day", String.valueOf(i2));
                    intent1.putExtra("month", month);
                    intent1.putExtra("year", year);
                    startActivity(intent1);
                }else {
                    Intent intent = new Intent(Schedule.this, weekdayScheduler.class);
                    intent.putExtra("day", String.valueOf(i2));
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    startActivity(intent);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goBack();
            }
        });

        saveSched.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){goBack();}
        });
        ActivityCompat.requestPermissions(Schedule.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        /*
        exportBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    exportSch(year, month);
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        */
    }

    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
/*
    @SuppressLint("SetWorldReadable")
    public void exportSch(String year, String month) throws IOException, ParseException {
        //This will overwrite any other file with the same name
        //The file can be accessed by going to files then downloads in the emulator
        String monthDates = "\n";
        //This gets the month name
        String monthName = getMonth(Integer.parseInt(month));
        Date date = new SimpleDateFormat("M", Locale.ENGLISH).parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Integer.parseInt(month));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        MonthDatabaseHelper db = new MonthDatabaseHelper(this);
        DayDatabaseHelper dbD = new DayDatabaseHelper(this);
        ShiftDatabaseHelper dbS = new ShiftDatabaseHelper(this);
        // get the month id
        String MID = db.findMonth(year, month);
        //This loops gets a string of dates in the format which will be put directly in the file
        for (int i = 0; i < maxDay; i++)
        {
            String dayName = "";
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date myDate = inFormat.parse((i+1)+"-"+month+"-"+year);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                dayName=simpleDateFormat.format(myDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Prints the date as ex: Monday 04-01
            //Can add employees working in columns order FullDay\t\tFullDay or AM1\t\tAM2\t\tPM1\t\t\PM2
            //Get the day id
            String DID = dbD.findDay(MID, String.valueOf(i+1));
            // First pass
            String apples = formatString("applesasdgasdgasgdsadf");
            String oranges = formatString("oranges");
            String bananas = formatString("bananas");
            String grapes = "grapes";
            String combine2 = String.format("%-15s %-15s", apples, oranges);
            if(dayName.equals("Sunday")|| dayName.equals("Saturday")) {
                //Get the shifts
                if (!DID.equals("0")) {
                    ArrayList<String> EID = (ArrayList<String>) dbS.returnShifts(DID, "F");
                    String FName = String.format("%-15s %-15s", formatString(FnameGet(1, EID)), formatString(FnameGet(2, EID)));
                    String LName = String.format("%-15s %-15s", formatString(LnameGet(1, EID)), formatString(LnameGet(2, EID)));
                    String Email = String.format("%-15s %-15s", formatString(EmailGet(1, EID)), formatString(EmailGet(2, EID)));
                    monthDates = monthDates + "\t\t\tFullDay\t\tFullDay\n" + dayName + " " + df.format(cal.getTime()) + "\n\t\t\t"+FName+"\n\t\t\t"+LName+"\n\t\t\t"+Email+"\n\n";
                }else {
                    String FName = String.format("%-15s %-15s %-15s %s", "Empty", "Empty", "Empty", "Empty");
                    String LName = String.format("%-15s %-15s %-15s %s", "Empty", "Empty", "Empty", "Empty");
                    String Email = String.format("%-15s %-15s %-15s %s", "Empty", "Empty", "Empty", "Empty");
                    monthDates = monthDates + "\t\t\tFullDay\t\tFullDay\n" + dayName + " " + df.format(cal.getTime()) + "\n\t\t\t"+FName+"\n\t\t\t"+LName+"\n\t\t\t"+Email+"\n\n";
                }

            }else {
                if (!DID.equals("0")) {
                    ArrayList<String> EID1 = (ArrayList<String>) dbS.returnShifts(DID, "AM");
                    ArrayList<String> EID2 = (ArrayList<String>) dbS.returnShifts(DID, "PM");
                    String FName = String.format("%-15s %-15s %-15s %s", formatString(FnameGet(1, EID1)), formatString(FnameGet(2, EID1)), formatString(FnameGet(1, EID2)), formatString(FnameGet(2, EID2)));
                    String LName = String.format("%-15s %-15s %-15s %s", formatString(LnameGet(1, EID1)), formatString(LnameGet(2, EID1)), formatString(LnameGet(1, EID2)), formatString(LnameGet(2, EID2)));
                    String Email = String.format("%-15s %-15s %-15s %s", formatString(EmailGet(1, EID1)), formatString(EmailGet(2, EID1)), formatString(EmailGet(1, EID2)), formatString(EmailGet(2, EID2)));
                    monthDates = monthDates + dayName + " " + df.format(cal.getTime()) + "\n\t\t\t"+FName+"\n\t\t\t"+LName+"\n\t\t\t"+Email+"\n\n";
                }else {
                    String FName = String.format("%-15s %-15s %-15s %s", "Empty", "Empty", "Empty", "Empty");
                    String LName = String.format("%-15s %-15s %-15s %s", "Empty", "Empty", "Empty", "Empty");
                    String Email = String.format("%-15s %-15s %-15s %s", "Empty", "Empty", "Empty", "Empty");
                    monthDates = monthDates + dayName + " " + df.format(cal.getTime()) + "\n\t\t\t"+FName+"\n\t\t\t"+LName+"\n\t\t\t"+Email+"\n\n";
                }
            }
        }


        //This saves the file to downloads
        File myExternalFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "/" + monthName + year + ".txt");
        try {
            //Use getBytes when writing string to file
            FileOutputStream fos = new FileOutputStream(myExternalFile);
            //This prints the AM and PM to the top
            fos.write("\t\t\tAM1\t\tAM2\t\tPM1\t\tPM2".getBytes());
            //This prints the date lines
            fos.write(monthDates.getBytes());
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Saving", Toast.LENGTH_SHORT).show();
        }
    }
    public String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }

    String formatString(String input) {
        if (input.length() > 14) {
            return input.substring(0, 11) + "..";
        } else {
            while (input.length() < 14) {
                input += " ";
            }
            return input;
        }
    }

    String FnameGet(int i, ArrayList<String> EID){
        // gets the Fname if there is one
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        if (EID.size() >= i) {
            String[] IdInfo = dbE.getInfoByID(EID.get(i-1));
            return (IdInfo[1]);
        } else {
            return "Empty";
    }
    }
    String LnameGet(int i, ArrayList<String> EID){
        // gets the Fname if there is one
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        if (EID.size() >= i) {
            String[] IdInfo = dbE.getInfoByID(EID.get(i-1));
            return (IdInfo[2]);
        } else {
            return "Empty";
        }
    }
    String EmailGet(int i, ArrayList<String> EID){
        // gets the Fname if there is one
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        if (EID.size() >= i) {
            String[] IdInfo = dbE.getInfoByID(EID.get(i-1));
            return (IdInfo[3]);
        } else {
            return "Empty";
        }
    }*/
}