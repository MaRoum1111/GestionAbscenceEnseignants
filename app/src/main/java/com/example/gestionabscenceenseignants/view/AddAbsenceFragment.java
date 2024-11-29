package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddAbsenceFragment extends Fragment {

    private EditText editTextProfName, editTextDate, editTextStartTime, editTextEndTime, editTextReason, editTextSubjectName;
    private Spinner spinnerStatus;
    private Button btnAddAbsence, btnCancel;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_absence, container, false);

        // Initialiser les champs de saisie
        editTextProfName = view.findViewById(R.id.profName);
        editTextDate = view.findViewById(R.id.date);
        editTextStartTime = view.findViewById(R.id.startTime);
        editTextEndTime = view.findViewById(R.id.endTime);
        editTextReason = view.findViewById(R.id.reason);
        editTextSubjectName = view.findViewById(R.id.subjectName);
        spinnerStatus = view.findViewById(R.id.status);
        btnAddAbsence = view.findViewById(R.id.addButton);
        btnCancel = view.findViewById(R.id.cancelButton);

        db = FirebaseFirestore.getInstance();

        // Ajouter l'absence
        btnAddAbsence.setOnClickListener(v -> {
            String profName = editTextProfName.getText().toString().trim();
            String date = editTextDate.getText().toString().trim();
            String startTime = editTextStartTime.getText().toString().trim();
            String endTime = editTextEndTime.getText().toString().trim();
            String reason = editTextReason.getText().toString().trim();
            String subjectName = editTextSubjectName.getText().toString().trim();
            String status = spinnerStatus.getSelectedItem().toString();

            if (profName.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || reason.isEmpty() || subjectName.isEmpty()) {
                Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Absence absence = new Absence(profName, date, startTime, endTime, reason, status, subjectName);

            db.collection("absences")
                    .add(absence)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getActivity(), "Absence ajoutée avec succès", Toast.LENGTH_SHORT).show();
                        clearFields();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Erreur lors de l'ajout de l'absence", Toast.LENGTH_SHORT).show();
                    });
        });

        // Annuler l'opération
        btnCancel.setOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    private void clearFields() {
        editTextProfName.setText("");
        editTextDate.setText("");
        editTextStartTime.setText("");
        editTextEndTime.setText("");
        editTextReason.setText("");
        editTextSubjectName.setText("");
        spinnerStatus.setSelection(0); // Réinitialiser le spinner
    }
}
