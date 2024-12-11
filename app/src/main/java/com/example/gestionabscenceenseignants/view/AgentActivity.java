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

        // Configuration de la toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Afficher le bouton hamburger

        // Initialiser le ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Récupérer la vue du header dans le NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_header_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_header_email);

        // Observer les données de l'utilisateur
        userViewModel.getUserName().observe(this, navUserName::setText);
        userViewModel.getUserEmail().observe(this, navUserEmail::setText);

        // Gérer les éléments du menu dans le NavigationView
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            Log.d("Agent Activity", "Item sélectionné : " + id); // Debugging

            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_settings) {
                Toast.makeText(this, "Ouvrir les paramètres", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_share) {
                shareApp();
            } else if (id == R.id.nav_about) {
                Toast.makeText(this, "À propos de nous : Version 1.0", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_logout) {
                logout();
            }

            drawerLayout.closeDrawers(); // Fermer le Drawer après avoir sélectionné un item
            return true;
        });

        // Configuration de BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            Log.d("AdminActivity", "Item sélectionné dans BottomNavigation : " + id); // Debugging

            if (id == R.id.nav_home) {
                loadFragment(new AccueilAgentFragment());
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceFragment());
            } else if (id == R.id.nav_emplois) {
                loadFragment(new EmploisFragment());
            }

            return true; // Indique que l'item a été sélectionné et l'action a été effectuée
        });

        // Afficher le HomeFragment par défaut lorsque l’activité démarre
        if (savedInstanceState == null) {
            loadFragment(new AccueilAgentFragment());
        }
    }

    // Fonction pour charger un fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    // Gérer l'ouverture du Drawer avec la toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("AgentActivity", "Menu item sélectionné avec la toolbar: " + item.getItemId()); // Debugging
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Fonction pour gérer la déconnexion
    private void logout() {
        Log.d("AdminActivity", "Déconnexion en cours..."); // Debugging
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // Fonction pour partager l'application
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Gestion Absences");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Découvrez cette application : Gestion Absences");
        startActivity(Intent.createChooser(shareIntent, "Partager via"));
    }

    // Méthodes de cycle de vie

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AgentActivity", "onStart: Activité démarrée");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AgentActivity", "onResume: Activité en pause reprise");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AgentActivity", "onPause: Activité en pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AgentActivity", "onStop: Activité arrêtée");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("AgentActivity", "onDestroy: Activité détruite");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("AgentActivity", "onRestart: Activité redémarrée");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("AgentActivity", "onSaveInstanceState: Sauvegarde de l'état de l'activité");
    }
}
