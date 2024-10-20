package com.example.englishkids.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.englishkids.R;
import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.DummiesData;
import com.example.englishkids.dao.UserDao;
import com.example.englishkids.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private UserDao userDao;
    private ExecutorService executorService;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Khởi tạo Room Database
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "user-database").build();
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();

        // Xử lý đăng nhập
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();
            login(username, password);
        });

        // Chuyển sang màn hình đăng ký
        btnRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login(String username, String password) {
        executorService.execute(() -> {
            User user = userDao.login(username, password);
            if (user != null) {
                runOnUiThread(() -> {
                    // Lưu trạng thái đăng nhập
                    SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("username", user.getUsername()); // Lưu thêm tên người dùng nếu cần
                    editor.apply();

                    // Insert dummy data on a background thread
                    executorService.execute(() -> {
                        DummiesData.insertDummyData(db);
                    });

                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Chuyển sang MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Đóng LoginActivity
                });
            } else {
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show());
            }
        });
    }


}