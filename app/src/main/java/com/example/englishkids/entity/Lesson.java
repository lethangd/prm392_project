package com.example.englishkids.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lesson")
public class Lesson {
    @PrimaryKey(autoGenerate = true)
    public int lesson_id;

    @ColumnInfo(name = "lesson_name")
    public String lessonName;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "lesson_type")
    public String lessonType;

    @ColumnInfo(name = "study_count")
    public int studyCount =0;

    @Ignore
    private int progress;


    public Lesson(String lessonName, String description, String lessonType) {
        this.lessonName = lessonName;
        this.description = description;
        this.lessonType = lessonType;
        this.studyCount = 0;
    }

    public int getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(int studyCount) {
        this.studyCount = studyCount;
    }

    public int getLesson_id() {
        return lesson_id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }
}
