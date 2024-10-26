package com.example.englishkids.repository;


import android.content.Context;
import android.os.AsyncTask;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.LessonDao;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;

public class LessonRepository {

    private LessonDao lessonDao;

    public LessonRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        lessonDao = db.lessonDao();
    }

    public List<Lesson> getAllLessons() {
        return lessonDao.getAllLessons();
    }

    public void insertLesson(Lesson lesson) {
        new InsertLessonAsyncTask(lessonDao).execute(lesson);
    }

    public Lesson getLessonById(int lessonId) {
        return  lessonDao.getLessonById(lessonId);
    }

    public List<Vocabulary> getUnlearnedVocabulary() {
        return  lessonDao.getUnlearnedVocabulary();
    }

    private static class InsertLessonAsyncTask extends AsyncTask<Lesson, Void, Void> {
        private LessonDao lessonDao;

        private InsertLessonAsyncTask(LessonDao lessonDao) {
            this.lessonDao = lessonDao;
        }

        @Override
        protected Void doInBackground(Lesson... lessons) {
            lessonDao.insertLesson(lessons[0]);
            return null;
        }
    }
}