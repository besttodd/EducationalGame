package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {
    public static int SETTINGS_REQUEST = 222;
    private SQLiteDatabase db;
    private Cursor cursor;

    ScoresAdapter scoresAdapter;

    ArrayList<String> idList = new ArrayList<String>();
    ArrayList<String> dateList = new ArrayList<String>();
    ArrayList<String> difficultyList = new ArrayList<String>();
    ArrayList<Integer> scoreList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBHelper dbhelper = new DBHelper(this);

        ListView listScores = findViewById(R.id.highScoresList);
        try {
            db = dbhelper.getReadableDatabase();
            cursor = db.query("HIGHSCORES", new String[] {"_id", "DATE", "DIFFICULTY", "SCORE"},
                    null, null, null, null, "SCORE"+ " DESC");

            if (cursor.moveToFirst()) {
                do { idList.add(cursor.getString(cursor.getColumnIndex("_id")));
                dateList.add(cursor.getString(cursor.getColumnIndex("DATE")));
                difficultyList.add(cursor.getString(cursor.getColumnIndex("DIFFICULTY")));
                scoreList.add(cursor.getInt(cursor.getColumnIndex("SCORE")));
                } while (cursor.moveToNext());
            }

            scoresAdapter = new ScoresAdapter(ScoresActivity.this, idList, dateList, difficultyList, scoreList);

            listScores.setAdapter(scoresAdapter);
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public void backClicked(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
