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

public class AdminActivity extends AppCompatActivity {

    // Déclaration des vues
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private BottomAppBar bottomAppBar;
    private Toolbar toolbar;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialiser les vues
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        toolbar = findViewById(R.id.toolbar);

        // Configurer la toolbar et afficher le bouton hamburger (menu latéral)
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Afficher le bouton hamburger

        // Initialiser le ViewModel pour observer les données utilisateur
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Récupérer la vue du header du NavigationView pour afficher les informations utilisateur
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_header_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_header_email);

        // Observer les données utilisateur et les mettre à jour dans le header du NavigationView
        userViewModel.getUserName().observe(this, navUserName::setText);
        userViewModel.getUserEmail().observe(this, navUserEmail::setText);

        // Configurer la gestion des éléments du menu dans le NavigationView
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            Log.d("AdminActivity", "Item sélectionné : " + id); // Debugging

            // Gérer l'élément sélectionné
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Ouvrir les paramètres", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_share) {
                shareApp(); // Fonction pour partager l'application
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "À propos de nous : Version 1.0", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logout(); // Fonction pour se déconnecter
            }

            // Fermer le Drawer après la sélection d'un élément
            drawerLayout.closeDrawers();
            return true;
        });

        // Configurer la gestion des éléments du BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("AdminActivity", "Item sélectionné dans BottomNavigation : " + id); // Debugging

            // Gérer l'élément sélectionné et charger le fragment correspondant
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
            return true; // Indique que l'item a été sélectionné
        });

        // Afficher le HomeFragment par défaut lorsque l'activité démarre
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }

    // Gestion des états du cycle de vie de l'activité (pour le débogage)
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AdminActivity", "L'activité est démarrée");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AdminActivity", "L'activité est en cours");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AdminActivity", "L'activité est en pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AdminActivity", "L'activité est arrêtée");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("AdminActivity", "L'activité est détruite");
    }

    // Fonction pour charger un fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment) // Remplacer le fragment actuel par le nouveau
                .commit();
    }

    // Gestion de l'ouverture du Drawer avec la toolbar (bouton hamburger)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("AdminActivity", "Menu item sélectionné avec la toolbar: " + item.getItemId()); // Debugging
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView); // Ouvrir le Drawer
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fonction pour gérer la déconnexion
    private void logout() {
        Log.d("AdminActivity", "Déconnexion en cours..."); // Debugging
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Nettoyer l'historique d'activités
        startActivity(intent);
    }

    // Fonction pour partager l'application
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gestion Absences");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez cette application : Gestion Absences");
        startActivity(Intent.createChooser(shareIntent, "Partager via")); // Lancer le choix d'application pour partager
    }
}