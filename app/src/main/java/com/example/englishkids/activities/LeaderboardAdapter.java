package com.example.englishkids.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishkids.R;

import java.util.List;
import java.util.Map;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final List<Map.Entry<String, Integer>> userScores;
    private final String currentUserId;

    public LeaderboardAdapter(List<Map.Entry<String, Integer>> userScores, String currentUserId) {
        this.userScores = userScores;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map.Entry<String, Integer> entry = userScores.get(position);
        String userId = entry.getKey();
        int score = entry.getValue();

        holder.userNameTextView.setText("User " + userId); // Display user ID or username if available
        holder.userScoreTextView.setText(String.valueOf(score));

        // Highlight current user's score
        if (userId.equals(currentUserId)) {
            holder.userNameTextView.setTextColor(holder.itemView.getContext().getColor(R.color.lightGray));
            holder.userScoreTextView.setTextColor(holder.itemView.getContext().getColor(R.color.lightGray));
        }
    }

    @Override
    public int getItemCount() {
        return userScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;
        TextView userScoreTextView;

        public ViewHolder(View view) {
            super(view);
            userNameTextView = view.findViewById(R.id.user_name);
            userScoreTextView = view.findViewById(R.id.user_score);
        }
    }
}
