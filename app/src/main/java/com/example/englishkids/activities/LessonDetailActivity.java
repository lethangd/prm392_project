package com.example.englishkids.activities;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishkids.R;
import com.example.englishkids.adapter.GrammarAdapter;
import com.example.englishkids.adapter.VocabularyAdapter;
import com.example.englishkids.entity.Vocabulary;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.repository.GrammarRepository;
import com.example.englishkids.repository.LessonRepository;
import com.example.englishkids.repository.VocabularyRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LessonDetailActivity extends AppCompatActivity {

    private TextView tvLessonName;
    private LessonRepository lessonRepository;
    private GrammarRepository grammarRepository;
    private VocabularyRepository vocabularyRepository;
    private ExecutorService executorService;
    private RecyclerView vocabRecyclerView, grammarRecyclerView;
    private VocabularyAdapter vocabularyAdapter;
    private GrammarAdapter grammarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);

        tvLessonName = findViewById(R.id.tvLessonName);
        vocabRecyclerView = findViewById(R.id.vocabRecyclerView);
        grammarRecyclerView = findViewById(R.id.grammarRecyclerView);

        vocabRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        grammarRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        lessonRepository = new LessonRepository(this);
        vocabularyRepository = new VocabularyRepository(this);
        grammarRepository = new GrammarRepository(this);
        executorService = Executors.newSingleThreadExecutor();

        // Get lesson ID from intent
        int lessonId = getIntent().getIntExtra("lesson_id", -1);
        loadLessonDetails(lessonId);  // Fetch lesson details
    }

    private void loadLessonDetails(int lessonId) {
        executorService.execute(() -> {
            List<Vocabulary> vocabList = vocabularyRepository.getVocabularyByLessonId(lessonId);
            List<Grammar> grammarList = grammarRepository.getGrammarByLessonId(lessonId);

            runOnUiThread(() -> {
                if (!vocabList.isEmpty()) {
                    vocabularyAdapter = new VocabularyAdapter(vocabList);
                    vocabRecyclerView.setAdapter(vocabularyAdapter);
                }
                if (!grammarList.isEmpty()) {
                    grammarAdapter = new GrammarAdapter(grammarList);
                    grammarRecyclerView.setAdapter(grammarAdapter);
                }
            });
        });
    }

}
