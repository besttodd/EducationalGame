package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HigherLowerGameActivity extends AppCompatActivity implements StateListener {
    Timer timer;
    HLGame game = new HLGame();
    Difficulty level;
    final Handler handler = new Handler();
    Runnable runnable;
    boolean result = false;
    SoundManager soundManager;
    Fragment settingsFragment;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_higher_lower_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        soundManager = new SoundManager();
        soundManager.loadSounds(this);
        soundManager.playMusic();

        //ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Log.i("Sensor detection", "No accelerometer fond on device");
        }
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                handleShakeEvent();
            }
        });

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);

        Button equals = findViewById(R.id.equalButton);
        if (level == Difficulty.MASTER) {
            equals.setVisibility(View.VISIBLE);
        }

        timer = new Timer("00:15");
        startTimer();
        newRound(level);
        showHideFragment(settingsFragment);
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
                showHideFragment(settingsFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case SOUND:
                soundManager.muteUnMuteSound();
                break;
            case MUSIC:
                if (soundManager.isMusicMuted()) { soundManager.resumeMusic(); }
                else { soundManager.pauseMusic(); }
                break;
            case RESTART:
                intent = getIntent();
                handler.removeCallbacks(runnable);
                finish();
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
            case SETTINGS:
                showHideFragment(settingsFragment);
                break;
            case SHAKE:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case GAME_OVER:
                intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("difficulty", level);
                intent.putExtra("score", game.getScore());
                startActivity(intent);
                break;
        }
    }

    public void checkSelected(View view) {
        TextView score = findViewById(R.id.scoreDisplay);
        int selectedCard = view.getId();

        switch (selectedCard) {
            case R.id.card1Button:
                result = game.checkCards(1, "Higher");
                break;
            case R.id.card2Button:
                result = game.checkCards(2, "Higher");
                break;
            case R.id.equalButton:
                result = game.checkCards(3, "Higher");
                break;
        }
        System.out.println(result);
        if (result) {
            Toast message = Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT);
            message.setGravity(Gravity.CENTER, 0, 0);
            message.show();
            soundManager.playSound(0);
            game.setScore(10);
            score.setText(String.format("Score: %s", Integer.toString(game.getScore())));
        } else {
            Toast notDone = Toast.makeText(this, "WRONG", Toast.LENGTH_SHORT);
            notDone.setGravity(Gravity.CENTER, 0, 0);
            notDone.show();
            soundManager.playSound(1);
            game.setScore(-5);
            score.setText(String.format("Score: %s", Integer.toString(game.getScore())));
        }
        newRound(level);
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
                    System.out.println("TICK TICK TICK TICK");
                    handler.postDelayed(this, 1000);
                } else {
                    onUpdate(State.GAME_OVER, level);
                }
            }
        });
    }

    void newRound(Difficulty level) {
        Button card1 = findViewById(R.id.card1Button);
        Button card2 = findViewById(R.id.card2Button);

        game.setCards(level);

        if (timer.isRunning()) {
            card1.setText(game.getCard1());
            card2.setText(game.getCard2());
        }
    }

    public void settingsClicked(View view) {
        showHideFragment(settingsFragment);
    }

    public void showHideFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (settingsFragment.isHidden()) {
            ft.show(fragment);
            Log.d("hidden","Show");
        } else {
            ft.hide(fragment);
            Log.d("Shown","Hide");
        }

        ft.commit();
    }

    void handleShakeEvent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        soundManager.closeAudio();
        handler.removeCallbacks(runnable);
    }
}
