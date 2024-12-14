package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.Adapter.AbsensesAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.model.Absence;
import java.util.List;

public class HomeTeacherFragment extends Fragment implements AbsensesAdapter.OnAbsenceClickListener {
    private RecyclerView recyclerView;
    private AbsensesAdapter adapter;
    private AbsenceViewModel absenceViewModel;
    private UserViewModel userViewModel;
    private TextView message;
    private TextView emptyView; // Déclaration du TextView pour le message "Aucune absence à afficher"
    private List<Absence> absenceList; // Liste des absences pour gérer les mises à jour
    private CardView cardView1,cardView2;
    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_home_teacher, container, false);

        // Initialize the RecyclerView
        recyclerView = rootView.findViewById(R.id.absenceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        message = rootView.findViewById(R.id.bonjourTextView);
        emptyView = rootView.findViewById(R.id.emptyView); // Récupération du TextView "Aucune absence à afficher"

        // Initialize the ViewModel to retrieve absences
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Récupérer les CardViews par leur ID
        cardView1 = rootView.findViewById(R.id.cardView1);
        cardView2 = rootView.findViewById(R.id.cardView2);
        // Ajouter des listeners pour gérer les clics
        cardView1.setOnClickListener(v -> {
            // Logique à exécuter quand l'utilisateur clique sur CardView 1 (Mes réclamations)
            navigateToClaimFragment();
        });

        cardView2.setOnClickListener(v -> {
            // Logique à exécuter quand l'utilisateur clique sur CardView 2 (Mes absences)
            navigateToAbsenceFragment();
        });
        // Observer for user name
        userViewModel.getUserName().observe(getViewLifecycleOwner(), userName -> {
            if (userName != null) {
                message.setText(userName);  // Afficher le nom de l'utilisateur
            }
        });

        // Load absences for the specific teacher (only today's absences)
        absenceViewModel.getAbsencesForCurrentDay(); // Récupérer les absences du jour actuel

        // Observe absences and update the adapter
        observeAbsences(rootView); // Pass the root view to the observe method

        return rootView;
    }
    private void observeAbsences(View rootView) {
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d("HomeTeacherFragment", "Absences observées : " + (absences != null ? absences.size() : "null"));

            if (absences != null && !absences.isEmpty()) {
                absenceList = absences;
                Log.d("HomeTeacherFragment", "Initialisation de l'adaptateur avec " + absences.size() + " absences.");

                if (adapter == null) {
                    adapter = new AbsensesAdapter(absenceList, this);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateAbsences(absenceList); // Mettre à jour les données
                }
                // Cacher le message de liste vide
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                Log.d("HomeTeacherFragment", "Aucune absence à afficher.");
                recyclerView.setAdapter(null);
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE); // Afficher le message "Aucune absence à afficher"
            }
        });

        // Observer le message d'erreur
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                Log.e("HomeTeacherFragment", errorMessage);
                emptyView.setText(errorMessage); // Modifier le texte du message d'erreur
                emptyView.setVisibility(View.VISIBLE); // Afficher le message d'erreur
                recyclerView.setVisibility(View.GONE); // Cacher le RecyclerView
            }
        });
    }  // Méthode pour naviguer vers le fragment des réclamations
    private void navigateToClaimFragment() {
        ClaimFragment claimFragment = new ClaimFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, claimFragment) // Remplacer par l'ID de votre FrameLayout
                .addToBackStack(null) // Ajouter à la pile arrière pour la navigation
                .commit();
    }
    // Méthode pour naviguer vers le fragment des absences
    private void navigateToAbsenceFragment() {
        AbsenceTeacherFragment absenceFragment = new AbsenceTeacherFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, absenceFragment) // Remplacer par l'ID de votre FrameLayout
                .addToBackStack(null) // Ajouter à la pile arrière pour la navigation
                .commit();
    }


    @Override
    public void onRecommend(Absence absence) {
        if (absence == null) {
            Log.e("HomeTeacherFragment", "Absence is null. Cannot proceed to ClaimFragment.");
            return;
        }

        ClaimFragment claimFragment = new ClaimFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, claimFragment) // Replace with the ID of your FrameLayout
                .addToBackStack(null) // Add to back stack for navigation
                .commit();
    }
}