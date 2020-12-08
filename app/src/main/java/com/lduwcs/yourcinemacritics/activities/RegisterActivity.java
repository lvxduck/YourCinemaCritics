package com.lduwcs.yourcinemacritics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.lduwcs.yourcinemacritics.R;
import com.thelumiereguy.neumorphicview.views.NeumorphicCardView;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtRegEmail, txtRegPassword, txtRegReEnterPassword;
    private NeumorphicCardView btnSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtRegEmail = findViewById(R.id.txtRegEmail);
        txtRegPassword = findViewById(R.id.txtRegPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtRegReEnterPassword = findViewById(R.id.txtRegReEnterPassword);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(v -> {
            String email, password, repassword;
            email = txtRegEmail.getText().toString();
            password = txtRegPassword.getText().toString().trim();
            repassword = txtRegReEnterPassword.getText().toString().trim();

            //VALIDATION
            //Validate email
            if (TextUtils.isEmpty(email)) {
                txtRegEmail.requestFocus();
                txtRegEmail.setError("Email is required!");
                return;
            }

            //Validate password
            if (TextUtils.isEmpty(password)) {
                txtRegPassword.requestFocus();
                txtRegPassword.setError("Password is required!");
                return;
            }

            //Minimum length of password
            if (password.length() < 6) {
                txtRegPassword.requestFocus();
                txtRegPassword.setError("Password must be equal or more than 6 characters!");
                return;
            }

            //Validate Re-password
            if (TextUtils.isEmpty(repassword)) {
                txtRegReEnterPassword.requestFocus();
                txtRegReEnterPassword.setError("Password is required!");
                return;
            }

            //Check Re-password
            if (!password.equals(repassword)) {
                txtRegReEnterPassword.requestFocus();
                txtRegReEnterPassword.setError("Password doesn't match!");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}