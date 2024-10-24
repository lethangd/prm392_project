package com.example.englishkids.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.GrammarType;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.User;
import com.example.englishkids.entity.Vocabulary;

@Database(entities = {User.class, Grammar.class, GrammarType.class, Vocabulary.class, Lesson.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract LessonDao lessonDao();
    public abstract GrammarDao grammarDao();
    public abstract GrammarTypeDao grammarTypeDao();
    public abstract VocabularyDao vocabularyDao();

    // Singleton instance of the database
    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "lesson_database")
                    .fallbackToDestructiveMigration() // Handle migrations, but this will delete data
                    .build();
        }
        return instance;
    }
}
