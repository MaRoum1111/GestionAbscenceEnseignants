package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.Adapter.AbsenceAdapter;
import com.example.gestionabscenceenseignants.Adapter.ClaimAdminAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.model.Claim;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class AdminClaimFragment extends Fragment {

    private RecyclerView recyclerView;  // RecyclerView pour afficher les absences
    private ClaimAdminAdapter adapter;  // L'adaptateur pour gérer la liste d'absences
    private ClaimViewModel claimViewModel;  // ViewModel pour gérer les données d'absences

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialiser le ViewModel pour obtenir et gérer les données des absences
        initializeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflater la vue pour le fragment
        View view = inflater.inflate(R.layout.fragment_admin_claim, container, false);

        // Initialiser le RecyclerView pour afficher les absences
        initializeRecyclerView(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observer les données des absences et mettre à jour l'interface utilisateur en conséquence
        observeAbsencesData();
    }

    // Fonction d'initialisation du ViewModel pour gérer les données
    private void initializeViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);
    }

    // Fonction d'initialisation du RecyclerView
    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAdminClaim);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Utilisation d'un LinearLayoutManager pour la liste
    }


    // Fonction pour observer les données d'absences et mettre à jour le RecyclerView
    private void observeAbsencesData() {
        claimViewModel.getClaims();
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), new Observer<List<Claim>>() {
            @Override
            public void onChanged(List<Claim> claims) {
                if (claims != null) {
                    updateRecyclerView(claims);  // Mettre à jour l'adaptateur avec les nouvelles données
                }
            }
        });
    }

    // Fonction pour mettre à jour le RecyclerView avec les données des absences
    private void updateRecyclerView(List<Claim> claims) {
        if (adapter == null) {
            // Si l'adaptateur n'est pas encore initialisé, créer un nouvel adaptateur
            adapter = new ClaimAdminAdapter(claims, new ClaimAdminAdapter.OnClaimClickListener(){
                @Override
                public void onClaimClick(String cin, String profName){
                    openDetailAbsenceFragment(cin, profName);  // Ouvrir le fragment de détail d'absence lors du clic sur une absence
                }
            });
            recyclerView.setAdapter(adapter);  // Associer l'adaptateur au RecyclerView
        } else {
            adapter.updateData(claims);  // Si l'adaptateur existe déjà, mettre à jour les données
        }
    }

    // Fonction pour ouvrir le fragment de détail d'absence avec les informations nécessaires
    private void openDetailAbsenceFragment(String cin, String profName) {
        Bundle bundle = new Bundle();
        bundle.putString("cin", cin);
        bundle.putString("profName", profName);


        AdminDetailClaimFragment adminDetailClaimFragment = new AdminDetailClaimFragment();
        adminDetailClaimFragment.setArguments(bundle);  // Passer les données au fragment de détail

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, adminDetailClaimFragment)  // Remplacer le fragment actuel par celui de détail
                .addToBackStack(null)  // Ajouter à la pile pour pouvoir revenir en arrière
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
