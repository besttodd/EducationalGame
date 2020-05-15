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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoresActivity extends BaseActivity implements StateListener {
    public static int SETTINGS_REQUEST = 222;
    private Fragment settingsFragment;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Game game;

    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> difficultyList = new ArrayList<>();
    ArrayList<Integer> scoreList = new ArrayList<>();
    ArrayList<String> gameList = new ArrayList<>();

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
        hideFragment(settingsFragment);

        //game = tabView.getText()  <----------- need to implement
        game = Game.MATHS;

        DBHelper dbhelper = new DBHelper(this);

        ListView listScores = findViewById(R.id.highScoresList);
        try {
            db = dbhelper.getReadableDatabase();
            cursor = db.query("HIGHSCORES", new String[]{"_id", "DATE", "DIFFICULTY", "SCORE", "GAME"},
                    null, null, null, null, "SCORE" + " DESC, DIFFICULTY" + " DESC");

            if (cursor.moveToFirst()) {
                do {
                    idList.add(cursor.getString(cursor.getColumnIndex("_id")));
                    dateList.add(cursor.getString(cursor.getColumnIndex("DATE")));
                    difficultyList.add(convert(cursor.getInt(cursor.getColumnIndex("DIFFICULTY"))));
                    scoreList.add(cursor.getInt(cursor.getColumnIndex("SCORE")));
                    gameList.add(cursor.getString(cursor.getColumnIndex("GAME")));
                } while (cursor.moveToNext());
            }

            ScoresAdapter scoresAdapter = new ScoresAdapter(ScoresActivity.this, idList, dateList, difficultyList, scoreList, gameList);
            listScores.setAdapter(scoresAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_settings:
                showFragment(settingsFragment);
                return true;
            case android.R.id.home:
                onBackPressed();
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

    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        switch (state) {
            case SETTINGS:
                hideFragment(settingsFragment);
                break;
            case SHAKE:
            case RESTART:
                Intent intent;
                if (game.equals(Game.MATHS)) { intent = new Intent(this, MathsGameActivity.class); }
                else { intent = new Intent(this, MemoryGameActivity.class); }
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
        }
    }
}
