package com.example.cmpt395finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AddColleagues extends AppCompatActivity {

    private Button BackBtn, AddEmpBtn;
    private EditText FNameBox, LNameBox, EmailBox, PhoneBox;
    private CheckBox opening, closing;
    private String training = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_colleagues);

        /* assign buttons to variables */
        BackBtn = findViewById(R.id.BackBtn);
        AddEmpBtn = findViewById(R.id.AddEmployeeBtn);
        FNameBox = findViewById(R.id.EmployeeFName);
        LNameBox = findViewById(R.id.employeeLName);
        EmailBox = findViewById(R.id.Email);
        PhoneBox = findViewById(R.id.Phone);
        opening = findViewById(R.id.OpeningCheck);
        closing = findViewById(R.id.ClosingCheck);

        /* Set on click listener for the back button */
        BackBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goBack();
            }
        });

        /* Set on click listener for add employee button */
        AddEmpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // starts helper class
                EmployeeDatabaseHelper db = new EmployeeDatabaseHelper(AddColleagues.this);
                // check if any box checked
                if (opening.isChecked() && closing.isChecked()){training = "B";
                } else if (opening.isChecked()) {training = "O";
                } else if (closing.isChecked()) {training = "C";}
                // checks if First name is empty
                if (FNameBox.getText().toString().trim().isEmpty()){
                    FNameBox.setError("Please enter a first name");
                }//checks if Fname is letters only
                else if (!FNameBox.getText().toString().trim().matches("[a-zA-Z]+")){
                    FNameBox.setError("Please enter a valid first name");
                }//checks if Fname is less than or equal to 25 length
                else if ((FNameBox.getText()).length() >25) {
                    FNameBox.setError("Max length is 25 characters");
                }//checks if Last name is empty
                else if (LNameBox.getText().toString().trim().isEmpty()) {
                    LNameBox.setError("Please enter a last name");
                }//checks if Lname is letters only
                else if (!LNameBox.getText().toString().trim().matches("[a-zA-Z]+")){
                    LNameBox.setError("Please enter a valid last name");
                }//checks if Lname is less than or equal to 25 length
                else if ((LNameBox.getText()).length() >25) {
                    LNameBox.setError("Max length is 25 characters");
                }//checks if Email is empty
                else if (EmailBox.getText().toString().trim().isEmpty()) {
                    EmailBox.setError("Please enter an email");
                }//checks if Email is valid
                else if (!EmailBox.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                    EmailBox.setError("Please enter a valid email");
                }//checks if Phone is empty
                else if (PhoneBox.getText().toString().trim().isEmpty()) {
                    PhoneBox.setError("Please enter a phone number");
                }//checks if Phone is numbers only
                else if (!PhoneBox.getText().toString().trim().matches("[0-9]+")){
                    PhoneBox.setError("Please enter a valid phone number");
                }//checks if Phone is less than or equal to 11 length
                else if ((PhoneBox.getText()).length() >11) {
                    PhoneBox.setError("Max length is 11 characters");
                }else if(!db.DupChecker(capitalizeFirstLetter(FNameBox.getText().toString().trim()), capitalizeFirstLetter(LNameBox.getText().toString().trim()), PhoneBox.getText().toString().trim(), EmailBox.getText().toString().trim()).equals("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddColleagues.this);
                    builder.setMessage("You already have an employee with this information. Please check your employee list and try again.");
                    builder.setCancelable(false);

                    builder.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    finish(); // exits the activity
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                    // if all checks pass, add employee to database
                }else{
                    Intent intent = new Intent(AddColleagues.this, StartingMonth.class);
                    intent.putExtra("FName", FNameBox.getText().toString().trim());
                    intent.putExtra("LName", LNameBox.getText().toString().trim());
                    intent.putExtra("email", EmailBox.getText().toString().trim());
                    intent.putExtra("phone", PhoneBox.getText().toString().trim());
                    intent.putExtra("training", training);
                    startActivity(intent);
                }
            }
        });
    }

    // Helper function for the back button.
    public void goBack(){
        Intent intent = new Intent(this, ViewColleagues.class);
        startActivity(intent);
    }
    public static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}