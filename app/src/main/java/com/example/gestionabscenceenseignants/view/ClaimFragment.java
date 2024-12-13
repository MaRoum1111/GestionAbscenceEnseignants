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
    private EditText editTextDate, editTextStartTime, editTextEndTime, editTextClaim, editTextClasse,editTextclaimDate;
    private Button btnSubmitClaim;
    private ClaimViewModel claimViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_claim, container, false);

        // Initialize ViewModel and UserRepository
        claimViewModel = new ViewModelProvider(this).get(ClaimViewModel.class);

        // Initialize UI components
        initUIComponents(view);

        // Set current date and time in the fields
        setCurrentDateAndTime();

        // Add DatePicker and TimePicker
        addDateAndTimePickers();

        // Handle add absence logic
        btnSubmitClaim.setOnClickListener(v -> addClaim());

        return view;
    }

    private void initUIComponents(View view) {
        editTextDate = view.findViewById(R.id.date);
        editTextStartTime = view.findViewById(R.id.startTime);
        editTextEndTime = view.findViewById(R.id.endTime);
        editTextClaim = view.findViewById(R.id.claim);
        editTextClasse = view.findViewById(R.id.classe);
        editTextclaimDate=view.findViewById((R.id.claimDate));
        btnSubmitClaim = view.findViewById(R.id.submitClaimButton);



    }

    @SuppressLint("DefaultLocale")
    private void setCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();

        // Set current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));
        editTextclaimDate.setText(String.format("%02d/%02d/%d", day, month + 1, year));
    }

    private void addDateAndTimePickers() {
        editTextDate.setOnClickListener(v -> showDatePicker());
        editTextclaimDate.setOnClickListener(v -> showDatePicker());
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
                    editTextclaimDate.setText(selectedDate);
                },
                day, year, month
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
                    editTextStartTime.setText(selectedTime);
                    editTextEndTime.setText(selectedTime);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void addClaim() {
        if (!validateInputs()) {
            return;
        }

        Claim claim = new Claim(
                editTextDate.getText().toString().trim(),
                editTextStartTime.getText().toString().trim(),
                editTextEndTime.getText().toString().trim(),
                editTextClaim.getText().toString().trim(),
                editTextClasse.getText().toString().trim(),
                editTextclaimDate.getText().toString().trim()

        );

        claimViewModel.addClaim(claim);
        claimViewModel.getErrorMessage().observe(getViewLifecycleOwner(), message -> {
            Toast.makeText(getActivity(), "Réclamation Ajouté avec succès", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

    }

    private boolean validateInputs() {

        String date = editTextDate.getText().toString().trim();
        String startTime = editTextStartTime.getText().toString().trim();
        String endTime = editTextEndTime.getText().toString().trim();
        String claim = editTextClaim.getText().toString().trim();
        String classe = editTextClasse.getText().toString().trim();

        if (date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || claim.isEmpty() || classe.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

}