package com.example.englishkids.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;

@Dao
public interface GrammarDao {

    @Insert
    void insertGrammar(Grammar grammar);

    @Insert
    void insertAll(List<Grammar> grammars);

    @Query("SELECT * FROM Grammar")
    List<Grammar> getAllGrammar();

    @Query("SELECT * FROM Grammar WHERE lesson_id = :lessonId")
    List<Grammar> getGrammarByLessonId(int lessonId);

    @Query("SELECT * FROM Grammar WHERE is_learned = 0 AND lesson_id = :lessonId")
    List<Grammar> getUnlearnedGrammar(int lessonId);

    @Query("UPDATE Grammar SET is_learned = 1 WHERE grammar_id = :grammarId")
    void markAsLearned(int grammarId);

    @Query("DELETE FROM grammar")
    void clearAll();

    @Query("SELECT * FROM Grammar WHERE grammar_id = :grammarId")
    Grammar getGrammarById(int grammarId);
    @Query("SELECT COUNT(*) FROM Grammar WHERE lesson_id = :lessonId")
    int getGrammarCountByLesson(int lessonId);


}
