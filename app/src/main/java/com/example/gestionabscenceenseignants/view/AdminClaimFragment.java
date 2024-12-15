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

// Classe pour afficher et gérer les réclamations des enseignants côté administrateur
public class AdminClaimFragment extends Fragment {

    private RecyclerView recyclerView;  // Composant pour afficher une liste sous forme de vue défilable
    private ClaimAdminAdapter adapter;  // Adaptateur pour lier les données des réclamations au RecyclerView
    private ClaimViewModel claimViewModel;  // ViewModel pour interagir avec les données des réclamations

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisation du ViewModel qui contient la logique métier liée aux réclamations
        initializeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Charger la vue associée à ce fragment
        View view = inflater.inflate(R.layout.fragment_admin_claim, container, false);

        // Configurer le RecyclerView pour afficher les réclamations
        initializeRecyclerView(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observer les données provenant du ViewModel et mettre à jour l'affichage
        observeAbsencesData();
    }

    // Méthode pour initialiser le ViewModel
    private void initializeViewModel() {
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);
    }

    // Configuration initiale du RecyclerView
    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAdminClaim);  // Lier le RecyclerView au fichier XML
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Disposer les éléments verticalement
    }

    // Observer les données des réclamations dans le ViewModel et les afficher dans le RecyclerView
    private void observeAbsencesData() {
        claimViewModel.getClaims();  // Charger les données des réclamations depuis le ViewModel
        claimViewModel.getClaim().observe(getViewLifecycleOwner(), new Observer<List<Claim>>() {
            @Override
            public void onChanged(List<Claim> claims) {
                if (claims != null) {
                    // Mettre à jour la liste affichée lorsqu'il y a des changements dans les données
                    updateRecyclerView(claims);
                }
            }
        });
    }

    // Méthode pour rafraîchir les données du RecyclerView
    private void updateRecyclerView(List<Claim> claims) {
        if (adapter == null) {
            // Créer un nouvel adaptateur si ce n'est pas encore fait
            adapter = new ClaimAdminAdapter(claims, new ClaimAdminAdapter.OnClaimClickListener() {
                @Override
                public void onClaimClick(String cin, String profName) {
                    // Gestion du clic sur un élément : afficher les détails d'une réclamation
                    openDetailAbsenceFragment(cin, profName);
                }
            });
            recyclerView.setAdapter(adapter);  // Associer l'adaptateur au RecyclerView
        } else {
            // Mettre à jour les données de l'adaptateur si celui-ci existe déjà
            adapter.updateData(claims);
        }
    }

    // Ouvrir un nouveau fragment pour afficher les détails d'une réclamation
    private void openDetailAbsenceFragment(String cin, String profName) {
        Bundle bundle = new Bundle();
        bundle.putString("cin", cin);  // Transmettre le CIN de l'enseignant
        bundle.putString("profName", profName);  // Transmettre le nom de l'enseignant

        // Créer une instance du fragment des détails et y passer les données
        AdminDetailClaimFragment adminDetailClaimFragment = new AdminDetailClaimFragment();
        adminDetailClaimFragment.setArguments(bundle);

        // Remplacer le fragment actuel par le nouveau et permettre un retour en arrière
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, adminDetailClaimFragment)
                .addToBackStack(null)
                .commit();
    }

}
