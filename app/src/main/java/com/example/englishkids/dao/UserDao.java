package com.example.englishkids.dao;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.englishkids.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User login(String username, String password);
}