package com.example.cmpt395finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DayDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "scheduleDay_db";
    private static final int DATABASE_VERSION = 1;
    // Table name and table values
    private static final String TABLE_NAME = "DaySchedule";
    private static final String COLUMN_DID = "DID";
    private static final String COLUMN_MID = "MID";
    private static final String COLUMN_DAY = "Day";
    private static final String COLUMN_FILLED = "Filled";


    public DayDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_DID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MID + " INTEGER,"
                + COLUMN_DAY + " INTEGER,"
                + COLUMN_FILLED + " INTEGER DEFAULT 0)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String addDay(Integer MID, Integer Day){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the values to add
        cv.put(COLUMN_MID, MID);
        cv.put(COLUMN_DAY, Day);
        // Adds the database
        String id = String.valueOf(db.insert(TABLE_NAME, null, cv));
        db.close();
        return id;
    }
// 0 empty/incomplete 1 filled
    public void fillDay(String DID, Integer filled){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the new value to edit
        cv.put(COLUMN_FILLED, filled);
        //Edits day
        long result = db.update(TABLE_NAME, cv, "DID=?", new String[] {DID});
        db.close();
    }
    public String findDay(String MID, String Day) {
        String DID = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                DayDatabaseHelper.COLUMN_DID,
        };
        String selection = DayDatabaseHelper.COLUMN_MID + " = ? AND "
                + DayDatabaseHelper.COLUMN_DAY + " = ?";
        String[] selectionArgs = {String.valueOf(MID), String.valueOf(Day)};
        Cursor cursor = db.query(
                DayDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            DID = cursor.getString(cursor.getColumnIndexOrThrow(DayDatabaseHelper.COLUMN_DID));
        }
        cursor.close();
        db.close();
        return DID;
    }
    void DeleteDay(String MID){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Deletes database
        db.delete(TABLE_NAME, "_mid=?", new String[]{MID});
        db.close();
    }

    public List<String> DaysFilled(String MID) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DayDatabaseHelper.COLUMN_DAY
        };
        String selection = DayDatabaseHelper.COLUMN_MID + " = ? AND "
                + DayDatabaseHelper.COLUMN_FILLED + " = ?";
        String[] selectionArgs = { String.valueOf(MID), String.valueOf("2") };
        String sortOrder = DayDatabaseHelper.COLUMN_DAY + " ASC";
        Cursor cursor = db.query(
                DayDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        List<String> days = new ArrayList<>();
        while (cursor.moveToNext()) {
            String day = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DayDatabaseHelper.COLUMN_DAY)));
            days.add(day);
        }
        cursor.close();
        db.close();
        return days;
    }

    public List<String> DaysFilledIncomplete(String MID) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DayDatabaseHelper.COLUMN_DAY
        };
        String selection = DayDatabaseHelper.COLUMN_MID + " = ? AND "
                + DayDatabaseHelper.COLUMN_FILLED + " = ?";
        String[] selectionArgs = { String.valueOf(MID), String.valueOf("1") };
        String sortOrder = DayDatabaseHelper.COLUMN_DAY + " ASC";
        Cursor cursor = db.query(
                DayDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        List<String> days = new ArrayList<>();
        while (cursor.moveToNext()) {
            String day = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DayDatabaseHelper.COLUMN_DAY)));
            days.add(day);
        }
        cursor.close();
        db.close();
        return days;
    }

    // return date
    public String getDay(String DID) {
        String day = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                DayDatabaseHelper.COLUMN_DAY,
        };
        String selection = DayDatabaseHelper.COLUMN_DID + " = ?";
        String[] selectionArgs = {String.valueOf(DID)};
        Cursor cursor = db.query(
                DayDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            day = cursor.getString(cursor.getColumnIndexOrThrow(DayDatabaseHelper.COLUMN_DAY));
        }
        cursor.close();
        db.close();
        return day;
    }

    // return filledDays incomplete id
    public List<String> DaysFilledID(String MID) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DayDatabaseHelper.COLUMN_DID
        };
        String selection = DayDatabaseHelper.COLUMN_MID + " = ? AND "
                + DayDatabaseHelper.COLUMN_FILLED + " = ?";
        String[] selectionArgs = { String.valueOf(MID), String.valueOf("2") };
        Cursor cursor = db.query(
                DayDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<String> days = new ArrayList<>();
        while (cursor.moveToNext()) {
            String day = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DayDatabaseHelper.COLUMN_DID)));
            days.add(day);
        }
        cursor.close();
        db.close();
        return days;
    }

    public List<String> DaysFilledIncompleteID(String MID) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DayDatabaseHelper.COLUMN_DID
        };
        String selection = DayDatabaseHelper.COLUMN_MID + " = ? AND "
                + DayDatabaseHelper.COLUMN_FILLED + " = ?";
        String[] selectionArgs = { String.valueOf(MID), String.valueOf("1") };
        Cursor cursor = db.query(
                DayDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<String> days = new ArrayList<>();
        while (cursor.moveToNext()) {
            String day = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DayDatabaseHelper.COLUMN_DID)));
            days.add(day);
        }
        cursor.close();
        db.close();
        return days;
    }
}
