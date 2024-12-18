package com.example.englishkids.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.englishkids.R;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.repository.GrammarRepository;
import com.example.englishkids.repository.VocabularyRepository;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashcardActivity extends AppCompatActivity {

    private TextView txtMeaning, txtFeedback;
    private EditText etWordInput;
    private ImageView imgWordImage;
    private Button btnCheckAnswer, btnNextFlashcard, btnUndo;
    private LinearLayout layoutSentence;
    private GridLayout layoutOptions;
    private final List<String> userSentence = new ArrayList<>();
    private List<Vocabulary> vocabularyList;
    private List<Grammar> grammarList;
    private boolean isVocabularyMode = true;
    private int currentVocabIndex = 0;
    private ExecutorService executorService;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        FirebaseApp.initializeApp(this);
        // Initialize ExecutorService
        executorService = Executors.newSingleThreadExecutor();
        int lessonId = getIntent().getIntExtra("lesson_id", -1);
        loadLessonData(lessonId);
        bindingView();
        bindingAction();
    }
    private void bindingAction() {
        btnCheckAnswer.setOnClickListener(v -> checkAnswer());
        btnNextFlashcard.setOnClickListener(v -> loadNextFlashcard());
        btnUndo.setOnClickListener(v -> undoLastWord());
        btnBack.setOnClickListener(v -> showExitConfirmation());
    }

    private void bindingView() {
        txtMeaning = findViewById(R.id.txt_meaning);
        etWordInput = findViewById(R.id.et_word_input);
        imgWordImage = findViewById(R.id.img_word_image);
        txtFeedback = findViewById(R.id.txt_feedback);
        btnCheckAnswer = findViewById(R.id.btn_check_answer);
        btnNextFlashcard = findViewById(R.id.btn_next_flashcard);
        btnUndo = findViewById(R.id.btn_undo);
        layoutOptions = findViewById(R.id.layout_options);
        layoutSentence = findViewById(R.id.layout_sentence);
        btnBack = findViewById(R.id.btn_back);
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn rời khỏi bài học?")
                .setPositiveButton("Có", (dialog, which) -> finish())
                .setNegativeButton("Không", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void loadLessonData(int lessonId) {
        executorService.execute(() -> {
            VocabularyRepository vocabularyRepository = new VocabularyRepository(this);
            GrammarRepository grammarRepository = new GrammarRepository(this);
            List<Vocabulary> vocabularyListt = vocabularyRepository.getVocabularyByLessonId(lessonId);
            vocabularyList = vocabularyRepository.getUnlearnedVocabulary(lessonId);
            grammarList = grammarRepository.getUnlearnedGrammar(lessonId);

            runOnUiThread(this::loadVocabularyFlashcard);
        });
    }

    private void loadVocabularyFlashcard() {
        if (currentVocabIndex < vocabularyList.size()) {
            Vocabulary vocab = vocabularyList.get(currentVocabIndex);
            txtMeaning.setText(vocab.getMeaning());
            etWordInput.setText("");
            txtFeedback.setText("");

            imgWordImage.setVisibility(View.VISIBLE);
            layoutOptions.setVisibility(View.GONE);
            layoutSentence.setVisibility(View.GONE);
            etWordInput.setVisibility(View.VISIBLE);
            btnCheckAnswer.setVisibility(View.VISIBLE);
            btnUndo.setVisibility(View.GONE);

            if (vocab.getImagePath() != null && !vocab.getImagePath().isEmpty()) {
                Glide.with(this)
                        .load(vocab.getImagePath())
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .into(imgWordImage);
            } else {
                imgWordImage.setImageResource(R.drawable.ic_image_placeholder);
            }
        } else {
            isVocabularyMode = false;
            currentVocabIndex = 0;
            loadGrammarFlashcard();
        }
    }


    private void loadGrammarFlashcard() {
        if (currentVocabIndex < grammarList.size()) {
            Grammar grammar = grammarList.get(currentVocabIndex);
            txtMeaning.setText("Sắp xếp câu: ");
            txtFeedback.setText("");
            layoutSentence.removeAllViews();
            userSentence.clear();

            imgWordImage.setVisibility(View.GONE);
            layoutOptions.setVisibility(View.VISIBLE);
            layoutSentence.setVisibility(View.VISIBLE);
            etWordInput.setVisibility(View.GONE);
            btnCheckAnswer.setVisibility(View.GONE);

            displayGrammarOptions(grammar.getCorrectSentence());
        } else {
            txtMeaning.setText("Hoàn thành bài học!");
            txtFeedback.setText("Chúc mừng, bạn đã hoàn thành!");
            layoutOptions.setVisibility(View.GONE);
            layoutSentence.setVisibility(View.GONE);
            btnCheckAnswer.setVisibility(View.GONE);
            etWordInput.setVisibility(View.GONE);
            btnNextFlashcard.setVisibility(View.GONE);
        }
    }

    private void displayGrammarOptions(String sentence) {
        List<String> words = new ArrayList<>(Arrays.asList(sentence.split(" ")));
        Collections.shuffle(words);

        layoutOptions.removeAllViews();
        for (String word : words) {
            Button wordButton = new Button(this);
            wordButton.setText(word);
            wordButton.setOnClickListener(v -> onWordSelected(wordButton, word));
            layoutOptions.addView(wordButton);
        }
    }

    private void onWordSelected(Button button, String word) {
        userSentence.add(word);

        TextView wordView = new TextView(this);
        wordView.setText(word);
        wordView.setPadding(8, 8, 8, 8);
        layoutSentence.addView(wordView);

        button.setEnabled(false);
        btnUndo.setVisibility(View.VISIBLE);

        if (userSentence.size() == layoutOptions.getChildCount()) {
            checkAnswer();
        }
    }

    private void undoLastWord() {
        if (!userSentence.isEmpty()) {
            String lastWord = userSentence.remove(userSentence.size() - 1);

            if (layoutSentence.getChildCount() > 0) {
                layoutSentence.removeViewAt(layoutSentence.getChildCount() - 1);
            }

            for (int i = 0; i < layoutOptions.getChildCount(); i++) {
                Button wordButton = (Button) layoutOptions.getChildAt(i);
                if (wordButton.getText().equals(lastWord)) {
                    wordButton.setEnabled(true);
                    break;
                }
            }

            if (userSentence.isEmpty()) {
                btnUndo.setVisibility(View.GONE);
            }
        }
    }

    private void checkAnswer() {
        if (isVocabularyMode) {
            String userInput = etWordInput.getText().toString().trim();
            Vocabulary currentVocab = vocabularyList.get(currentVocabIndex);

            if (userInput.equalsIgnoreCase(currentVocab.getWord())) {
                txtFeedback.setText("Chính xác!");
                txtFeedback.setTextColor(Color.GREEN);
                btnNextFlashcard.setVisibility(View.VISIBLE);

                VocabularyRepository vocabularyRepository = new VocabularyRepository(this);
                vocabularyRepository.markAsLearned(currentVocab.getVocab_id());
            } else {
                txtFeedback.setText("Sai rồi, thử lại!");
                txtFeedback.setTextColor(Color.RED);
            }
        } else {
            Grammar currentGrammar = grammarList.get(currentVocabIndex);
            String correctSentence = currentGrammar.getCorrectSentence();
            String userSentenceStr = String.join(" ", userSentence);

            if (userSentenceStr.equalsIgnoreCase(correctSentence)) {
                txtFeedback.setText("Chính xác!");
                txtFeedback.setTextColor(Color.GREEN);
                btnNextFlashcard.setVisibility(View.VISIBLE);

                // Mark grammar as learned
                GrammarRepository grammarRepository = new GrammarRepository(this);
                grammarRepository.markAsLearned(currentGrammar.getGrammar_id());
            } else {
                txtFeedback.setText("Sai rồi, thử lại!");
                txtFeedback.setTextColor(Color.RED);
            }
        }
    }


    private void loadNextFlashcard() {
        currentVocabIndex++;
        txtFeedback.setText("");
        btnNextFlashcard.setVisibility(View.GONE);
        btnUndo.setVisibility(View.GONE);

        if (isVocabularyMode) {
            loadVocabularyFlashcard();
        } else {
            loadGrammarFlashcard();
        }
    }
}
