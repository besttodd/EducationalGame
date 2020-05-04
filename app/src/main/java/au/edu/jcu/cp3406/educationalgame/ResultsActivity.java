package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ResultsActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;
    Difficulty level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView scoreDisplay = findViewById(R.id.finalScore);
        TextView highScore = findViewById(R.id.highScoreNotification);
        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        int newScore = Objects.requireNonNull(getIntent().getExtras()).getInt("score");
        scoreDisplay.setText(Integer.toString(newScore));

        DBHelper dbhelper = new DBHelper(this);
        db = dbhelper.getWritableDatabase();
        cursor = db.query("HIGHSCORES", new String[] {"_id", "SCORE"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            int existingScore = cursor.getInt(1);
            if (newScore > existingScore) {
                dbhelper.insertScore(db, getDate(), level.toString(), newScore);
                highScore.setText("Well Done\nYou got a HIGH SCORE!");
                Log.i("Results", "New high score added!");
                break;
            } else if (cursor.getCount() < 5) {
                dbhelper.insertScore(db, getDate(), level.toString(), newScore);
                highScore.setText("Well Done\nYou got a HIGH SCORE!");
                Log.i("Results", "New high score added!");
                break;
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    String getDate() {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yy");
        return df.format(d);
    }

    public void restart(View view) {
        Intent intent = new Intent(this, HigherLowerGameActivity.class);
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }

    public void mainMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
