package com.example.englishkids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishkids.R;

public class LevelSelection extends AppCompatActivity {

    private Button btnLevelOne, btnLevelTwo, btnLevelThree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_level_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();

    }

    private void bindingAction() {
        btnLevelOne.setOnClickListener(this::switchNextActivity);
        btnLevelTwo.setOnClickListener(this::switchNextActivity);
        btnLevelThree.setOnClickListener(this::switchNextActivity);
    }


    private void switchNextActivity(View view){
        changeUI((Button) view);
        Intent intent = new Intent(LevelSelection.this, LearningTarget.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void changeUI(Button button) {
        button.setBackgroundResource(R.drawable.button_oval_pressed);
    }


    private void bindingView() {
        btnLevelOne = findViewById(R.id.btn_level_one);
        btnLevelTwo = findViewById(R.id.btn_level_two);
        btnLevelThree = findViewById(R.id.btn_level_three);
    }
}