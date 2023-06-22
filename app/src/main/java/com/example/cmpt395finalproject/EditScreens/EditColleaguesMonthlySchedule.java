package com.example.cmpt395finalproject.EditScreens;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cmpt395finalproject.DayDatabaseHelper;
import com.example.cmpt395finalproject.EmployeeDatabaseHelper;
import com.example.cmpt395finalproject.EmployeeMonthlyAvailabilityDatabaseHelper;
import com.example.cmpt395finalproject.MonthDatabaseHelper;
import com.example.cmpt395finalproject.R;
import com.example.cmpt395finalproject.ShiftDatabaseHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditColleaguesMonthlySchedule extends Fragment {

    private String id, AID, Mon, Tue, Wed, Thu, Fri, Sat, Sun;
    private int Year;
    private int Month;
    private Spinner yearSpinner,monthSpinner;
    private ToggleButton MMonBtn, MTueBtn, MWedBtn, MThuBtn, MFriBtn, AMonBtn, ATueBtn, AWedBtn, AThuBtn, AFriBtn, SatBtn, SunBtn;
    private String MonCheck, TueCheck, WedCheck, ThuCheck, FriCheck, SatCheck, SunCheck, FName, LName;
    private Context mContext;
    private Button UpdateBtn, SelectTime, resetBtn;

    public EditColleaguesMonthlySchedule(String id) {

        this.id = id;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_edit_colleagues_monthly_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Calendar calendar = Calendar.getInstance();
         Year = calendar.get(Calendar.YEAR);
         Month = calendar.get(Calendar.MONTH)+1;
         infoGrabber();
    }

    @Override
    public void onStart() {
        super.onStart();
        infoGrabber();
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(mContext);
        dbE.getInfoByID(id);
        String[] IdInfo = dbE.getInfoByID(id);
        FName = IdInfo[1];
        LName = IdInfo[2];
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(String.valueOf(Month)+"/"+String.valueOf(Year)+": "+FName+" "+LName);
        yearSpinner = (Spinner) getView().findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner) getView().findViewById(R.id.MonthSpinner);
        // connect buttons
        MMonBtn = (ToggleButton) getView().findViewById(R.id.toggleMMondayBtn);
        MTueBtn = (ToggleButton) getView().findViewById(R.id.toggleMTuesdayBtn);
        MWedBtn = (ToggleButton) getView().findViewById(R.id.toggleMWednesdayBtn);
        MThuBtn = (ToggleButton) getView().findViewById(R.id.toggleMThursdayBtn);
        MFriBtn = (ToggleButton) getView().findViewById(R.id.toggleMFridayBtn);
        AMonBtn = (ToggleButton) getView().findViewById(R.id.toggleAMondayBtn);
        ATueBtn = (ToggleButton) getView().findViewById(R.id.toggleATuesdayBtn);
        AWedBtn = (ToggleButton) getView().findViewById(R.id.toggleAWednesdayBtn);
        AThuBtn = (ToggleButton) getView().findViewById(R.id.toggleAThursdayBtn);
        AFriBtn = (ToggleButton) getView().findViewById(R.id.toggleAFridayBtn);
        SatBtn = (ToggleButton) getView().findViewById(R.id.toggleSaturdayBtn);
        SunBtn = (ToggleButton) getView().findViewById(R.id.toggleSundayBtn);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.years, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getContext(), R.array.months, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter3);
        monthSpinner.setAdapter(adapter4);
        yearSpinner.setSelection(Year-2020);
        monthSpinner.setSelection(Month-1);
        fillBoxes();
        UpdateBtn = (Button) getView().findViewById(R.id.AddBtn);
        SelectTime = (Button) getView().findViewById(R.id.SelectTimeBtn);
        resetBtn = (Button) getView().findViewById(R.id.ResetButton);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoGrabber();
                fillBoxes();
            }
        });
        SelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                if (CheckShiftChange()) {
                    Log.d("1", "onClick: ");
                    //Ask user if they want to save changes
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("Do you want to save your changes to availability?");
                    builder.setTitle("Changes");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if user agree to change go though list and delete shifts and change days to incomplete status
                            Log.d("2", "onClick: ");
                            MonCheck = "0"; TueCheck = "0"; WedCheck = "0"; ThuCheck = "0"; FriCheck = "0"; SatCheck = "0"; SunCheck = "0";
                            ShiftChange("You have shifts that will be deleted if you change your availability. Do you want to continue?");
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // if user does not agree to change then do nothing
                        }
                    });
                    builder.show();
                }
                */
                Year = yearSpinner.getSelectedItemPosition()+2020;
                Month = monthSpinner.getSelectedItemPosition()+1;
                actionBar.setTitle(String.valueOf(Month)+"/"+String.valueOf(Year)+": "+FName+" "+LName);
                infoGrabber();
                fillBoxes();
            }
        });


        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShiftChange("You have shifts that will be deleted if you change your availability. Do you want to continue?");
                // if user agree to change go though list and delete shifts and change days to incomplete status
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(String.valueOf(Month)+"/"+String.valueOf(Year)+": "+FName+" "+LName);
    }

    // Returns the index for the drop down weekdays
    int weekdayAvailableFill(String status){
        if (status.equals("M")) return 0;
        else if (status.equals("A")) return 1;
        else if (status.equals("B")) return 2;
        else if (status.equals("N")) return 3;
        return 3;
    }
    // Returns the index for the drop down weekends
    int weekendAvailableFill(String status){
        if (status.equals("F")) return 0;
        else if (status.equals("N")) return 1;
        return 1;
    }
    // Returns the char for table base on buttons
    String weekdayAvailableReturn(boolean morning, boolean afternoon){
        if (morning && afternoon) return "B";
        else if (morning && !afternoon) return "M";
        else if (!morning && afternoon) return "A";
        else if (!morning && !afternoon) return "N";
        return "N";
    }
    // Returns the char for table base on buttons
    String weekendAvailableReturn(boolean fullDay){
        if (fullDay) return "F";
        else return "N";
    }

    void infoGrabber(){
        EmployeeMonthlyAvailabilityDatabaseHelper db = new EmployeeMonthlyAvailabilityDatabaseHelper(mContext);
        EmployeeDatabaseHelper db2 = new EmployeeDatabaseHelper(mContext);
        AID = db.findAID(id, String.valueOf(Year), String.valueOf(Month));
        int Smonth = Integer.parseInt(db2.ReturnStartDate(id)[0]);
        int Syear = Integer.parseInt(db2.ReturnStartDate(id)[1]);
        if (AID.equals("0")){
            String[] IdInfo = db.DefaultTime(id, Year, Month, Syear, Smonth);
            Mon = IdInfo[1];
            Tue = IdInfo[2];
            Wed = IdInfo[3];
            Thu = IdInfo[4];
            Fri = IdInfo[5];
            Sat = IdInfo[6];
            Sun = IdInfo[7];
            db.addEmployeeMonth(Integer.valueOf(id), Month, Year, Mon, Tue, Wed, Thu, Fri, Sat, Sun);
            AID = db.findAID(id, String.valueOf(Year), String.valueOf(Month));
        }else if (!db.checkModify(AID)) {
            String[] IdInfo = db.DefaultTime(id, Year, Month, Syear, Smonth);
            Mon = IdInfo[1];
            Tue = IdInfo[2];
            Wed = IdInfo[3];
            Thu = IdInfo[4];
            Fri = IdInfo[5];
            Sat = IdInfo[6];
            Sun = IdInfo[7];
            db.editEmployeeMonth(AID,Month, Year, Mon, Tue, Wed, Thu, Fri, Sat, Sun);
        }else {
            String[] IdInfo = db.getInfoByID(AID);
            Mon = IdInfo[1];
            Tue = IdInfo[2];
            Wed = IdInfo[3];
            Thu = IdInfo[4];
            Fri = IdInfo[5];
            Sat = IdInfo[6];
            Sun = IdInfo[7];
        }
    }

    void fillBoxes() {
        // check button if available
        if (Mon.equals("M") || Mon.equals("B")) MMonBtn.setChecked(true);
        else MMonBtn.setChecked(false);
        if (Tue.equals("M") || Tue.equals("B")) MTueBtn.setChecked(true);
        else MTueBtn.setChecked(false);
        if (Wed.equals("M") || Wed.equals("B")) MWedBtn.setChecked(true);
        else MWedBtn.setChecked(false);
        if (Thu.equals("M") || Thu.equals("B")) MThuBtn.setChecked(true);
        else MThuBtn.setChecked(false);
        if (Fri.equals("M") || Fri.equals("B")) MFriBtn.setChecked(true);
        else MFriBtn.setChecked(false);
        if (Sat.equals("F")) SatBtn.setChecked(true);
        else SatBtn.setChecked(false);
        if (Sun.equals("F")) SunBtn.setChecked(true);
        else SunBtn.setChecked(false);
        if (Mon.equals("A") || Mon.equals("B")) AMonBtn.setChecked(true);
        else AMonBtn.setChecked(false);
        if (Tue.equals("A") || Tue.equals("B")) ATueBtn.setChecked(true);
        else ATueBtn.setChecked(false);
        if (Wed.equals("A") || Wed.equals("B")) AWedBtn.setChecked(true);
        else AWedBtn.setChecked(false);
        if (Thu.equals("A") || Thu.equals("B")) AThuBtn.setChecked(true);
        else AThuBtn.setChecked(false);
        if (Fri.equals("A") || Fri.equals("B")) AFriBtn.setChecked(true);
        else AFriBtn.setChecked(false);
    }

    boolean CheckShiftChange(){
        boolean change = false;
        // checks if the shifts have been changed
        if (!Mon.equals(weekdayAvailableReturn(MMonBtn.isChecked(), AMonBtn.isChecked())) && !weekdayAvailableReturn(MMonBtn.isChecked(), AMonBtn.isChecked()).equals("B") && !Mon.equals("N")){
            MonCheck = weekdayAvailableReturn(MMonBtn.isChecked(), AMonBtn.isChecked());
            change = true;
        }
        if (!Tue.equals(weekdayAvailableReturn(MTueBtn.isChecked(), ATueBtn.isChecked())) && !weekdayAvailableReturn(MTueBtn.isChecked(), ATueBtn.isChecked()).equals("B") && !Tue.equals("N")){
            TueCheck = weekdayAvailableReturn(MTueBtn.isChecked(), ATueBtn.isChecked());
            change = true;
        }
        if (!Wed.equals(weekdayAvailableReturn(MWedBtn.isChecked(), AWedBtn.isChecked())) && !weekdayAvailableReturn(MWedBtn.isChecked(), AWedBtn.isChecked()).equals("B") && !Wed.equals("N")){
            WedCheck = weekdayAvailableReturn(MWedBtn.isChecked(), AWedBtn.isChecked());
            change = true;
        }
        if (!Thu.equals(weekdayAvailableReturn(MThuBtn.isChecked(), AThuBtn.isChecked())) && !weekdayAvailableReturn(MThuBtn.isChecked(), AThuBtn.isChecked()).equals("B") && !Thu.equals("N")){
            ThuCheck = weekdayAvailableReturn(MThuBtn.isChecked(), AThuBtn.isChecked());
            change = true;
        }
        if (!Fri.equals(weekdayAvailableReturn(MFriBtn.isChecked(), AFriBtn.isChecked())) && !weekdayAvailableReturn(MFriBtn.isChecked(), AFriBtn.isChecked()).equals("B") && !Fri.equals("N")){
            FriCheck = weekdayAvailableReturn(MFriBtn.isChecked(), AFriBtn.isChecked());
            change = true;
        }
        if (!Sat.equals(weekendAvailableReturn(SatBtn.isChecked())) && !Sat.equals("N")){
            SatCheck = weekendAvailableReturn(SatBtn.isChecked());
            change = true;
        }
        if (!Sun.equals(weekendAvailableReturn(SunBtn.isChecked())) && !Sun.equals("N")){
            SunCheck = weekendAvailableReturn(SunBtn.isChecked());
            change = true;
        }
        // if they have then return true
        return change;
    }
    // gets the days of the month base on week of the day
    int[] getDays(int year, int month) {
        List<Integer> days = new ArrayList<>();
        LocalDate date = LocalDate.of(year, month, 1);
        while (date.getMonthValue() == month) {
            switch (date.getDayOfWeek()) {
                case MONDAY:
                    if (!MonCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
                case TUESDAY:
                    if (!TueCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
                case WEDNESDAY:
                    if (!WedCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
                case THURSDAY:
                    if (!ThuCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
                case FRIDAY:
                    if (!FriCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
                case SATURDAY:
                    if (!SatCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
                case SUNDAY:
                    if (!SunCheck.equals("0")) days.add(date.getDayOfMonth());
                    break;
            }
            date = date.plusDays(1);
        }
        int[] daysArray = new int[days.size()];
        for (int i = 0; i < days.size(); i++) {
            daysArray[i] = days.get(i);
        }

        return daysArray;
    }


    // returns day of the week in int base on date
    int getDayOfWeek(int year, int month, int day) {
        LocalDate date = LocalDate.of(year, month, day);
        return date.getDayOfWeek().getValue();
    }

    // Ask for change in shifts
    void ShiftChange(String Text){
        EmployeeMonthlyAvailabilityDatabaseHelper db = new EmployeeMonthlyAvailabilityDatabaseHelper(mContext);
        MonthDatabaseHelper db2 = new MonthDatabaseHelper(mContext);
        DayDatabaseHelper db3 = new DayDatabaseHelper(mContext);
        ShiftDatabaseHelper db4 = new ShiftDatabaseHelper(mContext);
        Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);
        //Set Checks
        MonCheck = "0"; TueCheck = "0"; WedCheck = "0"; ThuCheck = "0"; FriCheck = "0"; SatCheck = "0"; SunCheck = "0";
        boolean check = false;

        // check which days were altered
        if (CheckShiftChange()) {
            // get the month id
            String MonthID = db2.findMonth(String.valueOf(Year), String.valueOf(Month));
            if (cYear >= Year && cMonth+1>= Month){
            if (MonthID != "0") {
                // get list of days of the month
                int[] days = getDays(Year, Month);
                for (int i = 0; i < days.length; i++) {
                    // get the days id
                    String DID = db3.findDay(MonthID, String.valueOf(days[i]));
                    if (!DID.equals("0") && days[i] >= cDay) {
                        // check if they have shifts base on those days id
                        String value = "";
                        switch (getDayOfWeek(Year, Month, days[i])) {
                            case 1:
                                value = Mon;
                                break;
                            case 2:
                                value = Tue;
                                break;
                            case 3:
                                value = Wed;
                                break;
                            case 4:
                                value = Thu;
                                break;
                            case 5:
                                value = Fri;
                                break;
                            case 6:
                                value = Sat;
                                break;
                            case 7:
                                value = Sun;
                                break;
                        }
                        // what shifts to check
                        if (value.equals("B")) {
                            // find if they have a morning shift
                            if (!db4.WorkingCheck(DID, id, "AM").equals("0")) {
                                check = true;
                                break;
                            }
                            // find if they have a pm shift
                            if (!db4.WorkingCheck(DID, id, "PM").equals("0")) {
                                check = true;
                                break;
                            }
                        } else if (value.equals("M")) {
                            // find if they have a morning shift
                            if (!db4.WorkingCheck(DID, id, "AM").equals("0")) {
                                check = true;
                                break;
                            }
                        } else if (value.equals("A")) {
                            // find if they have a pm shift
                            if (!db4.WorkingCheck(DID, id, "PM").equals("0")) {
                                check = true;
                                break;
                            }
                        } else if (value.equals("F")) {
                            // find if they have a shift
                            if (!db4.WorkingCheck(DID, id, "F").equals("0")) {
                                check = true;
                                break;
                            }
                        }
                    }
                }
                }
            }

        }
        // if they have shift then warn user of change
        if (check) {
            // create a dialog box to warn user of change
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Warning");
            builder.setMessage(Text);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // if user agree to change go though list and delete shifts and change days to incomplete status
                    String MonthID = db2.findMonth(String.valueOf(Year), String.valueOf(Month));
                    int[] days = getDays(Year, Month);
                    if (cYear >= Year && cMonth+1>= Month){
                    for (i = 0; i < days.length; i++) {
                        // get the days id
                        String DID = db3.findDay(MonthID, String.valueOf(days[i]));
                        if (!DID.equals("0") && days[i] >= cDay) {
                            // check if they have shifts base on those days id
                            String value = "";
                            switch (getDayOfWeek(Year, Month, days[i])) {
                                case 1:
                                    value = MonCheck;
                                    break;
                                case 2:
                                    value = TueCheck;
                                    break;
                                case 3:
                                    value = WedCheck;
                                    break;
                                case 4:
                                    value = ThuCheck;
                                    break;
                                case 5:
                                    value = FriCheck;
                                    break;
                                case 6:
                                    value = Sat;
                                    break;
                                case 7:
                                    value = Sun;
                                    break;
                            }
                            // what shifts to check
                            if (value.equals("N")) {
                                // delete shift
                                db3.fillDay(DID, 1);
                                db4.deleteShift(DID, id, "AM");
                                // delete shift
                                db4.deleteShift(DID, id, "PM");
                                if (shiftChecker(DID, "W")) {
                                    db3.fillDay(DID, 0);
                                }else {
                                    db3.fillDay(DID, 1);
                                }
                            } else if (value.equals("M")) {
                                // delete shift
                                db4.deleteShift(DID, id, "PM");
                                if (shiftChecker(DID, "W")) {
                                    db3.fillDay(DID, 0);
                                }else {
                                    db3.fillDay(DID, 1);
                                }
                            } else if (value.equals("A")) {
                                // delete shift
                                db4.deleteShift(DID, id, "AM");
                                if (shiftChecker(DID, "W")) {
                                    db3.fillDay(DID, 0);
                                }else {
                                    db3.fillDay(DID, 1);
                                }
                            } else if (value.equals("F")) {
                                // delete shift
                                db4.deleteShift(DID, id, "F");
                                if (shiftChecker(DID, "E")) {
                                    db3.fillDay(DID, 0);
                                }else {
                                    db3.fillDay(DID, 1);
                                }
                            }
                        }
                    }
                    }
                    // update the database
                    db.editEmployeeMonth(AID, Month, Year, weekdayAvailableReturn(MMonBtn.isChecked(), AMonBtn.isChecked()), weekdayAvailableReturn(MTueBtn.isChecked(), ATueBtn.isChecked()), weekdayAvailableReturn(MWedBtn.isChecked(), AWedBtn.isChecked()), weekdayAvailableReturn(MThuBtn.isChecked(), AThuBtn.isChecked()), weekdayAvailableReturn(MFriBtn.isChecked(), AFriBtn.isChecked()), weekendAvailableReturn(SatBtn.isChecked()), weekendAvailableReturn(SunBtn.isChecked()));
                    Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
                    db.markAsModified(id,String.valueOf(Year),String.valueOf(Month));
                    // reset the values on Check values
                    MonCheck = "0"; TueCheck = "0"; WedCheck = "0"; ThuCheck = "0"; FriCheck = "0"; SatCheck = "0"; SunCheck = "0";
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // if user does not agree to change then do nothing
                }
            });
            builder.show();
        } else {
            // if they do not have shifts then just update the database
            db.markAsModified(id,String.valueOf(Year),String.valueOf(Month));
            db.editEmployeeMonth(AID, Month, Year, weekdayAvailableReturn(MMonBtn.isChecked(), AMonBtn.isChecked()), weekdayAvailableReturn(MTueBtn.isChecked(), ATueBtn.isChecked()), weekdayAvailableReturn(MWedBtn.isChecked(), AWedBtn.isChecked()), weekdayAvailableReturn(MThuBtn.isChecked(), AThuBtn.isChecked()), weekdayAvailableReturn(MFriBtn.isChecked(), AFriBtn.isChecked()), weekendAvailableReturn(SatBtn.isChecked()), weekendAvailableReturn(SunBtn.isChecked()));
            Toast.makeText(mContext, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    // Check if day has shifts or not True if there is no shifts
    boolean shiftChecker(String DID, String Shift){
        ShiftDatabaseHelper db = new ShiftDatabaseHelper(mContext);
        // check if checking weekday or weekend
        if (Shift.equals("W")) {
            ArrayList<String> EID1 = (ArrayList<String>) db.returnShifts(DID, "AM");
            ArrayList<String> EID2 = (ArrayList<String>) db.returnShifts(DID, "PM");
            if (EID1.size() == 0 && EID2.size() == 0) {
                return true;
            } else {
                return false;
            }
        }else {
            ArrayList<String> EID3 = (ArrayList<String>) db.returnShifts(DID, "F");
            if (EID3.size() == 0) {
                return true;
            } else {
                return false;
            }
        }
    }
}