package au.edu.jcu.cp3406.educationalgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatusFragment extends Fragment {
    private TextView time;
    private TextView score;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        time = view.findViewById(R.id.timeDisplay);
        score = view.findViewById(R.id.scoreDisplay);
        return view;
    }

    void setScore(int points, int rounds) {
        String textPoints = String.format("Score: %s", Integer.toString(points));
        score.setText(textPoints);
        if (!(rounds == -1)) {
            String textRounds = String.format("Rounds: %s", Integer.toString(rounds));
            time.setText(textRounds);
        }
    }
}
