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

public class LoginEmailAuthActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private FirebaseAuth firebaseAuth;
    private SwipeRefreshLayout swipeLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        firebaseAuth = FirebaseAuth.getInstance();
        setWidgets();
    }

    private void setWidgets() {
        final TextInputEditText loginEmail = findViewById(R.id.login_email);
        final TextInputEditText loginPassword = findViewById(R.id.login_password);
        final Button btnLogin = findViewById(R.id.btn_login);
        final Button btnNewAccount = findViewById(R.id.btn_new_account);
        final ImageView btnBack = findViewById(R.id.btn_back);
        swipeLoading = findViewById(R.id.swipe_email_auth);
        swipeLoading.setEnabled(false);
        swipeLoading.setColorSchemeColors(getColor(R.color.white), getColor(R.color.colorAccent));
        swipeLoading.setProgressBackgroundColorSchemeColor(getColor(R.color.colorPrimary));

        btnLogin.setOnClickListener(view -> {
            switchLoading(true);
            final String email = loginEmail.getText().toString();
            final String password = loginPassword.getText().toString();
            authEmail(email, password);
        });

        btnNewAccount.setOnClickListener(view -> {
            goToRegisterUserActivity();
        });

        btnBack.setOnClickListener(view -> {
            onBackPressed();
        });

    }

    private void authEmail(final String email, final String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    switchLoading(false);
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        goToMainActivity();
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginEmailAuthActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void goToMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        finishAffinity();
        startActivity(intent);
    }

    private void goToRegisterUserActivity() {
        final Intent intent = new Intent(this, RegisterUserActivity.class);
        startActivity(intent);
    }

    private void switchLoading(final Boolean flag) {
        swipeLoading.setEnabled(flag);
        swipeLoading.setRefreshing(flag);
    }

}