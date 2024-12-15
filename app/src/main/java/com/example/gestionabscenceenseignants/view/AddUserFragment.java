package com.example.gestionabscenceenseignants.view;

// Import des bibliothèques nécessaires
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.User;

// Fragment pour ajouter un utilisateur
public class AddUserFragment extends Fragment {

    // Déclaration des composants UI
    private EditText editTextCIN, editTextName, editTextEmail, editTextPassword;
    private Spinner spinnerRole;
    private Button btnSubmit, btnCancel;

    // Déclaration du ViewModel pour interagir avec les données
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Liaison du fragment avec son layout XML
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        // Initialisation du ViewModel (utilisé pour la gestion des utilisateurs)
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialisation des composants de l'interface utilisateur
        initUIComponents(view);

        // Configuration des actions associées aux boutons
        setupButtonListeners();

        return view; // Retourne la vue du fragment
    }

    /**
     * Initialise les composants de l'interface utilisateur
     *
     * @param view La vue contenant les éléments
     */
    private void initUIComponents(View view) {
        editTextCIN = view.findViewById(R.id.editTextCIN);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    /**
     * Configure les actions des boutons "Soumettre" et "Annuler"
     */
    private void setupButtonListeners() {
        // Bouton Soumettre : Ajouter un utilisateur
        btnSubmit.setOnClickListener(v -> addUser());

        // Bouton Annuler : Revenir à l'écran précédent
        btnCancel.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
    }

    /**
     * Ajoute un utilisateur après validation des champs
     */
    private void addUser() {
        // Récupération des données saisies dans le formulaire
        String cin = editTextCIN.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        // Validation des champs du formulaire
        if (!validateInputs(cin, name, email, password)) {
            return; // Si la validation échoue, on arrête l'exécution
        }

        // Création d'un nouvel utilisateur avec les données saisies
        User newUser = new User(cin, name, email, password, role);

        // Ajout de l'utilisateur dans la base de données via le ViewModel
        userViewModel.addUser(newUser);

        // Réinitialisation du formulaire après l'ajout
        clearForm();

        // Affichage d'un message de succès
        Toast.makeText(getContext(), "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();
    }

    /**
     * Valide les données saisies par l'utilisateur
     *
     * @param cin       CIN de l'utilisateur
     * @param name      Nom de l'utilisateur
     * @param email     Email de l'utilisateur
     * @param password  Mot de passe de l'utilisateur
     * @return true si toutes les données sont valides, sinon false
     */
    private boolean validateInputs(String cin, String name, String email, String password) {
        // Vérifie que tous les champs sont remplis
        if (TextUtils.isEmpty(cin) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Vérifie que l'email est valide
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email invalide");
            editTextEmail.requestFocus();
            return false;
        }

        // Vérifie que le CIN contient exactement 8 chiffres
        if (cin.length() != 8) {
            editTextCIN.setError("Le CIN doit contenir 8 chiffres");
            editTextCIN.requestFocus();
            return false;
        }

        // Vérifie que le mot de passe a au moins 6 caractères
        if (password.length() < 6) {
            editTextPassword.setError("Le mot de passe doit comporter au moins 6 caractères");
            editTextPassword.requestFocus();
            return false;
        }

        return true; // Tous les champs sont valides
    }

    /**
     * Réinitialise les champs du formulaire
     */
    private void clearForm() {
        editTextCIN.setText("");
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        spinnerRole.setSelection(0); // Réinitialise le Spinner au premier élément
    }

}
