package com.example.gestionabscenceenseignants.view;

import static android.content.ContentValues.TAG;

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
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.Adapter.DetailAbsenceAdapter;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;

import java.util.List;

public class DetailAbsenceFragment extends Fragment implements DetailAbsenceAdapter.OnAbsenceActionListener {
    private RecyclerView recyclerView;
    private DetailAbsenceAdapter adapter;
    private AbsenceViewModel absenceViewModel;
    private String profCin, profname, nbAbsence;
    private List<Absence> absenceList; // Liste des absences pour gérer les mises à jour

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
                // Mise à jour de la liste des absences
                absenceList = absences;
                // Initialisation de l'adaptateur pour afficher les absences
                Log.d("DetailAbsenceFragment", "Initialisation de l'adaptateur avec " + absences.size() + " absences.");
                adapter = new DetailAbsenceAdapter(absenceList, this); // Passer le fragment comme listener
                recyclerView.setAdapter(adapter);
            } else {
                // Afficher un message ou une vue de type "Aucune absence" si la liste est vide
                Log.d("DetailAbsenceFragment", "Aucune absence à afficher.");
                recyclerView.setAdapter(null); // Optionnellement, vous pouvez mettre un adaptateur vide ou un message
            }
        });

        return rootView;
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
    public void onDelete(Absence absence) {
        // Gestion de la suppression de l'absence
        if (absenceList != null) {
            absenceList.remove(absence); // Supprimer localement de la liste
            adapter.notifyDataSetChanged(); // Mettre à jour l'adaptateur
            absenceViewModel.deleteAbsence(absence.getIdAbsence(), absence.getCin());
            Log.d("DetailAbsenceFragment", "Absence supprimée : " + absence.toString());

            // Afficher un message de confirmation
            Toast.makeText(getContext(), "Absence supprimée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEdit(Absence absence) {
        // Gestion de l'édition de l'absence
        Log.d("DetailAbsenceFragment", "Modification de l'absence : " + absence.toString());
        // Créer un nouveau fragment EditAbsenceFragment
        EditAbsenceFragment editFragment = new EditAbsenceFragment();
        // Passer les données de l'absence sélectionnée en tant qu'arguments
        Bundle args = new Bundle();
        args.putString("idAbsence", absence.getIdAbsence());
        args.putString("profName",absence.getProfName());
        args.putString("cin",absence.getCin());
        args.putString("salle", absence.getSalle());
        args.putString("date", absence.getDate());
        args.putString("startTime", absence.getStartTime());
        args.putString("endTime", absence.getEndTime());
        args.putString("classe", absence.getClasse());
        args.putString("status", absence.getStatus());
        editFragment.setArguments(args);

        // Remplacer le fragment actuel par EditAbsenceFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editFragment) // ID du conteneur de fragments
                .addToBackStack(null) // Ajouter à la pile pour permettre un retour en arrière
                .commit();
    }
        // Logic for editing (e.g., navigate to a new screen or show a dialog)
}
