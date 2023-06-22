package com.example.cmpt395finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewSchedule extends AppCompatActivity {
    public CompactCalendarView compactCalendar;
    public TextView title;
    public Date date = new Date();
    public Button Exit, exportBtn;
    public int month, year;
    public List<Event> events = new ArrayList<>();
    public ArrayList<String> TotalIncomplete = new ArrayList<>();
    public ArrayList<String> TotalComplete = new ArrayList<>();
    public String[] incompleteDays, completeDays;
    public SimpleDateFormat toMilli;
    public SimpleDateFormat dateFormatMonth = new SimpleDateFormat ("MMMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);

        // Gets the intent from mainActivity or scheduling page. need this to load month.
        Intent intent = getIntent();
        month = intent.getExtras().getInt("month") + 1;
        year = intent.getExtras().getInt("year") + 1900;
        //
        getSupportActionBar().hide();
        date.setMonth(intent.getExtras().getInt("month"));
        date.setYear(intent.getExtras().getInt("year"));

        MonthDatabaseHelper db = new MonthDatabaseHelper(this);
        DayDatabaseHelper dbD = new DayDatabaseHelper(this);

        //THIS IS THE PART YOU GAVE ME //
        String MID = db.findMonth(String.valueOf(year), String.valueOf(month));
        if (MID.equals("0")){
            MID = db.addMonth(String.valueOf(month), String.valueOf(year));
        }

        String MIDS[] = db.getAllMID().toArray(new String[0]);
        for (int i = 0; i < MIDS.length; i++) {
            String yearMonth[] = db.getYearMonth(MIDS[i]);
            incompleteDays = dbD.DaysFilledIncomplete(MIDS[i]).toArray(new String[0]);
            completeDays = dbD.DaysFilled(MIDS[i]).toArray(new String[0]);
            for (int j = 0; j < incompleteDays.length; j++) {
                TotalIncomplete.add(yearMonth[1] + "/" + incompleteDays[j] + "/" + yearMonth[0]);
            }
            for (int j = 0; j < completeDays.length; j++) {
                TotalComplete.add(yearMonth[1] + "/" + completeDays[j] + "/" + yearMonth[0]);
            }
        }


        //date.setYear(2016 - 1900); /* Starts at the year 1900 */
        compactCalendar = findViewById(R.id.compactcalendar_view);
        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendar.setCurrentDate(date);

        title = (TextView) findViewById(R.id.textView19);
        title.setText(dateFormatMonth.format(date));

        Exit = findViewById(R.id.buttonExit);
        toMilli = new SimpleDateFormat("MM/dd/yyyy");


        // For filled out days:
        for (int i = 0; i < TotalComplete.size(); i++){
            try {
                Date timeMilli = toMilli.parse(TotalComplete.get(i));
                events.add(new Event(Color.GREEN, timeMilli.getTime(), "teachers professional day"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < TotalIncomplete.size(); i++){
            try {
                Date timeMilli = toMilli.parse(TotalIncomplete.get(i));
                events.add(new Event(Color.YELLOW, timeMilli.getTime(), "teachers professional day"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < (TotalIncomplete.size() + TotalComplete.size()); i++){
            compactCalendar.addEvent(events.get(i));
        }

        // set an event for teachers' professional day 2016 which is 21st of october
        //Event ev1 = new Event(Color.parseColor("#FFE49976"), 1477054800000L, "teachers professional day");
        //compactCalendar.addEvent(ev1);

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override

            public void onDayClick(Date dateClicked) {
                if (dateClicked.getDay() == 0 || dateClicked.getDay() == 6) {
                    Intent intent1 = new Intent(NewSchedule.this, weekendScheduler.class);
                    intent1.putExtra("day", String.valueOf(dateClicked.getDate()));
                    intent1.putExtra("month", String.valueOf(dateClicked.getMonth() + 1));
                    intent1.putExtra("year", String.valueOf(dateClicked.getYear() + 1900));
                    intent1.putExtra("source", "MAIN");
                    startActivity(intent1);
                } else {
                    Intent intent = new Intent(NewSchedule.this, weekdayScheduler.class);
                    intent.putExtra("day", String.valueOf(dateClicked.getDate()));
                    intent.putExtra("month", (String.valueOf(dateClicked.getMonth() + 1)));
                    intent.putExtra("year", String.valueOf(dateClicked.getYear() + 1900));
                    intent.putExtra("source", "MAIN");
                    startActivity(intent);
                }
            }

            @Override
            public void onMonthScroll(Date x) {
                title.setText(dateFormatMonth.format(x));
                date.setDate(x.getDate());
                date.setMonth(x.getMonth());
                date.setYear(x.getYear());
            }
        });

        Exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){goBack();}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        exportBtn = findViewById(R.id.exportBtn);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                exportSch(String.valueOf(date.getYear() + 1900), String.valueOf(date.getMonth() + 1));
            } catch (IOException | ParseException e) {
                throw new RuntimeException(e);
            }
            }
        });
    }

    public void exportSch(String year, String month) throws IOException, ParseException {
        //This will overwrite any other file with the same name
        //The file can be accessed by going to files then downloads in the emulator
        String monthDates = "\n";
        //This gets the month name
        String monthName = getMonth(Integer.parseInt(month));
        Date date = new SimpleDateFormat("M", Locale.ENGLISH).parse(month);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DATE);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");
        MonthDatabaseHelper db = new MonthDatabaseHelper(this);
        DayDatabaseHelper dbD = new DayDatabaseHelper(this);
        ShiftDatabaseHelper dbS = new ShiftDatabaseHelper(this);
        // get the month id
        String MID = db.findMonth(year, month);
        Log.i("max day: " + maxDay, " here");
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
    }

    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }
}