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

    public StatusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_status, container, false);
        score = view.findViewById(R.id.scoreDisplay);
        return view;
    }

    public void setScore(String points) {
        score.setText(points);
    }
}
