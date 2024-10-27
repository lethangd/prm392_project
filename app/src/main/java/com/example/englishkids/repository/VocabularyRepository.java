package com.example.englishkids.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.VocabularyDao;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.entity.VocabularyProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VocabularyRepository {
    private VocabularyDao vocabularyDao;
    private final ExecutorService executorService;
    private FirebaseFirestore firestore;
    private String userId;

    public VocabularyRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        vocabularyDao = db.vocabularyDao();
        executorService = Executors.newSingleThreadExecutor();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
    }

    public List<Vocabulary> getAllVocabulary() {
        return vocabularyDao.getAllVocabulary();
    }

    public void insertVocabulary(Vocabulary vocabulary) {
        new InsertVocabularyAsyncTask(vocabularyDao).execute(vocabulary);
    }

    public List<Vocabulary> getVocabularyByLessonId(int lessonId) {
        return vocabularyDao.getVocabularyByLessonId(lessonId);
    }

    public List<Vocabulary> getUnlearnedVocabulary(int lessonId) {
        return  vocabularyDao.getUnlearnedVocabulary(lessonId);
    }
    public void markAsLearned(int vocabId) {
        executorService.execute(() -> {
            vocabularyDao.markAsLearned(vocabId);
        });
        firestore.collection("userProgress").document(userId)
                .collection("learnedVocabulary").document(String.valueOf(vocabId))
                .set(new VocabularyProgress(true))  // or use a simple Map<String, Object>
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Vocabulary progress saved"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to save progress", e));
    }


    private static class InsertVocabularyAsyncTask extends AsyncTask<Vocabulary, Void, Void> {
        private VocabularyDao vocabularyDao;

        private InsertVocabularyAsyncTask(VocabularyDao vocabularyDao) {
            this.vocabularyDao = vocabularyDao;
        }

        @Override
        protected Void doInBackground(Vocabulary... vocabularies) {
            vocabularyDao.insertVocabulary(vocabularies[0]);
            return null;
        }
    }
}
