package com.example.englishkids.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.englishkids.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    private EditText etCurrentPassword, etNewPassword;
    private Button btnChangePassword, btnLogout;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        bindingView();
        bindingAction();
        return view;
    }

    private void bindingAction() {
        // Change password
        btnChangePassword.setOnClickListener(v -> changePassword());

        // Log out
        btnLogout.setOnClickListener(v -> logout());
    }

    private void bindingView() {
        etCurrentPassword = getView().findViewById(R.id.editTextCurrentPassword);
        etNewPassword = getView().findViewById(R.id.editTextNewPassword);
        btnChangePassword = getView().findViewById(R.id.buttonChangePassword);
        btnLogout = getView().findViewById(R.id.buttonLogout);
    }

    private void changePassword() {
        String currentPassword = etCurrentPassword.getText().toString();
        String newPassword = etNewPassword.getText().toString();

        if (currentPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in both password fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && user.getEmail() != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            // Re-authenticate user
            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Update password
                    user.updatePassword(newPassword).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
                            firebaseAuth.signOut();
                            navigateToLogin();
                        } else {
                            Toast.makeText(getContext(), "Failed to change password", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Current password is incorrect", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void logout() {
        firebaseAuth.signOut();

        // Navigate to login screen
        navigateToLogin();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}
