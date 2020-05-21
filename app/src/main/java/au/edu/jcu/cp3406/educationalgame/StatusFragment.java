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

    void setScore(String points, String rounds) {
        score.setText(points);
        if (!rounds.equals("0")) {
            time.setText(rounds);
        }
    }
}
