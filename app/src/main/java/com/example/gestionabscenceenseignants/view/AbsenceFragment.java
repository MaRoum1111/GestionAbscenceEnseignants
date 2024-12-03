package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Correct import for FloatingActionButton
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.Adapter.AbsenceAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.model.Absence;

import java.util.List;

public class AbsenceFragment extends Fragment {

    private RecyclerView recyclerView;
    private AbsenceAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_absence, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser le ViewModel
        AbsenceViewModel absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        absenceViewModel.loadAbsences();

        // Observer la liste des absences
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), new Observer<List<Absence>>() {
            @Override
            public void onChanged(List<Absence> absences) {
                if (absences != null) {
                    adapter = new AbsenceAdapter(absences);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        // Observer les messages d'erreur (si nécessaire)
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Affiche ou gère le message d'erreur
            }
        });

        // Gérer le clic du bouton flottant
        FloatingActionButton boutonadd = view.findViewById(R.id.btnFloatingAction); // Corrected to FloatingActionButton
        boutonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir le fragment AddAbsenceFragment
                Fragment addAbsenceFragment = new AddAbsenceFragment();

                // Remplacer le fragment actuel par le nouveau
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, addAbsenceFragment)
                        .addToBackStack(null) // Ajoute à la pile pour pouvoir revenir
                        .commit();
            }
        });

        return view;
    }
}