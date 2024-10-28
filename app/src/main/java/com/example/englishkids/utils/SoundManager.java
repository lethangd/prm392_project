package com.example.englishkids.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.englishkids.R;

public class SoundManager {
    private MediaPlayer correctSoundPlayer;
    private MediaPlayer incorrectSoundPlayer;

    public SoundManager(Context context) {
        correctSoundPlayer = MediaPlayer.create(context, R.raw.correct_sound);
        incorrectSoundPlayer = MediaPlayer.create(context, R.raw.incorrect_sound);
    }

    public void playCorrectSound() {
        if (correctSoundPlayer != null) {
            correctSoundPlayer.start();
        }
    }

    public void playIncorrectSound() {
        if (incorrectSoundPlayer != null) {
            incorrectSoundPlayer.start();
        }
    }

    public void release() {
        if (correctSoundPlayer != null) {
            correctSoundPlayer.release();
            correctSoundPlayer = null;
        }
        if (incorrectSoundPlayer != null) {
            incorrectSoundPlayer.release();
            incorrectSoundPlayer = null;
        }
    }
}
