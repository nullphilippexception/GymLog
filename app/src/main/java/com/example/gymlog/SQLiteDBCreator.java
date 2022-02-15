package com.example.gymlog;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a helper class to create the database
 * @author "Philipp S."
 */
public class SQLiteDBCreator extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "GymLog";
    public static final String TABLE_NAME = "Exercises";
    public static final String COL_DATE = "date"; // date of the training session
    public static final String COL_NAME = "name"; // name of the specific exercise
    public static final String COL_SETS = "sets"; // number of sets
    public static final String COL_REPS = "reps"; // number of reps
    public static final String COL_WEIGHT = "weight"; // weight used during exercise

    public SQLiteDBCreator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This method creates table TABLE_NAME in database sqLiteDatabase when an object of this class is instantiated
     * @param sqLiteDatabase database that is supposed to hold the table TABLE_NAME
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COL_DATE + " TEXT, " + COL_NAME + " TEXT, " + COL_SETS + " INTEGER, " +
                COL_REPS + " INTEGER, " + COL_WEIGHT + " INTEGER" + ")");
    }

    /**
     * Resets table TABLE_NAME.
     * @param sqLiteDatabase database where table TABLE_NAME is supposed to be recreated
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}

