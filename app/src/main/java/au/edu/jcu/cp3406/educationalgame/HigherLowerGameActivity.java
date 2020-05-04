package au.edu.jcu.cp3406.educationalgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_higher_lower_game);

        level = (Difficulty) getIntent().getSerializableExtra("difficulty");

        FragmentManager fm = getSupportFragmentManager();
        settingsFragment = fm.findFragmentById(R.id.settingsFragment);

        soundManager = new SoundManager();
        soundManager.loadSounds(this);
        timer = new Timer("00:10");
        startTimer();
        newRound(level);
        showHideFragment(settingsFragment);
    }

    public void onUpdate(State state, Difficulty level) {
        Intent intent;

        switch (state) {
            case SOUND:
                soundManager.muteUnMuteSound();
                break;
            case MUSIC:
                soundManager.muteUnMuteMusic();
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        soundManager.closeAudio();
    }
}
