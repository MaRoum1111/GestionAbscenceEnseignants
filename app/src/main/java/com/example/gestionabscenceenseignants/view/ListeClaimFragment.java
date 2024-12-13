package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.Adapter.ClaimAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.Adapter.ClaimAdminDetailAdapter;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Claim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListeClaimFragment extends Fragment implements ClaimAdapter.OnClaimClickListener {

    private RecyclerView recyclerView;
    private ClaimAdapter adapter;
    private ClaimViewModel claimViewModel;
    private List<Claim> claimList;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue du fragment
        View view = inflater.inflate(R.layout.fragment_liste_claim, container, false);

        // Initialiser les composants de l'interface utilisateur
        initializeUI(view);

        // Initialiser et observer le ViewModel
        initializeViewModel();

        // Gérer le clic sur le bouton flottant
        setupFloatingActionButton(view);

        return view;
    }

    /**
     * Fonction pour initialiser l'interface utilisateur (UI)
     */
    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewClaims);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * Fonction pour initialiser le ViewModel et observer les réclamations
     */
    private void initializeViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        // Charger les réclamations dans le ViewModel
        claimViewModel.getClaimsForCurrentTeacher();

        // Observer les réclamations et mettre à jour l'adaptateur
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), claims -> {
            Log.d("DetailRéclamationsFragment", "réclamations reçues : " + (claims != null ? claims.size() : "null"));
            if (claims != null && !claims.isEmpty()) {
                // Mise à jour de la liste des réclamations
                claimList = claims;
                // Initialisation de l'adaptateur pour afficher les réclamations
                adapter = new ClaimAdapter(claimList, this); // Passer le fragment comme listener
                recyclerView.setAdapter(adapter);
            } else {
                // Afficher un message si aucune réclamation
                Log.d("DetailRéclamationsFragment", "Aucune réclamation à afficher.");
                recyclerView.setAdapter(null); // Optionnellement, vous pouvez mettre un adaptateur vide ou un message
            }
        });
    }

    /**
     * Fonction pour configurer le bouton flottant pour ajouter une réclamation
     */
    private void setupFloatingActionButton(View view) {
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> navigateToClaimFragment());
    }

    /**
     * Fonction pour naviguer vers le fragment de création d'une réclamation
     */
    private void navigateToClaimFragment() {
        Fragment claimFragment = new ClaimFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, claimFragment)
                .addToBackStack(null)  // Ajoute à la pile pour pouvoir revenir
                .commit();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDelete(Claim claim) {
        // Fonction pour gérer la suppression de la réclamation
        if (claimList != null) {
            claimList.remove(claim);
            adapter.notifyDataSetChanged();
            claimViewModel.deleteClaim(claim.getIdClaim());
            Log.d("ListeClaimFragment", "Réclamation supprimée : " + claim.toString());

            // Afficher un message de confirmation
            Toast.makeText(getContext(), "Réclamation supprimée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEdit(Claim claim) {
        // Fonction pour gérer l'édition de la réclamation
        Log.d("ListeClaimFragment", "Modification de la réclamation : " + claim.toString());
        navigateToEditClaimFragment(claim);
    }

    /**
     * Fonction pour naviguer vers le fragment d'édition de la réclamation
     */
    private void navigateToEditClaimFragment(Claim claim) {
        EditClaimFragment editClaimFragment = new EditClaimFragment();

        // Passer les données de la réclamation sélectionnée en tant qu'arguments
        Bundle args = new Bundle();
        args.putString("idClaim", claim.getIdClaim());
        args.putString("date", claim.getDate());
        args.putString("startTime", claim.getStartTime());
        args.putString("endTime", claim.getEndTime());
        args.putString("claim", claim.getClaim());
        args.putString("classe", claim.getClasse());
        args.putString("claimDate", claim.getClaimDate());
        args.putString("status",claim.getStatus());
        editClaimFragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editClaimFragment) // ID du conteneur de fragments
                .addToBackStack(null) // Ajouter à la pile pour permettre un retour en arrière
                .commit();
    }
}
