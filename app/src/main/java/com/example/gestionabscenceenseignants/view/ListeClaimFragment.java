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

    private RecyclerView recyclerView;  // RecyclerView pour afficher les réclamations
    private ClaimAdapter adapter;  // Adaptateur pour gérer l'affichage des réclamations
    private ClaimViewModel claimViewModel;  // ViewModel pour gérer la logique des réclamations
    private List<Claim> claimList;  // Liste des réclamations à afficher

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue du fragment
        View view = inflater.inflate(R.layout.fragment_liste_claim, container, false);

        // Initialiser les composants de l'interface utilisateur
        initializeUI(view);

        // Initialiser et observer le ViewModel pour récupérer les réclamations
        initializeViewModel();

        // Configurer le bouton flottant pour ajouter une réclamation
        setupFloatingActionButton(view);

        return view;  // Retourner la vue du fragment
    }

    /**
     * Fonction pour initialiser l'interface utilisateur (UI)
     * Cette fonction configure le RecyclerView pour afficher les réclamations.
     */
    private void initializeUI(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewClaims);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Définir le gestionnaire de mise en page pour le RecyclerView
    }

    /**
     * Fonction pour initialiser le ViewModel et observer les réclamations.
     * Le ViewModel est chargé d'obtenir les réclamations et de les mettre à jour dans l'interface utilisateur.
     */
    private void initializeViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);  // Initialiser le ViewModel

        // Charger les réclamations pour le professeur actuel
        claimViewModel.getClaimsForCurrentTeacher();

        // Observer les réclamations et mettre à jour l'adaptateur
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), claims -> {
            Log.d("DetailRéclamationsFragment", "réclamations reçues : " + (claims != null ? claims.size() : "null"));
            if (claims != null && !claims.isEmpty()) {
                // Mettre à jour la liste des réclamations
                claimList = claims;
                // Initialiser l'adaptateur pour afficher les réclamations
                adapter = new ClaimAdapter(claimList, this);  // Passer le fragment comme listener pour gérer les clics
                recyclerView.setAdapter(adapter);  // Assigner l'adaptateur au RecyclerView
            } else {
                // Afficher un message si aucune réclamation
                Log.d("DetailRéclamationsFragment", "Aucune réclamation à afficher.");
                recyclerView.setAdapter(null);  // Retirer l'adaptateur si aucune réclamation
            }
        });
    }

    /**
     * Fonction pour configurer le bouton flottant permettant d'ajouter une réclamation.
     * Lorsqu'on clique sur le bouton, on navigue vers le fragment de création de réclamation.
     */
    private void setupFloatingActionButton(View view) {
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);  // Récupérer le bouton flottant
        boutonAdd.setOnClickListener(v -> navigateToClaimFragment());  // Configurer l'action au clic pour naviguer vers le fragment de création
    }

    /**
     * Fonction pour naviguer vers le fragment de création d'une réclamation.
     * Cela permet à l'utilisateur d'ajouter une nouvelle réclamation.
     */
    private void navigateToClaimFragment() {
        Fragment claimFragment = new ClaimFragment();  // Créer une instance du fragment de réclamation
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, claimFragment)  // Remplacer l'ancien fragment par le nouveau
                .addToBackStack(null)  // Ajouter l'opération à la pile arrière pour pouvoir revenir en arrière
                .commit();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDelete(Claim claim) {
        // Fonction pour gérer la suppression d'une réclamation
        if (claimList != null) {
            claimList.remove(claim);  // Supprimer la réclamation de la liste
            adapter.notifyDataSetChanged();  // Notifier l'adaptateur que les données ont changé
            claimViewModel.deleteClaim(claim.getIdClaim());  // Supprimer la réclamation dans la base de données via le ViewModel
            Log.d("ListeClaimFragment", "Réclamation supprimée : " + claim.toString());

            // Afficher un message de confirmation à l'utilisateur
            Toast.makeText(getContext(), "Réclamation supprimée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEdit(Claim claim) {
        // Fonction pour gérer l'édition d'une réclamation
        Log.d("ListeClaimFragment", "Modification de la réclamation : " + claim.toString());
        navigateToEditClaimFragment(claim);  // Naviguer vers le fragment d'édition avec la réclamation sélectionnée
    }

    /**
     * Fonction pour naviguer vers le fragment d'édition de la réclamation.
     * Les informations de la réclamation sont passées en argument au fragment d'édition.
     */
    private void navigateToEditClaimFragment(Claim claim) {
        EditClaimFragment editClaimFragment = new EditClaimFragment();  // Créer une instance du fragment d'édition

        // Passer les données de la réclamation sélectionnée en tant qu'arguments
        Bundle args = new Bundle();
        args.putString("idClaim", claim.getIdClaim());  // Ajouter l'ID de la réclamation
        args.putString("date", claim.getDate());  // Ajouter la date de la réclamation
        args.putString("startTime", claim.getStartTime());  // Ajouter l'heure de début
        args.putString("endTime", claim.getEndTime());  // Ajouter l'heure de fin
        args.putString("claim", claim.getClaim());  // Ajouter le texte de la réclamation
        args.putString("classe", claim.getClasse());  // Ajouter la classe concernée
        args.putString("claimDate", claim.getClaimDate());  // Ajouter la date de la réclamation
        args.putString("status",claim.getStatus());  // Ajouter le statut de la réclamation
        editClaimFragment.setArguments(args);  // Passer les arguments au fragment

        // Remplacer le fragment actuel par le fragment d'édition
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editClaimFragment)  // ID du conteneur de fragments
                .addToBackStack(null)  // Ajouter à la pile pour permettre un retour en arrière
                .commit();
    }
}
