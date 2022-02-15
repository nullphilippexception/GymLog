package com.example.gymlog;

import android.content.Context;
import android.widget.Toast;

/**
 * This is a helper class that converts a string (the strings entered by the user) to an int
 * @author "Philipp S."
 */
public class StringIntConverter {

    /**
     * This method converts a string (user input fetched from edittext elements) and converts it to an int
     * @param context the context in which the error message toast is to be displayed
     * @param stringNumber the string that is supposed to be converted to a number
     * @return -1 if conversion was not succesful, otherwise the int equivalent of stringNumber
     * (-1 as default return was chosen because neither sets nor reps or weight can be negative)
     */
    public static int stringToInt(Context context, String stringNumber) {
        try {
            int result = Integer.parseInt(stringNumber);
            return result;
        }
        catch(NumberFormatException e) {
            Toast.makeText(context, "Can't convert " + stringNumber + " to an int, try again", Toast.LENGTH_LONG).show();
            return -1; // default return for bad item -> makes sense because there can't be negative sets, reps or weights
        }
    }
}
