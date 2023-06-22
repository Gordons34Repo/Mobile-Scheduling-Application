package com.example.cmpt395finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDatabaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private Context context;
    private static final String DATABASE_NAME = "employee_db";
    private static final int DATABASE_VERSION = 1;
    // Table name and table values
    private static final String TABLE_NAME = "employees";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FNAME = "Fname";
    private static final String COLUMN_LNAME = "Lname";

    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_STARTDATE = "startDate";
    private static final String COLUMN_ENDDATE = "endDate";
    private static final String COLUMN_TRAINED = "trained";


    public EmployeeDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
// create the employee table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FNAME + " TEXT,"
                + COLUMN_LNAME + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_STARTDATE + " TEXT,"
                + COLUMN_ENDDATE + " TEXT,"
                + COLUMN_TRAINED + " TEXT)";
        db.execSQL(createTableSql);
    }
// not sure what this does
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String  addEmployee(String FName, String LName, String Phone, String Email, String Train, String StartDate){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the values to add
        cv.put(COLUMN_FNAME, FName);
        cv.put(COLUMN_LNAME, LName);
        cv.put(COLUMN_PHONE, Phone);
        cv.put(COLUMN_EMAIL, Email);
        cv.put(COLUMN_STARTDATE, StartDate);
        cv.put(COLUMN_ENDDATE, "N");
        cv.put(COLUMN_TRAINED, Train);
        // Adds the database
        String id = String.valueOf(db.insert(TABLE_NAME, null, cv));
        db.close();
        return id;
    }

    public void editEmployee(String ID, String FName, String LName, String Phone, String Email, String Train){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the new value to edit
        cv.put(COLUMN_FNAME, FName);
        cv.put(COLUMN_LNAME, LName);
        cv.put(COLUMN_PHONE, Phone);
        cv.put(COLUMN_EMAIL, Email);
        cv.put(COLUMN_TRAINED, Train);
        //Edits employee
        long result = db.update(TABLE_NAME, cv, "id=?", new String[] {ID});
        if(result == -1){
            //Edit failed
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            //Edit Success
            Toast.makeText(context, "Employee updated", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public String[] ReturnStartDate(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String endDate = "0";
        String[] projection = {
                EmployeeDatabaseHelper.COLUMN_STARTDATE,
        };
        String selection = EmployeeDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {ID};
        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            endDate = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_STARTDATE));
        }
        db.close();
        return endDate.split("/");
    }

    //new things
    public String[] getInfoByID(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EmployeeDatabaseHelper.COLUMN_ID,
                EmployeeDatabaseHelper.COLUMN_FNAME,
                EmployeeDatabaseHelper.COLUMN_LNAME,
                EmployeeDatabaseHelper.COLUMN_EMAIL,
                EmployeeDatabaseHelper.COLUMN_PHONE,
                EmployeeDatabaseHelper.COLUMN_STARTDATE,
                EmployeeDatabaseHelper.COLUMN_ENDDATE,
                EmployeeDatabaseHelper.COLUMN_TRAINED,
        };

        String selection = EmployeeDatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        String[] IdInfo = new String[12];
        if (cursor.moveToNext()) {
            IdInfo[0] = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_ID)));
            IdInfo[1] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_FNAME));
            IdInfo[2] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_LNAME));
            IdInfo[3] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_EMAIL));
            IdInfo[4] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_PHONE));
            IdInfo[5] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_STARTDATE));
            IdInfo[6] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_ENDDATE));
            IdInfo[7] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_TRAINED));
        }
        cursor.close();
        Log.d("TAG", "This did run"+IdInfo[7]);
        db.close();
        return IdInfo;
    }

    //check for duplicate
    public String DupChecker(String FName, String LName, String Phone, String Email){
        // set default response zero
        String ID = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        // project ID if it exists
        String[] projection = {
                EmployeeDatabaseHelper.COLUMN_ID,
        };
        // select for all values
        String selection = EmployeeDatabaseHelper.COLUMN_FNAME + " = ? AND "
                + EmployeeDatabaseHelper.COLUMN_LNAME + " = ? AND "
                + EmployeeDatabaseHelper.COLUMN_EMAIL + " = ? AND " +
                EmployeeDatabaseHelper.COLUMN_PHONE + " = ?";
        String[] selectionArgs = {String.valueOf(FName), String.valueOf(LName), String.valueOf(Email), String.valueOf(Phone)};
        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            ID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_ID));
        }
        cursor.close();
        db.close();
        return ID;
    }

    // return all employeeIDs
    public List<String> getAllEID(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                EmployeeDatabaseHelper.COLUMN_ID,
        };
        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
//put values into array
        List<String> EIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_ID));
            EIDS.add(EID);
        }
        cursor.close();
        db.close();
        return EIDS;
    }

    public List<String> returnCertainEID(String Train, String Fname, String Lname){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                EmployeeDatabaseHelper.COLUMN_ID,
        };
        String selection = EmployeeDatabaseHelper.COLUMN_FNAME + " LIKE ? || '%' AND " + EmployeeDatabaseHelper.COLUMN_LNAME + " LIKE ? || '%' AND " + EmployeeDatabaseHelper.COLUMN_TRAINED + " LIKE ? || '%'";
        String[] selectionArgs = {Fname, Lname, Train};
        Cursor cursor = db.query(
                EmployeeDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
//put values into array
        List<String> EIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDatabaseHelper.COLUMN_ID));
            EIDS.add(EID);
        }
        cursor.close();
        db.close();
        return EIDS;
    }

}
