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
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.model.Absence;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class AbsenceFragment extends Fragment {

    private RecyclerView recyclerView;  // RecyclerView pour afficher les absences
    private AbsenceAdapter adapter;  // L'adaptateur pour gérer la liste d'absences
    private AbsenceViewModel absenceViewModel;  // ViewModel pour gérer les données d'absences

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialiser le ViewModel pour obtenir et gérer les données des absences
        initializeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflater la vue pour le fragment
        View view = inflater.inflate(R.layout.fragment_absence, container, false);

        // Initialiser le RecyclerView pour afficher les absences
        initializeRecyclerView(view);

        // Initialiser le bouton flottant pour ajouter une absence
        initializeFloatingActionButton(view);

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
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
    }

    // Fonction d'initialisation du RecyclerView
    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Utilisation d'un LinearLayoutManager pour la liste
    }

    // Fonction d'initialisation du bouton flottant pour ajouter une absence
    private void initializeFloatingActionButton(View view) {
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> openAddAbsenceFragment());  // Ouvrir le fragment d'ajout d'absence lorsque le bouton est cliqué
    }

    // Fonction pour ouvrir le fragment AddAbsenceFragment
    private void openAddAbsenceFragment() {
        Fragment addAbsenceFragment = new AddAbsenceFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, addAbsenceFragment)  // Remplacer le fragment actuel par celui d'ajout d'absence
                .addToBackStack(null)  // Ajouter la transaction à la pile arrière pour pouvoir revenir en arrière
                .commit();
    }

    // Fonction pour observer les données d'absences et mettre à jour le RecyclerView
    private void observeAbsencesData() {
        absenceViewModel.loadAbsenceCountsByProf();  // Charger les données d'absences par professeur
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), new Observer<List<Absence>>() {
            @Override
            public void onChanged(List<Absence> absences) {
                if (absences != null) {
                    updateRecyclerView(absences);  // Mettre à jour l'adaptateur avec les nouvelles données
                }
            }
        });
    }

    // Fonction pour mettre à jour le RecyclerView avec les données des absences
    private void updateRecyclerView(List<Absence> absences) {
        if (adapter == null) {
            // Si l'adaptateur n'est pas encore initialisé, créer un nouvel adaptateur
            adapter = new AbsenceAdapter(absences, new AbsenceAdapter.OnAbsenceClickListener() {
                @Override
                public void onAbsenceClick(String cin, String profName, int absenceCount) {
                    openDetailAbsenceFragment(cin, profName, absenceCount);  // Ouvrir le fragment de détail d'absence lors du clic sur une absence
                }
            });
            recyclerView.setAdapter(adapter);  // Associer l'adaptateur au RecyclerView
        } else {
            adapter.updateData(absences);  // Si l'adaptateur existe déjà, mettre à jour les données
        }
    }

    // Fonction pour ouvrir le fragment de détail d'absence avec les informations nécessaires
    private void openDetailAbsenceFragment(String cin, String profName, int absenceCount) {
        Bundle bundle = new Bundle();
        bundle.putString("cin", cin);
        bundle.putString("profName", profName);
        bundle.putInt("absenceCount", absenceCount);

        DetailAbsenceFragment detailAbsenceFragment = new DetailAbsenceFragment();
        detailAbsenceFragment.setArguments(bundle);  // Passer les données au fragment de détail

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, detailAbsenceFragment)  // Remplacer le fragment actuel par celui de détail
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
