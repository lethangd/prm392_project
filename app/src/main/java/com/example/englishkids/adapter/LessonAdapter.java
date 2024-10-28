package com.example.englishkids.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishkids.R;
import com.example.englishkids.activities.FlashcardActivity;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.repository.LessonRepository;

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
        Lesson lesson = lessonList.get(position);
        LessonRepository lessonRepository = new LessonRepository(context);
        holder.lessonTitle.setText(lesson.getLessonName());

        // Fetch progress asynchronously and update UI accordingly
        lessonRepository.getLessonProgress(lesson.getLesson_id(), progress  -> {
                lesson.setProgress(progress); // Update the lesson object with the retrieved progress

                // Ensure UI updates run on the main thread
                ((Activity) context).runOnUiThread(() -> {
                    updateStartButton(holder, progress); // Update button text based on progress
                    disableButtonIfPreviousIncomplete(holder, position); // Disable button if previous lesson incomplete
                    updateStarRating(holder, progress); // Update star rating based on progress
                });
        });

        holder.startButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardActivity.class);
            intent.putExtra("lesson_id", lesson.getLesson_id());
            context.startActivity(intent);
        });
    }

    // Helper Method to Set Button Text
    private void updateStartButton(LessonViewHolder holder, int progress) {
        if (progress == 0) {
            holder.startButton.setText("Bắt đầu");
        } else {
            holder.startButton.setText("Tiếp tục");
        }
    }

    // Helper Method to Disable Button if Previous Lesson is Incomplete
    private void disableButtonIfPreviousIncomplete(LessonViewHolder holder, int position) {
        if (position > 0 && lessonList.get(position - 1).getProgress() == 0) {
            holder.startButton.setEnabled(false);
            holder.startButton.setAlpha(0.5f); // Visually indicate disabled state
        } else {
            holder.startButton.setEnabled(true);
            holder.startButton.setAlpha(1.0f); // Reset visual indication
        }
    }

    // Helper Method to Set Star Rating Based on Progress
    private void updateStarRating(LessonViewHolder holder, int progress) {
        holder.star1.setImageResource(progress >= 34 ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        holder.star2.setImageResource(progress >= 67 ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        holder.star3.setImageResource(progress == 100 ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
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
        ImageView star1, star2, star3;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            startButton = itemView.findViewById(R.id.startButton);
            star1 = itemView.findViewById(R.id.star1); // Update these IDs as needed
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
        }
    }
}
