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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MemoryGameActivity extends BaseActivity implements StateListener {
    static int POINTS_CORRECT = 10;
    private Difficulty level;
    private SoundManager soundManager;

    private Tile[] tiles;
    private TileAdapter tileAdapter;
    private GridView gridView;
    private Fragment settingsFragment;

    private MemoryGame memoryGame = new MemoryGame();
    private List<Integer> sequence;
    private List<Integer> answers;
    private Integer[] previous = {0};
    private Iterator<Integer> iterator;
    private int rounds;

    private final Handler handler = new Handler();
    private Runnable runnable;

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
        Fragment gameFragment = fm.findFragmentById(R.id.memoryGameFragment);
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        boolean isLargeScreen = gameFragment != null;
        if (!isLargeScreen) {
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

        soundManager = (SoundManager) getApplication();
        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        rounds = 0;
        TextView roundsDisplay = findViewById(R.id.timeDisplay);
        roundsDisplay.setText(String.format("Rounds: %s", rounds));
        TileManager tileManager = new TileManager(this.getAssets(), "Shapes");
        sequence = memoryGame.newGame(level);
        tiles = tileManager.getTiles(memoryGame.getNumTiles());
        gridView = findViewById(R.id.gridView);
        tileAdapter = new TileAdapter(this, tiles);
        gridView.setAdapter(tileAdapter);
        answers = new ArrayList<>();

        playSequence();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tile tile = tiles[position];
                tiles[previous[0]].lightOff();

                tile.lightUp();
                soundManager.playSound(3);
                answers.add(position);
                previous[0] = position;

                if (!memoryGame.checkOrder(answers)) {
                    onUpdate(State.GAME_OVER, level);
                } else if (memoryGame.isSequenceComplete()) {
                    onUpdate(State.CONTINUE, level);
                }
                tileAdapter.notifyDataSetChanged();
            }
        });
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
        TextView score = findViewById(R.id.scoreDisplay);
        TextView roundsDisplay = findViewById(R.id.timeDisplay);
        Intent intent;

        switch (state) {
            case SETTINGS:
                hideFragment(settingsFragment);
                break;
            case SHAKE:
            case RESTART:
                intent = getIntent();
                handler.removeCallbacks(runnable);
                finish();
                intent.putExtra("difficulty", level);
                startActivity(intent);
                break;
            case CONTINUE:
                Toast.makeText(getBaseContext(), "YOU GOT IT\nKeep Going!", Toast.LENGTH_SHORT).show();
                memoryGame.setScore(POINTS_CORRECT);
                score.setText(String.format("Score: %s", Integer.toString(memoryGame.getScore())));
                rounds++;
                roundsDisplay.setText(String.format("Rounds: %s", rounds));
                sequence = memoryGame.createSequence(answers.size());
                answers = new ArrayList<>();
                playSequence();
                break;
            case GAME_OVER:
                intent = new Intent(this, ResultsActivity.class);
                intent.putExtra("score", memoryGame.getScore());
                intent.putExtra("difficulty", level);
                intent.putExtra("game", Game.MEMORY);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    public void playSequence() {
        iterator = sequence.iterator();

        handler.post(runnable = new Runnable() {
            @Override
            public void run() {
                if (iterator.hasNext()) {
                    tiles[previous[0]].lightOff();
                    previous[0] = iterator.next();
                    gridView.setAdapter(tileAdapter);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tiles[previous[0]].lightUp();
                            soundManager.playSound(3);
                            gridView.setAdapter(tileAdapter);
                        }
                    }, 1000);
                    handler.postDelayed(this, memoryGame.getSpeed());
                } else {
                    tiles[previous[0]].lightOff();
                    gridView.setAdapter(tileAdapter);
                    Toast.makeText(getBaseContext(), "YOUR TURN", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.i("Sequence", String.valueOf(sequence));
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
