package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.AutoCompleteTextView;
import java.util.List;

/**
 * Fragment pour afficher et gérer les absences.
 * Implémente une interface pour détecter les clics sur les éléments de la liste des absences.
 */
public class AbsenceFragment extends Fragment implements AbsenceAdapter.OnAbsenceClickListener {

    private RecyclerView recyclerView;  // RecyclerView pour afficher la liste des absences
    private AbsenceAdapter adapter;  // Adaptateur pour gérer et afficher les données des absences
    private AbsenceViewModel absenceViewModel;  // ViewModel pour manipuler les données des absences

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisation du ViewModel pour gérer les données d'absences
        initializeViewModel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gonfle la vue du fragment à partir du fichier XML
        View view = inflater.inflate(R.layout.fragment_absence, container, false);

        // Initialisation du RecyclerView
        initializeRecyclerView(view);

        // Initialisation du bouton flottant pour ajouter une absence
        initializeFloatingActionButton(view);

        // Initialisation de la barre de recherche pour filtrer les absences
        AutoCompleteTextView autoCompleteSearch = view.findViewById(R.id.search_bar);
        autoCompleteSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Action avant toute modification (non utilisée ici)
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString();
                // Recherche si la chaîne a au moins 3 caractères, sinon recharger les absences
                if (query.length() >= 3) {
                    absenceViewModel.searchAbsences(query);
                } else {
                    absenceViewModel.loadAbsenceCountsByProf();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Action après modification (non utilisée ici)
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observation des données d'absences et mise à jour de l'interface utilisateur
        observeAbsencesData();
    }

    // Initialisation du ViewModel pour interagir avec les données
    private void initializeViewModel() {
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
    }

    // Configuration du RecyclerView pour afficher les absences
    private void initializeRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Utilisation d'un layout en liste
    }

    // Configuration du bouton flottant pour ajouter une absence
    private void initializeFloatingActionButton(View view) {
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> openAddAbsenceFragment()); // Ouvre le fragment d'ajout d'absence
    }

    // Ouvre le fragment pour ajouter une nouvelle absence
    private void openAddAbsenceFragment() {
        Fragment addAbsenceFragment = new AddAbsenceFragment();
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, addAbsenceFragment) // Remplace le fragment actuel
                .addToBackStack(null) // Ajoute la transaction à la pile arrière
                .commit();
    }

    // Observe les données d'absences et met à jour le RecyclerView
    private void observeAbsencesData() {
        absenceViewModel.loadAbsenceCountsByProf(); // Charge les absences initiales
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), new Observer<List<Absence>>() {
            @Override
            public void onChanged(List<Absence> absences) {
                if (absences != null) {
                    adapter = new AbsenceAdapter(absences, AbsenceFragment.this); // Initialise l'adaptateur
                    recyclerView.setAdapter(adapter);
                    updateRecyclerView(absences); // Met à jour les données dans l'adaptateur
                }
            }
        });
    }

    // Met à jour les données du RecyclerView avec les absences
    private void updateRecyclerView(List<Absence> absences) {
        if (adapter == null) {
            recyclerView.setAdapter(adapter); // Associe l'adaptateur si non défini
        } else {
            adapter.updateData(absences); // Met à jour les données existantes
        }
    }

    // Ouvre le fragment de détail pour afficher plus d'informations sur une absence
    private void openDetailAbsenceFragment(String cin, String profName, int absenceCount) {
        Bundle bundle = new Bundle();
        bundle.putString("cin", cin);
        bundle.putString("profName", profName);
        bundle.putInt("absenceCount", absenceCount);

        DetailAbsenceFragment detailAbsenceFragment = new DetailAbsenceFragment();
        detailAbsenceFragment.setArguments(bundle); // Passe les données au fragment

        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, detailAbsenceFragment) // Remplace le fragment actuel
                .addToBackStack(null) // Ajoute la transaction à la pile arrière
                .commit();
    }

    // Implémentation du clic sur une absence pour afficher ses détails
    @Override
    public void onAbsenceClick(String cin, String profName, int absenceCount) {
        openDetailAbsenceFragment(cin, profName, absenceCount);
    }
}
