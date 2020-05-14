package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ResultsActivity extends BaseActivity implements StateListener {
    private static final int MAX_SCORES = 10;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Difficulty level;
    private Game game;
    private Fragment settingsFragment;
    private TextView highScore;
    private ImageView tweetImage;
    private String highScoreTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        hideFragment(settingsFragment);

        TextView scoreDisplay = findViewById(R.id.finalScore);
        highScore = findViewById(R.id.highScoreNotification);
        tweetImage = findViewById(R.id.tweetImage);
        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        game = (Game) getIntent().getSerializableExtra("game");
        assert level != null;
        int convertedLevel = convert(level);
        int newScore = Objects.requireNonNull(getIntent().getExtras()).getInt("score");
        scoreDisplay.setText(String.valueOf(newScore));
        highScoreTweet = "My new High Score on Maths Master is " + newScore;

        DBHelper dbhelper = new DBHelper(this);
        db = dbhelper.getWritableDatabase();
        cursor = db.query("HIGHSCORES", new String[]{"_id", "DIFFICULTY", "SCORE"},
                null, null, null, null, "SCORE" + " DESC, DIFFICULTY" + " DESC");

        if (cursor.getCount() < MAX_SCORES) {
            saveScore(dbhelper, newScore, convertedLevel);
        } else {
            for (int i = 0; i < 10; i++) {
                cursor.moveToNext();
                int existingScore = cursor.getInt(2);
                //int existigLevel = cursor.getInt(1);
                if (newScore > existingScore) {
                    saveScore(dbhelper, newScore, convertedLevel);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_twitter:
                Intent intent = new Intent(this, Twitter_Activity.class);
                intent.putExtra("highScore", highScoreTweet);
                startActivity(intent);
                return true;
            case R.id.action_open_settings:
                showFragment(settingsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideFragment(settingsFragment);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    void saveScore(DBHelper dbhelper, int newScore, int newLevel) {
        dbhelper.insertScore(db, getDate(), newLevel, newScore);
        highScore.setText("Well Done\nYou got a HIGH SCORE!");
        tweetImage.setVisibility(View.VISIBLE);
        Log.i("Results", "New high score added!");
    }

    int convert(Difficulty level) {
        int newLevel;

        switch (level) {
            case MEDIUM:
                newLevel = 1;
                break;
            case HARD:
                newLevel = 2;
                break;
            case MASTER:
                newLevel = 3;
                break;
            default:
                newLevel = 0;
        }
        return newLevel;
    }

    String getDate() {
        return new SimpleDateFormat("dd-MM-yy", Locale.getDefault()).format(new Date());
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
                showFragment(settingsFragment);
                break;
            case SHAKE:
            case RESTART:
                Intent intent = new Intent(this, HigherLowerGameActivity.class);
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
        }
    }
}
