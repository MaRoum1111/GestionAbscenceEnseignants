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

    private TextView message;
    private UserViewModel userViewModel;
    private AbsenceViewModel absenceViewModel;
    private TextView tvAbsencesCount;
    private CalendarView calendarView;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_accueil_agent, container, false);

        // Initialize the ViewModels
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize TextViews
        message = rootView.findViewById(R.id.bonjourTextView);
        tvAbsencesCount = rootView.findViewById(R.id.tv_absences_count); // Ensure you have this TextView in your layout
        calendarView = rootView.findViewById(R.id.calendarView);  // Initialize CalendarView

        // Observer for user name
        userViewModel.getUserName().observe(getViewLifecycleOwner(), userName -> {
            if (userName != null) {
                message.setText(userName);  // Afficher le nom de l'utilisateur
            }
        });

        // Observe absences count
        observeViewModel();
        initializeDateAndLoadAbsences();
        // Gérer les événements de clic sur le calendrier et les boutons radio
        setEventListeners();

        return rootView;
    }

    // Method to observe the AbsenceViewModel
    private void observeViewModel() {
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            if (absences != null) {
                tvAbsencesCount.setText("Nombre d'absences pour le jour sélectionné : " + absences.size());
            } else {
                tvAbsencesCount.setText("Aucune absence pour le jour sélectionné");
            }
        });

        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Log.e("AccueilAgentFragment", message);
                // Optionally display an error message
            }
        });
    }
    private void initializeDateAndLoadAbsences() {
        long currentDate = calendarView.getDate();
        @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(currentDate));
        absenceViewModel.fetchAbsencesByDate(selectedDate);
    }
    private void setEventListeners() {
        // Gérer les changements de date dans le calendrier
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            @SuppressLint("DefaultLocale")
            String selectedDateFormatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            Log.d("HomeFragment", "Selected date: " + selectedDateFormatted);
            absenceViewModel.fetchAbsencesByDate(selectedDateFormatted);
        });
    }
}