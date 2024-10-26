package com.example.englishkids.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Grammar")
public class Grammar {
    @PrimaryKey(autoGenerate = true)
    public int grammar_id;

    @ColumnInfo(name = "grammar_type_id")
    private int grammarTypeId;

    @ColumnInfo(name = "correct_sentence")
    public String correctSentence;

    @ColumnInfo(name = "lesson_id")
    public int lessonId;

    @ColumnInfo(name = "is_learned")
    private boolean isLearned;

    public Grammar(int grammarTypeId, String correctSentence, int lessonId, boolean isLearned) {
        this.grammarTypeId = grammarTypeId;
        this.correctSentence = correctSentence;
        this.lessonId = lessonId;
        this.isLearned = isLearned;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned; // Thêm dấu ngoặc nhọn ở đây
    } // Thêm dấu ngoặc nhọn ở đây

    public int getGrammarTypeId() {
        return grammarTypeId;
    }

    public void setGrammarTypeId(int grammarTypeId) {
        this.grammarTypeId = grammarTypeId;
    }

    public String getCorrectSentence() {
        return correctSentence;
    }

    public void setCorrectSentence(String correctSentence) {
        this.correctSentence = correctSentence;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}
