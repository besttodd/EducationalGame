package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends BaseActivity implements StateListener {
    private Difficulty level;
    private SoundManager soundManager;
    private Spinner difficultySpinner;
    private Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        level = Difficulty.EASY;
        soundManager = (SoundManager) getApplicationContext();
        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        hideFragment(settingsFragment);
        difficultySpinner = findViewById(R.id.difficultySpinner);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open_settings) {
            showFragment(settingsFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideFragment(settingsFragment);
        soundManager.pauseMusic();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (soundManager.isMusicOn()) {
            soundManager.resumeMusic();
        } else {
            soundManager.pauseMusic();
        }
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        if (state == State.SETTINGS) {
            hideFragment(settingsFragment);
        }
    }

    public void startMathsGame(View view) {
        String selection = difficultySpinner.getSelectedItem().toString();
        level = Difficulty.valueOf(selection.toUpperCase());

        Intent intent = new Intent(this, MathsGameActivity.class);
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }

    public void startMemoryGame(View view) {
        String selection = difficultySpinner.getSelectedItem().toString();
        level = Difficulty.valueOf(selection.toUpperCase());

        Intent intent = new Intent(this, MemoryGameActivity.class);
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }

    public void highScoresClicked(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivityForResult(intent, ScoresActivity.SETTINGS_REQUEST);
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
}
