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
    private CardView cardView1, cardView2;

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_home_teacher, container, false);

        // Initialiser le RecyclerView pour afficher les absences
        recyclerView = rootView.findViewById(R.id.absenceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser les vues pour afficher le message de bienvenue et le message quand il n'y a pas d'absences
        message = rootView.findViewById(R.id.bonjourTextView);
        emptyView = rootView.findViewById(R.id.emptyView); // Récupération du TextView "Aucune absence à afficher"

        // Initialiser le ViewModel pour récupérer les absences et les données de l'utilisateur
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Récupérer les CardViews par leur ID pour gérer les actions des utilisateurs
        cardView1 = rootView.findViewById(R.id.cardView1);
        cardView2 = rootView.findViewById(R.id.cardView2);

        // Ajouter des listeners pour gérer les clics sur les CardViews
        cardView1.setOnClickListener(v -> {
            // Logique à exécuter quand l'utilisateur clique sur CardView 1 (Mes réclamations)
            navigateToClaimFragment();
        });

        cardView2.setOnClickListener(v -> {
            // Logique à exécuter quand l'utilisateur clique sur CardView 2 (Mes absences)
            navigateToAbsenceFragment();
        });

        // Observer pour le nom de l'utilisateur, afin d'afficher un message de bienvenue
        userViewModel.getUserName().observe(getViewLifecycleOwner(), userName -> {
            if (userName != null) {
                message.setText(userName);  // Afficher le nom de l'utilisateur
            }
        });

        // Charger les absences pour l'enseignant (seulement les absences du jour)
        absenceViewModel.getAbsencesForCurrentDay(); // Récupérer les absences du jour actuel

        // Observer les absences et mettre à jour l'adaptateur
        observeAbsences(rootView); // Passer la vue root pour observer les absences

        return rootView;
    }

    // Méthode pour observer les absences et mettre à jour l'affichage
    private void observeAbsences(View rootView) {
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d("HomeTeacherFragment", "Absences observées : " + (absences != null ? absences.size() : "null"));

            if (absences != null && !absences.isEmpty()) {
                absenceList = absences;
                Log.d("HomeTeacherFragment", "Initialisation de l'adaptateur avec " + absences.size() + " absences.");

                // Si l'adaptateur n'existe pas encore, le créer et l'attacher au RecyclerView
                if (adapter == null) {
                    adapter = new AbsensesAdapter(absenceList, this);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Mettre à jour les données dans l'adaptateur
                    adapter.updateAbsences(absenceList);
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
    }

    // Méthode pour naviguer vers le fragment des réclamations
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

    // Méthode lorsque l'utilisateur clique sur une absence pour la recommander
    @Override
    public void onRecommend(Absence absence) {
        if (absence == null) {
            Log.e("HomeTeacherFragment", "Absence is null. Cannot proceed to ClaimFragment.");
            return;
        }

        ClaimFragment claimFragment = new ClaimFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, claimFragment) // Remplacer par l'ID de votre FrameLayout
                .addToBackStack(null) // Ajouter à la pile arrière pour la navigation
                .commit();
    }
}
