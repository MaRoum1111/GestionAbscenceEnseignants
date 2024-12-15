package com.example.gestionabscenceenseignants.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class TeacherActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private BottomAppBar bottomAppBar;
    private Toolbar toolbar;
    private UserViewModel userViewModel;

    // Méthode d'initialisation de l'activité
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        // Initialisation des vues
        drawerLayout = findViewById(R.id.drawer_layout); // Menu de navigation
        navigationView = findViewById(R.id.nav_view); // Vue de navigation
        bottomNavigationView = findViewById(R.id.bottomNavigation); // Menu de navigation en bas
        bottomAppBar = findViewById(R.id.bottomAppBar); // AppBar en bas
        toolbar = findViewById(R.id.toolbar); // Toolbar en haut

        // Configuration de la Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Active le bouton hamburger

        // Initialisation du ViewModel pour gérer les données de l'utilisateur
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Récupération des vues du header dans le NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_header_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_header_email);

        // Observation des données utilisateur via le ViewModel et mise à jour des vues
        userViewModel.getUserName().observe(this, navUserName::setText);
        userViewModel.getUserEmail().observe(this, navUserEmail::setText);

        // Configuration de la gestion des éléments du menu dans le NavigationView
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            Log.d("TeacherActivity", "Item sélectionné : " + id); // Debugging pour suivre les sélections

            // Gérer les actions en fonction de l'élément sélectionné
            if (id == R.id.nav_home) {
                loadFragment(new HomeTeacherFragment()); // Charge le fragment d'accueil
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Ouvrir les paramètres", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_share) {
                shareApp(); // Partage l'application
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "À propos de nous : Version 1.0", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logout(); // Déconnexion de l'utilisateur
            }

            drawerLayout.closeDrawers(); // Ferme le menu de navigation après sélection
            return true;
        });

        // Configuration du BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("TeacherActivity", "Item sélectionné dans BottomNavigation : " + id); // Debugging

            // Gérer les actions en fonction de l'élément sélectionné dans le menu en bas
            if (id == R.id.nav_home) {
                loadFragment(new HomeTeacherFragment()); // Charge le fragment d'accueil
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceTeacherFragment()); // Charge le fragment des absences
            } else if (id == R.id.nav_claim) {
                loadFragment(new ListeClaimFragment()); // Charge le fragment des réclamations
            }
            return true; // Indique que l'élément a été sélectionné et l'action effectuée
        });

        // Charge le HomeFragment par défaut lorsque l'activité démarre
        if (savedInstanceState == null) {
            loadFragment(new HomeTeacherFragment()); // Charge le fragment d'accueil
        }
    }

    // Fonction pour charger un fragment spécifique
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment) // Remplace le fragment actuel par celui passé en argument
                .commit();
    }

    // Gérer l'ouverture du Drawer avec la toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("TeacherActivity", "Menu item sélectionné avec la toolbar: " + item.getItemId());
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView); // Ouvre le menu de navigation (Drawer)
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fonction pour gérer la déconnexion
    private void logout() {
        Log.d("TeacherActivity", "Déconnexion en cours...");
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Crée une nouvelle tâche et nettoie les tâches précédentes
        startActivity(intent); // Démarre l'activité de connexion
    }

    // Fonction pour partager l'application
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND); // Action pour envoyer un message
        shareIntent.setType("text/plain"); // Type de contenu à partager
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gestion Absences"); // Sujet du message
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez cette application : Gestion Absences"); // Texte du message
        startActivity(Intent.createChooser(shareIntent, "Partager via")); // Ouvre un sélecteur pour choisir l'application de partage
    }
}
