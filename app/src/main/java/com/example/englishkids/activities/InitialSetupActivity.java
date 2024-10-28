package com.example.englishkids.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.englishkids.R;
import java.util.Calendar;

public class InitialSetupActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);
        EdgeToEdge.enable(this);
        startNextActivity();
    }

    private void saveFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
    }


    private void startNextActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(InitialSetupActivity.this, WelcomeScreen.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, 2000); // 2 seconds delay
    }
}