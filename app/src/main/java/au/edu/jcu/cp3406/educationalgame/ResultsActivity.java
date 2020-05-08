package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView scoreDisplay = findViewById(R.id.finalScore);
        TextView highScore = findViewById(R.id.highScoreNotification);
        ImageView tweetImage = findViewById(R.id.tweetImage);
        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        int newScore = Objects.requireNonNull(getIntent().getExtras()).getInt("score");
        scoreDisplay.setText(Integer.toString(newScore));

        DBHelper dbhelper = new DBHelper(this);
        db = dbhelper.getWritableDatabase();
        cursor = db.query("HIGHSCORES", new String[] {"_id", "SCORE"},
                null, null, null, null, null);

        //FIX HERE - Always writes to DB-------------------------------------------------
        //while (cursor.moveToNext()) {
        for (int i = 0; i < 10; i++) {
            cursor.moveToNext();
            int existingScore = cursor.getInt(1);
            if (newScore > existingScore) {
                dbhelper.insertScore(db, getDate(), level.toString(), newScore);
                highScore.setText("Well Done\nYou got a HIGH SCORE!");
                tweetImage.setVisibility(View.VISIBLE);
                Log.i("Results", "New high score added!");
                break;
            } /*else if (cursor.getCount() < 10) {
                dbhelper.insertScore(db, getDate(), level.toString(), newScore);
                highScore.setText("Well Done\nYou got a HIGH SCORE!");
                tweetImage.setVisibility(View.VISIBLE);
                Log.i("Results", "New high score added!");
                break;
            }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_twitter:
                Intent intent = new Intent(this, Twitter_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
