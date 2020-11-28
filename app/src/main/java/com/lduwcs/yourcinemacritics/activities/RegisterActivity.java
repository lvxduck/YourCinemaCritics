package com.lduwcs.yourcinemacritics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private EditText txtRegEmail, txtRegPassword, txtRegReEnterPassword;
    private CardView btnSignUp;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).setTitle("REGISTER");

        txtRegEmail = findViewById(R.id.txtRegEmail);
        txtRegPassword = findViewById(R.id.txtRegPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtRegReEnterPassword = findViewById(R.id.txtRegReEnterPassword);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, repassword;
                email = txtRegEmail.getText().toString();
                password = txtRegPassword.getText().toString().trim();
                repassword = txtRegReEnterPassword.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty() || repassword.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please Enter Correctly!",
                            Toast.LENGTH_SHORT).show();
                } else if(!password.equals(repassword)){
                    Log.d("TAG", password);
                    Log.d("TAG", repassword);
                    Toast.makeText(RegisterActivity.this, "Password doesn't match!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("TAG", "createUserWithEmail:success");
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }

            }
        });
    }
}