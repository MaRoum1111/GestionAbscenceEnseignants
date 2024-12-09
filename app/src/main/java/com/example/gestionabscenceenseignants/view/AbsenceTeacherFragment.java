package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AbsenceTeacherFragment extends Fragment {

    private RecyclerView recyclerView;
    private AbsenceAdapter adapter;
    private AbsenceViewModel absenceViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_absence, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser le ViewModel
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);

        // Observer les données des absences
        absenceViewModel.AbsencesByConnectedProf();
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), new Observer<List<Absence>>() {
            @Override
            public void onChanged(List<Absence> absences) {
                if (absences != null) {
                    // Vérifier si l'adaptateur est déjà initialisé
                    if (adapter == null) {
                        adapter = new AbsenceAdapter(absences, new AbsenceAdapter.OnAbsenceClickListener() {
                            @Override
                            public void onAbsenceClick(String cin, String profName, int absenceCount) {
                                // Création du Bundle pour passer les données au fragment de détail
                                Bundle bundle = new Bundle();
                                bundle.putString("cin", cin);
                                bundle.putString("profName", profName);
                                bundle.putInt("absenceCount", absenceCount);

                                // Remplacer le fragment actuel par DetailAbsenceFragment
                                DetailAbsenceFragment detailAbsenceFragment = new DetailAbsenceFragment();
                                detailAbsenceFragment.setArguments(bundle);

                                // Utiliser FragmentManager pour la transaction de fragment
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, detailAbsenceFragment)  // Remplacer le fragment actuel
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
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Affiche ou gère le message d'erreur (exemple : Toast, Dialog, etc.)
            }
        });

        // Gérer le clic du bouton flottant
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> {
            // Ouvrir le fragment AddAbsenceFragment
            Fragment addAbsenceFragment = new AddAbsenceFragment();

            // Remplacer le fragment actuel par le nouveau
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, addAbsenceFragment)
                    .addToBackStack(null)  // Ajoute à la pile pour pouvoir revenir
                    .commit();
        });

        return view;
    }
}
