package au.edu.jcu.cp3406.educationalgame;

import android.app.Application;
import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

public class SoundManager extends Application {
    private SoundPool soundPool;
    private int[] sounds;
    int soundOn;
    private boolean musicOn;
    private int streamId;
    private boolean loaded;

    public SoundManager() {
        sounds = new int[4];
        soundOn = 1;
        musicOn = true;
        streamId = -1;

        soundPool = new SoundPool(5, android.media.AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = status == 0;
                if (loaded) {
                    Log.i("SoundManager", "loaded sound: " + sampleId);
                    if (sampleId == 3) {
                        playMusic();
                    }
                }
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadSounds(this);
    }

    private void loadSounds(Context context) {
        sounds[3] = soundPool.load(context, R.raw.light_up, 0);
        sounds[2] = soundPool.load(context, R.raw.as_time_passes_zapsplat, 0);
        sounds[1] = soundPool.load(context, R.raw.correct, 0);
        sounds[0] = soundPool.load(context, R.raw.incorrect, 0);
    }

    public void playSound(int soundNum) {
        soundPool.play(sounds[soundNum], soundOn, soundOn, 1, 0, 1);
    }

    public void playMusic() {
        streamId = soundPool.play(sounds[2], 1, 1, 1, 10, 1);
    }

    public void toggleMusic() {
        if (musicOn) {
            muteMusic();
        } else {
            unMuteMusic();
        }
    }

    public void toggleSound() {
        if (soundOn == 1) {
            muteSound();
        } else {
            unMuteSound();
        }
    }

    void muteSound() {
        soundOn = 0;
    }

    void unMuteSound() {
        soundOn = 1;
    }

    void muteMusic() {
        soundPool.pause(streamId);
        musicOn = false;
    }

    void unMuteMusic() {
        soundPool.resume(streamId);
        musicOn = true;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public int isSoundOn() {
        return soundOn;
    }

    /*boolean audioReady() {
        return loaded;
    }*/

    void closeAudio() {
        sounds = null;
        soundPool.release();
        soundPool = null;
    }
}
