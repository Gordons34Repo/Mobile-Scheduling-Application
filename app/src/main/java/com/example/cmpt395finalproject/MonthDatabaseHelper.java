package com.example.cmpt395finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MonthDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "scheduleMonth_db";
    private static final int DATABASE_VERSION = 1;
    // Table name and table values
    private static final String TABLE_NAME = "monthSchedule";
    private static final String COLUMN_MID = "MID";
    private static final String COLUMN_YEAR = "Year";
    private static final String COLUMN_MONTH = "Month";


    public MonthDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_MID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_YEAR + " TEXT,"
                + COLUMN_MONTH + " TEXT)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String addMonth(String Month, String Year){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the values to add
        cv.put(COLUMN_MONTH, Month);
        cv.put(COLUMN_YEAR, Year);
        // Adds the database
        String id = String.valueOf(db.insert(TABLE_NAME, null, cv));
        db.close();
        return id;
    }


    void DeleteMonth(String MID){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Deletes database
        db.close();
        db.delete(TABLE_NAME, "_MID=?", new String[]{MID});
    }



    public String findMonth(String Year, String Month) {
        String MID = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MonthDatabaseHelper.COLUMN_MID,
        };
        String selection = MonthDatabaseHelper.COLUMN_YEAR + " = ? AND "
                + MonthDatabaseHelper.COLUMN_MONTH + " = ?";
        String[] selectionArgs = {String.valueOf(Year), String.valueOf(Month)};
        Cursor cursor = db.query(
                MonthDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            MID = cursor.getString(cursor.getColumnIndexOrThrow(MonthDatabaseHelper.COLUMN_MID));
        }
        db.close();
        cursor.close();
        return MID;
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
    // passes all MID
    public List<String> getAllMID(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MonthDatabaseHelper.COLUMN_MID,
        };
        Cursor cursor = db.query(
                MonthDatabaseHelper.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
//put values into array
        List<String> MIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(MonthDatabaseHelper.COLUMN_MID));
            MIDS.add(EID);
        }
        cursor.close();
        db.close();
        return MIDS;
    }

    // return the year and month of the MID
    public String[] getYearMonth(String MID){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MonthDatabaseHelper.COLUMN_YEAR,
                MonthDatabaseHelper.COLUMN_MONTH,
        };
        String selection = MonthDatabaseHelper.COLUMN_MID + " = ?";
        String[] selectionArgs = {String.valueOf(MID)};
        Cursor cursor = db.query(
                MonthDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        String[] YearMonth = new String[2];
        if (cursor.moveToFirst()) {
            YearMonth[0] = cursor.getString(cursor.getColumnIndexOrThrow(MonthDatabaseHelper.COLUMN_YEAR));
            YearMonth[1] = cursor.getString(cursor.getColumnIndexOrThrow(MonthDatabaseHelper.COLUMN_MONTH));
        }
        cursor.close();
        db.close();
        return YearMonth;
    }

    // return the mid of all year and month past a certain month and year
    public List<String> getMID(String Year, String Month){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                MonthDatabaseHelper.COLUMN_MID,
        };
        String selection = MonthDatabaseHelper.COLUMN_MID + " NOT IN (" +
                "SELECT " + MonthDatabaseHelper.COLUMN_MID + " " +
                "FROM " + MonthDatabaseHelper.TABLE_NAME + " " +
                "WHERE " + MonthDatabaseHelper.COLUMN_YEAR + " = ? " +
                "AND " + MonthDatabaseHelper.COLUMN_MONTH + " < ?) AND " +
                MonthDatabaseHelper.COLUMN_YEAR + " >= ?";
        String[] selectionArgs = {String.valueOf(Year), String.valueOf(Month), String.valueOf(Year)};
        Cursor cursor = db.query(
                MonthDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<String> MIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(MonthDatabaseHelper.COLUMN_MID));
            MIDS.add(EID);
        }
        cursor.close();
        db.close();
        return MIDS;
    }
}
