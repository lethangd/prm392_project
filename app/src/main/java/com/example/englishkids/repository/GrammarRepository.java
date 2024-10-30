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
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // Tạo tài liệu gốc nếu chưa tồn tại
        Map<String, Object> userData = new HashMap<>();
        userData.put("initialized", true); // Thêm trường dummy để tài liệu tồn tại

        firestore.collection("user").document(userId)
                .set(userData, SetOptions.merge())  // Chỉ thêm nếu chưa tồn tại
                .addOnSuccessListener(aVoid -> {
                    // Sau đó thêm vào subcollection learnedGrammar
                    Map<String, Object> grammarData = new HashMap<>();
                    grammarData.put("learned", true);

                    firestore.collection("user").document(userId)
                            .collection("learnedGrammar").document(String.valueOf(grammarId))
                            .set(grammarData)
                            .addOnSuccessListener(v -> Log.d("Firestore", "Grammar progress saved"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Failed to save grammar progress", e));
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Failed to initialize user document", e));
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
