package com.example.englishkids.activities;


import android.content.Intent;
import android.content.SharedPreferences;
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



import static android.content.Context.MODE_PRIVATE;

import com.example.englishkids.R;

public class SettingsFragment extends Fragment {

    private EditText newPassword;
    private Button changePasswordButton, logoutButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        newPassword = view.findViewById(R.id.editTextNewPassword);
        changePasswordButton = view.findViewById(R.id.buttonChangePassword);
        logoutButton = view.findViewById(R.id.buttonLogout);

        // Change password
        changePasswordButton.setOnClickListener(v -> changePassword());

        // Log out
        logoutButton.setOnClickListener(v -> logout());

        return view;
    }

    private void changePassword() {
        String password = newPassword.getText().toString();
        if (!password.isEmpty()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", password);  // Assuming password is stored as a string
            editor.apply();
            Toast.makeText(getContext(), "Password changed successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please enter a valid password", Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
