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
import com.example.gestionabscenceenseignants.model.Claim;
import com.example.gestionabscenceenseignants.model.User;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import com.example.gestionabscenceenseignants.Repository.UserRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAbsenceFragment extends Fragment {

    private AutoCompleteTextView editTextProfName;
    private EditText editTextDate, editTextStartTime, editTextEndTime, editTextReason, editTextSubjectName;
    private Spinner spinnerStatus;
    private Button btnAddAbsence, btnCancel;
    private AbsenceViewModel absenceViewModel;
    private UserRepository userRepository;
    private List<User> teachersList = new ArrayList<>();
    private String selectedCIN = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userRepository = new UserRepository();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_absence, container, false);

        // Initialize UI components
        initUIComponents(view);

        // Set current date and time in the fields
        setCurrentDateAndTime();

        // Add DatePicker and TimePicker
        addDateAndTimePickers();

        // Fetch teachers' names and CINs
        fetchTeachers();

        // Handle add absence logic
        setupValidationButton(view);

        // Cancel action
        btnCancel.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }

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

        // Set up spinner for absence status
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.statut_absence, // Assuming you have this array in your resources
                android.R.layout.simple_spinner_item
        );
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);
    }

    private void setupValidationButton(View view) {
        Button validateButton = view.findViewById(R.id.addButton);
        validateButton.setOnClickListener(v -> {
            if (validateInputs()) {
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

                absenceViewModel.addAbsence(absence);
                // Observer pour les erreurs ou succès
                absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
                    Toast.makeText(getActivity(), "Absence ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();

                });
            }});}

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

    private void fetchTeachers() {
        userRepository.getTeacherNamesAndCIN(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Handle success message
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                teachersList = users;
                List<String> teacherNames = new ArrayList<>();
                for (User user : users) {
                    teacherNames.add(user.getName());
                }

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
                // Handle success user if necessary
            }
        });
    }

    private void addAbsence() {
        if (!validateInputs()) {
            return;
        }

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

        absenceViewModel.addAbsence(absence);
        // Observer pour les erreurs ou succès
        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getActivity(), "Absence ajoutée avec succès", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();

        });
    }

    private boolean validateInputs() {
        String profName = editTextProfName.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String reason = editTextReason.getText().toString().trim();
        String subjectName = editTextSubjectName.getText().toString().trim();

        if (profName.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || reason.isEmpty() || subjectName.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedCIN.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez sélectionner un enseignant valide", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


        }