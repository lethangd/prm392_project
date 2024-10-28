package com.example.englishkids.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishkids.R;

public class TargetTime extends AppCompatActivity {

    private Button btnTimeOne, btnTimeTwo, btnTimeThree, btnTimeFour, btnNextTarget;
    private ProgressBar progressBarTime;
    private Button selectedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_target_time);
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
        progressBarTime.setMax(3);
        progressBarTime.setProgress(2);
    }

    private void bindingView() {
        btnTimeOne = findViewById(R.id.btn_time_one);
        btnTimeTwo = findViewById(R.id.btn_time_two);
        btnTimeThree = findViewById(R.id.btn_time_three);
        btnTimeFour = findViewById(R.id.btn_time_four);
        btnNextTarget = findViewById(R.id.btn_next_target);
        progressBarTime = findViewById(R.id.progress_bar_time);

        btnNextTarget.setVisibility(View.GONE); // Initially hide the Next button
    }

    private void bindingAction() {
        btnTimeOne.setOnClickListener(this::onOptionClick);
        btnTimeTwo.setOnClickListener(this::onOptionClick);
        btnTimeThree.setOnClickListener(this::onOptionClick);
        btnTimeFour.setOnClickListener(this::onOptionClick);
        btnNextTarget.setOnClickListener(this::switchNextActivity);
    }

    private void onOptionClick(View view) {
        resetButtonsUI();

        selectedButton = (Button) view;
        selectedButton.setBackgroundResource(R.drawable.button_oval_pressed);
        progressBarTime.setProgress(3);
        btnNextTarget.setVisibility(View.VISIBLE);
    }

    private void resetButtonsUI() {
        btnTimeOne.setBackgroundResource(R.drawable.button_oval);
        btnTimeTwo.setBackgroundResource(R.drawable.button_oval);
        btnTimeThree.setBackgroundResource(R.drawable.button_oval);
        btnTimeFour.setBackgroundResource(R.drawable.button_oval);

    }

    private void switchNextActivity(View view) {
        saveFirstLaunch();
        Intent intent = new Intent(TargetTime.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void saveFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.putString("targetTime",  selectedButton.getTag().toString());
        editor.apply();
    }
}