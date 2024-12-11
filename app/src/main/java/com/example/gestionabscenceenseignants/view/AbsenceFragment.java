package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
        // Initialisation des données ou des composants nécessaires avant la création de la vue
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Charger la vue du fragment
        View view = inflater.inflate(R.layout.fragment_absence, container, false);

        // Initialisation des composants de la vue
        recyclerView = view.findViewById(R.id.recyclerViewAbsences);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FloatingActionButton boutonAdd = view.findViewById(R.id.btnFloatingAction);
        boutonAdd.setOnClickListener(v -> {
            Fragment addAbsenceFragment = new AddAbsenceFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, addAbsenceFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observer des absences
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
                        adapter.notifyDataSetChanged(); // Rafraîchit l'adaptateur si nécessaire
                    }
                }
            }
        });

        // Observer des messages d'erreur
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                // Afficher un message d'erreur si nécessaire
                Toast.makeText(getContext(), "Erreur: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Code pour recharger les données ou initialiser des composants spécifiques
    }

    @Override
    public void onResume() {
        super.onResume();
        // Code pour réactiver les écouteurs ou autres interactions utilisateur
    }

    @Override
    public void onPause() {
        super.onPause();
        // Sauvegarder l'état temporaire ou arrêter des processus lourds
    }

    @Override
    public void onStop() {
        super.onStop();
        // Libérer des ressources ou sauvegarder des états persistants
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Libérer les ressources liées à la vue (adapter, listeners, etc.)
        adapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Nettoyer les ressources globales ou arrêter les services
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Code à exécuter lorsque le fragment est détaché de l'activité
    }
}