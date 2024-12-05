package com.example.gestionabscenceenseignants.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.LoginViewModel;
import com.example.gestionabscenceenseignants.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginButton;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);

        // Initialisation du ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observer le résultat du login
        loginViewModel.getLoginResult().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(LoginActivity.this, "Bienvenue " + user.getRole(), Toast.LENGTH_SHORT).show();
                    navigateToDashboard(user.getRole());
                } else {
                    Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Action lors du clic sur le bouton de connexion
        loginButton.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            } else {
                loginViewModel.login(email, password); // Lancer la connexion via le ViewModel
            }
        });
    }

    // Rediriger vers le tableau de bord en fonction du rôle
    private void navigateToDashboard(String role) {
        Intent intent;
        if ("Admin".equals(role)) {
            intent = new Intent(this, AdminActivity.class);
        } else if ("Agent".equals(role)) {
            intent = new Intent(this, AgentActivity.class);
        } else {
            intent = new Intent(this, TeacherActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
