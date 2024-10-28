package com.example.englishkids.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishkids.R;
import com.example.englishkids.components.CustomKeyboard;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.repository.LessonRepository;
import com.example.englishkids.repository.VocabularyRepository;
import com.example.englishkids.utils.ResultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FillVocabInBlankActivity extends AppCompatActivity {

    private EditText userInput;
    private CustomKeyboard keyboard;
    private int currentAnswerIndex = 0; // Index for the current answer
    private StringBuilder userAnswer = new StringBuilder();
    private HashMap<Character, Integer> letterCountMap = new HashMap<>();
    private HashMap<Character, Integer> userLetterCountMap = new HashMap<>();
    private Button hintButton, btnNext;
    private Executor executorService;
    private List<Vocabulary> vocabularyList;
    private ProgressBar progressBar;
    private ResultHandler resultHandler;
    private TextView pairOfWord;
    private TextView txtResult;
    private TextView word;
    private View resultLayout;
    private ImageButton btnSpeaker;
    private LessonRepository lessonRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fill_vocab_in_blank_acitivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.fill_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        bindingAction();

        loadVocabList();
        setupKeyboardListeners();
        updateUserInputDisplay();
    }

    private void bindingView() {
        word = findViewById(R.id.txt_vocab_fill);
        userInput = findViewById(R.id.user_input);
        keyboard = findViewById(R.id.keyboard);
        hintButton = findViewById(R.id.hint_button);
        progressBar = findViewById(R.id.progress_bar_fill);
        btnNext = findViewById(R.id.btn_next_fill);
        pairOfWord = findViewById(R.id.pair_word_fill);
        txtResult = findViewById(R.id.txt_result_fill);
        resultLayout = findViewById(R.id.ln_result_fill);
        btnSpeaker = findViewById(R.id.btn_speaker_fill);
        btnNext.setEnabled(false);
        btnNext.setAlpha(0.3f);
        lessonRepository = new LessonRepository(this);
    }

    private void bindingAction() {
        btnNext.setOnClickListener(this::onBtnNextClick);
        hintButton.setOnClickListener(this::onHintButtonClick);
    }

    private void onHintButtonClick(View view) {
        String answer = vocabularyList.get(currentAnswerIndex).word;
        for (char letter : answer.toCharArray()) {
            char lowerCaseLetter = Character.toLowerCase(letter);
            if (userLetterCountMap.get(lowerCaseLetter) < letterCountMap.get(lowerCaseLetter)) {
                Toast.makeText(this, "Gợi ý: " + letter, Toast.LENGTH_SHORT).show();
                keyboard.highlightButton(lowerCaseLetter);
                break;
            }
        }
    }

    private void onBtnNextClick(View view) {
        currentAnswerIndex++;
        if (currentAnswerIndex < vocabularyList.size()) {
            userAnswer.setLength(0);
            letterCountMap.clear();
            userLetterCountMap.clear();
            keyboard.enableAllButtons();
            setupLetterCountMap();
            updateUserInputDisplay();
            disableUnusedKeyboardButtons();
            resultLayout.setVisibility(View.GONE);
            btnNext.setEnabled(false);
            btnNext.setAlpha(0.3f);
            word.setText(vocabularyList.get(currentAnswerIndex).word);
        } else {
            updateProgressOfLesson();
        }
    }

    private void loadVocabList() {
        executorService = Executors.newSingleThreadExecutor();
        int lessonId = getIntent().getIntExtra("lesson_id", -1);
        executorService.execute(() -> {
            VocabularyRepository vocabularyRepository = new VocabularyRepository(this);
            vocabularyList = vocabularyRepository.getVocabularyByLessonId(lessonId);

            runOnUiThread(() -> {
                if (vocabularyList != null && !vocabularyList.isEmpty()) {
                    progressBar.setMax(vocabularyList.size() * 3);
                    progressBar.setProgress(getIntent().getIntExtra("progress", 0));
                    resultHandler = new ResultHandler(this, progressBar, vocabularyList, pairOfWord, txtResult, resultLayout, btnNext, btnSpeaker);
                    setupLetterCountMap();
                    updateUserInputDisplay();

                } else {
                    Toast.makeText(this, "Không tìm thấy từ vựng cho bài học này!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }




    private void setupLetterCountMap() {
        if (vocabularyList == null || vocabularyList.isEmpty()) {
            return;
        }

        String answer = vocabularyList.get(currentAnswerIndex).word;
        for (char letter : answer.toLowerCase().toCharArray()) {
            letterCountMap.put(letter, letterCountMap.getOrDefault(letter, 0) + 1);
            userLetterCountMap.put(letter, 0);
        }
        disableUnusedKeyboardButtons();
    }

    private void setupKeyboardListeners() {
        keyboard.setOnKeyboardListener(new CustomKeyboard.OnKeyboardListener() {
            @Override
            public void onLetterClick(char letter) {
                keyboard.resetButtonHighlights();
                handleLetterInput(letter);
            }

            @Override
            public void onDeleteClick() {
                handleDelete();
            }
        });
    }

    private void disableUnusedKeyboardButtons() {
        for (char letter = 'a'; letter <= 'z'; letter++) {
            if (!letterCountMap.containsKey(letter)) {
                keyboard.disableButton(letter);
            }
        }
    }

    private void handleLetterInput(char letter) {
        String answer = vocabularyList.get(currentAnswerIndex).word;
        char lowerCaseLetter = Character.toLowerCase(letter);

        if (userAnswer.length() < answer.length()) {
            char expectedLetter = answer.charAt(userAnswer.length());

            if (lowerCaseLetter == Character.toLowerCase(expectedLetter)) {
                int maxAllowed = letterCountMap.get(lowerCaseLetter);
                int currentCount = userLetterCountMap.get(lowerCaseLetter);

                if (currentCount < maxAllowed) {
                    userAnswer.append(lowerCaseLetter);
                    userLetterCountMap.put(lowerCaseLetter, currentCount + 1);
                    updateUserInputDisplay();

                    if (currentCount + 1 >= maxAllowed) {
                        keyboard.disableButton(lowerCaseLetter);
                    }
                    if (userAnswer.toString().equalsIgnoreCase(answer)) {
                        Vocabulary currentVocab = vocabularyList.get(currentAnswerIndex);
                        resultHandler.checkAnswer(userAnswer.toString(), answer ,currentVocab);
                        keyboard.disableDeleteButton();
                        btnNext.setEnabled(true);
                        btnNext.setAlpha(1f);
                    }
                }
            } else {
                keyboard.highlightWrongLetter(lowerCaseLetter);
            }
        }
    }

    private void updateProgressOfLesson() {
        int lessonId = getIntent().getIntExtra("lesson_id", -1);
        // Access the lesson repository in a background thread
        executorService.execute(() -> {
            Lesson lesson = lessonRepository.getLessonById(lessonId);
            if (lesson != null) {
                lesson.setProgress(lesson.getProgress() + 1);


                VocabularyRepository vocabRepository = new VocabularyRepository(this);
                List<Vocabulary> vocabList = vocabRepository.getVocabularyByLessonId(lessonId);
                for (Vocabulary vocab : vocabList) {
                    vocabRepository.markAsLearned(vocab.vocab_id);
                }
            }

            runOnUiThread(this::finish);
        });
    }


    private void handleDelete() {
        int length = userAnswer.length();
        if (length > 0) {
            char lastChar = userAnswer.charAt(length - 1);
            userAnswer.deleteCharAt(length - 1);
            userInput.setText(userAnswer.toString());

            char lowerCaseLastChar = Character.toLowerCase(lastChar);
            if (userLetterCountMap.containsKey(lowerCaseLastChar)) {
                userLetterCountMap.put(lowerCaseLastChar, userLetterCountMap.get(lowerCaseLastChar) - 1);
                updateUserInputDisplay();
                if (userLetterCountMap.get(lowerCaseLastChar) < letterCountMap.get(lowerCaseLastChar)) {
                    keyboard.enableButton(lowerCaseLastChar);
                }
            }
        }
    }

    private void updateUserInputDisplay() {
        if (vocabularyList == null || vocabularyList.isEmpty()) {
            return;
        }

        StringBuilder displayString = new StringBuilder();
        String answer = vocabularyList.get(currentAnswerIndex).word;
        for (int i = 0; i < answer.length(); i++) {
            if (i < userAnswer.length()) {
                displayString.append(userAnswer.charAt(i));
            } else {
                displayString.append("_ ");
            }
        }
        userInput.setText(displayString.toString().trim());
    }


}
