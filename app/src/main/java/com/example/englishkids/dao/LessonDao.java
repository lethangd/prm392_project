package com.example.englishkids.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;

@Dao
public interface LessonDao {
    @Insert
    void insertLesson(Lesson lesson);

    @Insert
    void insertAll(List<Lesson> lessons);

    @Insert
    void insertVocabulary(Vocabulary vocabulary);

    @Insert
    void insertGrammar(Grammar grammar);

    @Query("SELECT * FROM Lesson")
    List<Lesson> getAllLessons();

    @Query("SELECT * FROM Lesson WHERE lesson_id = :lessonId LIMIT 1")
    Lesson getLessonById(int lessonId);

    @Query("SELECT ((SELECT COUNT(*) FROM Vocabulary WHERE lesson_id = :lessonId AND isLearned = 1) + " +
            "(SELECT COUNT(*) FROM Grammar WHERE lesson_id = :lessonId AND is_learned = 1)) * 100 / " +
            "((SELECT COUNT(*) FROM Vocabulary WHERE lesson_id = :lessonId) + " +
            "(SELECT COUNT(*) FROM Grammar WHERE lesson_id = :lessonId)) AS progress")
    int getLessonProgress(int lessonId);


}
