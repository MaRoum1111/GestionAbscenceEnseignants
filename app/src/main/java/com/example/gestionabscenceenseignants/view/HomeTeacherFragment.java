package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class HomeTeacherFragment extends Fragment {

    private AbsenceViewModel absenceViewModel;
    private CalendarView calendarView;
    private TextView tvAbsencesCount;
    private ImageView Cclaim, Cabsence;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Liaison avec le layout XML
        View view = inflater.inflate(R.layout.fragment_home_teacher, container, false);

        // Initialisation des composants UI
        initializeUIComponents(view);

        // Initialisation du ViewModel
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);

        // Observateurs pour les données
        observeViewModel();

        // Appel pour récupérer les absences pour la date actuelle lorsque la vue est prête
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialiser la date actuelle et charger les absences pour cette date
        initializeDateAndLoadAbsences();

        // Gérer les événements de clic sur le calendrier et les boutons radio
        setEventListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("HomeFragment", "Fragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeFragment", "Fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("HomeFragment", "Fragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HomeFragment", "Fragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("HomeFragment", "Fragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("HomeFragment", "Fragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("HomeFragment", "Fragment onDetach");
    }

    /**
     * Initialiser les composants de l'interface utilisateur.
     */
    private void initializeUIComponents(View view) {
        calendarView = view.findViewById(R.id.calendarView);
        tvAbsencesCount = view.findViewById(R.id.tv_absences_count);
        Cabsence = view.findViewById(R.id.cardabsence);
        Cclaim = view.findViewById(R.id.cardclaim);
    }

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
                // Afficher un message d'erreur ou de succès si nécessaire
            }
        });
    }

    /**
     * Initialiser la date actuelle et charger les absences pour cette date.
     */
    private void initializeDateAndLoadAbsences() {
        long currentDate = calendarView.getDate();
        @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(currentDate));
        absenceViewModel.fetchAbsencesByDate(selectedDate);
    }

    /**
     * Définir les écouteurs pour les événements du calendrier et des boutons radio.
     */
    private void setEventListeners() {
        // Gérer les changements de date dans le calendrier
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            @SuppressLint("DefaultLocale")
            String selectedDateFormatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            Log.d("HomeFragment", "Selected date: " + selectedDateFormatted);
            absenceViewModel.fetchAbsencesByDate(selectedDateFormatted);
        });

        Cclaim.setOnClickListener(v -> openClaimManagement());
        Cabsence.setOnClickListener(v -> openAbsencesManagement());
    }


    private void openClaimManagement() {
        Fragment listeClaimFragment = new ListeClaimFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, listeClaimFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openAbsencesManagement() {
        Fragment absenceTeacherFragment = new AbsenceTeacherFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, absenceTeacherFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
