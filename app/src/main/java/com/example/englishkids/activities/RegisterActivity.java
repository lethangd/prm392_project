package com.example.englishkids.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.englishkids.R;
import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.UserDao;
import com.example.englishkids.entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterUsername, etRegisterPassword;
    private Button btnRegisterUser;
    private UserDao userDao;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegisterUser = findViewById(R.id.btnRegisterUser);

        // Khởi tạo Room Database
        AppDatabase db = AppDatabase.getInstance(this);
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();

        // Xử lý đăng ký
        btnRegisterUser.setOnClickListener(view -> {
            String username = etRegisterUsername.getText().toString();
            String password = etRegisterPassword.getText().toString();
            register(username, password);
        });
    }

    private void register(String username, String password) {
        executorService.execute(() -> {
            User newUser = new User(username, password);
            userDao.insertUser(newUser);
            runOnUiThread(() -> {
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại màn hình login
            });
        });
    }
}