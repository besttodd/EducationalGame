package au.edu.jcu.cp3406.educationalgame;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;

public class SoundManager extends Activity {
    SoundPool soundPool;
    int[] sounds;
    int notMuted;

    SoundManager() {
        soundPool = new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0);
        sounds = new int[2];
        notMuted = 1;
    }

    void loadSounds(Context context) {
        sounds[0] = soundPool.load(context, R.raw.correct, 1);
        sounds[1] = soundPool.load(context, R.raw.incorrect, 2);
    }

    void playSound(int soundNum) {
        soundPool.play(sounds[soundNum], notMuted, notMuted, 1, 0, 1);
    }

    void muteUnMuteSound() {
        if (notMuted == 1) {
            notMuted = 0;
        } else {
            notMuted = 1;
        }
    }

    void closeAudio() {
        sounds = null;
        soundPool.release();
        soundPool = null;
    }
}
