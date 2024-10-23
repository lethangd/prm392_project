package com.example.englishkids.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.englishkids.R;
import com.example.englishkids.adapter.LessonAdapter;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.repository.LessonRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PracticeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LessonAdapter lessonAdapter;
    private LessonRepository lessonRepository;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewPractice);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        lessonRepository = new LessonRepository(getContext());
        executorService = Executors.newSingleThreadExecutor();

        // Fetch all previously learned lessons in a background thread
        executorService.execute(() -> {
            List<Lesson> lessons = lessonRepository.getAllLessons();
            // Update the RecyclerView on the main thread
            getActivity().runOnUiThread(() -> {
                lessonAdapter = new LessonAdapter(lessons, getContext());
                recyclerView.setAdapter(lessonAdapter);
            });
        });

        return view;
    }
}
