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

        // Initialisation des champs de formulaire
        editTextCIN = view.findViewById(R.id.editTextCIN);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Initialiser le ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Charger les données transmises par le fragment précédent
        if (getArguments() != null) {
            Cin = getArguments().getString("cin");
            nom = getArguments().getString("name");
            email = getArguments().getString("email");
            pass = getArguments().getString("pass");
            String status = getArguments().getString("role");

            // Mettre à jour les EditText avec les données récupérées
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

        // Gérer le clic sur le bouton de validation
        btnSubmit.setOnClickListener(v -> {
            if (validateFields()) {
                User updatedUser = new User(
                        editTextCIN.getText().toString(),
                        editTextName.getText().toString(),
                        editTextEmail.getText().toString(),
                        editTextPassword.getText().toString(),
                        spinnerRole.getSelectedItem().toString()
                );

                // Après la modification réussie de l'utilisateur
                userViewModel.editUser(updatedUser);
                Toast.makeText(getContext(), "Utilisateur mis à jour avec succès", Toast.LENGTH_SHORT).show();

// Forcer le rafraîchissement des utilisateurs dans le fragment des utilisateurs
                userViewModel.loadUsers();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Gérer le clic sur le bouton d'annulation
        btnCancel.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // Cette méthode est appelée lorsque le fragment est visible à l'utilisateur
        // Idéal pour actualiser les données ou effectuer des tâches spécifiques lorsque le fragment est en première ligne
    }

    @Override
    public void onPause() {
        super.onPause();
        // Cette méthode est appelée lorsque le fragment n'est plus en avant-plan
        // Peut être utilisée pour sauvegarder l'état ou libérer des ressources
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cette méthode est appelée quand la vue du fragment est détruite
        // Idéal pour libérer les références aux vues et autres ressources associées
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cette méthode est appelée quand le fragment est détruit
        // Elle permet de nettoyer les ressources et références importantes
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Cette méthode est appelée quand le fragment est détaché de son activité
        // Utile pour effectuer des actions de nettoyage spécifiques liées au fragment
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

}
