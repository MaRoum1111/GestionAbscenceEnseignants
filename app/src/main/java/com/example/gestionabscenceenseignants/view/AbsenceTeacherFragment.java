package com.example.gestionabscenceenseignants.view;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.Adapter.TeacherAbsenceAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import java.util.List;

public class AbsenceTeacherFragment extends Fragment {
    private RecyclerView recyclerView; // RecyclerView pour afficher la liste des absences
    private TeacherAbsenceAdapter adapter; // Adaptateur pour le RecyclerView
    private AbsenceViewModel absenceViewModel; // ViewModel pour gérer les absences
    private List<Absence> absenceList;
    private TextView teacherNameTextView; // TextView pour afficher le nom de l'enseignant
    private TextView totalAbsencesTextView; // TextView pour afficher le total des absences

    // Méthode appelée pour créer la vue du fragment
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("absenecteacher", "onCreateView: Création de la vue pour AbsenceTeacherFragment");
        View rootView = inflater.inflate(R.layout.fragement_absence_teacher, container, false);

        // Initialisation des vues principales
        initializeViews(rootView);

        // Observer les absences pour le professeur actuel
        observeAbsencesData();

        return rootView;
    }

    // Fonction pour initialiser les vues et les composants du fragment
    private void initializeViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_view); // RecyclerView pour afficher les absences
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Mise en place d'un layout manager vertical

        // Initialisation des TextViews pour afficher les informations du professeur
        teacherNameTextView = rootView.findViewById(R.id.teacher_name); // Corrigé : initialisation correcte du TextView
        totalAbsencesTextView = rootView.findViewById(R.id.total_absences); // Initialisation du TextView pour le total des absences

        // Appel pour récupérer les absences pour le professeur actuel
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class); // Initialisation du ViewModel
        absenceViewModel.getAbsencesForCurrentTeacher();
    }

    // Fonction pour observer les absences et mettre à jour le RecyclerView
    private void observeAbsencesData() {
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d("absenecteacher", "Absences reçues : " + (absences != null ? absences.size() : "null"));
            if (absences != null && !absences.isEmpty()) {
                // Si des absences existent, mettre à jour le RecyclerView avec l'adaptateur
                updateRecyclerView(absences);

                // Mettre à jour le TextView avec le nombre d'absences
                totalAbsencesTextView.setText("Total Absences : " + absences.size());
            } else {
                // Sinon, réinitialiser l'adaptateur du RecyclerView et mettre à jour le texte
                recyclerView.setAdapter(null);
                totalAbsencesTextView.setText("Total Absences : 0");
            }
        });
    }

    // Fonction pour mettre à jour le RecyclerView avec la liste des absences
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(List<Absence> absences) {
        absenceList = absences;
        if (adapter == null) {
            // Si l'adaptateur est nul, créer un nouvel adaptateur et l'affecter au RecyclerView
            adapter = new TeacherAbsenceAdapter(absenceList);
            recyclerView.setAdapter(adapter);
        } else {
            // Si l'adaptateur existe déjà, notifier qu'il y a eu un changement
            adapter.notifyDataSetChanged();
        }
    }

    // Fonction appelée pour supprimer une absence
    @SuppressLint("NotifyDataSetChanged")
    public void onDelete(Absence absence) {
        if (absenceList != null) {
            absenceList.remove(absence); // Retirer l'absence de la liste
            adapter.notifyDataSetChanged(); // Notifier l'adaptateur du changement
            absenceViewModel.deleteAbsence(absence.getIdAbsence(), absence.getCin()); // Supprimer l'absence dans le ViewModel
            Log.d("absenecteacher", "Absence supprimée : " + absence);
            Toast.makeText(getContext(), "Absence supprimée avec succès", Toast.LENGTH_SHORT).show(); // Afficher un toast
        }
    }

    // Cycle de vie du fragment, méthodes appelées lors de différentes phases du cycle de vie
    @Override
    public void onStart() {
        super.onStart();
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
        adapter = null; // Libérer l'adaptateur pour éviter les fuites de mémoire
    }

}