package com.example.gestionabscenceenseignants.view;

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

import com.example.gestionabscenceenseignants.Adapter.AbsensesAdapter;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;

import java.util.List;

public class HomeTeacherFragment extends Fragment implements AbsensesAdapter.OnAbsenceClickListener {
    private RecyclerView recyclerView;
    private AbsensesAdapter adapter;
    private AbsenceViewModel absenceViewModel;
    private UserViewModel userViewModel;
    private TextView message;
    private List<Absence> absenceList; // Liste des absences pour gérer les mises à jour

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.fragment_home_teacher, container, false);

        // Initialize the RecyclerView
        recyclerView = rootView.findViewById(R.id.absenceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        message=rootView.findViewById(R.id.bonjourTextView);
        // Initialize the ViewModel to retrieve absences
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Observer for user name
        userViewModel.getUserName().observe(getViewLifecycleOwner(), userName -> {
            if (userName != null) {
                message.setText("Bonjour " + userName);  // Afficher le nom de l'utilisateur
            }
        });
        // Load absences for the specific teacher
        loadAbsencesForTeacher();

        // Observe absences and update the adapter
        observeAbsences(rootView); // Pass the root view to the observe method

        return rootView;
    }

    private void loadAbsencesForTeacher() {
        absenceViewModel.getAbsencesForCurrentDay(); // Assurez-vous que cette méthode utilise profCin correctement
        userViewModel.loadLoggedUserDetails();

    }

    private void observeAbsences(View rootView) { // Change the method to accept the rootView
        // Access emptyView using the rootView
        TextView emptyView = rootView.findViewById(R.id.emptyView);

        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d("HomeTeacherFragment", "Absences observées : " + (absences != null ? absences.size() : "null"));

            if (absences != null && !absences.isEmpty()) {
                absenceList = absences;
                Log.d("HomeTeacherFragment", "Initialisation de l'adaptateur avec " + absences.size() + " absences.");

                if (adapter == null) {
                    adapter = new AbsensesAdapter(absenceList, this);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.updateAbsences(absenceList); // Mettre à jour les données
                }
                // Cacher le message de liste vide
                emptyView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                Log.d("HomeTeacherFragment", "Aucune absence à afficher.");
                recyclerView.setAdapter(null);
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRecommend(Absence absence) {
        if (absence == null) {
            Log.e("HomeTeacherFragment", "Absence is null. Cannot proceed to ClaimFragment.");
            return;
        }

        ClaimFragment claimFragment = new ClaimFragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, claimFragment) // Replace with the ID of your FrameLayout
                .addToBackStack(null) // Add to back stack for navigation
                .commit();
    }
}
