package com.example.englishkids.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

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
     * Fetches the current user's score by counting the learned items in both learnedGrammar
     * and learnedVocabulary collections and returns it via a callback.
     */
    public void getCurrentUserScore(Callback<Integer> callback) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("userProgress")
                .document(userId)
                .collection("learnedGrammar")
                .get()
                .addOnCompleteListener(taskGrammar -> {
                    if (taskGrammar.isSuccessful()) {
                        int learnedGrammarCount = taskGrammar.getResult().size();

                        db.collection("userProgress")
                                .document(userId)
                                .collection("learnedVocabulary")
                                .get()
                                .addOnCompleteListener(taskVocab -> {
                                    if (taskVocab.isSuccessful()) {
                                        int learnedVocabularyCount = taskVocab.getResult().size();
                                        int totalScore = learnedGrammarCount + learnedVocabularyCount;

                                        callback.onSuccess(totalScore);
                                    } else {
                                        callback.onFailure(taskVocab.getException());
                                    }
                                });
                    } else {
                        callback.onFailure(taskGrammar.getException());
                    }
                });
    }

    /**
     * Fetches the scores of all users in the userProgress collection by counting the learned items
     * in each user's learnedGrammar and learnedVocabulary collections. Returns a map of user IDs and scores.
     */
    public void getAllUsersScores(Callback<Map<String, Integer>> callback) {
        db.collection("userProgress")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String, Integer> userScores = new HashMap<>();

                        for (DocumentSnapshot userDoc : task.getResult()) {
                            String userId = userDoc.getId();

                            db.collection("userProgress")
                                    .document(userId)
                                    .collection("learnedGrammar")
                                    .get()
                                    .addOnCompleteListener(taskGrammar -> {
                                        if (taskGrammar.isSuccessful()) {
                                            int learnedGrammarCount = taskGrammar.getResult().size();

                                            db.collection("userProgress")
                                                    .document(userId)
                                                    .collection("learnedVocabulary")
                                                    .get()
                                                    .addOnCompleteListener(taskVocab -> {
                                                        if (taskVocab.isSuccessful()) {
                                                            int learnedVocabularyCount = taskVocab.getResult().size();
                                                            int totalScore = learnedGrammarCount + learnedVocabularyCount;

                                                            userScores.put(userId, totalScore);

                                                            // Check if all users' scores have been fetched
                                                            if (userScores.size() == task.getResult().size()) {
                                                                callback.onSuccess(userScores);
                                                            }
                                                        } else {
                                                            callback.onFailure(taskVocab.getException());
                                                        }
                                                    });
                                        } else {
                                            callback.onFailure(taskGrammar.getException());
                                        }
                                    });
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }
}
