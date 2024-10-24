package com.example.englishkids.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.englishkids.entity.Vocabulary;

import java.util.List;

@Dao
public interface VocabularyDao {

    @Insert
    void insertVocabulary(Vocabulary vocabulary);

    @Insert
    void insertAll(List<Vocabulary> vocabularies);

    @Query("SELECT * FROM Vocabulary")
    List<Vocabulary> getAllVocabulary();

    @Query("SELECT * FROM Vocabulary WHERE lesson_id = :lessonId")
    List<Vocabulary> getVocabularyByLessonId(int lessonId);
}
