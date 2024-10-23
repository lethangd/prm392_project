package com.example.englishkids.dao;

import android.util.Log;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.GrammarType;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;

import java.util.ArrayList;
import java.util.List;

public class DummiesData {

    public static void insertDummyData(AppDatabase db) {
        Log.d("DummiesData", "Inserting dummy data...");

        // Kiểm tra xem đã có dữ liệu trong bảng GrammarType chưa
        if (db.grammarTypeDao().getAllGrammarTypes().isEmpty()) {
            // Create dummy grammar types
            List<GrammarType> grammarTypes = new ArrayList<>();
            grammarTypes.add(new GrammarType(1, "Simple Present", "Thì hiện tại đơn"));
            grammarTypes.add(new GrammarType(2, "Present Continuous", "Thì hiện tại tiếp diễn"));
            grammarTypes.add(new GrammarType(3, "Past Simple", "Thì quá khứ đơn"));
            grammarTypes.add(new GrammarType(4, "Future Simple", "Thì tương lai đơn"));

            // Insert grammar types into the database
            db.grammarTypeDao().insertAll(grammarTypes);
        }

        // Kiểm tra xem đã có dữ liệu trong bảng Lesson chưa
        if (db.lessonDao().getAllLessons().isEmpty()) {
            // Create dummy lessons
            List<Lesson> lessons = new ArrayList<>();
            lessons.add(new Lesson("Lesson 1", "Thì hiện tại đơn", "Grammar"));
            lessons.add(new Lesson("Lesson 2", "Thì hiện tại tiếp diễn", "Grammar"));
            lessons.add(new Lesson("Lesson 3", "Thì quá khứ đơn", "Grammar"));
            lessons.add(new Lesson("Lesson 4", "Thì tương lai đơn", "Grammar"));

            // Insert lessons into the database
            db.lessonDao().insertAll(lessons);
        }

        // Kiểm tra xem đã có dữ liệu trong bảng Grammar chưa
        if (db.grammarDao().getAllGrammar().isEmpty()) {
            // Create dummy grammar entries
            List<Grammar> grammars = new ArrayList<>();
            grammars.add(new Grammar(1, "I eat breakfast every day.", 1)); // Simple Present
            grammars.add(new Grammar(1, "She reads a book.", 1)); // Simple Present
            grammars.add(new Grammar(2, "They are playing soccer.", 2)); // Present Continuous
            grammars.add(new Grammar(2, "He is studying now.", 2)); // Present Continuous
            grammars.add(new Grammar(3, "I visited my grandmother last week.", 3)); // Past Simple
            grammars.add(new Grammar(3, "She watched a movie yesterday.", 3)); // Past Simple
            grammars.add(new Grammar(4, "I will travel to Japan next year.", 4)); // Future Simple
            grammars.add(new Grammar(4, "We will have a meeting tomorrow.", 4)); // Future Simple

            // Insert grammars into the database
            db.grammarDao().insertAll(grammars);
        }

        // Kiểm tra xem đã có dữ liệu trong bảng Vocabulary chưa
        if (db.vocabularyDao().getAllVocabulary().isEmpty()) {
            // Create dummy vocabulary entries
            List<Vocabulary> vocabularies = new ArrayList<>();
            vocabularies.add(new Vocabulary("Apple", "Quả táo", "path/to/image1", "path/to/audio1", 1));
            vocabularies.add(new Vocabulary("Run", "Chạy", "path/to/image2", "path/to/audio2", 2));
            vocabularies.add(new Vocabulary("Study", "Học", "path/to/image3", "path/to/audio3", 2));
            vocabularies.add(new Vocabulary("Eat", "Ăn", "path/to/image4", "path/to/audio4", 1));
            vocabularies.add(new Vocabulary("Dog", "Con chó", "path/to/image5", "path/to/audio5", 1));
            vocabularies.add(new Vocabulary("Play", "Chơi", "path/to/image6", "path/to/audio6", 2));
            vocabularies.add(new Vocabulary("Book", "Cuốn sách", "path/to/image7", "path/to/audio7", 1));
            vocabularies.add(new Vocabulary("Car", "Xe hơi", "path/to/image8", "path/to/audio8", 2));
            vocabularies.add(new Vocabulary("Teacher", "Giáo viên", "path/to/image9", "path/to/audio9", 2));
            vocabularies.add(new Vocabulary("Friend", "Bạn bè", "path/to/image10", "path/to/audio10", 1));

            // Insert vocabularies into the database
            db.vocabularyDao().insertAll(vocabularies);
        }

        // Verify data was added successfully
        List<Lesson> addedLessons = db.lessonDao().getAllLessons();
        List<Grammar> addedGrammars = db.grammarDao().getAllGrammar();
        List<Vocabulary> addedVocabularies = db.vocabularyDao().getAllVocabulary();

        Log.d("DummiesData", "Lessons count: " + addedLessons.size());
        Log.d("DummiesData", "Grammars count: " + addedGrammars.size());
        Log.d("DummiesData", "Vocabularies count: " + addedVocabularies.size());
    }
}
