package au.edu.jcu.cp3406.educationalgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "HIGHSCORES";
    private static final int VERSION = 3;

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

    void insertScore(SQLiteDatabase db, String date, int difficulty, int score, String game) {
        ContentValues scoreDetails = new ContentValues();
        scoreDetails.put("DATE", date);
        scoreDetails.put("DIFFICULTY", difficulty);
        scoreDetails.put("SCORE", score);
        scoreDetails.put("GAME", game);
        db.insert("HIGHSCORES", null, scoreDetails);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE HIGHSCORES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "DATE TEXT," +
                    "DIFFICULTY TEXT," +
                    "SCORE INTEGER);");
            //insertScore(db, "03-05-20", "EASY", 50);
        }
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE HIGHSCORES");
            db.execSQL("CREATE TABLE HIGHSCORES (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "DATE TEXT," +
                    "DIFFICULTY INTEGER," +
                    "SCORE INTEGER);");
            //insertScore(db, "03-05-20", 1, 50);
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE HIGHSCORES ADD COLUMN GAME TEXT");
            //insertScore(db, "03-05-20", 1, 50, "MEMORY");
            //insertScore(db, "03-05-20", 1, 50, "MATHS");
        }
    }
}
