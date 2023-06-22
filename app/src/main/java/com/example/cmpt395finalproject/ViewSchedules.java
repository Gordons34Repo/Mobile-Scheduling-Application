package com.example.cmpt395finalproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewSchedules extends AppCompatActivity {
    EmployeeMonthlyAvailabilityDatabaseHelper DB;
    RecyclerView recyclerView;
    ArrayList<String> EID, FName, LName, Email, trained, DupFreeEID, empID;
    CustomAdapter2 customAdapter;
    private String Year;
    private String Month;
    private String dayofweek;
    private String shift;
    private String DID;
    private String slot;
    private String Day;
    private String source;
    Button backBtn, SearchBtn;
    CheckBox OpeningTrained, ClosingTrained;
    private EditText SearchBar;
    private String TAG = "HEY DUDE";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_schedules);
        Intent intent2 = getIntent();
        Year = intent2.getExtras().getString("year");
        Month = intent2.getExtras().getString("month");
        dayofweek = intent2.getExtras().getString("dayofweek");
        shift = intent2.getExtras().getString("shift");
        DID = intent2.getExtras().getString("DID");
        Day = intent2.getExtras().getString("day");
        slot = intent2.getExtras().getString("slot");
        source = intent2.getExtras().getString("source");
        DB = new EmployeeMonthlyAvailabilityDatabaseHelper(ViewSchedules.this);
        EID = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        String shiftType;
        if (slot.equals("F")) {
            shiftType = "Full Day Shift";
        } else if (slot.equals("AM")) {
            shiftType = "Morning Shift";
        } else {
            shiftType = "Afternoon Shift";
        }
        actionBar.setTitle(Month+"/"+Day+"/"+(Year)+" "+shiftType);
        updateAvailbility(Year, Month);
        storeData();

        //return if it's week day or weekend
        Boolean WeekDay = true;
        if (dayofweek.equals("1") || dayofweek.equals("7")){
             WeekDay = false;
        }
        customAdapter = new CustomAdapter2(ViewSchedules.this, DupFreeEID, FName, LName, Email, trained, WeekDay, DID, slot, Month, Year, Day, source);


        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewSchedules.this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        backBtn = findViewById((R.id.backbutton));
        SearchBar = findViewById(R.id.viewSearchBar2);
        SearchBtn = findViewById(R.id.viewSearchBtn2);
        OpeningTrained = findViewById(R.id.OTrainedCheck3);
        ClosingTrained = findViewById(R.id.CTrainedChecked3);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slot.equals("F")){
                    Intent intent = new Intent(ViewSchedules.this, weekendScheduler.class);
                    intent.putExtra("day", Day);
                    intent.putExtra("month", Month);
                    intent.putExtra("year", Year);
                    intent.putExtra("source", source);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ViewSchedules.this, weekdayScheduler.class);
                    intent.putExtra("day", Day);
                    intent.putExtra("month", Month);
                    intent.putExtra("year", Year);
                    intent.putExtra("source", source);
                    startActivity(intent);
                }
            }
        });

        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String train = "";
                if (OpeningTrained.isChecked() && ClosingTrained.isChecked()) {
                    train = "B";
                } else if (OpeningTrained.isChecked()) {
                    train = "O";
                } else if (ClosingTrained.isChecked()) {
                    train = "C";
                }
                if (SearchBar.getText().toString().trim().equals("")) {
                    storeSearchData(train, "", "");
                }else {
                    String[] nameParts = SearchBar.getText().toString().trim().trim().split("\\s+");
                    String firstName = nameParts[0];
                    String lastName = (nameParts.length > 1) ? nameParts[nameParts.length - 1] : "";
                    storeSearchData(train, firstName, lastName);
                }
                Boolean WeekDay = true;
                if (dayofweek.equals("1") || dayofweek.equals("7")){
                    WeekDay = false;
                }
                customAdapter = new CustomAdapter2(ViewSchedules.this, DupFreeEID, FName, LName, Email, trained, WeekDay, DID, slot, Month, Year, Day, source);


                recyclerView = findViewById(R.id.RecyclerView);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewSchedules.this));

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewSchedules.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
            }
        });


    }



    // updates availbility of employees
    void updateAvailbility(String year, String month){
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        EmployeeMonthlyAvailabilityDatabaseHelper dbA = new EmployeeMonthlyAvailabilityDatabaseHelper(this);
        //get all employee IDs
        ArrayList<String> EIDs = (ArrayList<String>) dbE.getAllEID();
        // run loop though all employee IDs
        for (int i = 0; i < EIDs.size(); i++){
            Log.d(TAG, "updateAvailbility: i is: "+i);
            // check if employee has table for that month
            String AID = dbA.findAID(EIDs.get(i), year, month);
            int Smonth = Integer.parseInt(dbE.ReturnStartDate(EIDs.get(i))[0]);
            int Syear = Integer.parseInt(dbE.ReturnStartDate(EIDs.get(i))[1]);
            if (AID.equals("0")){
                // make table of latest month
                String[] IdInfo = dbA.DefaultTime(EIDs.get(i), Integer.valueOf(Year), Integer.valueOf(Month), Syear, Smonth);
                String Mon = IdInfo[1];
                String Tue = IdInfo[2];
                String Wed = IdInfo[3];
                String Thu = IdInfo[4];
                String Fri = IdInfo[5];
                String Sat = IdInfo[6];
                String Sun = IdInfo[7];
                dbA.addEmployeeMonth(Integer.valueOf(EIDs.get(i)), Integer.valueOf(Month), Integer.valueOf(Year), Mon, Tue, Wed, Thu, Fri, Sat, Sun);
            }else if (!dbA.checkModify(AID)) {
                Log.d(TAG, "updateAvailbility: updating "+EIDs.get(i)+" availability");
                String[] IdInfo = dbA.DefaultTime(EIDs.get(i), Integer.valueOf(Year), Integer.valueOf(Month), Syear, Smonth);
                Log.d(TAG, "updateAvailbility: FINDERS KEEPERS");
                String Mon = IdInfo[1];
                String Tue = IdInfo[2];
                String Wed = IdInfo[3];
                String Thu = IdInfo[4];
                String Fri = IdInfo[5];
                String Sat = IdInfo[6];
                String Sun = IdInfo[7];
                dbA.editEmployeeMonth(AID,Integer.valueOf(Month), Integer.valueOf(Year), Mon, Tue, Wed, Thu, Fri, Sat, Sun);
            }
        }
    }

    void storeData(){
        EID = (ArrayList<String>) DB.avaliableEmployees(Year, Month, dayofweek, shift, "B");
        DupFreeEID = new ArrayList<String>();
        Log.d("EIDS", "storeData: "+EID);
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        ShiftDatabaseHelper dbS = new ShiftDatabaseHelper(this);
        EmployeeMonthlyAvailabilityDatabaseHelper dbA = new EmployeeMonthlyAvailabilityDatabaseHelper(this);
        EmployeeDaysOffDatabsaeHelper dbD = new EmployeeDaysOffDatabsaeHelper(this);
        if (EID.size() > 0){
            FName = new ArrayList<String>();
            LName = new ArrayList<String>();
            Email = new ArrayList<String>();
            trained = new ArrayList<String>();
        }
        for (int i = 0; i < EID.size(); i++){
            if (dbS.WorkingCheck(DID, EID.get(i), slot).equals("0") && dbD.findDID(EID.get(i), Integer.valueOf(Year), Integer.valueOf(Month), Integer.valueOf(Day)).equals("0")){
                String[] IdInfo = dbE.getInfoByID(EID.get(i));
                DupFreeEID.add(EID.get(i));
                FName.add(IdInfo[1]);
                LName.add(IdInfo[2]);
                Email.add(IdInfo[3]);
                trained.add(IdInfo[7]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (slot.equals("F")){
            Intent intent = new Intent(ViewSchedules.this, weekendScheduler.class);
            intent.putExtra("day", Day);
            intent.putExtra("month", Month);
            intent.putExtra("year", Year);
            intent.putExtra("source", source);
            startActivity(intent);
        }else {
            Intent intent = new Intent(ViewSchedules.this, weekdayScheduler.class);
            intent.putExtra("day", Day);
            intent.putExtra("month", Month);
            intent.putExtra("year", Year);
            intent.putExtra("source", source);
            startActivity(intent);
        }
    }
    void storeSearchData(String train, String Fname, String Lname) {
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(this);
        empID = new ArrayList<>();
        empID = (ArrayList<String>) dbE.returnCertainEID(train, Fname, Lname);
        DupFreeEID = new ArrayList<String>();
        Log.d("EIDS", "storeData: " + EID);
        ShiftDatabaseHelper dbS = new ShiftDatabaseHelper(this);
        EmployeeMonthlyAvailabilityDatabaseHelper dbA = new EmployeeMonthlyAvailabilityDatabaseHelper(this);
        EmployeeDaysOffDatabsaeHelper dbD = new EmployeeDaysOffDatabsaeHelper(this);
        if (EID.size() > 0) {
            FName = new ArrayList<String>();
            LName = new ArrayList<String>();
            Email = new ArrayList<String>();
            trained = new ArrayList<String>();
        }
        for (int i = 0; i < EID.size(); i++) {
            if (dbS.WorkingCheck(DID, EID.get(i), slot).equals("0") && dbD.findDID(EID.get(i), Integer.valueOf(Year), Integer.valueOf(Month), Integer.valueOf(Day)).equals("0")) {
                for (int j = 0; j < empID.size(); j++) {
                    if (EID.get(i).equals(empID.get(j))) {
                        String[] IdInfo = dbE.getInfoByID(EID.get(i));
                        DupFreeEID.add(EID.get(i));
                        FName.add(IdInfo[1]);
                        LName.add(IdInfo[2]);
                        Email.add(IdInfo[3]);
                        trained.add(IdInfo[7]);
                    }
                }
            }
        }
    }

}

