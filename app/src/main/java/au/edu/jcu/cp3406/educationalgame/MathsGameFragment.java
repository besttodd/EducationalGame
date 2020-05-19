package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
    private SoundManager soundManager;
    private Difficulty level;
    private MathsGame game;

    private Context context;
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
        this.context = context;
        soundManager = ((SoundManager) context.getApplicationContext());
        game = new MathsGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maths_game, container, false);

        level = (Difficulty) getActivity().getIntent().getSerializableExtra("difficulty");
        soundManager = ((SoundManager) context.getApplicationContext());

        statusFragment = (StatusFragment) Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.statusFragment);
        assert statusFragment != null;
        mark = view.findViewById(R.id.markImage);
        Button equals = view.findViewById(R.id.equalButton);
        if (level == Difficulty.MASTER) {
            equals.setVisibility(View.VISIBLE);
        }
        card1 = view.findViewById(R.id.card1Button);
        card2 = view.findViewById(R.id.card2Button);

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
        return view;
    }

    void newRound(Difficulty level) {
        MathsGameActivity activity = (MathsGameActivity) getActivity();
        game.setCards(level);

        if (Objects.requireNonNull(activity).isTimerRunning()) {
            card1.setText(game.getCard1());
            card2.setText(game.getCard2());
        }
    }

    private void checkSelected(View view) {
        int selectedCard = view.getId();
        boolean result = false;

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

        if (result) {
            soundManager.playSound(1);
            mark.setImageResource(R.drawable.correct);
            game.setScore(POINTS_CORRECT);
            statusFragment.setScore(String.format("Score: %s", Integer.toString(game.getScore())));
        } else {
            soundManager.playSound(0);
            mark.setImageResource(R.drawable.incorrect);
            game.setScore(POINTS_INCORRECT);
            statusFragment.setScore(String.format("Score: %s", Integer.toString(game.getScore())));
        }
        newRound(level);
    }

    int getScore() {
        return game.getScore();
    }
}
