package com.kimi.easyget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterUserActivity extends AppCompatActivity {

    private static final String TAG = "Register Firebase";
    private static final String DELIMITER = " ";
    private FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout swipeLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firebaseAuth = FirebaseAuth.getInstance();

        setWidgets();
    }

    private void setWidgets() {
        final TextInputEditText nameInput = findViewById(R.id.name_register);
        final TextInputEditText lastNameInput = findViewById(R.id.last_name_register);
        final TextInputEditText emailInput = findViewById(R.id.email_register);
        final TextInputEditText passwordInput = findViewById(R.id.password_register);
        final TextInputEditText passwordConfirmInput = findViewById(R.id.password_confirm_register);
        final Button btnRegister = findViewById(R.id.btn_register);
        final ImageView btnBack = findViewById(R.id.btn_back);

        swipeLoading = findViewById(R.id.swipe_user_register);
        swipeLoading.setEnabled(false);
        swipeLoading.setColorSchemeColors(getColor(R.color.white), getColor(R.color.colorAccent));
        swipeLoading.setProgressBackgroundColorSchemeColor(getColor(R.color.colorPrimary));


        btnRegister.setOnClickListener(view -> {
            final String name = nameInput.getText().toString().trim();
            final String lastName = lastNameInput.getText().toString().trim();
            final String email = emailInput.getText().toString().trim();
            final String password = passwordInput.getText().toString().trim();

            switchLoading(true);
            createAccount(name, lastName, email, password);
        });

        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void createAccount(final String name, final String lastName, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        final FirebaseUser user = firebaseAuth.getCurrentUser();
                        createAndUpdateProfile(name, lastName, user);
                    } else {
                        // If sign in fails, display a message to the user.
                        switchLoading(false);
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterUserActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void createAndUpdateProfile(final String name, final String lastName, final FirebaseUser user) {
        final String displayName = String.join(DELIMITER, name, lastName);
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build();

        user.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(task -> {
                    switchLoading(false);
                    if (task.isSuccessful()) {
                        Log.d(TAG, "User profile updated.");
                        goToMainActivity();
                    }
                });
    }

    private void goToMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    private void switchLoading(final Boolean flag) {
        swipeLoading.setEnabled(flag);
        swipeLoading.setRefreshing(flag);
    }
}