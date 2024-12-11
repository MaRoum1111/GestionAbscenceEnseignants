package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestionabscenceenseignants.Adapter.ClaimAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.model.Claim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class ListeClaimFragement extends Fragment {

    private RecyclerView recyclerView;
    private ClaimAdapter adapter;
    private ClaimViewModel ClaimViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_liste_claim, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewClaims);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser le ViewModel
        ClaimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        
        ClaimViewModel.getClaimsForCurrentTeacher();
        ClaimViewModel.getClaim().observe(getViewLifecycleOwner(), new Observer<List<Claim>>() {
            @Override
            public void onChanged(List<Claim> claims) {
                if (claims != null) {
                    // Vérifier si l'adaptateur est déjà initialisé
                    if (adapter == null) {
                        adapter = new ClaimAdapter(claims, new ClaimAdapter.OnClaimClickListener() {
                            @Override
                            public void onClaimClick(String claimDate) {
                                // Création du Bundle pour passer les données au fragment de détail
                                Bundle bundle = new Bundle();
                                bundle.putString("date", claimDate);
                                // Remplacer le fragment actuel par DetailAbsenceFragment

                                DetailClaimFragement detailClaimFragement = new DetailClaimFragement();
                                detailClaimFragement.setArguments(bundle);

                                // Utiliser FragmentManager pour la transaction de fragment
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, detailClaimFragement)  // Remplacer le fragment actuel
                                        .addToBackStack(null)  // Ajouter à la pile arrière pour pouvoir revenir en arrière
                                        .commit();
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        // Mettre à jour l'adaptateur avec les nouvelles données
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        // Observer les messages d'erreur (si nécessaire)
        ClaimViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Affiche ou gère le message d'erreur (exemple : Toast, Dialog, etc.)
            }
        });

        // Gérer le clic du bouton flottant
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> {
            // Ouvrir le fragment AddAbsenceFragment
            Fragment claimFragement = new ClaimFragement();

            // Remplacer le fragment actuel par le nouveau
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, claimFragement)
                    .addToBackStack(null)  // Ajoute à la pile pour pouvoir revenir
                    .commit();
        });

        return view;
    }
}