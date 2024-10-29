package com.example.englishkids.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
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
import com.example.englishkids.activities.VocabularyActivity;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.repository.LessonRepository;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;
    private Context context;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable starAnimationRunnable;

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
        holder.lessonTitle.setText(lesson.getDescription());
        holder.lessonNumber.setText(lesson.getLessonName());

        lessonRepository.getLessonProgress(lesson.getLesson_id(), progress -> {
            lesson.setProgress(progress);

            ((Activity) context).runOnUiThread(() -> {
                updateStartButton(holder, progress);
                disableButtonIfPreviousIncomplete(holder, position);

                if (progress > 0) {
                    updateStarRating(holder, 2,position);
                } else {
                    updateStarRating(holder, lesson.getStudyCount(),position);
                }
            });
        });

        holder.startButton.setOnClickListener(v -> {
            Intent intent;
            if (lesson.getStudyCount() > 0) {
                intent = new Intent(context, FlashcardActivity.class);
            } else {
                intent = new Intent(context, VocabularyActivity.class);
            }
            intent.putExtra("lesson_id", lesson.getLesson_id());
            context.startActivity(intent);
        });
    }

    private void animateStartButton(Button startButton) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startButton.animate().scaleX(1.2f).scaleY(1.2f).alpha(0.8f).setDuration(1000)
                        .withEndAction(() -> startButton.animate().scaleX(1f).scaleY(1f)
                                .alpha(1f)
                                .setDuration(1000));

                handler.postDelayed(this, 5000);
            }
        };
        runnable.run();
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
            holder.startButton.setAlpha(0.5f);
            holder.starButtonBg.setVisibility(View.GONE);
        } else {
            holder.startButton.setEnabled(true);
            holder.startButton.setAlpha(1.0f); // Reset visual indication
            holder.starButtonBg.setVisibility(View.VISIBLE);
            animateStartButton(holder.starButtonBg);
        }
    }

    // Helper Method to Set Star Rating Based on Progress
    private void updateStarRating(LessonViewHolder holder, int progress, int position) {
        holder.star1.setImageResource(progress >= 1 ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        holder.star2.setImageResource(progress >= 2 ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        holder.star3.setImageResource(progress >= 3 ? R.drawable.ic_star_filled : R.drawable.ic_star_empty);
        if (position > 0 && lessonList.get(position - 1).getProgress() == 0) {
            return;
        }
        else if (progress == 0) {
            startAnimatingStar(holder.star1);
        } else if (progress == 1) {
            startAnimatingStar(holder.star2);
        } else if (progress == 2) {
            startAnimatingStar(holder.star3);
        }
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView lessonTitle,lessonNumber;
        Button startButton, starButtonBg;
        ImageView star1, star2, star3;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonNumber = itemView.findViewById(R.id.lessonNumber);
            lessonTitle = itemView.findViewById(R.id.lessonTitle);
            startButton = itemView.findViewById(R.id.startButton);
            starButtonBg = itemView.findViewById(R.id.startButtonbg);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
        }
    }
    private void startAnimatingStar(final ImageView star) {
        starAnimationRunnable = new Runnable() {
            @Override
            public void run() {
                animateStar(star);
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(starAnimationRunnable);
    }
    private void animateStar(ImageView star) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(star, "scaleX", 1f, 1.5f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(star, "scaleY", 1f, 1.5f);

        ObjectAnimator rotate = ObjectAnimator.ofFloat(star, "rotation", 0f, -30f);
        ObjectAnimator rotateRollback = ObjectAnimator.ofFloat(star, "rotation", -30f, 30f);
        ObjectAnimator rotateReverse = ObjectAnimator.ofFloat(star, "rotation", 30f, 0f);

        ObjectAnimator scaleXBack = ObjectAnimator.ofFloat(star, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleYBack = ObjectAnimator.ofFloat(star, "scaleY", 1.5f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY,rotate);
        animatorSet.setDuration(800);

        AnimatorSet animatorSetRollback = new AnimatorSet();
        animatorSetRollback.playTogether(scaleXBack, scaleYBack,rotateReverse);
        animatorSetRollback.setDuration(600);

        AnimatorSet animatorSetReverse = new AnimatorSet();
        animatorSetReverse.playTogether(rotateRollback);
        animatorSetReverse.setDuration(800);

        AnimatorSet finalAnimatorSet = new AnimatorSet();
        finalAnimatorSet.playSequentially(animatorSet, animatorSetReverse,animatorSetRollback);
        finalAnimatorSet.start();
    }



}
