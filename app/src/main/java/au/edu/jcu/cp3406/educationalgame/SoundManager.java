package au.edu.jcu.cp3406.educationalgame;

import android.app.Activity;
import android.content.Context;
import android.media.SoundPool;

public class SoundManager extends Activity {
    SoundPool soundPool;
    int[] sounds;
    int soundNotMuted;
    boolean musicMuted;
    int streamId;


    SoundManager() {
        soundPool = new SoundPool(3, android.media.AudioManager.STREAM_MUSIC, 0);
        sounds = new int[3];
        soundNotMuted = 1;
        musicMuted = false;
        streamId = -1;
    }

    void loadSounds(Context context) {
        sounds[0] = soundPool.load(context, R.raw.correct, 1);
        sounds[1] = soundPool.load(context, R.raw.incorrect, 2);
        sounds[2] = soundPool.load(context, R.raw.as_time_passes_zapsplat, 3);
    }

    void playSound(int soundNum) {
        soundPool.play(sounds[soundNum], soundNotMuted, soundNotMuted, 1, 0, 1);
    }

    void playMusic() {
        //do {
            streamId = soundPool.play(sounds[2], 1, 1, 1, 0, 1);
        //} while(streamId==0);
    }

    void muteUnMuteSound() {
        if (soundNotMuted == 1) {
            soundNotMuted = 0;
        } else {
            soundNotMuted = 1;
        }
    }

    void pauseMusic() {
        soundPool.pause(streamId);
        musicMuted = true;
    }

    void resumeMusic() {
        soundPool.resume(streamId);
        musicMuted = false;
    }

    boolean isMusicMuted() { return musicMuted; }

    void closeAudio() {
        sounds = null;
        soundPool.release();
        soundPool = null;
    }
}
