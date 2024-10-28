package com.example.englishkids.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.englishkids.R;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;

public class ResultHandler {
    private final Context context;
    private final ProgressBar progressBar;
    private int correctAnswerCount = 0;
    private final List<Vocabulary> vocabularyList;
    private final TextView pairOfWord;
    private final TextView txtResult;
    private final View resultLayout;
    private final Button btnNext;
    private final ImageButton btnSpeaker;
    private final SoundManager soundManager;
    private final int currentProgress;

    public ResultHandler(Context context, ProgressBar progressBar, List<Vocabulary> vocabularyList,
                         TextView pairOfWord, TextView txtResult, View resultLayout,
                         Button btnNext, ImageButton btnSpeaker) {
        this.context = context;
        this.progressBar = progressBar;
        this.vocabularyList = vocabularyList;
        this.pairOfWord = pairOfWord;
        this.txtResult = txtResult;
        this.resultLayout = resultLayout;
        this.btnNext = btnNext;
        this.btnSpeaker = btnSpeaker;
        this.soundManager = new SoundManager(context);
        this.currentProgress = progressBar.getProgress();
    }

    public boolean checkAnswer(String selectedAnswer, String correctAnswer, Vocabulary currentVocab) {
        fadeIn(resultLayout);
        pairOfWord.setText(String.format("%s - %s", currentVocab.getWord(), currentVocab.getMeaning()));

        if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
            // Correct answer
            correctAnswerCount++;
            progressBar.setProgress(currentProgress + correctAnswerCount);
            updateUIForCorrectAnswer();
            soundManager.playCorrectSound();
            return true;
        } else {
            // Incorrect answer
            updateUIForIncorrectAnswer();
            soundManager.playIncorrectSound();
            return false;
        }
    }

    private void updateUIForCorrectAnswer() {
        resultLayout.setBackgroundResource(R.drawable.layout_result);
        pairOfWord.setTextColor(Color.parseColor("#26a74e"));
        txtResult.setTextColor(Color.parseColor("#26a74e"));
        txtResult.setText("Chính xác");
        btnSpeaker.setImageResource(R.drawable.ic_speaker);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#42cd6e")));
    }

    private void updateUIForIncorrectAnswer() {
        resultLayout.setBackgroundResource(R.drawable.layout_result_incorrect);
        pairOfWord.setTextColor(Color.parseColor("#fc3e02"));
        txtResult.setTextColor(Color.parseColor("#fc3e02"));
        txtResult.setText("Chưa chính xác");
        btnSpeaker.setImageResource(R.drawable.ic_speaker_incorrect);
        btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#fc3e02")));
    }

    public int getCorrectAnswerCount() {
        return correctAnswerCount;
    }

    public void setProgressBarMax(int max) {
        progressBar.setMax(max);
    }

    public void resetCorrectAnswerCount() {
        correctAnswerCount = 0;
    }

    private void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(300).setListener(null);
    }
    public void release() {
        soundManager.release(); // Release sound resources
    }
}
