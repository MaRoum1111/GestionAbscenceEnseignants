package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.Calendar;

public class ClaimFragment extends Fragment {
    // Déclaration des variables pour les champs de saisie et les boutons
    private EditText editTextDate, editTextStartTime, editTextEndTime, editTextClaim, editTextClasse,editTextclaimDate;
    private Button btnSubmitClaim;
    private ClaimViewModel claimViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialise la vue du fragment
        View view = inflater.inflate(R.layout.fragment_claim, container, false);

        // Initialisation du ViewModel pour gérer les réclamations
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        // Initialisation des composants UI (champs de texte et boutons)
        initUIComponents(view);

        // Définir la date et l'heure actuelles dans les champs de saisie
        setCurrentDateAndTime();

        // Ajouter des gestionnaires pour les sélecteurs de date et d'heure
        addDateAndTimePickers();

        // Action à effectuer lorsque le bouton de soumission est cliqué
        btnSubmitClaim.setOnClickListener(v -> addClaim());

        // Retourne la vue du fragment
        return view;
    }

    // Initialisation des composants de l'interface utilisateur
    private void initUIComponents(View view) {
        editTextDate = view.findViewById(R.id.date);
        editTextStartTime = view.findViewById(R.id.startTime);
        editTextEndTime = view.findViewById(R.id.endTime);
        editTextClaim = view.findViewById(R.id.claim);
        editTextClasse = view.findViewById(R.id.classe);
        editTextclaimDate = view.findViewById(R.id.claimDate);
        btnSubmitClaim = view.findViewById(R.id.submitClaimButton);
    }

    // Définit la date et l'heure actuelles dans les champs de texte
    @SuppressLint("DefaultLocale")
    private void setCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        // Obtenir la date actuelle
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));
        editTextclaimDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));
    }

    // Ajouter des sélecteurs de date et d'heure aux champs de texte correspondants
    private void addDateAndTimePickers() {
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextclaimDate.setOnClickListener(v -> showDatePicker());
        editTextStartTime.setOnClickListener(v -> showTimePicker(editTextStartTime));
        editTextEndTime.setOnClickListener(v -> showTimePicker(editTextEndTime));
    }

    // Afficher un sélecteur de date
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view1, selectedYear, selectedMonth, selectedDay) -> {
                    @SuppressLint("DefaultLocale") String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editTextDate.setText(selectedDate);
                    editTextclaimDate.setText(selectedDate);
                },
                day, year, month
        );
        datePickerDialog.show();
    }

    // Afficher un sélecteur d'heure pour les champs de début et de fin
    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view1, selectedHour, selectedMinute) -> {
                    @SuppressLint("DefaultLocale") String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                    editText.setText(selectedTime);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    // Ajouter la réclamation à la base de données via le ViewModel
    private void addClaim() {
        // Validation des champs avant d'ajouter la réclamation
        if (!validateInputs()) {
            return;
        }

        // Créer un objet Claim avec les valeurs des champs de texte
        Claim claim = new Claim(
                editTextDate.getText().toString().trim(),
                editTextStartTime.getText().toString().trim(),
                editTextEndTime.getText().toString().trim(),
                editTextClaim.getText().toString().trim(),
                editTextClasse.getText().toString().trim(),
                editTextclaimDate.getText().toString().trim()
        );

        // Appeler la méthode pour ajouter la réclamation via le ViewModel
        claimViewModel.addClaim(claim);

        // Observer le message d'erreur ou de succès
        claimViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getActivity(), "Réclamation Ajoutée avec succès", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Valider les entrées de l'utilisateur
    private boolean validateInputs() {
        String date = editTextDate.getText().toString().trim();
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String claim = editTextClaim.getText().toString().trim();
        String classe = editTextClasse.getText().toString().trim();

        // Vérifier si tous les champs sont remplis
        if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || claim.isEmpty() || classe.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
