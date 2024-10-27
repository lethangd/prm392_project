package com.example.englishkids.dao;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.GrammarType;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DummiesData {

    private static final String TAG = "DummiesData";
    private static final ExecutorService executorService= Executors.newSingleThreadExecutor();

    public static void insertDummyData(AppDatabase db) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Step 1: Insert initial dummy data if tables are empty
        Log.d(TAG, "Inserting dummy data...");

        if (db.grammarTypeDao().getAllGrammarTypes().isEmpty()) {
            insertGrammarTypes(db);
        }

        if (db.lessonDao().getAllLessons().isEmpty()) {
            insertLessons(db);
        }

        if (db.grammarDao().getAllGrammar().isEmpty()) {
            insertGrammars(db);
        }

        if (db.vocabularyDao().getAllVocabulary().isEmpty()) {
            insertVocabulary(db);
        }

        // Step 2: Sync learned data from Firestore
        syncLearnedDataFromFirestore(db, firestore, userId);
    }

    private static void insertGrammarTypes(AppDatabase db) {
        List<GrammarType> grammarTypes = new ArrayList<>();
        grammarTypes.add(new GrammarType(1, "Simple Present", "Thì hiện tại đơn"));
        grammarTypes.add(new GrammarType(2, "Present Continuous", "Thì hiện tại tiếp diễn"));
        grammarTypes.add(new GrammarType(3, "Past Simple", "Thì quá khứ đơn"));
        grammarTypes.add(new GrammarType(4, "Future Simple", "Thì tương lai đơn"));
        db.grammarTypeDao().insertAll(grammarTypes);
    }

    private static void insertLessons(AppDatabase db) {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("Lesson 1", "Thì hiện tại đơn", "Grammar"));
        lessons.add(new Lesson("Lesson 2", "Thì hiện tại tiếp diễn", "Grammar"));
        lessons.add(new Lesson("Lesson 3", "Thì quá khứ đơn", "Grammar"));
        lessons.add(new Lesson("Lesson 4", "Thì tương lai đơn", "Grammar"));
        db.lessonDao().insertAll(lessons);
    }

    private static void insertGrammars(AppDatabase db) {
        List<Grammar> grammars = new ArrayList<>();
        grammars.add(new Grammar(1, "I eat breakfast every day.", 1, false));
        grammars.add(new Grammar(1, "She reads a book.", 1, false));
        grammars.add(new Grammar(2, "They are playing soccer.", 2, false));
        grammars.add(new Grammar(2, "He is studying now.", 2, false));
        grammars.add(new Grammar(3, "I visited my grandmother last week.", 3, false));
        grammars.add(new Grammar(3, "She watched a movie yesterday.", 3, false));
        grammars.add(new Grammar(4, "I will travel to Japan next year.", 4, false));
        grammars.add(new Grammar(4, "We will have a meeting tomorrow.", 4, false));
        db.grammarDao().insertAll(grammars);
    }

    private static void insertVocabulary(AppDatabase db) {
        List<Vocabulary> vocabularies = new ArrayList<>();
        vocabularies.add(new Vocabulary("Apple", "Quả táo", "path/to/image1", 1, false));
        vocabularies.add(new Vocabulary("Run", "Chạy", "path/to/image2", 2, false));
        vocabularies.add(new Vocabulary("Study", "Học", "path/to/image3", 2, false));
        vocabularies.add(new Vocabulary("Eat", "Ăn", "path/to/image4", 1, false));
        vocabularies.add(new Vocabulary("Dog", "Con chó", "path/to/image5", 1, false));
        vocabularies.add(new Vocabulary("Play", "Chơi", "path/to/image6", 2, false));
        vocabularies.add(new Vocabulary("Book", "Cuốn sách", "path/to/image7", 1, false));
        vocabularies.add(new Vocabulary("Car", "Xe hơi", "path/to/image8", 2, false));
        vocabularies.add(new Vocabulary("Teacher", "Giáo viên", "path/to/image9", 2, false));
        vocabularies.add(new Vocabulary("Friend", "Bạn bè", "path/to/image10", 1, false));
        db.vocabularyDao().insertAll(vocabularies);
    }

    private static void syncLearnedDataFromFirestore(AppDatabase db, FirebaseFirestore firestore, String userId) {
        // Sync learned Vocabulary
        firestore.collection("userProgress").document(userId).collection("learnedVocabulary")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        int vocabId = Integer.parseInt(document.getId());
                        executorService.execute(() -> {
                            db.vocabularyDao().markAsLearned(vocabId);
                        });

                    }
                    Log.d(TAG, "Vocabulary progress synced from Firestore.");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error syncing vocabulary progress", e));

        // Sync learned Grammar
        firestore.collection("userProgress").document(userId).collection("learnedGrammar")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        int grammarId = Integer.parseInt(document.getId());
                        executorService.execute(() -> {
                            db.grammarDao().markAsLearned(grammarId);
                        });

                    }
                    Log.d(TAG, "Grammar progress synced from Firestore.");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error syncing grammar progress", e));
    }
}
