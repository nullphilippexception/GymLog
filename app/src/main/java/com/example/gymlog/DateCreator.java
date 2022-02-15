package com.example.gymlog;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

/**
 * Helper class that creates a date according to specification in format dd.mm.yy
 * @author "Philipp S."
 */
public class DateCreator {

    /**
     * Method that returns today's date as determined by LocalDate in dd.mm.yy format
     * @return today's date in dd.mm.yy format
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String createDate() {
        String result = "";
        String currentDay = LocalDate.now().toString();

        // adjust date to make it user and European-friendly
        // standard LocalDate formatting is yyyy-mm-dd
        result += currentDay.substring(8,10) + "."; // adds day of date followed by point
        result += currentDay.substring(5,7) + "."; // adds month of date followed by point
        result += currentDay.substring(2,4); // adds last to digits of year

        return result;
    }
}
