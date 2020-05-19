package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MemoryGameActivity extends BaseActivity implements StateListener {
    private SoundManager soundManager;
    private SettingsFragment settingsFragment;
    private MemoryGameFragment memoryGameFragment;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        memoryGameFragment = (MemoryGameFragment) fm.findFragmentById(R.id.memoryGameFragment);
        settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.settingsFragment);
        String screen = getResources().getString(R.string.screen_type);
        if (screen.equals("phone")) {
            hideFragment(settingsFragment);
        }

        //ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Log.i("Sensor detection", "No accelerometer found on device");
        }
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                handleShakeEvent();
            }
        });

        TextView roundsDisplay = findViewById(R.id.timeDisplay);
        roundsDisplay.setText(String.format("Rounds: %s", "0"));
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
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case SETTINGS:
                hideFragment(settingsFragment);
                break;
            case SHAKE:
            case RESTART:
                intent = getIntent();
                finish();
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
            case CONTINUE:
                break;
            case GAME_OVER:
                intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("score", memoryGameFragment.getScore());
                intent.putExtra("difficulty", level);
                intent.putExtra("game", Game.MEMORY);
                startActivity(intent);
                break;
        }
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

    void handleShakeEvent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
