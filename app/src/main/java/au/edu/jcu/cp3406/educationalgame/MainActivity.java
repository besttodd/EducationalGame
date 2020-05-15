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
    Difficulty level;
    Spinner difficultySpinner;
    Fragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        hideFragment(settingsFragment);

        difficultySpinner = findViewById(R.id.difficultySpinner);
        level = Difficulty.EASY;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu on the app bar.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open_settings:
                showFragment(settingsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideFragment(settingsFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (allowRefresh)
        //{
            //allowRefresh = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.settingsFragment, settingsFragment).commit();
        //}
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void startHLGame(View view) {
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

    public void highScoresClicked(View view) {
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivityForResult(intent, ScoresActivity.SETTINGS_REQUEST);
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        switch (state) {
            case SETTINGS:
                hideFragment(settingsFragment);
                break;
        }
    }
}
