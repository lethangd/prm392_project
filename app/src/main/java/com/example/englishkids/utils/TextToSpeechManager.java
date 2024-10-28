package com.example.englishkids.utils;

import android.app.Activity;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TextToSpeechManager {

    private TextToSpeech textToSpeech;
    private Activity activity;

    public TextToSpeechManager(Activity activity) {
        this.activity = activity;
        textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // Set language (if needed)
                    textToSpeech.setLanguage(Locale.US); // Example: Set to US English
                }
            }
        });
    }

    public void speak(String text) {
        if (textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }
    }
}
