package com.example.gymlog;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.gymlog.SQLiteDBCreator.COL_DATE;
import static com.example.gymlog.SQLiteDBCreator.COL_NAME;
import static com.example.gymlog.SQLiteDBCreator.COL_REPS;
import static com.example.gymlog.SQLiteDBCreator.COL_SETS;
import static com.example.gymlog.SQLiteDBCreator.COL_WEIGHT;
import static com.example.gymlog.SQLiteDBCreator.TABLE_NAME;

/**
 * MainActivity is the central point of the application. It contains the methods that are triggered
 * when a button on one of the screens is pressed and then either directly interacts with the database
 * or delegates the interaction to DBSearcher; this class is also responsible for creating new visual elements
 * on the result_screen.xml containing the search resulsts of a date search
 * @author "Philipp S."
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This method is called when MainActivity is created, it sets the displayed screen to landing_page.xml
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
    }

    /**
     * This method is called when the "Entry" Button on the landing_page.xml screen is pressed
     */
    public void goToEntryCreationScreen(View view) {
        setContentView(R.layout.enter_screen);
    }

    /**
     * This method is called when the "Search" Button on the landing_page.xml screen is pressed
     */
    public void goToShowEntriesScreen(View view) {
        setContentView(R.layout.search_screen);
    }

    /**
     * This method is called when the "Enter" Button on the enter_screen.xml screen is pressed.
     * The method pulls data entered by the user from for EditText elements on the screen, checks
     * whether these elements are of the expected type and if so, inserts them into a SQLite database.
     * If inserting the data was successful, the display returns to the landing_page.xml screen. Otherwise
     * an error message is displayed.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertIntoDB(View view) {
        // get data entered by user from edittext elements on screen
        String exerciseName = ((EditText) findViewById(R.id.exercise_edittext)).getText().toString();
        String sets = ((EditText) findViewById(R.id.sets_edittext)).getText().toString();
        String reps = ((EditText) findViewById(R.id.reps_edittext)).getText().toString();
        String weight = ((EditText) findViewById(R.id.weight_edittext)).getText().toString();

        // values contains values that will be inserted into database
        ContentValues values = createContentValues(exerciseName, sets, reps, weight);

        try {
            // size > 0 means that converting and inserting values was successful
            if(values.size() > 0) {
                SQLiteDatabase database = new SQLiteDBCreator(this).getWritableDatabase();
                database.insert(TABLE_NAME, null, values);
                Toast.makeText(this, "Entry was added successfully", Toast.LENGTH_LONG).show();
                setContentView(R.layout.landing_page);
            }
        }
        catch(Exception e) {
            Toast.makeText(this, "Database error, inserting entry was not successful", Toast.LENGTH_LONG).show();
            Logger.getAnonymousLogger().log(Level.WARNING, "SQLite Error: " + e);
        }
    }

    /**
     * This method creates a ContentValues datastructure from a given input. It also checks the validity of the input.
     * @param exerciseName is the name of the exercise as entered by the user,
     * @param sets number of sets of this exercise as entered by the user
     * @param reps number of reps per set of exercise as entered by the user
     * @param weight weight used for exercise as entered by the user
     * @return a ContentValues datastructure that is either empty (if an error occured) or contains the transformed values of exerciseName, sets, reps and weight
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ContentValues createContentValues(String exerciseName, String sets, String reps, String weight) {
        ContentValues result = new ContentValues();
        int setsNumber = StringIntConverter.stringToInt(this, sets);
        int repsNumber = StringIntConverter.stringToInt(this, reps);
        int weightNumber = StringIntConverter.stringToInt(this, weight);

        // if there were no errors for any of the converted numbers, proceed to insert all values into result
        // error would be indicated by one of the values being equal to -1
        if(setsNumber > -1 && repsNumber > -1 && weightNumber > -1) {
            String currentDay = DateCreator.createDate();
            result.put(COL_DATE, currentDay);
            result.put(COL_NAME, exerciseName);
            result.put(COL_SETS, setsNumber);
            result.put(COL_REPS, repsNumber);
            result.put(COL_WEIGHT, weightNumber);
            Logger.getAnonymousLogger().log(Level.INFO, "Successfully created content values for entry on date " + currentDay);
        }

        return result;
    }

    /**
     * This method is called when the "Search" button on the search_screen.xml screen is pressed
     * It fetches the date entered by the user and searches the database for this date, all entries
     * with a matching date are then returned on a new screen, result_screen.xml
     */
    public void searchDBForDate(View view) {
        String searchDate = ((EditText) findViewById(R.id.training_date_edittext)).getText().toString();
        SQLiteDatabase database = new SQLiteDBCreator(this).getReadableDatabase();
        Cursor cursor = DBSearcher.searchDB(database, searchDate);
        setContentView(R.layout.result_screen);
        try {
            // fetch layout on result_screen.xml that will contain the elements to be inserted
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.resultscreen_linearlayout);
            while(cursor.moveToNext()) {
                // fetch data from current cursor row, assign it to variables and then create TextView with these variables
                // then attach newly created textview to layout
                String exerciseName = cursor.getString(cursor.getColumnIndex(COL_NAME));
                String sets = cursor.getString(cursor.getColumnIndex(COL_SETS));
                String reps = cursor.getString(cursor.getColumnIndex(COL_REPS));
                String weight = cursor.getString(cursor.getColumnIndex(COL_WEIGHT));
                String txtViewString = exerciseName + ": " + sets + " / " + reps + " / " + weight;
                TextView tmpTextView = createTextView(txtViewString);
                linearLayout.addView(tmpTextView);
            }
            cursor.close();
            Button goToEntryButton = createFowardingButton();
            linearLayout.addView(goToEntryButton);
        }
        catch(Exception e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "An error occured: " + e.getMessage());
        }
    }

    /**
     * This is a helper method that takes a txtViewString input, applies different formatting tools to it and then returns a formatted textview
     * @param txtViewString the string that will be the text of the textview, consisting of exercise name, sets, reps and weight
     * @return is a formatted TextView that contains txtViewString as text
     */
    private TextView createTextView(String txtViewString) {
        TextView tmpTextView = new TextView(this);
        tmpTextView.setText(txtViewString);
        tmpTextView.setTextColor(Color.BLACK);
        tmpTextView.setTextSize(30);
        tmpTextView.setPadding(16, 0, 16, 20);
        return tmpTextView;
    }

    /**
     * This is a helper method that creates a formatted button which will enable the user to go to the enter_screen.xml screen
     * once the button is pressed
     */
    private Button createFowardingButton() {
        Button resultButton = new Button(this);
        resultButton.setBackgroundColor(Color.rgb(40, 180, 50));
        resultButton.setText("Go to data entry");
        resultButton.setTextColor(Color.WHITE);
        resultButton.setHeight(100);
        resultButton.setTextSize(30);
        resultButton.setPadding(16, 0, 16, 0);
        resultButton.setOnClickListener(v -> setContentView(R.layout.enter_screen));
        return resultButton;
    }
}
