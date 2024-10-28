package com.example.englishkids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishkids.R;

import java.util.Objects;

public class LearningTarget extends AppCompatActivity {

    private Button btnTravel, btnCareer, btnExam, btnMedia, btnFun, btnNext;
    private ProgressBar progressBarTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_learning_target);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindingView();
        bindingAction();
        initProgressBar();
    }

    private void initProgressBar() {
        progressBarTarget.setMax(3);
        progressBarTarget.setProgress(1);
    }

    private void bindingView() {
        btnTravel = findViewById(R.id.btn_travel);
        btnCareer = findViewById(R.id.btn_career);
        btnExam = findViewById(R.id.btn_exam);
        btnMedia = findViewById(R.id.btn_media);
        btnFun = findViewById(R.id.btn_fun);
        btnNext = findViewById(R.id.btn_next_target);
        progressBarTarget = findViewById(R.id.progress_bar_target);
        btnNext.setVisibility(View.GONE);
    }

    private void bindingAction() {
        btnTravel.setOnClickListener(this::onOptionClick);
        btnCareer.setOnClickListener(this::onOptionClick);
        btnExam.setOnClickListener(this::onOptionClick);
        btnMedia.setOnClickListener(this::onOptionClick);
        btnFun.setOnClickListener(this::onOptionClick);
        btnNext.setOnClickListener(this::switchNextActivity);
    }

    private void onOptionClick(View view) {
        Button selectedButton = (Button) view;
        progressBarTarget.setProgress(2);
        if (Objects.equals(selectedButton.getBackground().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.button_oval_pressed)).getConstantState())) {
            selectedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_oval));
        } else {
            selectedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_oval_pressed));
        }

        if (isAnyButtonChecked()) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.GONE);
        }
    }

    private boolean isAnyButtonChecked() {
        return btnTravel.getBackground().getConstantState().equals(
                ContextCompat.getDrawable(this, R.drawable.button_oval_pressed).getConstantState()) ||
                btnCareer.getBackground().getConstantState().equals(
                        ContextCompat.getDrawable(this, R.drawable.button_oval_pressed).getConstantState()) ||
                btnExam.getBackground().getConstantState().equals(
                        ContextCompat.getDrawable(this, R.drawable.button_oval_pressed).getConstantState()) ||
                btnMedia.getBackground().getConstantState().equals(
                        ContextCompat.getDrawable(this, R.drawable.button_oval_pressed).getConstantState()) ||
                btnFun.getBackground().getConstantState().equals(
                        ContextCompat.getDrawable(this, R.drawable.button_oval_pressed).getConstantState());
    }

    private void switchNextActivity(View view) {
        Intent intent = new Intent(LearningTarget.this, TargetTime.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
