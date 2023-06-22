package com.example.cmpt395finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewColleagues extends AppCompatActivity{
    Context context;

    EmployeeDatabaseHelper DB;
    RecyclerView recyclerView;
    ArrayList<String> EID, empID, empFName, empLName, empPhone, empEmail, trained;
    CustomAdapter customAdapter;
    private EditText SearchBar;
    Button AddEmployeeBtn, BackBtn, SearchBtn;
    CheckBox OpeningTrained, ClosingTrained;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_colleagues);

        /* Define the database and its fields */
        DB = new EmployeeDatabaseHelper(ViewColleagues.this);
        empID = new ArrayList<>();
        empFName = new ArrayList<>();
        empLName = new ArrayList<>();
        empPhone = new ArrayList<>();
        empEmail = new ArrayList<>();
        trained = new ArrayList<>();

        storeData();
        customAdapter = new CustomAdapter(ViewColleagues.this, empID, empFName, empLName, empPhone,
                empEmail, trained);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewColleagues.this));
        AddEmployeeBtn = findViewById(R.id.ViewAddEmployeeBtn);
        BackBtn = findViewById(R.id.ViewBackBtn);
        SearchBar = findViewById(R.id.ViewSearchBar);
        SearchBtn = findViewById(R.id.ViewSearchBtn);
        OpeningTrained = findViewById(R.id.OTrainedCheck);
        ClosingTrained = findViewById(R.id.CTrainedChecked);

        // Helper function to go to the add employee page.
        AddEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goAddColleagues();
            }
        });

        // Helper function to go back to the previous page.
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
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
                customAdapter = new CustomAdapter(ViewColleagues.this, empID, empFName, empLName, empPhone,
                        empEmail, trained);
                recyclerView = findViewById(R.id.RecyclerView);
                recyclerView.setAdapter(customAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewColleagues.this));
            }
        });

    }


    /* Helper function to enter data into the database.*/
    void storeData() {
        empID = (ArrayList<String>) DB.getAllEID();
        if (empID.size() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < empID.size(); i++) {
                String[] IdInfo = DB.getInfoByID(empID.get(i));
                empFName.add(IdInfo[1]);
                empLName.add(IdInfo[2]);
                empEmail.add(IdInfo[3]);
                empPhone.add(IdInfo[4]);
                if (IdInfo[7].equals("O")) {
                    trained.add("Opening Trained");
                } else if (IdInfo[7].equals("C")) {
                    trained.add("Closing Trained");
                } else if (IdInfo[7].equals("N")) {
                    trained.add("Not Trained");
                } else {
                    trained.add("Both Trained");
                }
            }
        }
    }
    /* The following are helper functions for redirecting the user to another page. */
    public void goAddColleagues(){
        Intent intent = new Intent(this, AddColleagues.class);
        startActivity(intent);
    }
    public void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    void storeSearchData(String train, String Fname, String Lname){
        empID = new ArrayList<>();
        empFName = new ArrayList<>();
        empLName = new ArrayList<>();
        empPhone = new ArrayList<>();
        empEmail = new ArrayList<>();
        trained = new ArrayList<>();
        empID = (ArrayList<String>) DB.returnCertainEID(train, Fname, Lname);
        if (empID.size() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < empID.size(); i++) {
                String[] IdInfo = DB.getInfoByID(empID.get(i));
                empFName.add(IdInfo[1]);
                empLName.add(IdInfo[2]);
                empEmail.add(IdInfo[3]);
                empPhone.add(IdInfo[4]);
                if (IdInfo[7].equals("O")) {
                    trained.add("Opening Trained");
                } else if (IdInfo[7].equals("C")) {
                    trained.add("Closing Trained");
                } else if (IdInfo[7].equals("N")) {
                    trained.add("Not Trained");
                } else {
                    trained.add("Both Trained");
                }
            }
        }
    }

}
