package com.example.englishkids.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishkids.R;
import com.example.englishkids.activities.LessonDetailActivity;
import com.example.englishkids.entity.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;
    private Context context;

    // Constructor to initialize lesson list and context
    public LessonAdapter(List<Lesson> lessonList, Context context) {
        this.lessonList = lessonList;
        this.context = context;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each lesson
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        // Bind data to the view holder
        Lesson lesson = lessonList.get(position);
        holder.lessonTitle.setText(lesson.getLessonName());

        // Set click listener for the "Start" button
        holder.startButton.setOnClickListener(v -> {
            // Open LessonDetailActivity and pass the selected lesson's ID
            Intent intent = new Intent(context, LessonDetailActivity.class);
            intent.putExtra("lesson_id", lesson.getLesson_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of lessons in the list
        return lessonList.size();
    }

    // View holder class to hold references to the views in each item
    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitle;
        Button startButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            startButton = itemView.findViewById(R.id.startButton);
        }
    }
}
