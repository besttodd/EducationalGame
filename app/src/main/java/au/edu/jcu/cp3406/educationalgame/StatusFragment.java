package au.edu.jcu.cp3406.educationalgame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

public class StatusFragment extends Fragment {
    View view;
    TextView score;
    TextView time;

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_status, container, false);
        score = view.findViewById(R.id.scoreDisplay);
        time = view.findViewById(R.id.timeDisplay);
        return view;
    }

    public void setScore(String points, String rounds) {
        score.setText(points);

        if (!rounds.equals("0")) { time.setText(rounds);}
    }
}
