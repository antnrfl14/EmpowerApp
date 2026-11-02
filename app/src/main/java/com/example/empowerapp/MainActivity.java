package com.example.empowerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText emailEditText, passwordEditText;
    private Button btnLogin, btnRegister, btnLogout, superButton, btnSettings, btnInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        superButton = findViewById(R.id.superButton);
        btnSettings = findViewById(R.id.btnSettings);
        btnInfo = findViewById(R.id.btnInfo);

        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogout = findViewById(R.id.btn_logout);

        superButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });
        btnSettings.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Settings...", Toast.LENGTH_SHORT).show());
        btnInfo.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Info...", Toast.LENGTH_SHORT).show());

        btnRegister.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(MainActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        btnLogin.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            updateUI(mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            updateUI(null);
        });
    }

    private void updateUI(FirebaseUser user) {
        View cardStatus = findViewById(R.id.cardStatus);
        View cardSecurity = findViewById(R.id.cardSecurity);
        View superButton = findViewById(R.id.superButton);

        if (user != null) {
            findViewById(R.id.authPanel).setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

            cardStatus.setVisibility(View.VISIBLE);
            cardSecurity.setVisibility(View.VISIBLE);
            superButton.setVisibility(View.VISIBLE);
            btnSettings.setVisibility(View.VISIBLE);
            btnInfo.setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.authPanel).setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);

            cardStatus.setVisibility(View.GONE);
            cardSecurity.setVisibility(View.GONE);
            superButton.setVisibility(View.GONE);
            btnSettings.setVisibility(View.GONE);
            btnInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}