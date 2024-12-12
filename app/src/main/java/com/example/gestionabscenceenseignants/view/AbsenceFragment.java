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

    private RecyclerView recyclerView;
    private AbsenceAdapter adapter;
    private AbsenceViewModel absenceViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialisations qui ne nécessitent pas d'éléments de la vue
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_absence, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gérer le clic du bouton flottant
        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> {
            Fragment addAbsenceFragment = new AddAbsenceFragment();

            // Remplacer le fragment actuel par le nouveau
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, addAbsenceFragment)
                    .addToBackStack(null)  // Ajoute à la pile pour pouvoir revenir
                    .commit();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observer les données des absences
        absenceViewModel.loadAbsenceCountsByProf();
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), new Observer<List<Absence>>() {
            @Override
            public void onChanged(List<Absence> absences) {
                if (absences != null) {
                    if (adapter == null) {
                        adapter = new AbsenceAdapter(absences, new AbsenceAdapter.OnAbsenceClickListener() {
                            @Override
                            public void onAbsenceClick(String cin, String profName, int absenceCount) {
                                Bundle bundle = new Bundle();
                                bundle.putString("cin", cin);
                                bundle.putString("profName", profName);
                                bundle.putInt("absenceCount", absenceCount);

                                DetailAbsenceFragment detailAbsenceFragment = new DetailAbsenceFragment();
                                detailAbsenceFragment.setArguments(bundle);

                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.frame_layout, detailAbsenceFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateData(absences); // Mise à jour des données
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Appelé lorsque le fragment devient visible à l'utilisateur
    }

    @Override
    public void onResume() {
        super.onResume();
        // Appelé lorsque l'utilisateur peut interagir avec le fragment
    }

    @Override
    public void onPause() {
        super.onPause();
        // Appelé lorsque l'utilisateur quitte temporairement le fragment
    }

    @Override
    public void onStop() {
        super.onStop();
        // Appelé lorsque le fragment n'est plus visible
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Libérer les ressources liées à la vue
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Effectuer les nettoyages finaux du fragment
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Appelé lorsque le fragment est détaché de l'activité
    }
}
