package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.gestionabscenceenseignants.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.widget.Toolbar;

public class AdminActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialiser les vues
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);

        // Configuration de la toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Afficher le bouton hamburger
        }

        // Charger HomeFragment par défaut au démarrage de l'activité
        loadFragment(new HomeFragment());

        // Gestion des sélections dans le menu Drawer
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceFragment());
            } else if (id == R.id.nav_users) {
                loadFragment(new UsersFragment());
            } else if (id == R.id.nav_statistics) {
                loadFragment(new ReportFragment());
            }
            drawerLayout.closeDrawers(); // Fermer le Drawer après sélection
            return true;
        });

        // Configuration de BottomAppBar
        bottomAppBar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(navigationView));
        bottomAppBar.replaceMenu(R.menu.bottom_nav_menu); // Ajouter le menu pour la BottomAppBar
        bottomAppBar.setOnMenuItemClickListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(new HomeFragment());
            } else if (id == R.id.nav_absences) {
                loadFragment(new AbsenceFragment());
            } else if (id == R.id.nav_users) {
                loadFragment(new UsersFragment());
            } else if (id == R.id.nav_statistics) {
                loadFragment(new ReportFragment());
            }
            return true;
        });

        // Configuration du FloatingActionButton
        fab.setOnClickListener(view -> {
            // Créer une instance de BottomSheetDialog
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AdminActivity.this);

            // Charger le layout de la BottomSheet
            View bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheetlayout, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            // Initialiser les vues de la BottomSheet
            ImageView cancelButton = bottomSheetView.findViewById(R.id.cancelButton);
            LinearLayout layoutVideo = bottomSheetView.findViewById(R.id.layoutVideo);
            LinearLayout layoutShorts = bottomSheetView.findViewById(R.id.layoutShorts);

            // Action pour le bouton "Annuler"
            cancelButton.setOnClickListener(v -> bottomSheetDialog.dismiss());

            // Action pour "Ajouter un utilisateur"
            layoutVideo.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                // Action pour ajouter un utilisateur (exemple : afficher un nouveau fragment)
            });

            // Action pour "Ajouter une absence"
            layoutShorts.setOnClickListener(v -> {
                bottomSheetDialog.dismiss();
                // Action pour ajouter une absence
            });

            // Afficher la BottomSheet
            bottomSheetDialog.show();
        });
    }

    // Fonction pour charger un fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)  // Remplacer le contenu dans le FrameLayout
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
