package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView scoreDisplay = findViewById(R.id.finalScore);
        int newScore = Objects.requireNonNull(getIntent().getExtras()).getInt("score");
        scoreDisplay.setText(Integer.toString(newScore));

        SQLiteOpenHelper dbhelper = new DBHelper(this);
        db = dbhelper.getWritableDatabase();
        cursor = db.query("HIGHSCORES", new String[] {"_id", "SCORE"},
                null, null, null, null, null);
        /*initialize/open db
        highestsearch db for highest score
        if (newScore > highestScore) {
        db.insertScore()
        }*/
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
