package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.ActionBar;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class Twitter_Activity extends AppCompatActivity implements StateListener {
    private TextView userInfo;
    private TweetAdapter adapter;
    private Button authenticate;
    private Twitter twitter = TwitterFactory.getSingleton();
    private User user;
    private List<Tweet> tweets;
    private String tweetText;

    private Difficulty level;
    private SoundManager soundManager;
    private Game game;
    private SettingsFragment settingsFragment;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");
        soundManager = (SoundManager) getApplicationContext();
        game = (Game) getIntent().getSerializableExtra("game");

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = (SettingsFragment) fm.findFragmentById(R.id.settingsFragment);
        hideFragment(settingsFragment);

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

        userInfo = findViewById(R.id.user_info);
        ListView tweetList = findViewById(R.id.tweets);
        authenticate = findViewById(R.id.authorise);

        tweets = new ArrayList<>();
        adapter = new TweetAdapter(this, tweets);
        tweetList.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Background.run(new Runnable() {
            @Override
            public void run() {
                final boolean status;
                final String text;
                if (isAuthorised()) {
                    try {
                        tweetText = Objects.requireNonNull(getIntent().getExtras()).getString("highScore");
                        twitter.updateStatus(tweetText);
                    } catch (TwitterException ignored) {
                        Log.i("TwitterTweet", "UpdateStatus Failed");
                    }

                    text = user.getScreenName();
                    tweets.clear();
                    tweets.addAll(queryTwitter(game.toString()));
                    status = false;
                } else {
                    text = "Unknown";
                    authorise();
                    status = true;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userInfo.setText(text);
                        authenticate.setEnabled(status);
                        adapter.notifyDataSetChanged();
                    }
                });
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
        } else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        soundManager.pauseMusic();
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
        hideFragment(settingsFragment);
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        switch (state) {
            case SETTINGS:
                hideFragment(settingsFragment);
                break;
            case RESTART:
                restart(level);
                break;
        }
    }

    public void authorise(View view) {
        authorise();
    }

    public void authorise() {
        Intent intent = new Intent(this, Authenticate_Activity.class);
        startActivity(intent);
    }

    private boolean isAuthorised() {
        try {
            user = twitter.verifyCredentials();
            Log.i("TwitterActivity", "verified");
            return true;
        } catch (Exception e) {
            Log.i("TwitterActivity", "not verified");
            return false;
        }
    }

    private List<Tweet> queryTwitter(String topic) {
        List<Tweet> results = new ArrayList<>();

        Query query = new Query();
        query.setQuery("#" + topic);

        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                String name = status.getUser().getScreenName();
                String message = status.getText();
                Tweet tweet = new Tweet(name, message);
                results.add(tweet);
            }
        } catch (final Exception e) {
            Log.e("MainActivity", "query error: " + e.getLocalizedMessage());
        }

        return results;
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

    private void handleShakeEvent() {
        restart(level);
    }

    public void restart(Difficulty level) {
        Intent intent;
        if (game.equals(Game.MATHS)) {
            intent = new Intent(this, MathsGameActivity.class);
        } else {
            intent = new Intent(this, MemoryGameActivity.class);
        }
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }
}
