package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.Adapter.ClaimAdminDetailAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.List;

public class AdminDetailClaimFragment extends Fragment implements ClaimAdminDetailAdapter.OnClaimDetailClickListener {
    // Déclaration des variables pour RecyclerView, Adaptateur, ViewModel, et autres informations nécessaires
    private RecyclerView recyclerView;
    private ClaimAdminDetailAdapter adapter;
    private ClaimViewModel claimViewModel;
    private String profCin, profname;
    private List<Claim> claimList; // Liste des réclamations pour gestion et mise à jour

    // La méthode onCreateView est utilisée pour initialiser la vue du fragment
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue du fragment à partir du fichier XML
        View rootView = inflater.inflate(R.layout.fragment_admin_detail_claim, container, false);

        // Initialisation du RecyclerView pour afficher les réclamations
        recyclerView = rootView.findViewById(R.id.recyclerViewClaims);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialisation des TextViews pour afficher le nom et le CIN du professeur
        TextView name = rootView.findViewById(R.id.teacher_name);
        TextView cin = rootView.findViewById(R.id.teacher_cin);

        // Récupérer les arguments passés au fragment (CIN et nom du professeur)
        getArgumentsData();

        // Afficher les informations du professeur dans les TextViews
        displayTeacherInfo(name, cin);

        // Initialisation du ViewModel pour gérer les données de réclamations
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        // Charger les réclamations du professeur spécifique
        getClaimByProf();

        // Observer les données des réclamations et mettre à jour l'interface en conséquence
        observeClaims();

        return rootView;
    }

    // Récupérer les arguments (CIN et nom du professeur) passés au fragment
    private void getArgumentsData() {
        if (getArguments() != null) {
            profCin = getArguments().getString("cin");
            profname = getArguments().getString("profName");
        }
    }

    // Afficher les informations du professeur dans les TextViews
    private void displayTeacherInfo(TextView name, TextView cin) {
        name.setText(profname);
        cin.setText(profCin);
    }

    // Observer les réclamations du professeur et mettre à jour le RecyclerView
    private void observeClaims() {
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), claims -> {
            if (claims != null && !claims.isEmpty()) {
                claimList = claims;
                // Si des réclamations existent, initialiser et associer l'adaptateur
                adapter = new ClaimAdminDetailAdapter(claimList, this); // Le fragment implémente le listener
                recyclerView.setAdapter(adapter);
            } else {
                // Si aucune réclamation, on peut mettre un adaptateur vide ou afficher un message
                recyclerView.setAdapter(null);
            }
        });
    }

    // Charger les réclamations d'un professeur spécifique basé sur son CIN
    private void getClaimByProf() {
        Log.d("DetailAbsenceFragment", "Chargement des absences pour le professeur : " + profCin);
        claimViewModel.getClaimByProf(profCin);
    }

    // Méthodes pour accepter ou rejeter une réclamation en mettant à jour localement la liste
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAccept(Claim claim) {
        if (claimList != null) {
            claimList.remove(claim); // Retirer la réclamation de la liste
            adapter.notifyDataSetChanged(); // Mettre à jour l'adaptateur pour refléter le changement
            claimViewModel.onAccept(claim.getIdClaim()); // Effectuer l'acceptation via ViewModel

            // Afficher un message de confirmation
            Toast.makeText(getContext(), "Réclamation approuvée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onReject(Claim claim) {
        if (claimList != null) {
            claimList.remove(claim); // Retirer la réclamation de la liste
            adapter.notifyDataSetChanged(); // Mettre à jour l'adaptateur
            claimViewModel.onReject(claim.getIdClaim()); // Rejeter la réclamation via ViewModel

            // Afficher un message de confirmation
            Toast.makeText(getContext(), "Réclamation rejetée avec succès", Toast.LENGTH_SHORT).show();
        }
    }
}
