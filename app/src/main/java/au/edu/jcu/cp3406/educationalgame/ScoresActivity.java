package au.edu.jcu.cp3406.educationalgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ScoresActivity extends BaseActivity implements StateListener {
    public static int SETTINGS_REQUEST = 222;
    private Game game = Game.MATHS;  //Which game to restart

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

        //Attach the SectionsPagerAdapter to the ViewPager
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);

        //Attach the ViewPager to the TabLayout
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdate(State state, Difficulty level) {
        //SHAKE is only state event on this activity
        Intent intent;
        if (game.equals(Game.MATHS)) { intent = new Intent(this, MathsGameActivity.class); }
        else { intent = new Intent(this, MemoryGameActivity.class); }
        intent.putExtra("difficulty", level);
        startActivity(intent);
    }

    private static class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override public int getCount() {
            return 2;
        }

        @NonNull
        @Override public Fragment getItem(int position) {
            return new ScoresFragment(Objects.requireNonNull(getPageTitle(position)));
        }

        @Override public CharSequence getPageTitle(int position) {
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
}
