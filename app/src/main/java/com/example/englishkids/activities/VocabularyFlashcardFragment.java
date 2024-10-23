package com.example.englishkids.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.englishkids.R;
import com.example.englishkids.entity.Vocabulary;

public class VocabularyFlashcardFragment extends Fragment {
    private Vocabulary currentVocabulary;
    private TextView txtMeaning;
    private EditText etWordInput;
    private TextView txtFeedback;
    private ImageView imgWordImage;
    private Button btnCheckAnswer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.flashcard_vocabulary, container, false);

        txtMeaning = view.findViewById(R.id.txt_meaning);
        etWordInput = view.findViewById(R.id.et_word_input);
        txtFeedback = view.findViewById(R.id.txt_feedback);
        imgWordImage = view.findViewById(R.id.img_word_image);
        btnCheckAnswer = view.findViewById(R.id.btn_check_answer);

        // Assuming currentVocabulary is passed to this fragment
        loadVocabularyData();

        btnCheckAnswer.setOnClickListener(v -> checkAnswer());

        return view;
    }

    private void loadVocabularyData() {
        if (currentVocabulary != null) {
            txtMeaning.setText(currentVocabulary.getMeaning());

            // Load the image if available
            if (currentVocabulary.getImagePath() != null && !currentVocabulary.getImagePath().isEmpty()) {
                // Load image using an image library like Glide or Picasso
                Glide.with(this).load(currentVocabulary.getImagePath()).into(imgWordImage);
            }
        }
    }

    private void checkAnswer() {
        String userInput = etWordInput.getText().toString().trim();
        if (userInput.equalsIgnoreCase(currentVocabulary.getWord())) {
            txtFeedback.setText("Correct!");
            txtFeedback.setTextColor(Color.GREEN);
        } else {
            txtFeedback.setText("Try again!");
            txtFeedback.setTextColor(Color.RED);
        }
    }
}
