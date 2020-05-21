package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class MathsGameFragment extends Fragment {
    private static final int POINTS_CORRECT = 10;
    private static final int POINTS_INCORRECT = -10;
    private Difficulty level;
    private SoundManager soundManager;
    private MathsGame game;
    private TextView taskTextView;
    private String task;
    private Button card1;
    private Button card2;
    private ImageView mark;
    private StatusFragment statusFragment;

    public MathsGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        soundManager = ((SoundManager) context.getApplicationContext());
        game = new MathsGame();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            view = inflater.inflate(R.layout.fragment_maths_game_land, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_maths_game, container, false);
        }

        level = (Difficulty) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("difficulty");
        taskTextView = view.findViewById(R.id.taskDisplay);
        card1 = view.findViewById(R.id.card1Button);
        card2 = view.findViewById(R.id.card2Button);
        mark = view.findViewById(R.id.markImage);
        Button equals = view.findViewById(R.id.equalButton);
        if (level == Difficulty.MASTER) {
            equals.setVisibility(View.VISIBLE);
        }
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelected(v);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelected(v);
            }
        });
        equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSelected(v);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        statusFragment = (StatusFragment) Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.statusFragment);
    }

    void newRound(Difficulty level) {
        MathsGameActivity activity = (MathsGameActivity) getActivity();
        game.setCards(level);
        task = game.getTask(level);
        String taskText = "Select the - " + task;

        if (Objects.requireNonNull(activity).isTimerRunning()) {
            card1.setText(game.getCard1());
            card2.setText(game.getCard2());
            taskTextView.setText(taskText);
        }
    }

    private void checkSelected(View view) {
        int selectedCard = view.getId();
        boolean result = false;

        switch (selectedCard) {
            case R.id.card1Button:
                result = game.checkCards(1, task);
                break;
            case R.id.card2Button:
                result = game.checkCards(2, task);
                break;
            case R.id.equalButton:
                result = game.checkCards(3, task);
                break;
        }

        if (result) {
            soundManager.playSound(1);
            mark.setImageResource(R.drawable.correct);
            game.setScore(POINTS_CORRECT);
            statusFragment.setScore(game.getScore(), -1);
        } else {
            soundManager.playSound(0);
            mark.setImageResource(R.drawable.incorrect);
            game.setScore(POINTS_INCORRECT);
            statusFragment.setScore(game.getScore(), -1);
        }
        newRound(level);
    }

    int getScore() {
        return game.getScore();
    }
}
