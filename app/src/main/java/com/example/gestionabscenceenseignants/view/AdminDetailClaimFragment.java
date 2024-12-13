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
import com.example.gestionabscenceenseignants.Adapter.DetailAbsenceAdapter;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.List;

public class AdminDetailClaimFragment extends Fragment implements ClaimAdminDetailAdapter.OnClaimDetailClickListener {
    private RecyclerView recyclerView;
    private ClaimAdminDetailAdapter adapter;
    private ClaimViewModel claimViewModel;
    private String profCin, profname;
    private List<Claim> claimList; // Liste des absences pour gérer les mises à jour

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue du fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_detail_claim, container, false);

        // Initialisation du RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewClaims);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialisation des TextViews
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView name = rootView.findViewById(R.id.teacher_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView cin = rootView.findViewById(R.id.teacher_cin);
        // Récupérer les arguments passés au fragment (le CIN du professeur)
        getArgumentsData();

        // Affichage des informations du professeur
        displayTeacherInfo(name, cin);

        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        getClaimByProf();

        observeClaims();

        return rootView;
    }

    private void getArgumentsData() {
        // Récupérer les arguments passés au fragment
        if (getArguments() != null) {
            profCin = getArguments().getString("cin");
            profname = getArguments().getString("profName");

        }
    }

    private void displayTeacherInfo(TextView name, TextView cin) {
        name.setText(profname);
        cin.setText(profCin);
    }

    private void observeClaims() {
        // Observer les absences et mettre à jour l'adaptateur
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), claims -> {
            if (claims != null && !claims.isEmpty()) {
                claimList = claims;
                adapter = new ClaimAdminDetailAdapter(claimList, this); // Passer le fragment comme listener
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setAdapter(null); // Optionnellement, vous pouvez mettre un adaptateur vide ou un message
            }
        });
    }

    private void getClaimByProf() {
        // Charger les absences pour le professeur spécifique
        Log.d("DetailAbsenceFragment", "Chargement des absences pour le professeur : " + profCin);
        claimViewModel.getClaimByProf(profCin);
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d("DetailAbsenceFragment", "onStart called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DetailAbsenceFragment", "onResume called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("DetailAbsenceFragment", "onPause called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("DetailAbsenceFragment", "onStop called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DetailAbsenceFragment", "onDestroy called");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onAccept(Claim claim) {
        // Gestion de la suppression de l'absence
        if (claimList != null) {
            claimList.remove(claim); // Supprimer localement de la liste
            adapter.notifyDataSetChanged(); // Mettre à jour l'adaptateur
            claimViewModel.onAccept(claim.getIdClaim());

            // Afficher un message de confirmation
            Toast.makeText(getContext(), "Réclamation approuvée avec succès", Toast.LENGTH_SHORT).show();
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onReject(Claim claim) {
        // Gestion de la suppression de l'absence
        if (claimList != null) {
            claimList.remove(claim); // Supprimer localement de la liste
            adapter.notifyDataSetChanged(); // Mettre à jour l'adaptateur
            claimViewModel.onReject(claim.getIdClaim());

            Toast.makeText(getContext(), "Réclamation réjeter avec succès", Toast.LENGTH_SHORT).show();
        }
    }

}
