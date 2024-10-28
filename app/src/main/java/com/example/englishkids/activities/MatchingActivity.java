package com.example.englishkids.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.os.HandlerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.englishkids.R;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.repository.VocabularyRepository;
import com.example.englishkids.utils.SoundManager;
import com.example.englishkids.utils.TextToSpeechManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MatchingActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private Button selectedWordButton = null;
    private Button nextButton;
    private Button selectedMeaningButton = null;
    private List<Vocabulary> vocabularyList = new ArrayList<>();  // Initialize as an empty list
    private List<Pair<Integer, String>> meanings;
    private boolean isCorrect = false;
    private SoundManager soundManager;
    private TextToSpeechManager textToSpeechManager;
    private ProgressBar progressBar;
    private boolean isAnimating = false;
    private int correctMatchesCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_matching);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.matching_act), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();
        executorService = Executors.newSingleThreadExecutor();
        loadVocabularyData();

    }

    private void bindingAction() {
        nextButton.setOnClickListener(this::onNextButtonClick);
    }

    private void onNextButtonClick(View view) {
        openFillingVocabSection();
    }
    private void openFillingVocabSection() {
        Intent intent = new Intent(this, FillVocabInBlankActivity.class);
        intent.putExtra("lesson_id", getIntent().getIntExtra("lesson_id", -1));
        intent.putExtra("progress", progressBar.getProgress());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void bindingView() {
        progressBar = findViewById(R.id.progress_bar_matching);
        soundManager = new SoundManager(this);
        textToSpeechManager = new TextToSpeechManager(this);
        nextButton = findViewById(R.id.btn_continue);
    }

    private void loadVocabularyData() {
        int lessonId = getIntent().getIntExtra("lesson_id", -1);
        // Run database access in a background thread
        executorService.execute(() -> {
            VocabularyRepository vocabularyRepository = new VocabularyRepository(this);
            vocabularyList = vocabularyRepository.getVocabularyByLessonId(lessonId);
            progressBar.setMax(vocabularyList.size() * 3);
            progressBar.setProgress(getIntent().getIntExtra("progress", 0));
            runOnUiThread(this::inflateExercise);
        });
    }

    private void inflateExercise() {

        Collections.shuffle(vocabularyList);
        meanings = new ArrayList<>();
        for (Vocabulary v : vocabularyList) {
            meanings.add(Pair.create(v.vocab_id, v.meaning));
        }
        Collections.shuffle(meanings);

        LinearLayout vocabularyRow = findViewById(R.id.vocabularyRow);
        LinearLayout meaningRow = findViewById(R.id.meaningRow);

        for (Vocabulary v : vocabularyList) {
            FrameLayout wordButtonLayout = createButton(v.word, v.vocab_id, 1);
            Button wordButton = (Button) wordButtonLayout.getChildAt(0);
            wordButton.setOnClickListener(view -> {
                selectWord(wordButton);
                checkMatch();
            });
            vocabularyRow.addView(wordButtonLayout);
        }

        for (Pair<Integer, String> meaning : meanings) {
            FrameLayout meaningButtonLayout = createButton(meaning.second, meaning.first, 2);
            Button meaningButton = (Button) meaningButtonLayout.getChildAt(0);
            meaningButton.setOnClickListener(view -> {
                selectMeaning(meaningButton);
                checkMatch();
            });
            meaningRow.addView(meaningButtonLayout);
        }
    }

    private FrameLayout createButton(String text, int id, int type) {
        Button button = new Button(this);
        button.setText(text);
        button.setTag(id);
        button.setTextSize(20f);
        button.setTextColor(Color.BLACK);
        button.setBackgroundResource(R.drawable.button_oval);
        button.setAllCaps(false);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(80)
        );
        buttonParams.setMargins((type == 1) ? 20 : 50, 60, (type == 1) ? 50 : 20, 10);
        button.setLayoutParams(buttonParams);

        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        frameLayout.addView(button);

        return frameLayout;
    }

    private void selectWord(Button wordButton) {
        if (isAnimating) {
            return;
        }

        if (selectedWordButton != null) {
            selectedWordButton.setBackgroundResource(R.drawable.button_oval);
        }

        selectedWordButton = wordButton;
        textToSpeechManager.speak(selectedWordButton.getText().toString());
        selectedWordButton.setBackgroundResource(R.drawable.button_oval_pressed);
    }


    private void selectMeaning(Button meaningButton) {
        if (isAnimating) {
            return;
        }

        if (selectedMeaningButton != null) {
            selectedMeaningButton.setBackgroundResource(R.drawable.button_oval);
        }
        selectedMeaningButton = meaningButton;
        selectedMeaningButton.setBackgroundResource(R.drawable.button_oval_pressed);
    }


    private void checkMatch() {
        if (isAnimating) {
            return;
        }

        if (selectedWordButton != null && selectedMeaningButton != null) {
            int selectedWordId = (int) selectedWordButton.getTag();
            int selectedMeaningId = (int) selectedMeaningButton.getTag();

            if (selectedWordId == selectedMeaningId) {
                isCorrect = true;
                soundManager.playCorrectSound();
                progressBar.setProgress(progressBar.getProgress() + 1);
                disableMatchedButtons();
            } else {
                isCorrect = false;
                soundManager.playIncorrectSound();
                showRedBorder(selectedWordButton);
                showRedBorder(selectedMeaningButton);
                HandlerCompat.createAsync(Looper.getMainLooper()).postDelayed(this::resetSelections, 800);
            }
        }
    }


    private void showRedBorder(Button button) {
        button.setBackgroundResource(R.drawable.button_oval_incorrect);
    }

    private void disableMatchedButtons() {
        isAnimating = true;

        selectedWordButton.setEnabled(false);
        selectedMeaningButton.setEnabled(false);
        selectedWordButton.setBackgroundResource(R.drawable.button_oval_correct);
        selectedMeaningButton.setBackgroundResource(R.drawable.button_oval_correct);

        playAnimationOnButton(selectedWordButton);
        playAnimationOnButton(selectedMeaningButton);

        correctMatchesCount++;

        HandlerCompat.createAsync(Looper.getMainLooper()).postDelayed(this::resetSelections, 800);
        if (correctMatchesCount == vocabularyList.size()) {
            nextButton.setVisibility(View.VISIBLE);
        }
    }



    private void resetSelections() {
        if (selectedWordButton != null && isCorrect && selectedMeaningButton != null) {
            selectedWordButton.setBackgroundResource(R.drawable.button_oval_disabled);
            selectedMeaningButton.setBackgroundResource(R.drawable.button_oval_disabled);
            selectedWordButton.setTextColor(getColor(R.color.text_disable));
            selectedMeaningButton.setTextColor(getColor(R.color.text_disable));
        } else if (selectedWordButton != null && selectedMeaningButton != null && !isCorrect) {
            selectedWordButton.setBackgroundResource(R.drawable.button_oval);
            selectedMeaningButton.setBackgroundResource(R.drawable.button_oval);
        }

        selectedWordButton = null;
        selectedMeaningButton = null;
        isAnimating = false;
    }


    private void playAnimationOnButton(Button button) {
        LottieAnimationView animationView = new LottieAnimationView(this);
        animationView.setAnimation(R.raw.correct_answer2);

        int sizeInPixels = dpToPx(60);
        animationView.setLayoutParams(new FrameLayout.LayoutParams(
                sizeInPixels,
                sizeInPixels,
                Gravity.END | Gravity.CENTER_VERTICAL
        ));
        animationView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        animationView.setVisibility(View.VISIBLE);

        FrameLayout parentLayout = (FrameLayout) button.getParent();
        parentLayout.addView(animationView);

        animationView.playAnimation();
        animationView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                parentLayout.removeView(animationView);
            }
        });
    }

    public int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();if (textToSpeechManager != null) {
            textToSpeechManager.shutdown();
        }
        if (soundManager != null) {
            soundManager.release();
        }
    }
}
