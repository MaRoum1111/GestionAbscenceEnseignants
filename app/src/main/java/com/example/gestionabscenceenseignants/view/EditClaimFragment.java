package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.ClaimViewModel;
import com.example.gestionabscenceenseignants.model.Claim;

public class EditClaimFragment extends Fragment {

    private EditText dateField, startTimeField, endTimeField, claimDateField, classeField, claimField;
    private ClaimViewModel claimViewModel;
    private String idClaim;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_claim, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des champs
        initializeFields(view);

        // Initialisation du ViewModel
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        // Charger les données transmises par le fragment précédent
        loadDataFromArguments();

        // Gérer le clic sur le bouton de validation
        setupValidationButton(view);

        // Gérer le clic sur le bouton d'annulation
        setupCancelButton(view);
    }

    private void initializeFields(View view) {
        dateField = view.findViewById(R.id.date);
        startTimeField = view.findViewById(R.id.startTime);
        endTimeField = view.findViewById(R.id.endTime);
        claimDateField = view.findViewById(R.id.claimDate);
        classeField = view.findViewById(R.id.classe);
        claimField = view.findViewById(R.id.claim);
    }

    private void loadDataFromArguments() {
        if (getArguments() != null) {
            idClaim = getArguments().getString("idClaim");
            dateField.setText(getArguments().getString("date"));
            startTimeField.setText(getArguments().getString("startTime"));
            endTimeField.setText(getArguments().getString("endTime"));
            claimField.setText(getArguments().getString("claim"));
            classeField.setText(getArguments().getString("classe"));
            claimDateField.setText(getArguments().getString("claimDate"));
        }
    }

    private void setupValidationButton(View view) {
        Button validateButton = view.findViewById(R.id.editButton);
        validateButton.setOnClickListener(v -> {
            if (validateFields()) {
                Claim updatedClaim = new Claim(
                        idClaim,
                        dateField.getText().toString(),
                        startTimeField.getText().toString(),
                        endTimeField.getText().toString(),
                        claimField.getText().toString(),
                        classeField.getText().toString(),
                        claimDateField.getText().toString()
                );

                // Appeler la méthode de mise à jour dans le ViewModel
                claimViewModel.updateClaim(idClaim, updatedClaim);

                Toast.makeText(getContext(), "Réclamation mise à jour avec succès", Toast.LENGTH_SHORT).show();

                // Retourner au fragment précédent
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void setupCancelButton(View view) {
        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Méthode pour valider les champs
    private boolean validateFields() {
        if (claimDateField.getText().toString().isEmpty()) {
            claimDateField.setError("Veuillez entrer la date de réclamation");
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
        if (classeField.getText().toString().isEmpty()) {
            classeField.setError("Veuillez entrer la classe");
            return false;
        }
        if (claimField.getText().toString().isEmpty()) {
            claimField.setError("Veuillez entrer la description de réclamation");
            return false;
        }
        return true;
    }
}
