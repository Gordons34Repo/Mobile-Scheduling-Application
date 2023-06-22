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

public class ShiftDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "scheduleShift_db";
    private static final int DATABASE_VERSION = 1;
    // Table name and table values
    private static final String TABLE_NAME = "ShiftSchedule";
    private static final String COLUMN_SID = "SID";
    private static final String COLUMN_DID = "DID";
    private static final String COLUMN_EID = "EID";
    private static final String COLUMN_SHIFTSLOT = "ShiftSlot";
    private static final String COLUMN_TRAINING = "Training";


    public ShiftDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_SID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DID + " INTEGER,"
                + COLUMN_EID + " INTEGER,"
                + COLUMN_SHIFTSLOT + " TEXT,"
                + COLUMN_TRAINING + " TEXT)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String addShift(String DID, String EID, String Slot, String Training){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the values to add
        cv.put(COLUMN_DID, DID);
        cv.put(COLUMN_EID, EID);
        cv.put(COLUMN_SHIFTSLOT, Slot);
        cv.put(COLUMN_TRAINING, Training);
        // Adds the database
        String id = String.valueOf(db.insert(TABLE_NAME, null, cv));
        db.close();
        return id;
    }

    public String WorkingCheck(String DID, String EID, String Slot){
        String SID = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                ShiftDatabaseHelper.COLUMN_SID,
        };
        String selection = ShiftDatabaseHelper.COLUMN_DID + " = ? AND "
                + ShiftDatabaseHelper.COLUMN_EID + " = ? AND "
                + ShiftDatabaseHelper.COLUMN_SHIFTSLOT + " = ?";
        String[] selectionArgs = {String.valueOf(DID), String.valueOf(EID), String.valueOf(Slot)};
        Cursor cursor = db.query(
                ShiftDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            SID = cursor.getString(cursor.getColumnIndexOrThrow(ShiftDatabaseHelper.COLUMN_SID));
        }
        cursor.close();
        db.close();
        return SID;
    }


    void DeleteShift(String DID, String EID){
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Deletes database
        String whereClause = COLUMN_DID + "=? AND " + COLUMN_EID + "=?";
        String[] whereArgs = {DID, EID};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public String findShift(String DID, String Slot, String EID) {
        String SID = "0";
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ShiftDatabaseHelper.COLUMN_SID
        };
        String selection = ShiftDatabaseHelper.COLUMN_DID + " = ? AND "
                + ShiftDatabaseHelper.COLUMN_SHIFTSLOT + " = ? AND "
                + ShiftDatabaseHelper.COLUMN_EID + " = ?";
        String[] selectionArgs = { String.valueOf(DID), String.valueOf(Slot), String.valueOf(EID)};
        Cursor cursor = db.query(
                ShiftDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            EID = cursor.getString(cursor.getColumnIndexOrThrow(ShiftDatabaseHelper.COLUMN_SID));
        }
        cursor.close();
        db.close();
        return SID;
    }

    public List<String> returnShifts(String DID, String Slot) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ShiftDatabaseHelper.COLUMN_EID
        };
        String selection = ShiftDatabaseHelper.COLUMN_DID + " = ? AND "
                + ShiftDatabaseHelper.COLUMN_SHIFTSLOT + " = ?";
        String[] selectionArgs = { String.valueOf(DID), String.valueOf(Slot)};
        // Query the database
        Cursor cursor = db.query(
                ShiftDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<String> EIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(ShiftDatabaseHelper.COLUMN_EID));
            EIDS.add(EID);
        }
        cursor.close();
        db.close();
        return EIDS;
    }

    // delete shift from employee taking the DID, EID and Slot
    public void deleteShift(String DID, String EID, String Slot){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_DID + "=? AND " + COLUMN_EID + "=? AND " + COLUMN_SHIFTSLOT + "=?";
        String[] whereArgs = {DID, EID, Slot};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    // edits shift training
    public void editShiftTraining(String DID, String EID, String Training){
        Log.d("Training is updating", "editShiftTraining: "+DID);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TRAINING, Training);
        String whereClause = COLUMN_DID + "=? AND " + COLUMN_EID + "=?";
        String[] whereArgs = {DID, EID};
        db.update(TABLE_NAME, cv, whereClause, whereArgs);
        db.close();
    }

    // return the training
    public List<String> returnTraining(String DID, String Slot) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                ShiftDatabaseHelper.COLUMN_TRAINING
        };
        String selection = ShiftDatabaseHelper.COLUMN_DID + " = ? AND "
                + ShiftDatabaseHelper.COLUMN_SHIFTSLOT + " = ?";
        String[] selectionArgs = { String.valueOf(DID), String.valueOf(Slot)};
        // Query the database
        Cursor cursor = db.query(
                ShiftDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        List<String> EIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(ShiftDatabaseHelper.COLUMN_TRAINING));
            EIDS.add(EID);
        }
        cursor.close();
        db.close();
        return EIDS;
    }

}
