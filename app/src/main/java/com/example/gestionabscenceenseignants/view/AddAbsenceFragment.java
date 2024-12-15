package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.model.User;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.Repository.UserRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAbsenceFragment extends Fragment {

    // Déclaration des champs de saisie et autres composants de l'interface utilisateur
    private AutoCompleteTextView editTextProfName; // Champ pour saisir le nom de l'enseignant
    private EditText editTextDate, editTextStartTime, editTextEndTime, editTextReason, editTextSubjectName; // Champs pour date, heure et autres détails
    private Spinner spinnerStatus; // Dropdown pour le statut de l'absence
    private Button btnAddAbsence, btnCancel; // Boutons pour valider ou annuler
    private AbsenceViewModel absenceViewModel; // ViewModel pour gérer les données d'absence
    private UserRepository userRepository; // Référentiel pour récupérer les données des enseignants
    private List<User> teachersList = new ArrayList<>(); // Liste des enseignants
    private String selectedCIN = ""; // CIN de l'enseignant sélectionné

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation des ViewModels et du référentiel
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_absence, container, false);

        // Initialisation des composants de l'interface utilisateur
        initUIComponents(view);

        // Remplissage des champs de date et heure avec les valeurs actuelles
        setCurrentDateAndTime();

        // Ajout des actions pour les sélecteurs de date et d'heure
        addDateAndTimePickers();

        // Chargement des enseignants depuis le référentiel
        fetchTeachers();

        // Configuration de l'action pour le bouton de validation
        setupValidationButton(view);

        // Action pour le bouton d'annulation
        btnCancel.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }

    // Méthode pour initialiser les composants de l'interface utilisateur
    private void initUIComponents(View view) {
        editTextProfName = view.findViewById(R.id.profName);
        editTextDate = view.findViewById(R.id.date);
        editTextStartTime = view.findViewById(R.id.startTime);
        editTextEndTime = view.findViewById(R.id.endTime);
        editTextReason = view.findViewById(R.id.reason);
        editTextSubjectName = view.findViewById(R.id.subjectName);
        spinnerStatus = view.findViewById(R.id.status);
        btnAddAbsence = view.findViewById(R.id.addButton);
        btnCancel = view.findViewById(R.id.cancelButton);

        // Configuration de l'adaptateur pour le spinner de statut d'absence
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.statut_absence, // Tableau défini dans les ressources
                android.R.layout.simple_spinner_item
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
    }

    // Méthode pour configurer le bouton de validation
    private void setupValidationButton(View view) {
        Button validateButton = view.findViewById(R.id.addButton);
        validateButton.setOnClickListener(v -> {
            if (validateInputs()) {
                // Création de l'objet Absence
                Absence absence = new Absence(
                        editTextProfName.getText().toString().trim(),
                        editTextDate.getText().toString().trim(),
                        editTextStartTime.getText().toString().trim(),
                        editTextEndTime.getText().toString().trim(),
                        editTextReason.getText().toString().trim(),
                        spinnerStatus.getSelectedItem().toString(),
                        editTextSubjectName.getText().toString().trim(),
                        selectedCIN
                );

                // Ajout de l'absence via le ViewModel
                absenceViewModel.addAbsence(absence);

                // Observation des messages de succès ou d'erreur
                absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
                    Toast.makeText(getActivity(), "Absence ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                });
            }
        });
    }

    // Méthode pour définir la date et l'heure actuelles
    @SuppressLint("DefaultLocale")
    private void setCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String currentTime = String.format("%02d:%02d", hour, minute);
        editTextStartTime.setText(currentTime);
        editTextEndTime.setText(currentTime);
    }

    // Méthodes pour afficher les sélecteurs de date et d'heure
    private void addDateAndTimePickers() {
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextStartTime.setOnClickListener(v -> showTimePicker(editTextStartTime));
        editTextEndTime.setOnClickListener(v -> showTimePicker(editTextEndTime));
    }

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
                },
                year, month, day
        );
        datePickerDialog.show();
    }

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

    // Méthode pour récupérer la liste des enseignants
    private void fetchTeachers() {
        userRepository.getTeacherNamesAndCIN(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Gestion des messages de succès
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                teachersList = users;
                List<String> teacherNames = new ArrayList<>();
                for (User user : users) {
                    teacherNames.add(user.getName());
                }

                // Configuration de l'AutoCompleteTextView pour les noms des enseignants
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_dropdown_item_1line, teacherNames);
                editTextProfName.setAdapter(adapter);
                editTextProfName.setOnItemClickListener((parent, view, position, id) -> {
                    String selectedName = (String) parent.getItemAtPosition(position);
                    for (User user : teachersList) {
                        if (user.getName().equals(selectedName)) {
                            selectedCIN = user.getCin();
                            break;
                        }
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getActivity(), "Erreur: " + errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccessUser(User user) {
                // Gérer un utilisateur spécifique si nécessaire
            }
        });
    }

    // Méthode pour valider les champs de saisie
    private boolean validateInputs() {
        String profName = editTextProfName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String reason = editTextReason.getText().toString().trim();
        String subject = editTextSubjectName.getText().toString().trim();

        if (profName.isEmpty()) {
            editTextProfName.setError("Veuillez entrer le nom du professeur.");
            return false;
        }
        if (date.isEmpty()) {
            editTextDate.setError("Veuillez sélectionner une date.");
            return false;
        }
        if (startTime.isEmpty()) {
            editTextStartTime.setError("Veuillez sélectionner une heure de début.");
            return false;
        }
        if (endTime.isEmpty()) {
            editTextEndTime.setError("Veuillez sélectionner une heure de fin.");
            return false;
        }
        if (reason.isEmpty()) {
            editTextReason.setError("Veuillez entrer une raison.");
            return false;
        }
        if (subject.isEmpty()) {
            editTextSubjectName.setError("Veuillez entrer le nom du sujet.");
            return false;
        }
        return true;
    }
}
