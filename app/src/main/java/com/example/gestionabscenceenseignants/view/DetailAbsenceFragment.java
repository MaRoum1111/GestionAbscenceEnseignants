package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log; // Importer la classe Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.Adapter.DetailAbsenceAdapter;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;

public class DetailAbsenceFragment extends Fragment {
    private RecyclerView recyclerView;
    private DetailAbsenceAdapter adapter;
    private AbsenceViewModel absenceViewModel;
    private String profCin, profname, nbAbsence;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue du fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_absence, container, false);

        // Initialisation du RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView name = rootView.findViewById(R.id.teacher_name);
        TextView cin = rootView.findViewById(R.id.teacher_cin);
        TextView nb = rootView.findViewById(R.id.total_absences);

        // Récupérer les arguments passés au fragment (le CIN du professeur)
        if (getArguments() != null) {
            profCin = getArguments().getString("cin");
            profname = getArguments().getString("profName");
            nbAbsence = String.valueOf(getArguments().getInt("absenceCount", 0)); // Utilisation de int avec une valeur par défaut de 0
        }
        name.setText(profname);
        cin.setText(profCin);
        nb.setText("Total des absences: " + nbAbsence);

        // Initialisation du ViewModel pour récupérer les absences
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);

        // Charger les absences de ce professeur spécifique
        Log.d("DetailAbsenceFragment", "Chargement des absences pour le professeur : " + profCin);
        absenceViewModel.loadAbsencesByProf(profCin);

        // Observer les absences et mettre à jour l'adaptateur
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d("DetailAbsenceFragment", "Absences reçues : " + (absences != null ? absences.size() : "null"));

            if (absences != null && !absences.isEmpty()) {
                // Initialisation de l'adaptateur pour afficher les absences
                Log.d("DetailAbsenceFragment", "Initialisation de l'adaptateur avec " + absences.size() + " absences.");
                adapter = new DetailAbsenceAdapter(absences, new DetailAbsenceAdapter.OnAbsenceActionListener() {
                    @Override
                    public void onDelete(Absence absence) {
                        // Gestion de la suppression de l'absence
                        Log.d("DetailAbsenceFragment", "Suppression de l'absence : " + absence.toString());
                    }

                    @Override
                    public void onEdit(Absence absence) {
                        // Gestion de l'édition de l'absence (par exemple, ouvrir un formulaire d'édition)
                        Log.d("DetailAbsenceFragment", "Modification de l'absence : " + absence.toString());
                    }
                });

                // Affecter l'adaptateur au RecyclerView
                recyclerView.setAdapter(adapter);
            } else {
                // Afficher un message ou une vue de type "Aucune absence" si la liste est vide
                Log.d("DetailAbsenceFragment", "Aucune absence à afficher.");
                recyclerView.setAdapter(null); // Optionnellement, vous pouvez mettre un adaptateur vide ou un message
            }
        });

        return rootView;
    }

    // Méthode statique pour créer une nouvelle instance du fragment et passer le CIN
    public static DetailAbsenceFragment newInstance(String cin) {
        DetailAbsenceFragment fragment = new DetailAbsenceFragment();
        Bundle args = new Bundle();
        args.putString("CIN", cin); // Passer le CIN du professeur
        fragment.setArguments(args);
        return fragment;
    }
}
