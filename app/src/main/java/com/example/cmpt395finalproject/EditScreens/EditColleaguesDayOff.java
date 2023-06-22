package com.example.cmpt395finalproject.EditScreens;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cmpt395finalproject.DayDatabaseHelper;
import com.example.cmpt395finalproject.EmployeeDatabaseHelper;
import com.example.cmpt395finalproject.EmployeeDaysOffDatabsaeHelper;
import com.example.cmpt395finalproject.MonthDatabaseHelper;
import com.example.cmpt395finalproject.R;
import com.example.cmpt395finalproject.ShiftDatabaseHelper;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EditColleaguesDayOff extends Fragment {
    private Context mContext;
    public CompactCalendarView compactCalendar;
    public TextView title;
    public Date date = new Date();
    public Button Exit, exportBtn;
    public int month, year;
    public List<Event> events = new ArrayList<>();

    public String[] incompleteDays, completeDays;
    public SimpleDateFormat toMilli;
    public SimpleDateFormat dateFormatMonth = new SimpleDateFormat ("MMMM yyyy", Locale.getDefault());
    private String id, FName,LName;
    private CalendarView cal;
    public EditColleaguesDayOff(String id) {
        this.id = id;
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit_colleagues_day_off, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        cal = (CalendarView) getView().findViewById(R.id.calendarView2);
        EmployeeDaysOffDatabsaeHelper db = new EmployeeDaysOffDatabsaeHelper(mContext);
        EmployeeDatabaseHelper dbE = new EmployeeDatabaseHelper(mContext);
        MonthDatabaseHelper dbM = new MonthDatabaseHelper(mContext);
        DayDatabaseHelper dbD = new DayDatabaseHelper(mContext);
        ShiftDatabaseHelper dbS = new ShiftDatabaseHelper(mContext);
        dbE.getInfoByID(id);
        String[] IdInfo = dbE.getInfoByID(id);
        FName = IdInfo[1];
        LName = IdInfo[2];
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(FName+" "+LName);
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        int thisMonth = calendar.get(Calendar.MONTH);
        date.setMonth(thisMonth);
        date.setYear(thisYear- 1900);

        //date.setYear(2016 - 1900); /* Starts at the year 1900 */
        compactCalendar = (CompactCalendarView) getView().findViewById(R.id.compactcalendar_view);
        compactCalendar.shouldDrawIndicatorsBelowSelectedDays(true);
        compactCalendar.setCurrentDate(date);

        title = (TextView)  getView().findViewById(R.id.textView19);
        title.setText(dateFormatMonth.format(date));
        toMilli = new SimpleDateFormat("MM/dd/yyyy");
        events = new ArrayList<>();
        updateDaysOff();


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override

            public void onDayClick(Date dateClicked) {
                int i1 = dateClicked.getMonth();
                int i2 = dateClicked.getDate();
                int i = dateClicked.getYear() + 1900;
                String date = (i1 + 1) + "/" + i2 + "/" + i;
                String DID = db.findDID(id, i, i1+1,i2);
                if (DID.equals("0")){
                    //find MID and DID and check if there is a shift
                    updateDaysOff();
                    String MID = dbM.findMonth(String.valueOf(i), String.valueOf(i1+1));
                    if (!MID.equals("0")){
                        String DID2 = dbD.findDay(MID, String.valueOf(i2));
                        if (!DID2.equals("0")){
                            if (!dbS.WorkingCheck(DID2, id, "AM").equals("0") || !dbS.WorkingCheck(DID2, id, "PM").equals("0") || !dbS.WorkingCheck(DID2, id, "F").equals("0") ){
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setMessage("Adding this day will remove all shifts for this day. Do you want to continue?");
                                builder.setTitle("Changes");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int j) {
                                        dbS.deleteShift(DID2, id, "AM");
                                        // delete shift
                                        dbS.deleteShift(DID2, id, "PM");
                                        dbS.deleteShift(DID2, id, "F");
                                        db.addEmployeeDayOff(Integer.valueOf(id), i, i1+1, i2);
                                        updateDaysOff();
                                        //Get day of week
                                        LocalDate DOW = LocalDate.of(i, i1+1, i2);
                                        int D = DOW.getDayOfWeek().getValue();
                                        // if Check if day is empty after and mark day apporpriately
                                        if (D == 6 || D == 7){
                                            ArrayList<String> EID3 = (ArrayList<String>) dbS.returnShifts(DID2, "F");
                                            if (EID3.size() == 0) {
                                                dbD.fillDay(DID2, 0);
                                            } else {
                                                dbD.fillDay(DID2, 1);
                                            }
                                        }else {
                                            ArrayList<String> EID1 = (ArrayList<String>) dbS.returnShifts(DID2, "AM");
                                            ArrayList<String> EID2 = (ArrayList<String>) dbS.returnShifts(DID2, "PM");
                                            if (EID1.size() == 0 && EID2.size() == 0) {
                                                dbD.fillDay(DID2, 0);
                                            } else {
                                                dbD.fillDay(DID2, 1);
                                            }
                                        }
                                    }
                                });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // if user does not agree to change then do nothing
                                    }
                                });
                                builder.show();
                            }else {
                                db.addEmployeeDayOff(Integer.valueOf(id), i, i1+1, i2);
                                updateDaysOff();
                            }
                        }else {
                            db.addEmployeeDayOff(Integer.valueOf(id), i, i1+1, i2);
                            updateDaysOff();
                        }
                    }else {
                        db.addEmployeeDayOff(Integer.valueOf(id), i, i1+1, i2);
                        updateDaysOff();
                    }

                }else {
                    db.DeleteEmployeeMonth(id, i, i1+1,i2);
                    updateDaysOff();
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



        /*
        dbE.getInfoByID(id);
        String[] IdInfo = dbE.getInfoByID(id);
        FName = IdInfo[1];
        LName = IdInfo[2];
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Emp "+FName+" "+LName);
         */
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(FName+" "+LName);
    }

    // update the days off
    void updateDaysOff(){
        EmployeeDaysOffDatabsaeHelper db = new EmployeeDaysOffDatabsaeHelper(mContext);
        for (int i = 0; i < events.size(); i++){
            compactCalendar.removeEvent(events.get(i));
        }
        List<String> DOID = new ArrayList<>();
        events = new ArrayList<>();
        DOID = db.findEmployeeDayOff(id);
        for (int i = 0; i < DOID.size(); i++){
            try {
                Date timeMilli = toMilli.parse(DOID.get(i));
                events.add(new Event(Color.RED, timeMilli.getTime(), "teachers professional day"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        for (int i = 0; i < DOID.size(); i++){
            compactCalendar.addEvent(events.get(i));
        }
    }
}