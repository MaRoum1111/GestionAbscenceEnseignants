package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.User;

public class EditUserFragment extends Fragment {

    // Déclaration des variables de la vue
    private EditText editTextCIN, editTextName, editTextEmail, editTextPassword;
    private Spinner spinnerRole;
    private UserViewModel userViewModel;

    private String nom, Cin, email, pass, role;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflater le layout du fragment
        return inflater.inflate(R.layout.fragment_edit_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialisation des éléments de la vue
        initViews(view);

        // Initialiser le ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Charger les données transmises par le fragment précédent
        loadDataFromArguments();

        // Configurer les clics des boutons
        configureButtonClicks();
    }

    // Initialisation des vues
    private void initViews(View view) {
        editTextCIN = view.findViewById(R.id.editTextCIN);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);
    }

    // Charger les données transmises par l'activité précédente
    private void loadDataFromArguments() {
        if (getArguments() != null) {
            Cin = getArguments().getString("cin");
            nom = getArguments().getString("name");
            email = getArguments().getString("email");
            pass = getArguments().getString("pass");
            String status = getArguments().getString("role");

            // Mettre à jour les champs EditText avec les données récupérées
            editTextCIN.setText(Cin);
            editTextName.setText(nom);
            editTextEmail.setText(email);
            editTextPassword.setText(pass);

            // Mettre à jour le Spinner avec la valeur du rôle
            if (status != null) {
                String[] statuses = getResources().getStringArray(R.array.role_array);
                for (int i = 0; i < statuses.length; i++) {
                    if (statuses[i].equals(status)) {
                        spinnerRole.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    // Configurer les clics des boutons
    private void configureButtonClicks() {
        Button btnSubmit = requireView().findViewById(R.id.btnSubmit);
        Button btnCancel = requireView().findViewById(R.id.btnCancel);

        // Gérer le clic sur le bouton de validation
        btnSubmit.setOnClickListener(v -> {
            if (validateFields()) {
                updateUser();
            }
        });

        // Gérer le clic sur le bouton d'annulation
        btnCancel.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Mettre à jour l'utilisateur dans le ViewModel
    private void updateUser() {
        User updatedUser = new User(
                editTextCIN.getText().toString(),
                editTextName.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString(),
                spinnerRole.getSelectedItem().toString()
        );

        // Appel à la méthode dans le ViewModel pour éditer l'utilisateur
        userViewModel.editUser(updatedUser);

        // Afficher un message de succès
        Toast.makeText(getContext(), "Utilisateur mis à jour avec succès", Toast.LENGTH_SHORT).show();

        // Rafraîchir la liste des utilisateurs dans le fragment des utilisateurs
        userViewModel.loadUsers();

        // Retourner au fragment précédent
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    // Méthode pour valider les champs
    private boolean validateFields() {
        if (editTextName.getText().toString().isEmpty()) {
            editTextName.setError("Veuillez entrer le nom");
            return false;
        }
        if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError("Veuillez entrer un email valide");
            return false;
        }
        if (editTextPassword.getText().toString().isEmpty()) {
            editTextPassword.setError("Veuillez entrer un mot de passe");
            return false;
        }
        return true;
    }

    // Méthodes sur le cycle de vie du fragment
    @Override
    public void onResume() {
        super.onResume();
        // Cette méthode est appelée lorsque le fragment est visible à l'utilisateur
    }

    @Override
    public void onPause() {
        super.onPause();
        // Cette méthode est appelée lorsque le fragment n'est plus en avant-plan
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cette méthode est appelée quand la vue du fragment est détruite
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cette méthode est appelée quand le fragment est détruit
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Cette méthode est appelée quand le fragment est détaché de son activité
    }
}
