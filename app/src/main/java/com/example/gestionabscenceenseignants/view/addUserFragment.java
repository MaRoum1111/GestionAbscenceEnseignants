package com.example.gestionabscenceenseignants.view;

import android.net.Uri;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.User;

public class addUserFragment extends Fragment {

    private EditText editTextCIN, editTextName, editTextEmail, editTextPassword;
    private Spinner spinnerRole;
    private Button btnSubmit, btnCancel;
    private UserViewModel userViewModel;

    // Déclaration du ActivityResultLauncher pour la sélection de photo
    private ActivityResultLauncher<String> getContentLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Cette méthode est appelée quand le fragment est créé (avant onCreateView)
        // C'est ici qu'on pourrait initialiser des ressources ou des données nécessaires pour l'activité
        // Si vous avez des données à récupérer d'une activité ou d'un autre fragment, faites-le ici
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflation du layout
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        // Initialisation des champs de formulaire
        editTextCIN = view.findViewById(R.id.editTextCIN);
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);

        // Initialisation du ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Gestion du clic sur le bouton "Submit"
        btnSubmit.setOnClickListener(v -> {
            // Récupération des valeurs du formulaire
            String cin = editTextCIN.getText().toString().trim();
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            // Validation des champs
            if (TextUtils.isEmpty(cin) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Email invalide");
                editTextEmail.requestFocus();
                return;
            }

            if (cin.length() != 8) {
                editTextCIN.setError("Le CIN doit contenir 8 chiffres");
                editTextCIN.requestFocus();
                return;
            }

            if (password.length() < 6) {
                editTextPassword.setError("Le mot de passe doit comporter au moins 6 caractères");
                editTextPassword.requestFocus();
                return;
            }

            // Création d'un nouvel utilisateur
            User newUser = new User(cin, name, email, password, role);

            // Ajout de l'utilisateur à la base de données via le ViewModel
            userViewModel.addUser(newUser);

            // Réinitialisation du formulaire après soumission
            clearForm();

            // Message de succès
            Toast.makeText(getContext(), "Utilisateur ajouté avec succès", Toast.LENGTH_SHORT).show();
        });

        // Gestion du clic sur le bouton "Cancel"
        btnCancel.setOnClickListener(v -> clearForm());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Cette méthode est appelée quand le fragment devient visible pour l'utilisateur
        // C'est ici qu'on peut mettre en place les observateurs pour les LiveData ou effectuer des tâches qui nécessitent que le fragment soit visible
    }

    @Override
    public void onStop() {
        super.onStop();
        // Cette méthode est appelée quand le fragment n'est plus visible
        // C'est ici qu'on peut libérer les ressources, annuler des appels réseau, ou sauvegarder l'état si nécessaire
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cette méthode est appelée quand la vue du fragment est détruite (avant onDestroy)
        // C'est ici qu'on peut libérer les ressources liées à la vue si nécessaire
    }

    private void clearForm() {
        // Réinitialisation des champs du formulaire
        editTextCIN.setText("");
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        spinnerRole.setSelection(0); // Réinitialisation du Spinner
    }
}
