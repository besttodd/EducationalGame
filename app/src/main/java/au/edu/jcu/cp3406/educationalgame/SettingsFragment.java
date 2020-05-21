package au.edu.jcu.cp3406.educationalgame;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private Difficulty level;
    private SoundManager soundManager;
    private StateListener listener;
    private Switch sound;
    private Switch music;

    public SettingsFragment() {
        level = Difficulty.EASY;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (StateListener) context;
        level = (Difficulty) Objects.requireNonNull(getActivity()).getIntent().getSerializableExtra("difficulty");
        if (level == null) {
            level = Difficulty.EASY;
        }
        soundManager = ((SoundManager) context.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sound = view.findViewById(R.id.soundSwitch);
        music = view.findViewById(R.id.musicSwitch);
        final Spinner spinner = view.findViewById(R.id.settingsdifficulty);
        spinner.setSelection(getIndex(spinner, level));

        sound.setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.toggleSound();
            }
        });
        music.setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundManager.toggleMusic();
            }
        });

        view.findViewById(R.id.closeButton).setOnClickListener(new Switch.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selection = spinner.getSelectedItem().toString().toUpperCase();
                if (!selection.equals(level.toString())) {
                    level = Difficulty.valueOf(selection.toUpperCase());
                    listener.onUpdate(State.RESTART, level);
                } else {
                    listener.onUpdate(State.SETTINGS, level);
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (soundManager.audioReady()) {
            music.setChecked(soundManager.isMusicOn());
        } else {music.setChecked(true);}
        sound.setChecked(soundManager.isSoundOn());
    }

    private int getIndex(Spinner spinner, Difficulty level) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            String selection = spinner.getItemAtPosition(i).toString().toUpperCase();
            if (selection.equals(level.toString())) {
                index = i;
            }
        }
        return index;
    }
}
