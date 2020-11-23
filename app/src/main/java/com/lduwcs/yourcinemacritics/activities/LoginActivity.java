package com.lduwcs.yourcinemacritics.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.lduwcs.yourcinemacritics.R;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText txtEmail, txtPassword;
    private TextView btnSignInWithGoogle, btnCreateAccount;
    private CardView btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setTitle("LOGIN");

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtRegReEnterPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle);
    }
}