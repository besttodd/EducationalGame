package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HigherLowerGameFragment extends Fragment {
    private StateListener listener;
    private Context context;
    private HigherLowerGameFragment gameFragment;
    private StatusFragment statusFragment;
    private SettingsFragment settingsFragment;

    public HigherLowerGameFragment() {
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
        return inflater.inflate(R.layout.fragment_higher_lower_game, container, false);
    }
}
