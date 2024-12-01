package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.User;

public class addUserFragment extends Fragment {

    private EditText editTextName, editTextEmail, editTextPassword;
    private Spinner spinnerRole;
    private Button btnSubmit, btnCancel;
    private Uri profilePhotoUri; // To store the URI of the selected photo

    private UserViewModel userViewModel;

    // Declare the ActivityResultLauncher for image picking
    private ActivityResultLauncher<String> getContentLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        // Initialize form fields
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);

        // Initialize the ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Register the ActivityResultLauncher to get image content
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    profilePhotoUri = result;
                    // Update ImageView with selected image (assuming you have an ImageView)
                    // imageViewProfile.setImageURI(result);
                }
            }
        });

        // Set up photo picker
        view.findViewById(R.id.imageViewProfile).setOnClickListener(v -> {
            // Launch the photo picker
            getContentLauncher.launch("image/*");
        });

        // Handle submit button click
        btnSubmit.setOnClickListener(v -> {
            // Get form values
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            // Simple validation
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new User object
            User newUser = new User(null, name, email, password, role, profilePhotoUri);

            // Add user via ViewModel
            userViewModel.addUser(newUser);

            // Observe error messages
            userViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
                if (error != null && !error.isEmpty()) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                } else {
                    // Success message and return to previous screen
                    Toast.makeText(getContext(), "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
            });
        });

        // Handle cancel button click
        btnCancel.setOnClickListener(v -> {
            // Return to the previous screen
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
