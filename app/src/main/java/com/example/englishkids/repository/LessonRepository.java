package com.example.englishkids.repository;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.LessonDao;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LessonRepository {

    private LessonDao lessonDao;
    private final ExecutorService executorService;

    public LessonRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        executorService = Executors.newSingleThreadExecutor();
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
    public void getLessonProgress(int lessonId, OnResultListener<Integer> listener) {
        executorService.execute(() -> {
            int progress = lessonDao.getLessonProgress(lessonId);
            new Handler(Looper.getMainLooper()).post(() -> listener.onResult(progress));
        });
    }


    public interface OnResultListener<T> {
        void onResult(T result);
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