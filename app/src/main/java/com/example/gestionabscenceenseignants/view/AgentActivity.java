package com.example.gestionabscenceenseignants.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class AgentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout; // Déclaration de la variable pour le DrawerLayout
    private NavigationView navigationView; // Déclaration pour la navigation
    private BottomNavigationView bottomNavigationView; // Déclaration de la vue de navigation en bas
    private BottomAppBar bottomAppBar; // Déclaration de la barre d'application en bas
    private Toolbar toolbar; // Déclaration de la barre d'outils (Toolbar)
    private UserViewModel userViewModel; // Déclaration du ViewModel pour l'utilisateur

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent); // Configuration de la vue de l'activité

        // Initialiser les vues de l'interface utilisateur
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        toolbar = findViewById(R.id.toolbar);

        // Configuration de la Toolbar avec un bouton hamburger pour ouvrir le DrawerLayout
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Active le bouton du Drawer dans la barre d'outils

        // Initialisation du ViewModel pour observer les données de l'utilisateur
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Récupération des données d'utilisateur à afficher dans le header du Drawer
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_header_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_header_email);

        // Observer les données utilisateur et les mettre à jour dans le header
        userViewModel.getUserName().observe(this, navUserName::setText);
        userViewModel.getUserEmail().observe(this, navUserEmail::setText);

        // Gestion de la sélection d'un élément dans le Drawer (NavigationView)
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId(); // Récupérer l'ID de l'élément sélectionné
            Log.d("Agent Activity", "Item sélectionné : " + id); // Log pour débogage

            // Charger un fragment ou effectuer une action en fonction de l'élément sélectionné
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment()); // Charger le fragment d'accueil
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Ouvrir les paramètres", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_share) {
                shareApp(); // Partager l'application
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "À propos de nous : Version 1.0", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logout(); // Déconnexion de l'utilisateur
            }

            drawerLayout.closeDrawers(); // Fermer le Drawer après avoir sélectionné un item
            return true;
        });

        // Configuration du BottomNavigationView pour la gestion de la navigation en bas de l'écran
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("AgentActivity", "Item sélectionné dans BottomNavigation : " + id); // Log pour débogage

            // Charger un fragment en fonction de l'item sélectionné
            if (id == R.id.nav_home) {
                loadFragment(new AccueilAgentFragment());
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceFragment());
            }

            return true;
        });

        // Charger le fragment d'accueil par défaut lorsque l'activité démarre
        if (savedInstanceState == null) {
            loadFragment(new AccueilAgentFragment());
        }
    }

    // Méthode pour charger un fragment dans le layout de l'activité
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment) // Remplacer le contenu du conteneur par le fragment
                .commit(); // Commit de la transaction
    }

    // Gérer l'ouverture du Drawer avec le bouton de la Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("AgentActivity", "Menu item sélectionné avec la toolbar: " + item.getItemId()); // Log pour débogage
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView); // Ouvrir le Drawer si le bouton hamburger est sélectionné
            return true;
        }
        return super.onOptionsItemSelected(item); // Traiter les autres éléments de menu
    }

    // Fonction pour gérer la déconnexion de l'utilisateur
    private void logout() {
        Log.d("AdminActivity", "Déconnexion en cours..."); // Log pour débogage
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        // Créer une nouvelle intention pour rediriger l'utilisateur vers l'écran de connexion
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Effacer les activités précédentes
        startActivity(intent);
    }

    // Fonction pour partager l'application via une intention
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND); // Créer une intention pour partager du texte
        shareIntent.setType("text/plain"); // Type de contenu à partager
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gestion Absences"); // Sujet du message
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez cette application : Gestion Absences"); // Texte du message
        startActivity(Intent.createChooser(shareIntent, "Partager via")); // Lancer le partage via un sélecteur d'application
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("AgentActivity", "onSaveInstanceState: Sauvegarde de l'état de l'activité");
    }
}
