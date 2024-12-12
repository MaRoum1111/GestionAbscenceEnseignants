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
    private List<Absence> absenceList; // Liste des absences

    // Méthode appelée lors de la création du fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initialisation des composants globaux pour AbsenceTeacherFragment");
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class); // Initialisation du ViewModel
    }

    // Méthode appelée pour créer la vue du fragment
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Création de la vue pour AbsenceTeacherFragment");
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
        TextView name = rootView.findViewById(R.id.teacher_name);
        TextView cin = rootView.findViewById(R.id.teacher_cin);
        TextView nb = rootView.findViewById(R.id.total_absences);

        // Appel pour récupérer les absences pour le professeur actuel
        absenceViewModel.getAbsencesForCurrentTeacher();
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            // Si des absences sont disponibles, mettre à jour les informations
            if (absences != null && !absences.isEmpty()) {
                updateTeacherInfo(absences, name, cin, nb);
            } else {
                // Sinon, afficher un message indiquant qu'il n'y a pas d'absences
                displayNoAbsenceMessage(name, cin, nb);
            }
        });
    }

    // Fonction pour mettre à jour les informations du professeur avec les données des absences
    @SuppressLint("SetTextI18n")
    private void updateTeacherInfo(List<Absence> absences, TextView name, TextView cin, TextView nb) {
        absenceList = absences;
        Absence firstAbsence = absences.get(0);
        name.setText(firstAbsence.getProfName()); // Nom du professeur
        cin.setText(firstAbsence.getCin()); // CIN du professeur
        nb.setText("Total des absences : " + absences.size()); // Total des absences
    }

    // Fonction pour afficher un message si aucune absence n'est trouvée
    @SuppressLint("SetTextI18n")
    private void displayNoAbsenceMessage(TextView name, TextView cin, TextView nb) {
        name.setText("Nom indisponible");
        cin.setText("CIN indisponible");
        nb.setText("Aucune absence trouvée");
    }

    // Fonction pour observer les absences et mettre à jour le RecyclerView
    private void observeAbsencesData() {
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d(TAG, "Absences reçues : " + (absences != null ? absences.size() : "null"));
            if (absences != null && !absences.isEmpty()) {
                // Si des absences existent, mettre à jour le RecyclerView avec l'adaptateur
                updateRecyclerView(absences);
            } else {
                // Sinon, réinitialiser l'adaptateur du RecyclerView
                recyclerView.setAdapter(null);
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
            Log.d(TAG, "Absence supprimée : " + absence);
            Toast.makeText(getContext(), "Absence supprimée avec succès", Toast.LENGTH_SHORT).show(); // Afficher un toast
        }
    }




    // Cycle de vie du fragment, méthodes appelées lors de différentes phases du cycle de vie
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Fragment visible à l'utilisateur");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Fragment interactif");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Fragment perd le focus");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Fragment n'est plus visible");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: Libération des ressources de la vue");
        adapter = null; // Libérer l'adaptateur pour éviter les fuites de mémoire
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Fragment détruit");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Fragment détaché de l'activité");
    }
}
