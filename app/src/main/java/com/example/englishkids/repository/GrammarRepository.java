package com.example.englishkids.repository;


import android.content.Context;
import android.os.AsyncTask;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.GrammarDao;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.Vocabulary;

import java.util.List;

public class GrammarRepository {
    private GrammarDao grammarDao;

    public GrammarRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        grammarDao = db.grammarDao();
    }

    public List<Grammar> getAllGrammar() {
        return grammarDao.getAllGrammar();
    }

    public void insertGrammar(Grammar grammar) {
        new InsertGrammarAsyncTask(grammarDao).execute(grammar);
    }

    public List<Grammar> getGrammarByLessonId(int lessonId) {
        return grammarDao.getGrammarByLessonId(lessonId);
    }
    public List<Grammar> getUnlearnedGrammar(int lessonId) {
        return  grammarDao.getUnlearnedGrammar(lessonId);
    }
    private static class InsertGrammarAsyncTask extends AsyncTask<Grammar, Void, Void> {
        private GrammarDao grammarDao;

        private InsertGrammarAsyncTask(GrammarDao grammarDao) {
            this.grammarDao = grammarDao;
        }

        @Override
        protected Void doInBackground(Grammar... grammars) {
            grammarDao.insertGrammar(grammars[0]);
            return null;
        }
    }
}
