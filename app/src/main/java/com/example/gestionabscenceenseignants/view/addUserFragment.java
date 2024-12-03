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
import android.widget.ImageView;
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
    private Uri profilePhotoUri; // URI pour stocker l'image du profil
    private ImageView imageViewProfile; // ImageView pour afficher la photo du profil
    private UserViewModel userViewModel;

    // Déclaration du ActivityResultLauncher pour la sélection de photo
    private ActivityResultLauncher<String> getContentLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflation du layout
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        // Initialisation des champs de formulaire
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        spinnerRole = view.findViewById(R.id.spinnerRole);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        imageViewProfile = view.findViewById(R.id.imageViewProfile); // Initialisation de ImageView

        // Initialisation du ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Enregistrement du ActivityResultLauncher pour obtenir une image
        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    profilePhotoUri = result;
                    // Mise à jour de l'image dans l'ImageView avec l'image sélectionnée
                    imageViewProfile.setImageURI(result);
                }
            }
        });

        // Configuration du clic sur l'ImageView pour lancer le sélecteur d'image
        imageViewProfile.setOnClickListener(v -> {
            // Lancer le sélecteur d'image
            getContentLauncher.launch("image/*");
        });

        // Gestion du clic sur le bouton "Submit"
        btnSubmit.setOnClickListener(v -> {
            // Récupération des valeurs du formulaire
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String role = spinnerRole.getSelectedItem().toString();

            // Validation simple des champs
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validation de l'email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(getContext(), "Email invalide", Toast.LENGTH_SHORT).show();
                return;
            }

            // Conversion de l'URI en chaîne de caractères
            String photoUriString = profilePhotoUri != null ? profilePhotoUri.toString() : null;

            // Création d'un nouvel utilisateur
            User newUser = new User(null, name, email, password, role, photoUriString);

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

    private void clearForm() {
        // Réinitialisation des champs du formulaire
        editTextName.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        spinnerRole.setSelection(0); // Réinitialisation du Spinner
        imageViewProfile.setImageResource(R.drawable.icc_add); // Réinitialisation de l'image du profil
    }
}
