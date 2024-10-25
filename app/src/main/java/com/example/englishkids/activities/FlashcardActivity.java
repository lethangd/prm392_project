package com.example.englishkids.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.englishkids.R;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.repository.GrammarRepository;
import com.example.englishkids.repository.VocabularyRepository;

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
    private Button btnCheckAnswer, btnNextFlashcard;
    private LinearLayout layoutOptions; // Layout chứa các từ trong bài tập ghép ngữ pháp
    private int currentVocabIndex = 0;
    private List<Vocabulary> vocabularyList;
    private List<Grammar> grammarList;
    private boolean isVocabularyMode = true; // Chuyển giữa chế độ từ vựng và ngữ pháp
    private ExecutorService executorService; // Để chạy tác vụ trên background thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        executorService = Executors.newSingleThreadExecutor();

        // Khởi tạo các View
        txtMeaning = findViewById(R.id.txt_meaning);
        etWordInput = findViewById(R.id.et_word_input);
        imgWordImage = findViewById(R.id.img_word_image);
        txtFeedback = findViewById(R.id.txt_feedback);
        btnCheckAnswer = findViewById(R.id.btn_check_answer);
        btnNextFlashcard = findViewById(R.id.btn_next_flashcard);
        layoutOptions = findViewById(R.id.layout_options);

        // Sự kiện kiểm tra đáp án
        btnCheckAnswer.setOnClickListener(v -> checkAnswer());
        btnNextFlashcard.setOnClickListener(v -> loadNextFlashcard());

        int lessonId = getIntent().getIntExtra("lesson_id", -1);
        loadLessonData(lessonId); // Gọi sau khi các View được khởi tạo
    }

    private void loadLessonData(int lessonId) {
        // Thực hiện truy vấn dữ liệu trong background thread
        executorService.execute(() -> {
            VocabularyRepository vocabularyRepository = new VocabularyRepository(this);
            GrammarRepository grammarRepository = new GrammarRepository(this);

            vocabularyList = vocabularyRepository.getVocabularyByLessonId(lessonId);
            grammarList = grammarRepository.getGrammarByLessonId(lessonId);

            runOnUiThread(this::loadVocabularyFlashcard); // Sau khi có dữ liệu, bắt đầu với từ vựng
        });
    }

    private void loadVocabularyFlashcard() {
        if (currentVocabIndex < vocabularyList.size()) {
            Vocabulary vocab = vocabularyList.get(currentVocabIndex);
            txtMeaning.setText(vocab.getMeaning());
            etWordInput.setText("");
            txtFeedback.setText("");
            layoutOptions.setVisibility(View.GONE); // Ẩn layout ghép từ cho phần từ vựng
            etWordInput.setVisibility(View.VISIBLE);
            btnCheckAnswer.setVisibility(View.VISIBLE);

            // Hiển thị ảnh từ nếu có
            if (vocab.getImagePath() != null && !vocab.getImagePath().isEmpty()) {
                Glide.with(this).load(vocab.getImagePath()).into(imgWordImage);
            } else {
                imgWordImage.setImageResource(R.drawable.ic_image_placeholder);
            }
        } else {
            // Chuyển sang ngữ pháp nếu đã hết từ vựng
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
            layoutOptions.setVisibility(View.VISIBLE); // Hiển thị layout ghép từ
            etWordInput.setVisibility(View.GONE);
            btnCheckAnswer.setVisibility(View.GONE);

            // Hiển thị các từ trong câu ngữ pháp
            displayGrammarOptions(grammar.getCorrectSentence());
        } else {
            txtMeaning.setText("Hoàn thành bài học!");
            txtFeedback.setText("Chúc mừng, bạn đã hoàn thành!");
            layoutOptions.setVisibility(View.GONE);
            btnCheckAnswer.setVisibility(View.GONE);
            etWordInput.setVisibility(View.GONE);
            btnNextFlashcard.setVisibility(View.GONE);
        }
    }

    private void displayGrammarOptions(String sentence) {
        // Tách các từ và trộn chúng để làm câu đố
        List<String> words = new ArrayList<>(Arrays.asList(sentence.split(" ")));
        Collections.shuffle(words); // Trộn các từ

        layoutOptions.removeAllViews();
        for (String word : words) {
            Button wordButton = new Button(this);
            wordButton.setText(word);
            wordButton.setOnClickListener(v -> onWordSelected(wordButton, word));
            layoutOptions.addView(wordButton);
        }
    }

    private void onWordSelected(Button button, String word) {
        // Xử lý khi người dùng chọn từ trong phần ngữ pháp
        if (txtFeedback.getText().toString().isEmpty()) {
            txtFeedback.setText(word);
        } else {
            txtFeedback.append(" " + word);
        }
        button.setEnabled(false);
    }

    private void checkAnswer() {
        String userInput = etWordInput.getText().toString().trim();
        Vocabulary currentVocab = vocabularyList.get(currentVocabIndex);

        if (userInput.equalsIgnoreCase(currentVocab.getWord())) {
            txtFeedback.setText("Chính xác!");
            txtFeedback.setTextColor(Color.GREEN);
            btnNextFlashcard.setVisibility(View.VISIBLE); // Hiện nút Tiếp theo khi đúng
        } else {
            txtFeedback.setText("Sai rồi, thử lại!");
            txtFeedback.setTextColor(Color.RED);
        }
    }

    private void loadNextFlashcard() {
        currentVocabIndex++;
        txtFeedback.setText(""); // Xóa phản hồi
        btnNextFlashcard.setVisibility(View.GONE); // Ẩn nút Tiếp theo

        if (isVocabularyMode) {
            loadVocabularyFlashcard();
        } else {
            loadGrammarFlashcard();
        }
    }
}
