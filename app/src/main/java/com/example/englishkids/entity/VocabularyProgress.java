package com.example.englishkids.entity;

public class VocabularyProgress {
    private boolean isLearned;

    public VocabularyProgress(boolean isLearned) {
        this.isLearned = isLearned;
    }

    public boolean isLearned() {
        return isLearned;
    }
}