package com.example.englishkids.dao;

import com.example.englishkids.entity.Vocabulary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// MockVocabularyData.java
public class MockVocabularyData {
    public static List<Vocabulary> getVocabulary() {
        List<Vocabulary> vocabList = new ArrayList<>();
        vocabList.add(new Vocabulary("Cat", "Con mèo", "path/to/cat_image", "path/to/cat_audio", 1));
        vocabList.add(new Vocabulary("Cow", "Con bò", "path/to/cow_image", "path/to/cow_audio", 2));
        vocabList.add(new Vocabulary("Dog", "Con chó", "path/to/dog_image", "path/to/dog_audio", 1));
        vocabList.add(new Vocabulary("Fish", "Con cá", "path/to/fish_image", "path/to/fish_audio", 2));
        vocabList.add(new Vocabulary("Bird", "Con chim", "path/to/bird_image", "path/to/bird_audio", 1));
        // Add more vocabulary items as needed
        return vocabList;
    }
}


