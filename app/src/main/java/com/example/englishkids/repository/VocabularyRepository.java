package com.example.englishkids.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.VocabularyDao;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;

public class VocabularyRepository {
    private VocabularyDao vocabularyDao;

    public VocabularyRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        vocabularyDao = db.vocabularyDao();
    }

    public List<Vocabulary> getAllVocabulary() {
        return vocabularyDao.getAllVocabulary();
    }

    public void insertVocabulary(Vocabulary vocabulary) {
        new InsertVocabularyAsyncTask(vocabularyDao).execute(vocabulary);
    }

    public List<Vocabulary> getVocabularyByLessonId(int lessonId) {
        return vocabularyDao.getVocabularyByLessonId(lessonId);
    }

    private static class InsertVocabularyAsyncTask extends AsyncTask<Vocabulary, Void, Void> {
        private VocabularyDao vocabularyDao;

        private InsertVocabularyAsyncTask(VocabularyDao vocabularyDao) {
            this.vocabularyDao = vocabularyDao;
        }

        @Override
        protected Void doInBackground(Vocabulary... vocabularies) {
            vocabularyDao.insertVocabulary(vocabularies[0]);
            return null;
        }
    }
}
