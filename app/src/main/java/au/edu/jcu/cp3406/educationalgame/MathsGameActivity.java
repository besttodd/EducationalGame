package au.edu.jcu.cp3406.educationalgame;

import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MathsGameActivity extends BaseActivity implements StateListener {
    private String gameTime = "00:15";
    private Difficulty level;
    private SoundManager soundManager;
    private Timer timer;
    private boolean timerPaused;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private SettingsFragment settingsFragment;
    private MathsGameFragment gameFragment;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        soundManager = (SoundManager) getApplicationContext();
        FragmentManager fm = getSupportFragmentManager();
        gameFragment = (MathsGameFragment) fm.findFragmentById(R.id.gameFragment);
        StatusFragment statusFragment = (StatusFragment) fm.findFragmentById(R.id.statusFragment);
        assert statusFragment != null;
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

        Button equals = findViewById(R.id.equalButton);
        if (level == Difficulty.MASTER) {
            equals.setVisibility(View.VISIBLE);
        }

        if (savedInstanceState == null) {
            timer = new Timer(gameTime);
            startTimer();
        } else {
            timer = new Timer(savedInstanceState.getString("time"));
            startTimer();
            statusFragment.setScore(savedInstanceState.getInt("score"), -1);
        }
        timerPaused = false;
        gameFragment.newRound(level);
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
        soundManager.pauseMusic();
        timerPaused = true;
        gameTime = timer.toString();
        handler.removeCallbacks(runnable);
        mSensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (soundManager.isMusicOn()) {
            soundManager.resumeMusic();
        } else {
            soundManager.pauseMusic();
        }
        if (timerPaused) {
            timer = new Timer(gameTime);
            startTimer();
        }
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("time", timer.toString());
        outState.putInt("score", gameFragment.getScore());
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case SETTINGS:
                hideFragment(settingsFragment);
                break;
            case RESTART:
                intent = getIntent();
                handler.removeCallbacks(runnable);
                finish();
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
            case GAME_OVER:
                handler.removeCallbacks(runnable);
                finish();
                intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("difficulty", level);
                intent.putExtra("score", gameFragment.getScore());
                intent.putExtra("game", Game.MATHS);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(runnable);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //soundManager.closeAudio();
        handler.removeCallbacks(runnable);
    }

    private void startTimer() {
        timer.startTimer();
        final TextView time = findViewById(R.id.timeDisplay);

        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                timer.tick();
                time.setText(String.format("Time: %s", timer.toString()));
                if (timer.isRunning()) {
                    Log.i("Timer", "TICK TICK TICK TICK");
                    handler.postDelayed(this, 1000);
                } else {
                    onUpdate(State.GAME_OVER, level);
                }
            }
        });
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
        Intent intent = getIntent();
        handler.removeCallbacks(runnable);
        finish();
        intent.putExtra("difficulty", level);
        startActivity(intent);
        Log.i("SensorActivated", "Shake event detected");
    }

    public boolean isTimerRunning() {
        return timer.isRunning();
    }
}
