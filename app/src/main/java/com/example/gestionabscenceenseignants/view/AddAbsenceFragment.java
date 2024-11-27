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
    private Button btnAddAbsence;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_absence, container, false);

        // Initialiser les champs de saisie
        editTextProfName = view.findViewById(R.id.editTextProfName);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextStartTime = view.findViewById(R.id.editTextStartTime);
        editTextEndTime = view.findViewById(R.id.editTextEndTime);
        editTextReason = view.findViewById(R.id.editTextReason);
        editTextSubjectName = view.findViewById(R.id.editTextSubjectName);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        btnAddAbsence = view.findViewById(R.id.btnAddAbsence);

        db = FirebaseFirestore.getInstance();

        btnAddAbsence.setOnClickListener(v -> {
            // Récupérer les valeurs saisies
            String profName = editTextProfName.getText().toString().trim();
            String date = editTextDate.getText().toString().trim();
            String startTime = editTextStartTime.getText().toString().trim();
            String endTime = editTextEndTime.getText().toString().trim();
            String reason = editTextReason.getText().toString().trim();
            String subjectName = editTextSubjectName.getText().toString().trim();
            String status = spinnerStatus.getSelectedItem().toString();

            // Vérifier que les champs ne sont pas vides
            if (profName.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || reason.isEmpty() || subjectName.isEmpty()) {
                Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer l'objet Absence
            Absence absence = new Absence(profName, date, startTime, endTime, reason, status, subjectName);

            // Ajouter l'absence à Firestore
            db.collection("absences")
                    .add(absence)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getActivity(), "Absence ajoutée avec succès", Toast.LENGTH_SHORT).show();
                        // Réinitialiser le formulaire
                        clearFields();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Erreur lors de l'ajout de l'absence", Toast.LENGTH_SHORT).show();
                    });
        });

        return view;
    }

    private void clearFields() {
        editTextProfName.setText("");
        editTextDate.setText("");
        editTextStartTime.setText("");
        editTextEndTime.setText("");
        editTextReason.setText("");
        editTextSubjectName.setText("");
    }
}
