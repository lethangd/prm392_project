package com.example.englishkids.repository;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.GrammarDao;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.GrammarProgress;
import com.example.englishkids.entity.Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GrammarRepository {
    private GrammarDao grammarDao;
    private final ExecutorService executorService;
    private FirebaseFirestore firestore;
    private String userId;

    public GrammarRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        grammarDao = db.grammarDao();
        executorService = Executors.newSingleThreadExecutor();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();
    }

    public List<Grammar> getAllGrammar() {
        return grammarDao.getAllGrammar();
    }

    public void insertGrammar(Grammar grammar) {
        new InsertGrammarAsyncTask(grammarDao).execute(grammar);
    }

    public List<Grammar> getGrammarByLessonId(int lessonId) {
        return grammarDao.getGrammarByLessonId(lessonId);
    }

    public void markAsLearned(int grammarId) {
        executorService.execute(() -> grammarDao.markAsLearned(grammarId));
        firestore.collection("userProgress").document(userId)
                .collection("learnedGrammar").document(String.valueOf(grammarId))
                .set(new GrammarProgress(true))
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Grammar progress saved"))
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to save progress", e));
    }
    public List<Grammar> getUnlearnedGrammar(int lessonId) {
        return  grammarDao.getUnlearnedGrammar(lessonId);
    }
    private static class InsertGrammarAsyncTask extends AsyncTask<Grammar, Void, Void> {
        private GrammarDao grammarDao;

        private InsertGrammarAsyncTask(GrammarDao grammarDao) {
            this.grammarDao = grammarDao;
        }

        @Override
        protected Void doInBackground(Grammar... grammars) {
            grammarDao.insertGrammar(grammars[0]);
            return null;
        }
    }
}
