package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class ScoresActivity extends BaseActivity implements StateListener {
    public static int SETTINGS_REQUEST = 222;
    Fragment settingsFragment;
    private SQLiteDatabase db;
    private Cursor cursor;

    ScoresAdapter scoresAdapter;

    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> difficultyList = new ArrayList<>();
    ArrayList<Integer> scoreList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        showHideFragment(settingsFragment);

        DBHelper dbhelper = new DBHelper(this);

        ListView listScores = findViewById(R.id.highScoresList);
        try {
            db = dbhelper.getReadableDatabase();
            cursor = db.query("HIGHSCORES", new String[]{"_id", "DATE", "DIFFICULTY", "SCORE"},
                    null, null, null, null, "SCORE" + " DESC, DIFFICULTY" + " DESC");

            if (cursor.moveToFirst()) {
                do {
                    idList.add(cursor.getString(cursor.getColumnIndex("_id")));
                    dateList.add(cursor.getString(cursor.getColumnIndex("DATE")));
                    difficultyList.add(convert(cursor.getInt(cursor.getColumnIndex("DIFFICULTY"))));
                    scoreList.add(cursor.getInt(cursor.getColumnIndex("SCORE")));
                } while (cursor.moveToNext());
            }

            scoresAdapter = new ScoresAdapter(ScoresActivity.this, idList, dateList, difficultyList, scoreList);

            listScores.setAdapter(scoresAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_settings:
                //setSettingOptions();
                showHideFragment(settingsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    public void backClicked(View view) {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    String convert(int level) {
        String newLevel = "";

        switch (level) {
            case 0:
                newLevel = "EASY";
                break;
            case 1:
                newLevel = "MEDIUM";
                break;
            case 2:
                newLevel = "HARD";
                break;
            case 3:
                newLevel = "MASTER";
                break;
        }
        return newLevel;
    }

    public void showHideFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (settingsFragment.isHidden()) {
            ft.show(fragment);
        } else {
            ft.hide(fragment);
        }

        ft.commit();
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        switch (state) {
            case SETTINGS:
                showHideFragment(settingsFragment);
                break;
        }
    }
}
