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
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Déclaration des éléments de l'interface utilisateur
    private EditText emailField, passwordField;
    private Button loginButton;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Définit la vue associée à cette activité

        // Initialisation des champs de texte et du bouton
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);

        // Initialisation du ViewModel, qui gère la logique de la connexion
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Observer les résultats du login dans le ViewModel
        loginViewModel.getLoginResult().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                // Si un utilisateur valide est renvoyé
                if (user != null) {
                    // Affiche un message de bienvenue avec le rôle de l'utilisateur
                    Toast.makeText(LoginActivity.this, "Bienvenue " + user.getRole(), Toast.LENGTH_SHORT).show();
                    // Redirige vers le tableau de bord en fonction du rôle de l'utilisateur
                    navigateToDashboard(user.getRole());
                } else {
                    // Affiche un message d'erreur si la connexion a échoué
                    Toast.makeText(LoginActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Définition du comportement au clic sur le bouton de connexion
        loginButton.setOnClickListener(v -> {
            // Récupère les valeurs des champs email et mot de passe
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Vérifie si les champs sont vides
            if (email.isEmpty() || password.isEmpty()) {
                // Affiche un message d'erreur si un des champs est vide
                Toast.makeText(this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
            } else {
                // Si les champs sont remplis, lance la connexion via le ViewModel
                loginViewModel.login(email, password);
            }
        });
    }


    // Fonction pour rediriger l'utilisateur vers son tableau de bord selon son rôle
    private void navigateToDashboard(String role) {
        Intent intent;
        // Vérifie le rôle de l'utilisateur et redirige vers l'activité correspondante
        if ("Admin".equals(role)) {
            intent = new Intent(this, AdminActivity.class); // Redirige vers l'activité Admin
        } else if ("Agent".equals(role)) {
            intent = new Intent(this, AgentActivity.class); // Redirige vers l'activité Agent
        } else {
            intent = new Intent(this, TeacherActivity.class); // Redirige vers l'activité Teacher
        }
        // Lance l'activité correspondante et termine l'activité actuelle
        startActivity(intent);
        finish();
    }
}
