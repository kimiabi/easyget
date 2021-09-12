package com.kimi.easyget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        final GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

       setWidget(user);

    }

    private void setWidget(final FirebaseUser user) {
        final ImageView photoUser = findViewById(R.id.photo_user);
        final TextView nameUser = findViewById(R.id.name_user);
        final TextView emailUser = findViewById(R.id.email_user);
        final Button btnSignOut = findViewById(R.id.btn_signout);


        if (user != null) {
            Glide.with(this).load(user.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(photoUser);
            nameUser.setText(user.getDisplayName());
            emailUser.setText(user.getEmail());
        }

        btnSignOut.setOnClickListener(view -> {
            firebaseAuth.signOut();
            googleSignInClient.signOut().addOnCompleteListener(MainActivity.this, task -> goToLogin());
        });
    }

    private void goToLogin() {
        final Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }
}