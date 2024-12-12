package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log; // Importer la classe Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.Adapter.ClaimAdapter;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Claim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListeClaimFragment extends Fragment implements ClaimAdapter.OnClaimClickListener  {
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

        // Initialisation du RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewClaims);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Initialisation du ViewModel pour récupérer les absences
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        claimViewModel.getClaimsForCurrentTeacher(); // Charger les données dans le ViewModel

        // Observer les absences et mettre à jour l'adaptateur
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), claims -> {
            Log.d("DetailRéclamationsFragment", "réclamations reçues : " + (claims != null ? claims.size() : "null"));
            if (claims != null && !claims.isEmpty()) {
                // Mise à jour de la liste des absences
                claimList = claims;
                // Initialisation de l'adaptateur pour afficher les absences
                Log.d("DetailRéclamationsFragment", "Initialisation de l'adaptateur avec " + claims.size() + " réclamations.");
                adapter = new ClaimAdapter(claimList,this); // Passer le fragment comme listener
                recyclerView.setAdapter(adapter);
            } else {
                // Afficher un message ou une vue de type "Aucune absence" si la liste est vide
                Log.d("DetailRéclamationsFragment", "Aucune réclamation à afficher.");
                recyclerView.setAdapter(null); // Optionnellement, vous pouvez mettre un adaptateur vide ou un message
            }
        });
        // Gérer le clic du bouton flottant
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> {

            Fragment ClaimFragement = new ClaimFragment();

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, ClaimFragement)
                    .addToBackStack(null)  // Ajoute à la pile pour pouvoir revenir
                    .commit();
        });
        return view;
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDelete(Claim claim) {
        // Gestion de la suppression de l'absence
        if (claimList != null) {
            claimList.remove(claim);
            adapter.notifyDataSetChanged();
            claimViewModel.deleteClaim(claim.getIdClaim());
            Log.d("ListeClaimFragment", "réclamation supprimée : " + claim.toString());

            // Afficher un message de confirmation
            Toast.makeText(getContext(), "Réclamation supprimée avec succès", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onEdit(Claim claim) {
        // Gestion de l'édition de l'absence
        Log.d("ListeClaimFragment", "Modification de la réclamation : " + claim.toString());
        editClaimFragment editClaimFragment = new editClaimFragment();

        // Passer les données de l'absence sélectionnée en tant qu'arguments
        Bundle args = new Bundle();
        args.putString("idClaim", claim.getIdClaim());
        args.putString("date", claim.getDate());
        args.putString("startTime", claim.getStartTime());
        args.putString("endTime", claim.getEndTime());
        args.putString("claim", claim.getClaim());
        args.putString("classe", claim.getClasse());
        args.putString("claimDate", claim.getClaimDate());
        editClaimFragment.setArguments(args);


        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editClaimFragment) // ID du conteneur de fragments
                .addToBackStack(null) // Ajouter à la pile pour permettre un retour en arrière
                .commit();
    }
}
