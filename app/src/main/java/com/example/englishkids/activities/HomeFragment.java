package com.example.englishkids.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishkids.R;
import com.example.englishkids.adapter.LessonAdapter;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.repository.LessonRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LessonAdapter lessonAdapter;
    private LessonRepository lessonRepository;
    private ExecutorService executorService;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerView_lessons);
        progressBar = view.findViewById(R.id.progress_bar_home);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                updateProgressBar();
            }
        });

        // Initialize repository and load data
        lessonRepository = new LessonRepository(getContext());
        executorService = Executors.newSingleThreadExecutor();
        loadLessons();

        return view;
    }

    private void loadLessons() {
        executorService.execute(() -> {
            List<Lesson> lessons = lessonRepository.getAllLessons();

            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    lessonAdapter = new LessonAdapter(lessons, getContext());
                    recyclerView.setAdapter(lessonAdapter);
                    progressBar.setMax(Math.max(0, lessons.size() - 1));
                    progressBar.setProgress(0);
                });
            }
        });
    }

    private void updateProgressBar() {
        if (layoutManager != null && lessonAdapter != null) {
            int totalItems = lessonAdapter.getItemCount();
            if (totalItems <= 1) return;

            int position = layoutManager.findFirstCompletelyVisibleItemPosition();
            if (position != RecyclerView.NO_POSITION) {
                progressBar.setProgress(position);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}