package au.edu.jcu.cp3406.educationalgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ScoresActivity extends BaseActivity implements StateListener {
    public static int SETTINGS_REQUEST = 222;
    private Game game = Game.MATHS;  //Which game to restart

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);
        hideFragment(settingsFragment);*/

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

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
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
        //SHAKE is only state event on this activity
        Intent intent;
        if (game.equals(Game.MATHS)) {
            intent = new Intent(this, MathsGameActivity.class);
        } else {
            intent = new Intent(this, MemoryGameActivity.class);
        }
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return new ScoresFragment(Objects.requireNonNull(getPageTitle(position)));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Maths Master";
                case 1:
                    return "Memory Master";
                default:
                    return "BLANK";
            }
        }
    }

    void handleShakeEvent() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
