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

public class HomeFragment extends Fragment {

    private AbsenceViewModel absenceViewModel;
    private CalendarView calendarView;
    private TextView tvAbsencesCount;
    private RadioButton rbFilterMonth, rbFilterYear;
    private RadioGroup radioGroupFilters;
    private ImageView Creport, Cuser, Cabsence;

    @SuppressLint("MissingInflatedId")
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
        Creport = view.findViewById(R.id.cardreport);
        Cabsence = view.findViewById(R.id.cardabsence);
        Cuser = view.findViewById(R.id.cardclaim);

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

        // Récupérer la date actuelle lorsque l'application est ouverte
        long currentDate = calendarView.getDate();
        @SuppressLint({"DefaultLocale", "SimpleDateFormat"})
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date(currentDate));

        // Appel pour récupérer les absences pour la date actuelle
        absenceViewModel.fetchAbsencesByDate(selectedDate);

        // Écouteur pour le CalendarView
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            @SuppressLint("DefaultLocale")
            String selectedDateFormatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
            Log.d("HomeFragment", "Selected date: " + selectedDateFormatted);
            absenceViewModel.fetchAbsencesByDate(selectedDateFormatted);
        });

        // Écouteurs pour les boutons radio
        rbFilterMonth.setOnClickListener(v -> filterByMonth());
        rbFilterYear.setOnClickListener(v -> filterByYear());
        Cuser.setOnClickListener(v -> openClaimManagement());
        Creport.setOnClickListener(v -> openStatsPage());
        Cabsence.setOnClickListener(v -> openAbsencesManagement());
    }

    @Override
    public void onStart() {
        super.onStart();
        // Le fragment est devenu visible et interactif
        Log.d("HomeFragment", "Fragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Le fragment est visible et l'utilisateur peut interagir avec lui
        Log.d("HomeFragment", "Fragment onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        // Le fragment n'est plus au premier plan mais peut encore être visible
        Log.d("HomeFragment", "Fragment onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        // Le fragment n'est plus visible, il est peut-être caché ou détruit
        Log.d("HomeFragment", "Fragment onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // La vue du fragment est détruite, mais le fragment peut rester en mémoire
        Log.d("HomeFragment", "Fragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Le fragment est détruit
        Log.d("HomeFragment", "Fragment onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Le fragment est détaché de son activité
        Log.d("HomeFragment", "Fragment onDetach");
    }

    private void observeViewModel() {
        // Observer les données des absences
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            // Mettre à jour l'interface avec les données récupérées
            if (absences != null) {
                tvAbsencesCount.setText("Nombre d'absences pour le jour sélectionné : " + absences.size());
            } else {
                tvAbsencesCount.setText("Aucune absence pour le jour sélectionné");
            }
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
        Fragment reportFragment = new ReportFragment(); // Remplacez par le fragment approprié
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, reportFragment);
        transaction.addToBackStack(null); // Ajoute à la pile arrière
        transaction.commit();
    }

    private void openClaimManagement() {
        Fragment usersFragment = new UsersFragment(); // Remplacez par le fragment approprié
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, usersFragment);
        transaction.addToBackStack(null); // Ajoute à la pile arrière
        transaction.commit();
    }

    private void openAbsencesManagement() {
        Fragment absenceFragment = new AbsenceFragment(); // Remplacez par le fragment approprié
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, absenceFragment);
        transaction.addToBackStack(null); // Ajoute à la pile arrière
        transaction.commit();
    }
}
