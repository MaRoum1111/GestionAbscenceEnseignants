package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.model.Absence;

public class EditAbsenceFragment extends Fragment {

    private EditText dateField, startTimeField, endTimeField, reasonField, subjectField;
    private AutoCompleteTextView profNameField;
    private Spinner statusSpinner;
    private AbsenceViewModel absenceViewModel;
    private String absenceId,Cin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_absence, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialiser les champs
        profNameField = view.findViewById(R.id.profName);
        dateField = view.findViewById(R.id.date);
        startTimeField = view.findViewById(R.id.startTime);
        endTimeField = view.findViewById(R.id.endTime);
        reasonField = view.findViewById(R.id.reason);
        subjectField = view.findViewById(R.id.subjectName);
        statusSpinner = view.findViewById(R.id.status);

        Button validateButton = view.findViewById(R.id.editButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Initialiser le ViewModel
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);

        // Charger les données transmises par le fragment précédent
        if (getArguments() != null) {
            absenceId = getArguments().getString("idAbsence");
            Cin=getArguments().getString("cin");
            profNameField.setText(getArguments().getString("profName"));
            dateField.setText(getArguments().getString("date"));
            startTimeField.setText(getArguments().getString("startTime"));
            endTimeField.setText(getArguments().getString("endTime"));
            reasonField.setText(getArguments().getString("reason"));
            subjectField.setText(getArguments().getString("subjectName"));
            // Sélectionner le statut correspondant dans le Spinner
            String status = getArguments().getString("status");
            if (status != null) {
                String[] statuses = getResources().getStringArray(R.array.statut_absence);
                for (int i = 0; i < statuses.length; i++) {
                    if (statuses[i].equals(status)) {
                        statusSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }

        // Gérer le clic sur le bouton de validation
        validateButton.setOnClickListener(v -> {
            if (validateFields()) {
                Absence updatedAbsence = new Absence(
                        absenceId,
                        profNameField.getText().toString(),
                        dateField.getText().toString(),
                        startTimeField.getText().toString(),
                        endTimeField.getText().toString(),
                        reasonField.getText().toString(),
                        statusSpinner.getSelectedItem().toString(),
                        subjectField.getText().toString(),Cin
                );

                // Appeler la méthode de mise à jour dans le ViewModel
                absenceViewModel.updateAbsence(absenceId, updatedAbsence);

                Toast.makeText(getContext(), "Absence mise à jour avec succès", Toast.LENGTH_SHORT).show();

                // Retourner au fragment précédent
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Gérer le clic sur le bouton d'annulation
        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Méthode pour valider les champs
    private boolean validateFields() {
        if (profNameField.getText().toString().isEmpty()) {
            profNameField.setError("Veuillez entrer le nom du professeur");
            return false;
        }
        if (dateField.getText().toString().isEmpty()) {
            dateField.setError("Veuillez entrer une date valide");
            return false;
        }
        if (startTimeField.getText().toString().isEmpty()) {
            startTimeField.setError("Veuillez entrer l'heure de début");
            return false;
        }
        if (endTimeField.getText().toString().isEmpty()) {
            endTimeField.setError("Veuillez entrer l'heure de fin");
            return false;
        }
        if (reasonField.getText().toString().isEmpty()) {
            reasonField.setError("Veuillez entrer une raison");
            return false;
        }
        if (subjectField.getText().toString().isEmpty()) {
            subjectField.setError("Veuillez entrer le nom de la matière");
            return false;
        }
        return true;
    }
}
