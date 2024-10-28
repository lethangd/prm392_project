package com.example.englishkids.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.englishkids.R;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.repository.VocabularyRepository;
import com.example.englishkids.utils.ResultHandler;
import com.example.englishkids.utils.SoundManager;
import com.example.englishkids.utils.TextToSpeechManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VocabularyActivity extends AppCompatActivity {

    private TextView word, pairOfWord, txtResult;
    private Button optionOne, optionTwo, btnNext;
    private ImageView imageDisplay;
    private ProgressBar progressBar;
    private List<Vocabulary> vocabularyList;
    private int currentIndex = 0;
    private ExecutorService executorService;
    private LinearLayout resultLayout;
    private ImageButton btnSpeaker;
    private TextToSpeechManager textToSpeechManager;
    private List<Vocabulary> incorrectVocabularyList = new ArrayList<>();
    private SoundManager soundManager;
    private ResultHandler resultHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocabulary);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_vocab), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        loadVocabularyList();
        bindingAction();
    }

    private void bindingView() {
        word = findViewById(R.id.txt_vocab);
        optionOne = findViewById(R.id.optionOne);
        optionTwo = findViewById(R.id.optionTwo);
        btnNext = findViewById(R.id.btnNext);
        imageDisplay = findViewById(R.id.imageDisplay);
        progressBar = findViewById(R.id.progress_bar);
        resultLayout = findViewById(R.id.ln_result);
        pairOfWord = findViewById(R.id.pair_word);
        txtResult = findViewById(R.id.txt_result);
        btnSpeaker = findViewById(R.id.btn_speaker);
        textToSpeechManager = new TextToSpeechManager(this);
        executorService = Executors.newSingleThreadExecutor();
        soundManager = new SoundManager(this);
    }

    private void bindingAction() {
        optionOne.setOnClickListener(v -> {
            checkAnswer(optionOne.getText().toString());
        });
        optionTwo.setOnClickListener(v -> {
            checkAnswer(optionTwo.getText().toString());
        });
        btnNext.setOnClickListener(v -> showNextVocabulary());
        btnSpeaker.setOnClickListener(v -> textToSpeechManager.speak(word.getText().toString()));
    }

    private void loadVocabularyList() {
        int lessonId = getIntent().getIntExtra("lesson_id", -1);

        executorService.execute(() -> {
            VocabularyRepository vocabularyRepository = new VocabularyRepository(this);
            vocabularyList = vocabularyRepository.getVocabularyByLessonId(lessonId);

            runOnUiThread(() -> {
                if (!vocabularyList.isEmpty()) {
                    displayVocabulary(vocabularyList.get(currentIndex), "");
                    progressBar.setMax(vocabularyList.size() * 3);
                    resultHandler = new ResultHandler(this, progressBar, vocabularyList, pairOfWord, txtResult, resultLayout, btnNext, btnSpeaker);
                }
            });
        });
    }

    private void displayVocabulary(Vocabulary vocab, String incorrectMeaning) {
        word.setText(vocab.getWord());

        if (vocab.getImagePath() != null && !vocab.getImagePath().isEmpty()) {
            Glide.with(this).load(R.drawable.bg_lesson).into(imageDisplay);
        } else {
            imageDisplay.setImageResource(R.drawable.ic_image_placeholder);
        }

        boolean isOptionOneCorrect = Math.random() < 0.5;
        optionOne.setText(isOptionOneCorrect ? vocab.getMeaning() : (incorrectMeaning.isEmpty() ? getRandomIncorrectMeaning(vocab.getMeaning()) : incorrectMeaning));
        optionTwo.setText(isOptionOneCorrect ? (incorrectMeaning.isEmpty() ? getRandomIncorrectMeaning(vocab.getMeaning()) : incorrectMeaning) : vocab.getMeaning());

        textToSpeechManager.speak(vocab.getWord());
    }

    private String getRandomIncorrectMeaning(String correctMeaning) {
        Vocabulary randomVocab;
        do {
            randomVocab = vocabularyList.get((int) (Math.random() * vocabularyList.size()));
        } while (randomVocab.getMeaning().equals(correctMeaning));
        return randomVocab.getMeaning();
    }

    private void checkAnswer(String selectedAnswer) {
        Vocabulary currentVocab = vocabularyList.get(currentIndex);
        boolean isCorrect = resultHandler.checkAnswer(selectedAnswer, currentVocab.getMeaning() ,currentVocab);
        if(!isCorrect) {
            incorrectVocabularyList.add(currentVocab);
        }
        optionOne.setVisibility(View.GONE);
        optionTwo.setVisibility(View.GONE);
        fadeIn(btnNext);
    }

    private void showNextVocabulary() {
        String incorrectMeaning = "";
        currentIndex++;
        if (currentIndex < vocabularyList.size()) {
            displayVocabulary(vocabularyList.get(currentIndex), incorrectMeaning);
        } else if (!incorrectVocabularyList.isEmpty()) {
            if (incorrectVocabularyList.size() < 2) {
                incorrectMeaning = getRandomIncorrectMeaning(incorrectVocabularyList.get(0).getMeaning());
            }
            vocabularyList = new ArrayList<>(incorrectVocabularyList);
            incorrectVocabularyList.clear();
            currentIndex = 0;
            displayVocabulary(vocabularyList.get(currentIndex), incorrectMeaning);
        } else {
            openMatchingSection();
            return;
        }

        optionOne.setVisibility(View.VISIBLE);
        optionTwo.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.GONE);
        resultLayout.setVisibility(View.GONE);
    }

    private void openMatchingSection() {
        Intent intent = new Intent(VocabularyActivity.this, MatchingActivity.class);
        intent.putExtra("lesson_id", getIntent().getIntExtra("lesson_id", -1));
        intent.putExtra("progress", resultHandler.getCorrectAnswerCount());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void fadeIn(View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(300).setListener(null);
    }

    private void fadeOut(View view) {
        view.animate().alpha(0f).setDuration(300).withEndAction(() -> view.setVisibility(View.GONE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
        if (textToSpeechManager != null) {
            textToSpeechManager.shutdown();
        }

        // Release sounds
        if (soundManager != null) {
            soundManager.release();
        }

        // Release the result handler sound resources
        if (resultHandler != null) {
            resultHandler.release();
        }
    }
}
