package com.example.englishkids.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Vocabulary")
public class Vocabulary {
    @PrimaryKey(autoGenerate = true)
    public int vocab_id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "meaning")
    public String meaning;

    @ColumnInfo(name = "image_path")
    public String imagePath;

    @ColumnInfo(name = "audio_path")
    public String audioPath;

    @ColumnInfo(name = "lesson_id")
    public int lessonId;

    public Vocabulary(String word, String meaning, String imagePath, String audioPath, int lessonId) {
        this.word = word;
        this.meaning = meaning;
        this.imagePath = imagePath;
        this.audioPath = audioPath;
        this.lessonId = lessonId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }
}