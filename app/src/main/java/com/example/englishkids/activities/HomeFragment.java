package com.example.englishkids.activities;

import android.annotation.SuppressLint;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private LessonAdapter lessonAdapter;
    private LessonRepository lessonRepository;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_lessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        lessonRepository = new LessonRepository(getContext());

        List<Lesson> lessons = lessonRepository.getAllLessons();
        lessonAdapter = new LessonAdapter(lessons);
        recyclerView.setAdapter(lessonAdapter);

        return view;
    }
}
