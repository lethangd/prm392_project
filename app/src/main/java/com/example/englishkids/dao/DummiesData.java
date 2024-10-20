package com.example.englishkids.dao;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class DummiesData {

    public static void insertDummyData(AppDatabase db) {
        // Tạo dữ liệu mẫu cho Lesson
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("Lesson 1", "Description for Lesson 1", "Grammar"));
        lessons.add(new Lesson("Lesson 2", "Description for Lesson 2", "Vocabulary"));

        // Tạo dữ liệu mẫu cho Grammar
        List<Grammar> grammars = new ArrayList<>();
        grammars.add(new Grammar("Correct sentence example 1", "Correct sentence 1", 1));

        // Tạo dữ liệu mẫu cho Vocabulary
        List<Vocabulary> vocabularies = new ArrayList<>();
        vocabularies.add(new Vocabulary( "Word 1", "Meaning 1", "path/to/image1", "path/to/audio1", 1));

        // Thêm dữ liệu vào cơ sở dữ liệu
        db.lessonDao().insertAll(lessons);
        db.grammarDao().insertAll(grammars);
        db.vocabularyDao().insertAll(vocabularies);
    }
}
