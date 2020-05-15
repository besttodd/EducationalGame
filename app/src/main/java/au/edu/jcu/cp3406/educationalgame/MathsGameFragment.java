package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MathsGameFragment extends Fragment {
    private StateListener listener;
    private Context context;
    private MathsGameFragment gameFragment;
    private StatusFragment statusFragment;
    private SettingsFragment settingsFragment;

    public MathsGameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maths_game, container, false);
    }
}
