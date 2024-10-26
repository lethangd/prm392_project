package com.example.englishkids.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishkids.R;
import com.example.englishkids.dao.MockGrammarData;

import java.util.ArrayList;
import java.util.List;

public class GrammarActivity extends AppCompatActivity {

    private TextView txtInstruction, txtFeedbackGrammar;
    private LinearLayout layoutSentence;
    private GridLayout gridOptions;
    private List<String> sentenceParts;  // Holds correct sentence parts
    private List<String> userSelection = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

    }

    private void bindingAction() {
    }

    private void bindingView() {
        
    }

    private void loadGrammarExercise(List<String> sentenceParts) {
        // Display sentence parts as buttons for user selection
        for (String word : sentenceParts) {
            Button wordButton = new Button(this);
            wordButton.setText(word);
            wordButton.setOnClickListener(this::onWordSelected);
            gridOptions.addView(wordButton);
        }
    }

    private void onWordSelected(View view) {
        Button selectedButton = (Button) view;
        String selectedWord = selectedButton.getText().toString();

        // Add word to user's sentence arrangement
        userSelection.add(selectedWord);

        // Display the current sentence arrangement
        TextView wordView = new TextView(this);
        wordView.setText(selectedWord);
        layoutSentence.addView(wordView);

        // Disable the selected button
        selectedButton.setEnabled(false);

        // Check if the user has selected all words
        if (userSelection.size() == sentenceParts.size()) {
            checkGrammarAnswer();
        }
    }

    private void checkGrammarAnswer() {
        if (userSelection.equals(sentenceParts)) {
            txtFeedbackGrammar.setText("Xuất sắc!");
            txtFeedbackGrammar.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            txtFeedbackGrammar.setText("Sai rồi!");
            txtFeedbackGrammar.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        // Reset after feedback
        new Handler().postDelayed(this::resetExercise, 2000);
    }

    private void resetExercise() {
        layoutSentence.removeAllViews();
        gridOptions.removeAllViews();
        userSelection.clear();

        // Reload sentence parts
        loadGrammarExercise(sentenceParts);
    }
    private List<String> getSentencePartsForLesson() {
        // Fetch the correct sentence parts for the lesson
        return MockGrammarData.getSentenceParts();
    }
}
