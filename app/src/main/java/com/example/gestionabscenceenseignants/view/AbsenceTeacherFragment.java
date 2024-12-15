package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
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
    private TeacherAbsenceAdapter adapter; // Adaptateur pour gérer les données des absences
    private AbsenceViewModel absenceViewModel; // ViewModel pour gérer les données des absences
    private List<Absence> absenceList; // Liste des absences du professeur
    private TextView teacherNameTextView; // TextView pour afficher le nom du professeur
    private TextView totalAbsencesTextView; // TextView pour afficher le total des absences

    // Méthode appelée lors de la création de la vue associée au fragment
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("absenecteacher", "onCreateView: Création de la vue pour AbsenceTeacherFragment");
        // Chargement de la vue depuis le fichier XML
        View rootView = inflater.inflate(R.layout.fragement_absence_teacher, container, false);

        // Initialiser les composants de l'interface utilisateur
        initializeViews(rootView);

        // Observer les données pour mettre à jour l'interface utilisateur
        observeAbsencesData();

        return rootView;
    }

    // Initialisation des vues et des composants nécessaires
    private void initializeViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.recycler_view); // RecyclerView pour afficher les absences
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Disposition verticale pour la liste

        // Initialisation des TextViews pour le nom du professeur et le total des absences
        teacherNameTextView = rootView.findViewById(R.id.teacher_name);
        totalAbsencesTextView = rootView.findViewById(R.id.total_absences);

        // Initialisation du ViewModel pour récupérer les données des absences
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        absenceViewModel.getAbsencesForCurrentTeacher(); // Charger les absences pour le professeur actuel
    }

    // Observation des données d'absences pour mettre à jour l'interface utilisateur
    private void observeAbsencesData() {
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d("absenecteacher", "Absences reçues : " + (absences != null ? absences.size() : "null"));
            if (absences != null && !absences.isEmpty()) {
                // Si la liste des absences est non vide, la transmettre à l'adaptateur
                updateRecyclerView(absences);

                // Mise à jour du TextView avec le nombre total d'absences
                totalAbsencesTextView.setText("Total Absences : " + absences.size());
            } else {
                // Si aucune absence n'est trouvée, réinitialiser le RecyclerView
                recyclerView.setAdapter(null);
                totalAbsencesTextView.setText("Total Absences : 0");
            }
        });
    }

    // Mise à jour du RecyclerView avec les données des absences
    @SuppressLint("NotifyDataSetChanged")
    private void updateRecyclerView(List<Absence> absences) {
        absenceList = absences; // Stocker les données des absences localement
        if (adapter == null) {
            // Créer un nouvel adaptateur si celui-ci n'existe pas encore
            adapter = new TeacherAbsenceAdapter(absenceList);
            recyclerView.setAdapter(adapter); // Associer l'adaptateur au RecyclerView
        } else {
            // Sinon, notifier que les données ont changé pour rafraîchir l'affichage
            adapter.notifyDataSetChanged();
        }
    }

    // Libération des ressources lors de la destruction de la vue
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null; // Éviter les fuites de mémoire en libérant l'adaptateur
    }
}
