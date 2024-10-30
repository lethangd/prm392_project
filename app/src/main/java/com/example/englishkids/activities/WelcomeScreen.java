package com.example.englishkids.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.dao.DummiesData;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishkids.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WelcomeScreen extends AppCompatActivity {

    private Button btnStart, btnLogin;
    BottomSheetDialog bottomSheetDialog;

    private FirebaseAuth firebaseAuth;
    private AppDatabase db;
    private ExecutorService executorService;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bindingView();
        bindingAction();
    }



    private void bindingAction() {
        btnStart.setOnClickListener(this::onStartClick);
        btnLogin.setOnClickListener(this::onLoginClick);
    }

    private void onLoginClick(View view) {
        showLoginBottomSheet();
    }

    private void onStartClick(View view) {
        Intent intent = new Intent(WelcomeScreen.this, LevelSelection.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void bindingView() {
        btnStart = findViewById(R.id.btn_continue_welcome);
        btnLogin = findViewById(R.id.btn_have_account);
    }

    private void showLoginBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_login, null);
        bottomSheetDialog.setContentView(bottomSheetView);
        initComponentForBottomSheet();
        configureGoogleSignIn();
        configureFacebookLogin();
        bottomSheetView.findViewById(R.id.btn_login_google).setOnClickListener(this::loginByGoogle);

        bottomSheetView.findViewById(R.id.btn_login_facebook).setOnClickListener(this::loginByFacebook);

        bottomSheetView.findViewById(R.id.btn_login_email).setOnClickListener(this::loginByEmail);
        bottomSheetDialog.show();
    }

    private void initComponentForBottomSheet() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
    }

    private void loginByFacebook(View view) {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        bottomSheetDialog.dismiss();
    }

    private void loginByGoogle(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        bottomSheetDialog.dismiss();
    }

    private void loginByEmail(View view) {
       showEmailLoginOptions();
       bottomSheetDialog.dismiss();
    }
    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void configureFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                updateSharedPreferences(user.getEmail());
                executeDummyDataInsertion();
                navigateToMainActivity();
            } else {
                Toast.makeText(WelcomeScreen.this, "Facebook sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                updateSharedPreferences(account.getEmail());
                executeDummyDataInsertion();
                navigateToMainActivity();
            } else {
                Toast.makeText(WelcomeScreen.this, "Google sign-in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateSharedPreferences(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.apply();
    }
    private void executeDummyDataInsertion() {
        executorService.execute(() -> DummiesData.insertDummyData(db));
    }

    private void navigateToMainActivity() {
        Toast.makeText(WelcomeScreen.this, "Login successful", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(WelcomeScreen.this, MainActivity.class));
        finish();
    }

    // Method to show email login options
    private void showEmailLoginOptions() {
        BottomSheetDialog emailBottomSheetDialog = new BottomSheetDialog(this);
        View emailBottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_email_login, null);
        emailBottomSheetDialog.setContentView(emailBottomSheetView);

        emailBottomSheetView.findViewById(R.id.btn_email_login).setOnClickListener(v -> {
            emailBottomSheetDialog.dismiss();
            saveFirstLaunch();
            Intent intent = new Intent(WelcomeScreen.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        emailBottomSheetView.findViewById(R.id.btn_email_signup).setOnClickListener(v -> {
            emailBottomSheetDialog.dismiss();
            saveFirstLaunch();
            Intent intent = new Intent(WelcomeScreen.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        emailBottomSheetDialog.show();
    }
    private void saveFirstLaunch() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
    }
}
