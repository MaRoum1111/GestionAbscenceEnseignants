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

    // Déclaration des vues utilisées dans le fragment
    private EditText dateField, startTimeField, endTimeField, reasonField, subjectField;
    private AutoCompleteTextView profNameField;
    private Spinner statusSpinner;
    private AbsenceViewModel absenceViewModel;
    private String absenceId, Cin;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("EditAbsenceFragment", "onCreateView called");
        // Inflater la vue du fragment
        return inflater.inflate(R.layout.fragment_edit_absence, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("EditAbsenceFragment", "onViewCreated called");

        // Initialiser les vues à partir du layout
        initializeViews(view);

        // Charger les données envoyées depuis le fragment précédent
        loadArguments();

        // Gérer les actions sur les boutons (validation et annulation)
        handleButtonClicks();
    }

    // Initialisation des vues
    private void initializeViews(View view) {
        // Récupération des composants de l'UI
        profNameField = view.findViewById(R.id.profName);
        dateField = view.findViewById(R.id.date);
        startTimeField = view.findViewById(R.id.startTime);
        endTimeField = view.findViewById(R.id.endTime);
        reasonField = view.findViewById(R.id.reason);
        subjectField = view.findViewById(R.id.subjectName);
        statusSpinner = view.findViewById(R.id.status);

        // Initialisation des boutons de validation et d'annulation
        Button validateButton = view.findViewById(R.id.editButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        // Initialiser le ViewModel pour interagir avec la logique métier
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
    }

    // Charger les données envoyées dans les arguments du fragment
    private void loadArguments() {
        if (getArguments() != null) {
            // Récupérer les arguments passés au fragment (ID de l'absence et autres informations)
            absenceId = getArguments().getString("idAbsence");
            Cin = getArguments().getString("cin");
            // Remplir les champs du formulaire avec les données de l'absence existante
            profNameField.setText(getArguments().getString("profName"));
            dateField.setText(getArguments().getString("date"));
            startTimeField.setText(getArguments().getString("startTime"));
            endTimeField.setText(getArguments().getString("endTime"));
            reasonField.setText(getArguments().getString("classe"));
            subjectField.setText(getArguments().getString("salle"));
            // Définir le statut dans le Spinner en fonction de la donnée passée
            setStatusSpinner(getArguments().getString("status"));
        }
    }

    // Sélectionner le statut de l'absence dans le Spinner
    private void setStatusSpinner(String status) {
        if (status != null) {
            String[] statuses = getResources().getStringArray(R.array.statut_absence);
            for (int i = 0; i < statuses.length; i++) {
                if (statuses[i].equals(status)) {
                    statusSpinner.setSelection(i); // Sélectionner le statut approprié
                    break;
                }
            }
        }
    }

    // Gérer les clics sur les boutons (Valider et Annuler)
    private void handleButtonClicks() {
        // Récupérer les boutons de validation et d'annulation
        Button validateButton = getView().findViewById(R.id.editButton);
        Button cancelButton = getView().findViewById(R.id.cancelButton);

        // Action à effectuer lors du clic sur le bouton de validation
        validateButton.setOnClickListener(v -> {
            // Vérifier que tous les champs sont valides
            if (validateFields()) {
                // Créer un objet Absence avec les nouvelles données saisies
                Absence updatedAbsence = new Absence(
                        absenceId,
                        profNameField.getText().toString(),
                        dateField.getText().toString(),
                        startTimeField.getText().toString(),
                        endTimeField.getText().toString(),
                        reasonField.getText().toString(),
                        statusSpinner.getSelectedItem().toString(),
                        subjectField.getText().toString(), Cin
                );
                // Appeler la méthode de mise à jour dans le ViewModel pour enregistrer les modifications
                absenceViewModel.updateAbsence(absenceId, updatedAbsence);
                // Afficher un message de succès
                Toast.makeText(getContext(), "Absence mise à jour avec succès", Toast.LENGTH_SHORT).show();
                // Retour à l'écran précédent
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Action à effectuer lors du clic sur le bouton d'annulation
        cancelButton.setOnClickListener(v -> {
            // Retourner à l'écran précédent sans effectuer de modifications
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Méthode de validation des champs du formulaire
    private boolean validateFields() {
        // Vérifier que chaque champ est rempli correctement
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
