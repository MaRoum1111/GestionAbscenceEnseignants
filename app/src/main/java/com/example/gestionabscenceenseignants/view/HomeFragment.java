package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    // Déclaration des variables nécessaires pour les vues et le ViewModel
    private AbsenceViewModel absenceViewModel;
    private CalendarView calendarView;
    private TextView tvAbsencesCount;
    private ImageView Creport, CSchedule, Cabsence;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liaison avec le layout XML du fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialisation des composants de l'interface utilisateur
        initializeUIComponents(view);

        // Initialisation du ViewModel qui va gérer la logique des absences
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);

        // Observation des données du ViewModel pour mise à jour dynamique
        observeViewModel();

        // Retourner la vue du fragment une fois préparée
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialiser la date actuelle et charger les absences pour cette date
        initializeDateAndLoadAbsences();

        // Gérer les événements de clic pour le calendrier et les boutons radio
        setEventListeners();
    }


    /**
     * Initialiser les composants de l'interface utilisateur (calendrier, texte et icônes).
     */
    private void initializeUIComponents(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        tvAbsencesCount = view.findViewById(R.id.tv_absences_count);
        Creport = view.findViewById(R.id.cardreport);
        Cabsence = view.findViewById(R.id.cardabsence);
        CSchedule = view.findViewById(R.id.cardSchedule);
    }

    /**
     * Observer les données du ViewModel concernant les absences et gérer leur affichage.
     */
    private void observeViewModel() {
        // Observer le nombre d'absences et mettre à jour l'UI avec la valeur récupérée
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            if (absences != null) {
                tvAbsencesCount.setText("Nombre d'absences pour le jour sélectionné : " + absences.size());
            } else {
                tvAbsencesCount.setText("Aucune absence pour le jour sélectionné");
            }
        });

        // Observer les erreurs éventuelles dans le ViewModel
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                // Afficher un message d'erreur ou de succès si nécessaire
            }
        });
    }

    /**
     * Initialiser la date actuelle du calendrier et charger les absences pour cette date.
     */
    private void initializeDateAndLoadAbsences() {
        long currentDate = calendarView.getDate();
        @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(currentDate));
        absenceViewModel.fetchAbsencesByDate(selectedDate);
    }

    /**
     * Définir les écouteurs pour les événements du calendrier et des boutons.
     */
    private void setEventListeners() {
        // Gérer les changements de date dans le calendrier
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            @SuppressLint("DefaultLocale")
            String selectedDateFormatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            Log.d("HomeFragment", "Selected date: " + selectedDateFormatted);
            absenceViewModel.fetchAbsencesByDate(selectedDateFormatted);
        });

        // Gérer les clics sur les cartes pour ouvrir les autres fragments
        Creport.setOnClickListener(v -> openStatsPage());
        Cabsence.setOnClickListener(v -> openAbsencesManagement());
    }

    /**
     * Ouvrir la page des statistiques.
     */
    private void openStatsPage() {
        // Remplacer le fragment actuel par le fragment des statistiques
        Fragment reportFragment = new ReportFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, reportFragment);
        transaction.addToBackStack(null); // Ajouter à la pile arrière pour pouvoir revenir en arrière
        transaction.commit();
    }

    /**
     * Ouvrir la gestion des réclamations (fonctionnalité non utilisée dans cet exemple).
     */
    private void openClaimManagement() {
        // Remplacer le fragment actuel par le fragment de gestion des réclamations
        Fragment adminClaimFragment = new AdminClaimFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, adminClaimFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Ouvrir la gestion des absences.
     */
    private void openAbsencesManagement() {
        // Remplacer le fragment actuel par le fragment de gestion des absences
        Fragment absenceFragment = new AbsenceFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, absenceFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
