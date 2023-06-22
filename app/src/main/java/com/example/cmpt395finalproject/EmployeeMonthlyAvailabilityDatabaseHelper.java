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

public class EmployeeMonthlyAvailabilityDatabaseHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String DATABASE_NAME = "employeeMonthSchedule_db";
    private static final int DATABASE_VERSION = 1;
    // Table name and table values
    private static final String TABLE_NAME = "employeeMonthSchedule";
    private static final String COLUMN_AID = "AID";
    private static final String COLUMN_EID = "EID";
    private static final String COLUMN_MONTH = "Month";
    private static final String COLUMN_YEAR = "Year";
    private static final String COLUMN_MON = "Monday";
    private static final String COLUMN_TUES = "Tuesday";
    private static final String COLUMN_WED = "Wednesday";
    private static final String COLUMN_THUR = "Thursday";

    private static final String COLUMN_FRI = "Friday";
    private static final String COLUMN_SAT = "Saturday";
    private static final String COLUMN_SUN = "Sunday";
    private static final String COLUMN_MODIFY = "Modify";

    public EmployeeMonthlyAvailabilityDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_AID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EID + " INTEGER,"
                + COLUMN_MONTH + " INTEGER,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_MON + " TEXT,"
                + COLUMN_TUES + " TEXT,"
                + COLUMN_WED + " TEXT,"
                + COLUMN_THUR + " TEXT,"
                + COLUMN_FRI + " TEXT,"
                + COLUMN_SAT + " TEXT,"
                + COLUMN_SUN + " TEXT,"
                + COLUMN_MODIFY + " TEXT)";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String addEmployeeMonth(Integer EID, Integer Month, Integer Year, String Monday, String Tuesday, String Wednesday, String Thursday, String Friday, String Saturday, String Sunday) {
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the values to add
        cv.put(COLUMN_EID, EID);
        cv.put(COLUMN_MONTH, Month);
        cv.put(COLUMN_YEAR, Year);
        cv.put(COLUMN_MON, Monday);
        cv.put(COLUMN_TUES, Tuesday);
        cv.put(COLUMN_WED, Wednesday);
        cv.put(COLUMN_THUR, Thursday);
        cv.put(COLUMN_FRI, Friday);
        cv.put(COLUMN_SAT, Saturday);
        cv.put(COLUMN_SUN, Sunday);
        cv.put(COLUMN_MODIFY, "0");
        // Adds the database
        String id = String.valueOf(db.insert(TABLE_NAME, null, cv));
        db.close();
        return id;
    }

    public void editEmployeeMonth(String AID, Integer Month, Integer Year, String Monday, String Tuesday, String Wednesday, String Thursday, String Friday, String Saturday, String Sunday) {
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the new value to edit
        cv.put(COLUMN_MONTH, Month);
        cv.put(COLUMN_YEAR, Year);
        cv.put(COLUMN_MON, Monday);
        cv.put(COLUMN_TUES, Tuesday);
        cv.put(COLUMN_WED, Wednesday);
        cv.put(COLUMN_THUR, Thursday);
        cv.put(COLUMN_FRI, Friday);
        cv.put(COLUMN_SAT, Saturday);
        cv.put(COLUMN_SUN, Sunday);
        //Edits employee schedule
        long result = db.update(TABLE_NAME, cv, "AID=?", new String[]{AID});
        db.close();
    }


    void DeleteEmployeeMonth(String ID) {
        // set up to add to database
        SQLiteDatabase db = this.getWritableDatabase();
        // Deletes database
        db.delete(TABLE_NAME, "_id=?", new String[]{ID});
        db.close();

    }

    public String findAID(String EID, String Year, String Month) {
        String AID = "0";
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID,
        };
        String selection = EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID + " = ? AND "
                + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        String[] selectionArgs = {String.valueOf(EID), String.valueOf(Year), String.valueOf(Month)};
        Cursor cursor = db.query(
                EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            AID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID));
        }
        cursor.close();
        db.close();
        return AID;
    };
    // returns list of employee ids without schedule for a month
    public List<String>  returnEIDS(String year, String month){
        //open database
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID,
        };

        // sort for row not in the year and month
        String selection = EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID + " NOT IN (" +
                "SELECT " + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID + " " +
                "FROM " + EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME + " " +
                "WHERE " + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? " +
                "AND " + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?" +
                "AND " + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MODIFY + " != ?)";
        String[] selectionArgs = {String.valueOf(year), String.valueOf(month), "0"};
        Cursor cursor = db.query(
                EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        // put values in to array
        List<String> EIDS = new ArrayList<>();
        while (cursor.moveToNext()) {
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID));
            EIDS.add(EID);
        }
        cursor.close();
        db.close();
        return EIDS;
    }

    public String[] getInfoByID(String AID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MON,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_TUES,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_WED,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_THUR,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_FRI,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SAT,
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SUN,
        };

        String selection = EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID + " = ?";
        String[] selectionArgs = {String.valueOf(AID)};

        Cursor cursor = db.query(
                EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        String[] IdInfo = new String[12];
        if (cursor.moveToNext()) {
            IdInfo[0] = String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID)));
            IdInfo[1] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MON));
            IdInfo[2] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_TUES));
            IdInfo[3] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_WED));
            IdInfo[4] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_THUR));
            IdInfo[5] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_FRI));
            IdInfo[6] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SAT));
            IdInfo[7] = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SUN));
        }
        cursor.close();
        Log.d("TAG", "This did run" + IdInfo[7]);
        db.close();
        return IdInfo;
    }

    public List<String> avaliableEmployees(String Year, String Month, String dayofweek, String shift1, String shift2) {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("TAG", "DAY OF WEEK IS: "+dayofweek);
        // collect employee id
        String[] projection = {
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID
        };
        // pick day
        String selection;
        if (dayofweek.equals("2")) {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MON + " = ? OR "
                     + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MON + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        } else if (dayofweek.equals("3")) {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_TUES + " = ? OR "
                     + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_TUES + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        } else if (dayofweek.equals("4")) {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_WED + " = ? OR "
                        + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_WED + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        } else if (dayofweek.equals("5")) {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_THUR + " = ? OR "
                     + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_THUR + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        } else if (dayofweek.equals("6")) {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_FRI + " = ? OR "
                     + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_FRI + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        } else if (dayofweek.equals("7")) {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SAT + " = ? OR "
                     + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SAT + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        }else {
             selection = "(" + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SUN + " = ? OR "
                     + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_SUN + " = ? ) AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ?";
        }

// select command
        String[] selectionArgs = {String.valueOf(shift1), String.valueOf(shift2), String.valueOf(Year), String.valueOf(Month)};

        Cursor cursor = db.query(
                EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME,
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
            String EID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID));
            EIDS.add(EID);
        }
        cursor.close();
        db.close();
        return EIDS;
    }


    public String[] DefaultTime(String id, int Year, int Month, int Syear, int Smonth) {
        // start sql
        SQLiteDatabase db = this.getReadableDatabase();
        if (Month == 1) {
            Month = 12;
            Year = Year - 1;
        }else {
            Month = Month - 1;
        }
        if (Syear > Year) {
            Year = Syear;
            Month = Smonth;
        }else if (Syear == Year && Smonth >= Month) {
                Year = Syear;
                Month = Smonth;
            }
        // find last month
        String AID = "0";
        while(AID.equals("0")) {
            String[] projection = {
                    EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID,
            };
            String selection = EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_EID + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_YEAR + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MONTH + " = ? AND "
                    + EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MODIFY + " = ?";
            String[] selectionArgs = {id, String.valueOf(Year), String.valueOf(Month), "1"};
            Cursor cursor = db.query(
                    EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                AID = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID));
            }
            cursor.close();
            if (Month == 1) {
                Month = 12;
                Year -= 1;
            }else {
                Month -= 1;
            }
        }
        db.close();
        //return last ID
        return getInfoByID(AID);
    }

    // mark table as modified
    public void markAsModified(String id, String year, String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        // Store the new value to edit
        cv.put(COLUMN_MODIFY, "1");
        //Edits employee schedule base on ID and year and month

        long result = db.update(TABLE_NAME, cv, "EID=? AND Month=? AND Year=?", new String[]{id, month, year});
        db.close();
    }

    // find if the table has been modified
    public boolean checkModify(String AID){
        boolean result = false;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MODIFY
        };
        String selection = EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_AID + " = ?";
        String[] selectionArgs = {AID};
        Cursor cursor = db.query(
                EmployeeMonthlyAvailabilityDatabaseHelper.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToFirst()) {
            String modify = cursor.getString(cursor.getColumnIndexOrThrow(EmployeeMonthlyAvailabilityDatabaseHelper.COLUMN_MODIFY));
            if (modify.equals("1")) {
                return true;
            }
        }
        db.close();
        Log.d("TAG", "checkModify: "+result);
        return result;
    }
}


