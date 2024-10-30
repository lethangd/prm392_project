package com.example.englishkids.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishkids.R;

public class WelcomeScreen extends AppCompatActivity {

    private Button btnStart, btnLogin;

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
        showLoginBottomSheet();
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

    private void showLoginBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_login, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        bottomSheetView.findViewById(R.id.btn_login_google).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.btn_login_facebook).setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
        });

        bottomSheetView.findViewById(R.id.btn_login_email).setOnClickListener(v -> {
            showEmailLoginOptions();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.show();
    }



    // Method to show email login options
    private void showEmailLoginOptions() {
        BottomSheetDialog emailBottomSheetDialog = new BottomSheetDialog(this);
        View emailBottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_email_login, null);
        emailBottomSheetDialog.setContentView(emailBottomSheetView);

        emailBottomSheetView.findViewById(R.id.btn_email_login).setOnClickListener(v -> {
            emailBottomSheetDialog.dismiss();
            saveFirstLaunch();
            Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        emailBottomSheetView.findViewById(R.id.btn_email_signup).setOnClickListener(v -> {
            emailBottomSheetDialog.dismiss();
            saveFirstLaunch();
            Intent intent = new Intent(WelcomeScreen.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        emailBottomSheetDialog.show();
    }
    private void saveFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
    }
}
