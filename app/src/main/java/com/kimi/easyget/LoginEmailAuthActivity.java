package com.kimi.easyget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailAuthActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        firebaseAuth = FirebaseAuth.getInstance();
        setWidget();
    }

    private void setWidget() {
        final TextInputEditText loginEmail = findViewById(R.id.login_email);
        final TextInputEditText loginPassword = findViewById(R.id.login_password);
        final Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(view -> {
            final String email = loginEmail.getText().toString();
            final String password = loginPassword.getText().toString();
            authEmail(email, password);
        });

    }

    private void authEmail(final String email, final String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
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

}