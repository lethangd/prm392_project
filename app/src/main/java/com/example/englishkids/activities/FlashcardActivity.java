package com.example.englishkids.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.englishkids.R;
import com.example.englishkids.entity.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class FlashcardActivity extends AppCompatActivity {

    private TextView txtMeaning;
    private EditText etWordInput;
    private TextView txtFeedback;
    private ImageView imgWordImage;
    private Button btnCheckAnswer;

    // Vocabulary data (could be passed or retrieved from a database)
    private List<Vocabulary> vocabularyList;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_vocabulary);

        // Initialize views
        txtMeaning = findViewById(R.id.txt_meaning);
        etWordInput = findViewById(R.id.et_word_input);
        txtFeedback = findViewById(R.id.txt_feedback);
        imgWordImage = findViewById(R.id.img_word_image);
        btnCheckAnswer = findViewById(R.id.btn_check_answer);

        // Load vocabulary data (for demo, manually adding data here)
        loadVocabularyData();

        // Load the first word
        loadVocabularyCard(currentIndex);

        btnCheckAnswer.setOnClickListener(v -> checkAnswer());
    }

    // Simulate loading the vocabulary list (you'd usually retrieve this from a database)
    private void loadVocabularyData() {
        vocabularyList = new ArrayList<>();
        vocabularyList.add(new Vocabulary("cow", "A large farm animal", "image_path_cow", "audio_path_cow", 1));
        vocabularyList.add(new Vocabulary("apple", "A fruit that is usually red or green", "image_path_apple", "audio_path_apple", 1));
        vocabularyList.add(new Vocabulary("dog", "A domestic pet animal", "image_path_dog", "audio_path_dog", 1));
        // Add more words as needed
    }

    // Load the vocabulary flashcard
    private void loadVocabularyCard(int index) {
        if (index < vocabularyList.size()) {
            Vocabulary vocab = vocabularyList.get(index);

            // Display the meaning
            txtMeaning.setText(vocab.getMeaning());

            // Reset input and feedback
            etWordInput.setText("");
            txtFeedback.setText("");

            // Load image (assuming you use an image loader like Glide or Picasso)
            if (vocab.getImagePath() != null && !vocab.getImagePath().isEmpty()) {
                Glide.with(this).load(vocab.getImagePath()).into(imgWordImage);
            }
        } else {
            // Handle end of vocabulary list (could restart or go back to a previous screen)
            txtMeaning.setText("You've completed the vocabulary!");
            etWordInput.setVisibility(View.GONE);
            btnCheckAnswer.setVisibility(View.GONE);
            txtFeedback.setText("Well done!");
        }
    }

    // Check if the entered word is correct
    private void checkAnswer() {
        String userInput = etWordInput.getText().toString().trim();
        Vocabulary currentVocab = vocabularyList.get(currentIndex);

        if (userInput.equalsIgnoreCase(currentVocab.getWord())) {
            txtFeedback.setText("Correct!");
            txtFeedback.setTextColor(Color.GREEN);
            currentIndex++;
            loadNextFlashcard();
        } else {
            txtFeedback.setText("Try again!");
            txtFeedback.setTextColor(Color.RED);
        }
    }

    // Load the next flashcard after a delay
    private void loadNextFlashcard() {
        new Handler().postDelayed(() -> loadVocabularyCard(currentIndex), 1000);
    }
}
