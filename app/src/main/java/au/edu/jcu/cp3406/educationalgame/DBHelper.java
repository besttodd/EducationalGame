package au.edu.jcu.cp3406.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "high_scores";
    private static final int VERSION = 1;

    DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        Log.i("DBHelper", "constructor called");
    }

    public void onCreate(SQLiteDatabase db) {
        Log.i("DBHelper", "onCreate called");
        updateDatabase(db, 0, VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String text = String.format(Locale.getDefault(), "onUpgrade: old %d new %d", oldVersion, newVersion);
        Log.i("DBHelper", text);

        updateDatabase(db, oldVersion, newVersion);
    }

    public void insertScore(SQLiteDatabase db, String date, String difficulty, int score) {
        ContentValues scoreDetails = new ContentValues();
        scoreDetails.put("DATE", date);
        scoreDetails.put("DIFFICULTY", difficulty);
        scoreDetails.put("SCORE", score);
        db.insert("HIGHSCORES", null, scoreDetails);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE HIGHSCORES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "DATE TEXT," +
                    "DIFFICULTY TEXT," +
                    "SCORE INTEGER);");
            insertScore(db, "3rd March", "EASY", 150);
        }
        if (oldVersion < 2) {
            //db.execSQL("ALTER TABLE DRINK ADD COLUMN FAVORITE NUMERIC;");
        }
    }
}
