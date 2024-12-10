package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;

public class HomeFragment extends Fragment {

    private AbsenceViewModel absenceViewModel;
    private CalendarView calendarView;
    private TextView tvAbsencesCount;
    private RadioButton rbFilterMonth, rbFilterYear;
    private RadioGroup radioGroupFilters;
    private CardView Creport,Cuser,Cabsence;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liaison avec le layout XML
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialisation des composants UI
        calendarView = view.findViewById(R.id.calendarView);
        tvAbsencesCount = view.findViewById(R.id.tv_absences_count);
        rbFilterMonth = view.findViewById(R.id.rb_filter_month);
        rbFilterYear = view.findViewById(R.id.rb_filter_year);
        radioGroupFilters = view.findViewById(R.id.radioGroup);
        Creport=view.findViewById(R.id.cardreport);
        Cabsence=view.findViewById(R.id.cardabsence);
        Cuser=view.findViewById(R.id.carduser);
        // Initialisation du ViewModel
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);

        // Observateurs pour les données
        observeViewModel();

        // Écouteur pour le CalendarView
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            @SuppressLint("DefaultLocale") String selectedDate =  String.format("%02d/%02d/%04d", dayOfMonth, month + 1,year);
            Log.d("HomeFragment", "Selected date: " + selectedDate);
            absenceViewModel.fetchAbsencesByDate(selectedDate);
        });

        // Écouteurs pour les boutons radio
        rbFilterMonth.setOnClickListener(v -> filterByMonth());
        rbFilterYear.setOnClickListener(v -> filterByYear());
        Cuser.setOnClickListener(v -> openUsersManagement());
        Creport.setOnClickListener(v -> openStatsPage());
        Cabsence.setOnClickListener(v -> openAbsencesManagement());
        return view;
    }

    private void observeViewModel() {
        // Observer les données des absences
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            // Mettre à jour l'interface avec les données récupérées
            tvAbsencesCount.setText("Nombre d'absences pour le jour sélectionné : " + (absences != null ? absences.size() : 0));
        });

        // Observer les messages d'erreur ou de succès
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                // Afficher un message à l'utilisateur si nécessaire (Toast, Snackbar, etc.)
            }
        });
    }

    private void filterByMonth() {
        // Logique pour filtrer les données par mois
        absenceViewModel.loadAbsenceCountsByProf(); // Exemple de méthode
    }

    private void filterByYear() {
        // Logique pour filtrer les données par année
        absenceViewModel.loadAbsenceCountsByProf(); // Exemple de méthode
    }
    private void openStatsPage() {
        Fragment reportFragment= new ReportFragment(); // Remplacez par le fragment approprié
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, reportFragment);
        transaction.addToBackStack(null); // Ajoute à la pile arrière
        transaction.commit();
    }

    private void openUsersManagement() {
        Fragment usersFragment= new UsersFragment(); // Remplacez par le fragment approprié
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, usersFragment);
        transaction.addToBackStack(null); // Ajoute à la pile arrière
        transaction.commit();
    }

    private void openAbsencesManagement() {
        Fragment AbsenceFragment= new AbsenceFragment(); // Remplacez par le fragment approprié
        FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, AbsenceFragment);
        transaction.addToBackStack(null); // Ajoute à la pile arrière
        transaction.commit();    }
}

