package com.example.englishkids.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Grammar")
public class Grammar {
    @PrimaryKey(autoGenerate = true)
    public int grammar_id;

    @ColumnInfo(name = "exercise")
    public String exercise;

    @ColumnInfo(name = "correct_sentence")
    public String correctSentence;

    @ColumnInfo(name = "lesson_id")
    public int lessonId;

    public Grammar(String exercise, String correctSentence, int lessonId) {
        this.exercise = exercise;
        this.correctSentence = correctSentence;
        this.lessonId = lessonId;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
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
