package com.lduwcs.yourcinemacritics.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.lduwcs.yourcinemacritics.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtRegEmail, txtRegPassword;
    private CardView btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).setTitle("REGISTER");

        txtRegEmail = findViewById(R.id.txtRegEmail);
        txtRegPassword = findViewById(R.id.txtRegPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
    }
}