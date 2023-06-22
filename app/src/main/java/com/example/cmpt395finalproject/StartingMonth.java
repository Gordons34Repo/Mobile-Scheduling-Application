package com.example.cmpt395finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StartingMonth extends AppCompatActivity {

    private ToggleButton MMonBtn, MTueBtn, MWedBtn, MThuBtn, MFriBtn, AMonBtn, ATueBtn, AWedBtn, AThuBtn, AFriBtn, SatBtn, SunBtn;

    private Button AddBtn, backBtn;

    private TextView monthText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_month);
        // starts helper class
        EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(StartingMonth.this);
        EmployeeMonthlyAvailabilityDatabaseHelper db2 = new EmployeeMonthlyAvailabilityDatabaseHelper(StartingMonth.this);
        // connect buttons to xml
        MMonBtn = findViewById(R.id.toggleMMondayBtn);
        MTueBtn = findViewById(R.id.toggleMTuesdayBtn);
        MWedBtn = findViewById(R.id.toggleMWednesdayBtn);
        MThuBtn = findViewById(R.id.toggleMThursdayBtn);
        MFriBtn = findViewById(R.id.toggleMFridayBtn);
        AMonBtn = findViewById(R.id.toggleAMondayBtn);
        ATueBtn = findViewById(R.id.toggleATuesdayBtn);
        AWedBtn = findViewById(R.id.toggleAWednesdayBtn);
        AThuBtn = findViewById(R.id.toggleAThursdayBtn);
        AFriBtn = findViewById(R.id.toggleAFridayBtn);
        SatBtn = findViewById(R.id.toggleSaturdayBtn);
        SunBtn = findViewById(R.id.toggleSundayBtn);
        AddBtn = findViewById(R.id.addBtn);
        monthText = findViewById(R.id.monthText);
        Intent intent = getIntent();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.getDefault());
        String currentMonth = monthFormat.format(new Date());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        monthText.setText("Starting Month of: \n"+ currentMonth + " " + String.valueOf(currentYear));
        String FName = intent.getExtras().getString("FName");
        String LName = intent.getExtras().getString("LName");
        String email = intent.getExtras().getString("email");
        String phone = intent.getExtras().getString("phone");
        String training = intent.getExtras().getString("training");
        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH)+1;
        //add emplyoee
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = db.addEmployee(capitalizeFirstLetter(FName), capitalizeFirstLetter(LName), phone, email, training, String.valueOf(Month)+"/"+String.valueOf(Year));
                String AID = db2.addEmployeeMonth(Integer.valueOf(id), Month, Year, weekdayAvailableReturn(MMonBtn.isChecked(), AMonBtn.isChecked()), weekdayAvailableReturn(MTueBtn.isChecked(), ATueBtn.isChecked()), weekdayAvailableReturn(MWedBtn.isChecked(), AWedBtn.isChecked()), weekdayAvailableReturn(MThuBtn.isChecked(), AThuBtn.isChecked()), weekdayAvailableReturn(MFriBtn.isChecked(), AFriBtn.isChecked()), weekendAvailableReturn(SatBtn.isChecked()), weekendAvailableReturn(SunBtn.isChecked()));
                db2.markAsModified(AID, String.valueOf(Year), String.valueOf(Month));
                Intent intent1 = new Intent(StartingMonth.this, ViewColleagues.class);
                startActivity(intent1);
            }
        });
    }

    // Returns the char for table base on index weekdays
    String weekdayAvailableReturn(boolean morning, boolean afternoon){
        if (morning && afternoon) return "B";
        else if (morning && !afternoon) return "M";
        else if (!morning && afternoon) return "A";
        else if (!morning && !afternoon) return "N";
        return "N";
    }
    // Returns the char for table base on index weekend
    String weekendAvailableReturn(boolean fullDay){
        if (fullDay) return "F";
        else return "N";
    }

    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    }
