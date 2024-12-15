package com.example.gestionabscenceenseignants.view;

// Importation des bibliothèques nécessaires
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

public class AdminActivity extends AppCompatActivity {

    // Déclaration des composants d'interface utilisateur
    private DrawerLayout drawerLayout; // Menu latéral (drawer)
    private NavigationView navigationView; // Vue de navigation pour le menu latéral
    private BottomNavigationView bottomNavigationView; // Navigation en bas de l'écran
    private BottomAppBar bottomAppBar; // Barre d'applications en bas
    private Toolbar toolbar; // Barre d'outils en haut de l'écran
    private UserViewModel userViewModel; // ViewModel pour gérer les données utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin); // Définir le layout de l'activité

        // Initialisation des vues
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        toolbar = findViewById(R.id.toolbar);

        // Configurer la toolbar et afficher le bouton hamburger pour le menu latéral
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Affiche le bouton pour ouvrir le menu

        // Initialisation du ViewModel pour observer les données utilisateur
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Configuration de l'en-tête du menu latéral pour afficher les informations utilisateur
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_header_name); // Nom d'utilisateur
        TextView navUserEmail = headerView.findViewById(R.id.nav_header_email); // Email de l'utilisateur

        // Mise à jour des données utilisateur dans l'en-tête via le ViewModel
        userViewModel.getUserName().observe(this, navUserName::setText);
        userViewModel.getUserEmail().observe(this, navUserEmail::setText);

        // Configuration de la gestion des clics sur le menu latéral
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId(); // Récupère l'ID de l'élément cliqué
            Log.d("AdminActivity", "Item sélectionné : " + id); // Log pour débogage

            // Gestion des actions selon l'élément sélectionné
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment()); // Charge le fragment d'accueil
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Ouvrir les paramètres", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_share) {
                shareApp(); // Partage l'application
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "À propos de nous : Version 1.0", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logout(); // Déconnecte l'utilisateur
            }

            drawerLayout.closeDrawers(); // Ferme le menu après une sélection
            return true;
        });

        // Configuration des clics pour le menu de navigation en bas
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("AdminActivity", "Item sélectionné dans BottomNavigation : " + id); // Log pour débogage

            // Gestion des fragments selon l'élément sélectionné
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceFragment());
            } else if (id == R.id.nav_users) {
                loadFragment(new UsersFragment());
            } else if (id == R.id.nav_statistics) {
                loadFragment(new ReportFragment());
            } else if (id == R.id.nav_claim) {
                loadFragment(new AdminClaimFragment());
            }
            return true;
        });

        // Charge le fragment d'accueil par défaut au démarrage
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }


    // Fonction pour charger un fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment) // Remplacement du contenu de la vue
                .commit();
    }

    // Gestion de l'ouverture du menu latéral via le bouton hamburger
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("AdminActivity", "Menu item sélectionné avec la toolbar: " + item.getItemId());
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView); // Ouvre le menu latéral
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fonction pour déconnecter l'utilisateur
    private void logout() {
        Log.d("AdminActivity", "Déconnexion en cours...");
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Réinitialise l'activité
        startActivity(intent);
    }

    // Fonction pour partager l'application
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gestion Absences");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez cette application : Gestion Absences");
        startActivity(Intent.createChooser(shareIntent, "Partager via")); // Ouvre une boîte de dialogue pour choisir une application de partage
    }
}
