package com.example.gymlog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.gymlog.SQLiteDBCreator.COL_DATE;
import static com.example.gymlog.SQLiteDBCreator.COL_NAME;
import static com.example.gymlog.SQLiteDBCreator.COL_REPS;
import static com.example.gymlog.SQLiteDBCreator.COL_SETS;
import static com.example.gymlog.SQLiteDBCreator.COL_WEIGHT;
import static com.example.gymlog.SQLiteDBCreator.TABLE_NAME;

/**
 * DBSearcher is a helper class which sole purpose it is to search a database for a specific date and return all rows
 * of the database whose date match the search date in form of cursor
 * @author "Philipp S."
 */
public class DBSearcher {

    /**
     * This is the method that fulfills the purpose of DBSearcher, is searches a database for a searchDate and returns
     * all entries whose date matches the searchDate
     * @param database db that is supposed to be searched for searchDate
     * @param searchDate date the database is supposed to be searched for, format "dd.mm.yy"
     * @return Cursor that contains all rows of the db matching search criterion
     */
    public static Cursor searchDB(SQLiteDatabase database, String searchDate) {
        String[] projection = {COL_NAME, COL_SETS, COL_REPS, COL_WEIGHT};
        String selection = COL_DATE + " like ? ";
        String[] selectionArgs = {"%" + searchDate + "%"};

        Cursor cursor = database.query(TABLE_NAME, projection, selection, selectionArgs,
                null, null, null);

        Logger.getAnonymousLogger().log(Level.INFO, "For date " + searchDate + " cursor contains " + cursor.getCount() + " rows");

        return cursor;
    }
}
