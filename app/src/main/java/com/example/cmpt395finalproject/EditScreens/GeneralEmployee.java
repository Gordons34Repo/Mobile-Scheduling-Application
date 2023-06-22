package com.example.cmpt395finalproject.EditScreens;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cmpt395finalproject.DayDatabaseHelper;
import com.example.cmpt395finalproject.EmployeeDatabaseHelper;
import com.example.cmpt395finalproject.MonthDatabaseHelper;
import com.example.cmpt395finalproject.R;
import com.example.cmpt395finalproject.ShiftDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;


public class GeneralEmployee extends Fragment {


    private String id, FName, LName, Email, Phone, trained;
    private Context mContext;
    private Button UpdateBtn;
    ArrayList<String> MIDS, DIDS;

    private String OpeningModify = "N";
    private String ClosingModify = "N";
    private Boolean OpeningStart = false;
    private Boolean ClosingStart = false;
    private Boolean OpeningEnd, ClosingEnd;

    public GeneralEmployee(String id) {

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
        return inflater.inflate(R.layout.fragment_general_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(mContext);
        String[] IdInfo = db.getInfoByID(id);
        FName = IdInfo[1];
        LName = IdInfo[2];
        Email = IdInfo[3];
        Phone = IdInfo[4];
        trained = IdInfo[7];
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(FName + " " + LName);
        EditText FNameBox = getView().findViewById(R.id.employeeFName);
        EditText LNameBox = (EditText) getView().findViewById(R.id.employeeLName2);
        EditText EmailBox = (EditText) getView().findViewById(R.id.email);
        EditText PhoneBox = (EditText) getView().findViewById(R.id.phone);
        CheckBox Opening = (CheckBox) getView().findViewById(R.id.openingCheck);
        CheckBox Closing = (CheckBox) getView().findViewById(R.id.closingCheck);
        FNameBox.setText(FName);
        LNameBox.setText(LName);
        EmailBox.setText(Email);
        PhoneBox.setText(Phone);
        if (trained.equals("B")) {
            Opening.setChecked(true);
            Closing.setChecked(true);
            OpeningStart = true;
            ClosingStart = true;
        } else if (trained.equals("C")) {
            ClosingStart = true;
            Closing.setChecked(true);
        } else if (trained.equals("O")) {
            OpeningStart = true;
            Opening.setChecked(true);
        }
        UpdateBtn = (Button) getView().findViewById(R.id.UpdateGeneralBtn);

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String training = "N";
                if (Opening.isChecked() && Closing.isChecked()) {
                    training = "B";
                    OpeningEnd = true;
                    ClosingEnd = true;
                } else if (Opening.isChecked()) {
                    training = "O";
                    OpeningEnd = true;
                    ClosingEnd = false;
                } else if (Closing.isChecked()) {
                    training = "C";
                    ClosingEnd = true;
                    OpeningEnd = false;
                } else {
                    Log.d("BROSKY", "onClick: HEY BROS");
                    OpeningEnd = false;
                    ClosingEnd = false;
                }
                //check if FName is empty
                if (FNameBox.getText().toString().equals("")) {
                    FNameBox.setError("First Name is required");
                    return;
                }//check if Fname is letters only
                else if (!FNameBox.getText().toString().matches("[a-zA-Z]+")) {
                    FNameBox.setError("First Name must be letters only");
                    return;
                }//check if Fname is max 25 characters
                else if (FNameBox.getText().toString().length() > 25) {
                    FNameBox.setError("First Name must be less than 25 characters");
                    return;
                }//check if LName is empty
                if (LNameBox.getText().toString().equals("")) {
                    LNameBox.setError("Last Name is required");
                    return;
                }//check if Lname is letters only
                else if (!LNameBox.getText().toString().matches("[a-zA-Z]+")) {
                    LNameBox.setError("Last Name must be letters only");
                    return;
                }//check if Lname is max 25 characters
                else if (LNameBox.getText().toString().length() > 25) {
                    LNameBox.setError("Last Name must be less than 25 characters");
                    return;
                }//check if email is empty
                if (EmailBox.getText().toString().equals("")) {
                    EmailBox.setError("Email is required");
                    return;
                }//check if email is valid
                else if (!EmailBox.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                    EmailBox.setError("Email is not valid");
                    return;
                }//check if phone is empty
                if (PhoneBox.getText().toString().equals("")) {
                    PhoneBox.setError("Phone is required");
                    return;
                }//check if phone is valid
                else if (!PhoneBox.getText().toString().matches("[0-9]+")) {
                    PhoneBox.setError("Phone must be numbers only");
                    return;
                }//check if phone is max 11 characters
                else if (PhoneBox.getText().toString().length() > 11) {
                    PhoneBox.setError("Phone must be less than 10 characters");
                    return;
                } else {
                    // checks if training has been modified
                    boolean trainingModify = false;
                    if (OpeningStart != OpeningEnd) {
                        trainingModify = true;
                        if (OpeningEnd) {
                            OpeningModify = "A";
                        } else {
                            OpeningModify = "R";
                        }
                    }
                    if (ClosingStart != ClosingEnd) {
                        trainingModify = true;
                        if (ClosingEnd) {
                            ClosingModify = "A";
                        } else {
                            ClosingModify = "R";
                        }
                    }
                    if (trainingModify) {
                        modifyTraining(training);
                        db.editEmployee(id, FNameBox.getText().toString(), LNameBox.getText().toString(), PhoneBox.getText().toString(), EmailBox.getText().toString(), training);

                    }else {
                        db.editEmployee(id, FNameBox.getText().toString(), LNameBox.getText().toString(), PhoneBox.getText().toString(), EmailBox.getText().toString(), training);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(FName + " " + LName);
    }

    public void modifyTraining(String training) {
        // start database
        MonthDatabaseHelper dbMonth = new MonthDatabaseHelper(mContext);
        DayDatabaseHelper dbDay = new DayDatabaseHelper(mContext);
        ShiftDatabaseHelper dbShift = new ShiftDatabaseHelper(mContext);
        // get current month
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        MIDS = (ArrayList<String>) dbMonth.getMID(String.valueOf(currentYear), String.valueOf(currentMonth));
        // loop through all months
        for (int i = 0; i < MIDS.size(); i++) {

            // loops though all complete days
            String[] MonthYear = dbMonth.getYearMonth(MIDS.get(i));
            DIDS = (ArrayList<String>) dbDay.DaysFilledID(MIDS.get(i));
            if (MonthYear[0].equals(String.valueOf(currentYear)) && MonthYear[1].equals(String.valueOf(currentMonth+1))) {
                // loop through all days
                for (int j = 0; j < DIDS.size(); j++) {
                    if (Integer.valueOf(dbDay.getDay(DIDS.get(j))) >= calendar.get(Calendar.DAY_OF_MONTH)) {
                            dbShift.editShiftTraining(DIDS.get(j), id, training);
                            markDayState(DIDS.get(j));
                    }
                    }
                }else{
                    // loop through all days
                    for (int j = 0; j < DIDS.size(); j++) {
                        dbShift.editShiftTraining(DIDS.get(j), id, training);
                        markDayState(DIDS.get(j));
                    }
                }//else end

            // loops though all incomplete days
            DIDS = new ArrayList<String>();
            DIDS = (ArrayList<String>) dbDay.DaysFilledIncompleteID(MIDS.get(i));
            // loop through all days
            if (MonthYear[0].equals(String.valueOf(currentYear)) && MonthYear[1].equals(String.valueOf(currentMonth+1))) {
                // loop through all days
                for (int j = 0; j < DIDS.size(); j++) {
                    if (Integer.valueOf(dbDay.getDay(DIDS.get(j))) >= calendar.get(Calendar.DAY_OF_MONTH)) {
                        dbShift.editShiftTraining(DIDS.get(j), id, training);
                        markDayState(DIDS.get(j));
                    }
                }
            }else{
                // loop through all days
                for (int j = 0; j < DIDS.size(); j++) {
                    dbShift.editShiftTraining(DIDS.get(j), id, training);
                    markDayState(DIDS.get(j));
                }
            }//else end

            }//month loop end
        }//function end
        // marks day state
        public void markDayState (String dayID){
            DayDatabaseHelper dbDay = new DayDatabaseHelper(mContext);
            ShiftDatabaseHelper dbShift = new ShiftDatabaseHelper(mContext);
            ArrayList<String> Shifts = (ArrayList<String>) dbShift.returnTraining(dayID, "F");
            boolean TrainedOP = false;
            boolean TrainedCL = false;
            if (Shifts.size() == 0) {
                ArrayList<String> Shift1 = (ArrayList<String>) dbShift.returnTraining(dayID, "AM");
                ArrayList<String> Shift2 = (ArrayList<String>) dbShift.returnTraining(dayID, "PM");
                if (Shift1.size() == 2 && Shift2.size() == 2){
                        if (((Shift1.get(0).equals("O") || Shift1.get(0).equals("B")) ||
                                (Shift1.get(1).equals("O") || Shift1.get(1).equals("B"))) &&
                                ((Shift2.get(0).equals("C") || Shift2.get(0).equals("B")) ||
                                (Shift2.get(1).equals("C") || Shift2.get(1).equals("B")))){
                        dbDay.fillDay(dayID, 2);
                    }else {
                        dbDay.fillDay(dayID, 1);
                    }
                } else {
                    dbDay.fillDay(dayID, 1);
                }
            } else if (Shifts.size() == 2) {
                for (int i = 0; i < Shifts.size(); i++) {
                    if (Shifts.get(i).equals("O")) {
                        TrainedOP = true;
                    }
                    if (Shifts.get(i).equals("C")) {
                        TrainedCL = true;
                    }
                    if (Shifts.get(i).equals("B")) {
                        TrainedOP = true;
                        TrainedCL = true;
                    }
                }
                if (TrainedOP && TrainedCL) {
                    dbDay.fillDay(dayID, 2);
                }//weekday complete
                else {
                    dbDay.fillDay(dayID, 1);
                }//weekday incomplete
            } else {
                dbDay.fillDay(dayID, 1);
            }// weekend incomplete
        }
}