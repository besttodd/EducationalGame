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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MathsGameActivity extends BaseActivity implements StateListener {
    static String GAME_TIME = "00:15";
    static int POINTS_CORRECT = 10;
    static int POINTS_INCORRECT = 5;
    private Timer timer;
    private MathsGame game = new MathsGame();
    private Difficulty level;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private boolean result = false;
    private Fragment settingsFragment;
    private SoundManager soundManager;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maths_game);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        soundManager = ((SoundManager) getApplication());

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

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");

        FragmentManager fm = getSupportFragmentManager();
        Fragment gameFragment = fm.findFragmentById(R.id.game);
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        boolean isLargeScreen = gameFragment != null;

        Button equals = findViewById(R.id.equalButton);
        if (level == Difficulty.MASTER) {
            equals.setVisibility(View.VISIBLE);
        }

        if (!isLargeScreen) {
            hideFragment(settingsFragment);
        }

        timer = new Timer(GAME_TIME);
        startTimer();
        newRound(level);
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
        super.onPause();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case SETTINGS:
                showFragment(settingsFragment);
                break;
            case SHAKE:
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
                intent.putExtra("score", game.getScore());
                intent.putExtra("game", Game.MATHS);
                startActivity(intent);
                break;
        }

    }

    public void checkSelected(View view) {
        TextView score = findViewById(R.id.scoreDisplay);
        ImageView mark = findViewById(R.id.markImage);
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
            //Toast.makeText(this, "CORRECT", Toast.LENGTH_SHORT).show();
            soundManager.playSound(1);
            mark.setImageResource(R.drawable.correct);
            game.setScore(POINTS_CORRECT);
            score.setText(String.format("Score: %s", Integer.toString(game.getScore())));
        } else {
            //Toast.makeText(this, "WRONG", Toast.LENGTH_SHORT).show();
            soundManager.playSound(0);
            mark.setImageResource(R.drawable.incorrect);
            game.setScore(POINTS_INCORRECT);
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
                    Log.i("Timer", "TICK TICK TICK TICK");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //soundManager.closeAudio();
        handler.removeCallbacks(runnable);
    }
}
