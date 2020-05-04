package au.edu.jcu.cp3406.educationalgame;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;

public class SoundManager extends Activity {
    SoundPool soundPool;
    int[] sounds;
    int soundNotMuted;

    SoundManager() {
        soundPool = new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0);
        sounds = new int[2];
        soundNotMuted = 1;
    }

    void loadSounds(Context context) {
        sounds[0] = soundPool.load(context, R.raw.correct, 1);
        sounds[1] = soundPool.load(context, R.raw.incorrect, 2);
    }

    void playSound(int soundNum) {
        soundPool.play(sounds[soundNum], soundNotMuted, soundNotMuted, 1, 0, 1);
    }

    void muteUnMuteSound() {
        if (soundNotMuted == 1) {
            soundNotMuted = 0;
        } else {
            soundNotMuted = 1;
        }
    }

    void muteUnMuteMusic() {
    }

    void closeAudio() {
        sounds = null;
        soundPool.release();
        soundPool = null;
    }
}
