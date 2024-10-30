package com.example.englishkids.activities;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishkids.R;
import com.example.englishkids.adapter.LeaderboardAdapter;
import com.example.englishkids.utils.ScoreManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private LeaderboardAdapter adapter;
    private ScoreManager scoreManager;
    private FirebaseFirestore firestore;
    private String currentUserId;
    private String currentUserName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_leaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        scoreManager = new ScoreManager();
        firestore = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        checkAndPromptUserName();
        loadCurrentUserAndLeaderboard();

        return view;
    }

    private void loadCurrentUserAndLeaderboard() {
        FirebaseFirestore.getInstance()
                .collection("user") // Adjust to the Firestore path where usernames are stored
                .document(currentUserId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUserName = documentSnapshot.getString("userName"); // Retrieve "username" field
                        loadLeaderboard(); // Proceed to load leaderboard after fetching username
                    } else {
                        Log.e("Firestore", "Username not found for current user.");
                        Toast.makeText(getContext(), "Failed to load username.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching username", e);
                    Toast.makeText(getContext(), "Failed to load username.", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkAndPromptUserName() {
        DocumentReference userDocRef = firestore.collection("user").document(currentUserId);
        userDocRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.contains("userName")) {
                loadLeaderboard();
            } else {
                promptForUserName(userDocRef);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Failed to check user name", Toast.LENGTH_SHORT).show()
        );
    }

    private void promptForUserName(DocumentReference userDocRef) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nhập tên của bạn để hiển thị trên bảng xếp hạng");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Tiếp tục", (dialog, which) -> {
            String userName = input.getText().toString().trim();
            if (!userName.isEmpty()) {
                userDocRef.update("userName", userName)
                        .addOnSuccessListener(aVoid -> loadLeaderboard())
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Failed to save user name", Toast.LENGTH_SHORT).show()
                        );
            } else {
                Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                promptForUserName(userDocRef);
            }
        });

        builder.setCancelable(false); // Prevent closing without entering a name
        builder.show();
    }

    private void loadLeaderboard() {
        scoreManager.getAllUsersScores(new ScoreManager.Callback<Map<String, Integer>>() {
            @Override
            public void onSuccess(Map<String, Integer> userScores) {
                List<Map.Entry<String, Integer>> scoreList = new ArrayList<>(userScores.entrySet());
                scoreList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

                adapter = new LeaderboardAdapter(scoreList, currentUserName);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
