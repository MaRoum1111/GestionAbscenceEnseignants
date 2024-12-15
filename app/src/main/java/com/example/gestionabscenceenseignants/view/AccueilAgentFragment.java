package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AccueilAgentFragment extends Fragment {

    // Déclaration des variables pour les vues et les ViewModels
    private TextView message; // TextView pour afficher un message de bienvenue ou le nom de l'utilisateur
    private UserViewModel userViewModel; // ViewModel pour gérer les données utilisateur
    private AbsenceViewModel absenceViewModel; // ViewModel pour gérer les données d'absence
    private TextView tvAbsencesCount; // TextView pour afficher le nombre d'absences
    private CalendarView calendarView; // Vue calendrier pour sélectionner une date

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Chargement de la vue pour le fragment depuis le fichier XML
        View rootView = inflater.inflate(R.layout.fragment_accueil_agent, container, false);

        // Initialisation des ViewModels
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialisation des TextViews et du CalendarView
        message = rootView.findViewById(R.id.bonjourTextView); // Message de bienvenue
        tvAbsencesCount = rootView.findViewById(R.id.tv_absences_count); // Texte pour le nombre d'absences
        calendarView = rootView.findViewById(R.id.calendarView); // Sélecteur de date

        // Observation du nom de l'utilisateur et mise à jour de l'interface
        userViewModel.getUserName().observe(getViewLifecycleOwner(), userName -> {
            if (userName != null) {
                message.setText(userName); // Afficher le nom de l'utilisateur
            }
        });

        // Observer les absences et gérer les erreurs
        observeViewModel();

        // Initialisation de la date actuelle et chargement des absences pour cette date
        initializeDateAndLoadAbsences();

        // Gérer les interactions utilisateur, comme les changements de date
        setEventListeners();

        return rootView;
    }

    // Méthode pour observer les données dans AbsenceViewModel
    private void observeViewModel() {
        // Observer les absences et mettre à jour l'interface utilisateur
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            if (absences != null) {
                tvAbsencesCount.setText("Nombre d'absences pour le jour sélectionné : " + absences.size());
            } else {
                tvAbsencesCount.setText("Aucune absence pour le jour sélectionné");
            }
        });

        // Observer les messages d'erreur et les enregistrer dans les logs
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Log.e("AccueilAgentFragment", message);
                // Optionnellement, afficher un message d'erreur à l'utilisateur
            }
        });
    }

    // Méthode pour initialiser la date et charger les absences correspondantes
    private void initializeDateAndLoadAbsences() {
        long currentDate = calendarView.getDate(); // Obtenir la date actuelle depuis le calendrier
        @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(currentDate)); // Formater la date
        absenceViewModel.fetchAbsencesByDate(selectedDate); // Charger les absences pour la date sélectionnée
    }

    // Méthode pour configurer les écouteurs d'événements utilisateur
    private void setEventListeners() {
        // Gérer les changements de date dans le calendrier
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            @SuppressLint("DefaultLocale")
            String selectedDateFormatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year); // Formater la date sélectionnée
            Log.d("HomeFragment", "Selected date: " + selectedDateFormatted); // Enregistrer la date dans les logs
            absenceViewModel.fetchAbsencesByDate(selectedDateFormatted); // Charger les absences pour la nouvelle date
        });
    }
}
