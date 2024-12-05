package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.gestionabscenceenseignants.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Pour afficher le bouton hamburger

        // Initialiser le ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Récupérer la vue du header dans le NavigationView
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_header_name);
        TextView navUserEmail = headerView.findViewById(R.id.nav_header_email);

        // Observer les données de l'utilisateur
        userViewModel.getUserName().observe(this, navUserName::setText);
        userViewModel.getUserEmail().observe(this, navUserEmail::setText);

        // Ouverture/fermeture du menu Drawer
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_settings) {
                // Ajouter ici l'action pour "Settings"
            } else if (id == R.id.nav_share) {
                // Ajouter ici l'action pour "Share"
            } else if (id == R.id.nav_about) {
                // Ajouter ici l'action pour "About Us"
            } else if (id == R.id.nav_logout) {
                // Ajouter l'action de déconnexion
            }
            drawerLayout.closeDrawers(); // Fermer le Drawer après avoir sélectionné un item
            return true;
        });

        // Configuration de BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceFragment());
            } else if (id == R.id.nav_users) {
                loadFragment(new UsersFragment());
            } else if (id == R.id.nav_statistics) {
                loadFragment(new ReportFragment());
            }
            return true; // Indique que l'item a été sélectionné et l'action a été effectuée
        });

        // Afficher le HomeFragment par défaut lorsque l'activité démarre
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment()); // Charger HomeFragment comme premier fragment
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
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navigationView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
