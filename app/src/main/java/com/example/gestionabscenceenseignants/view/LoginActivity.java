package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.controller.AuthController;
import com.example.gestionabscenceenseignants.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private AuthController authController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);

        authController = new AuthController();

        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });
    }

    // Méthode pour connecter l'utilisateur et rediriger en fonction du rôle
    private void loginUser(String email, String password) {
        authController.login(email, password, new AuthController.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(LoginActivity.this, "Bienvenue " + user.getRole(), Toast.LENGTH_SHORT).show();
                navigateToDashboard(user.getRole());
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LoginActivity.this, "Erreur : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Rediriger vers le tableau de bord en fonction du rôle
    private void navigateToDashboard(String role) {
        Intent intent;
        if ("administrateur".equals(role)) {
            intent = new Intent(this, AdminActivity.class);
        } else if ("agent".equals(role)) {
            intent = new Intent(this, AgentActivity.class);
        } else {
            intent = new Intent(this, TeacherActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
