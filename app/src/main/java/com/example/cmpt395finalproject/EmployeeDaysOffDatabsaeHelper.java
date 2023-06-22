package com.example.cmpt395finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDaysOffDatabsaeHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "employeeDaysOff_db";
    private static final int DATABASE_VERSION = 1;
    // Table name and table values
    private static final String TABLE_NAME = "employeeDayOff";
    private static final String COLUMN_DID = "DID";
    private static final String COLUMN_EID = "EID";
    private static final String COLUMN_YEAR = "Year";
    private static final String COLUMN_MONTH = "Month";
    private static final String COLUMN_DAY = "Day";

    public EmployeeDaysOffDatabsaeHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_DID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EID + " INTEGER,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_MONTH + " INTEGER,"
                + COLUMN_DAY + " INTEGER)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void addEmployeeDayOff(Integer EID,Integer Year, Integer Month, Integer Day){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the values to add
        cv.put(COLUMN_EID, EID);
        cv.put(COLUMN_YEAR, Year);
        cv.put(COLUMN_MONTH, Month);
        cv.put(COLUMN_DAY, Day);
        // Adds the database
        db.insert(TABLE_NAME, null, cv);
        db.close();
        Log.d("THIS BLOODY HAPPEND", "addEmployeeDayOff: ");
    }
    public String findDID(String EID, Integer Year, Integer Month, Integer Day) {
        String DID = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                EmployeeDaysOffDatabsaeHelper.COLUMN_DID,
        };
        String selection = EmployeeDaysOffDatabsaeHelper.COLUMN_EID + " = ? AND "
                + EmployeeDaysOffDatabsaeHelper.COLUMN_YEAR + " = ? AND "
                + EmployeeDaysOffDatabsaeHelper.COLUMN_MONTH + " = ? AND "
                + EmployeeDaysOffDatabsaeHelper.COLUMN_DAY + " = ?";
        String[] selectionArgs = {EID, String.valueOf(Year), String.valueOf(Month), String.valueOf(Day)};
        Cursor cursor = db.query(
                EmployeeDaysOffDatabsaeHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            DID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDaysOffDatabsaeHelper.COLUMN_DID));
        }
        cursor.close();
        db.close();
        return DID;
    }

    // returns all employee days off
    public List<String> findEmployeeDayOff(String EID){
        List<String> employeeDayOff = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                EmployeeDaysOffDatabsaeHelper.COLUMN_YEAR,
                EmployeeDaysOffDatabsaeHelper.COLUMN_MONTH,
                EmployeeDaysOffDatabsaeHelper.COLUMN_DAY,
        };
        String selection = EmployeeDaysOffDatabsaeHelper.COLUMN_EID + " = ?";
        String[] selectionArgs = {EID};
        Cursor cursor = db.query(
                EmployeeDaysOffDatabsaeHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            do {
                String year = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDaysOffDatabsaeHelper.COLUMN_YEAR));
                String month = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDaysOffDatabsaeHelper.COLUMN_MONTH));
                String day = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeDaysOffDatabsaeHelper.COLUMN_DAY));
                employeeDayOff.add(month + "/" + day + "/" + year);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return employeeDayOff;
    }

    public void DeleteEmployeeMonth(String EID, Integer Year, Integer Month, Integer Day){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Deletes database
        String whereClause = COLUMN_EID + "=? AND " + COLUMN_YEAR + "=? AND " + COLUMN_MONTH + "=? AND " + COLUMN_DAY + "=?";
        String[] whereArgs = {EID, String.valueOf(Year), String.valueOf(Month), String.valueOf(Day)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

}
