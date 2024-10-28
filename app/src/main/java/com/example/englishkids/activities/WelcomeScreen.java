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

public class WelcomeScreen extends AppCompatActivity {

    private Button btnStart,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();
    }

    private void bindingAction() {
        btnStart.setOnClickListener(this::onStartClick);
        btnLogin.setOnClickListener(this::onLoginClick);
    }

    private void onLoginClick(View view) {

    }

    private void onStartClick(View view) {
        Intent intent = new Intent(WelcomeScreen.this, LevelSelection.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void bindingView() {
        btnStart = findViewById(R.id.btn_continue_welcome);
        btnLogin = findViewById(R.id.btn_have_account);
    }
}