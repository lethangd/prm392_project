package com.example.englishkids.utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ScoreManager {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public ScoreManager() {
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    // Interface for callback handling
    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    /**
     * Fetches the scores of all users in the userProgress collection by counting the learned items
     * in each user's learnedGrammar and learnedVocabulary collections. Returns a map of user names and scores.
     */
    public void getAllUsersScores(Callback<Map<String, Integer>> callback) {
        db.collection("user")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Map<String, Integer> userScores = new HashMap<>();

                        for (QueryDocumentSnapshot userDoc : task.getResult()) {
                            String userId = userDoc.getId();

                            final String userName = userDoc.getString("userName") != null
                                    ? userDoc.getString("userName")
                                    : ("Người dùng ẩn danh");
                            Log.d("Firestore", "User: " + userId  + " - " + userName);
                            CollectionReference learnedVocabulary = userDoc.getReference().collection("learnedVocabulary");
                            CollectionReference learnedGrammar = userDoc.getReference().collection("learnedGrammar");

                            // Count learned vocab and grammar for each user
                            learnedVocabulary.get().addOnSuccessListener(vocabSnapshot -> {
                                int vocabCount = vocabSnapshot.size();

                                learnedGrammar.get().addOnSuccessListener(grammarSnapshot -> {
                                    int grammarCount = grammarSnapshot.size();
                                    int totalScore = vocabCount + grammarCount;
                                    userScores.put(userName, totalScore); // Use userName as key instead of userId

                                    // Callback when the last document has been processed
                                    if (userScores.size() == task.getResult().size()) {
                                        callback.onSuccess(userScores);
                                    }
                                });
                            });
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
}
