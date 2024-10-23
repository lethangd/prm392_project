package com.example.englishkids.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import com.example.englishkids.entity.GrammarType;

import java.util.List;

@Dao
public interface GrammarTypeDao {

    @Insert
    void insertAll(List<GrammarType> grammarTypes);

    @Query("SELECT * FROM grammar_type")
    List<GrammarType> getAllGrammarTypes();

    @Delete
    void delete(GrammarType grammarType);
}
