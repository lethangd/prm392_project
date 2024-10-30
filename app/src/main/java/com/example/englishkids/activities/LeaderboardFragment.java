package com.example.englishkids.activities;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.englishkids.R;
import com.example.englishkids.utils.ScoreManager;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private ScoreManager scoreManager;
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scoreManager = new ScoreManager();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadLeaderboard();

        return view;
    }

    private void loadLeaderboard() {
        scoreManager.getAllUsersScores(new ScoreManager.Callback<Map<String, Integer>>() {
            @Override
            public void onSuccess(Map<String, Integer> userScores) {
                List<Map.Entry<String, Integer>> scoreList = new ArrayList<>(userScores.entrySet());
                scoreList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Sort by score

                adapter = new LeaderboardAdapter(scoreList, currentUserId);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
