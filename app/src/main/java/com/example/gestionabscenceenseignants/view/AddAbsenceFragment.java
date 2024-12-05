package com.example.gestionabscenceenseignants.view;

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

    private AutoCompleteTextView editTextProfName;
    private EditText editTextDate, editTextStartTime, editTextEndTime, editTextReason, editTextSubjectName;
    private Spinner spinnerStatus;
    private Button btnAddAbsence, btnCancel;
    private AbsenceViewModel absenceViewModel;
    private UserRepository userRepository;
    private List<User> teachersList = new ArrayList<>();
    private String selectedCIN = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_absence, container, false);

        // Initialize ViewModel and UserRepository
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
        userRepository = new UserRepository();

        // Initialize UI components
        editTextProfName = view.findViewById(R.id.profName);
        editTextDate = view.findViewById(R.id.date);
        editTextStartTime = view.findViewById(R.id.startTime);
        editTextEndTime = view.findViewById(R.id.endTime);
        editTextReason = view.findViewById(R.id.reason);
        editTextSubjectName = view.findViewById(R.id.subjectName);
        spinnerStatus = view.findViewById(R.id.status);
        btnAddAbsence = view.findViewById(R.id.addButton);
        btnCancel = view.findViewById(R.id.cancelButton);

        // Set current date and time in the fields
        setCurrentDateAndTime();

        // Add DatePicker and TimePicker
        addDateAndTimePickers();

        // Fetch teachers' names and CINs
        fetchTeachers();

        // Handle add absence logic
        btnAddAbsence.setOnClickListener(v -> addAbsence());

        // Cancel action
        btnCancel.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }

    private void setCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        // Set current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));

        // Set current time
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        String currentTime = String.format("%02d:%02d", hour, minute);
        editTextStartTime.setText(currentTime);
        editTextEndTime.setText(currentTime); // Default: same time for start and end
    }

    private void addDateAndTimePickers() {
        editTextDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        editTextDate.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        editTextStartTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view1, selectedHour, selectedMinute) -> {
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        editTextStartTime.setText(selectedTime);
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });

        editTextEndTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view1, selectedHour, selectedMinute) -> {
                        String selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                        editTextEndTime.setText(selectedTime);
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });
    }

    private void fetchTeachers() {
        userRepository.getTeacherNamesAndCIN(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {}

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
        });
    }

    private void addAbsence() {
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

        if (selectedCIN.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez sélectionner un enseignant valide", Toast.LENGTH_SHORT).show();
            return;
        }

        Absence absence = new Absence(profName, date, startTime, endTime, reason, status, subjectName, selectedCIN);
        absenceViewModel.addAbsence(absence);

        absenceViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                if (message.equals("Absence ajoutée avec succès")) {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}
