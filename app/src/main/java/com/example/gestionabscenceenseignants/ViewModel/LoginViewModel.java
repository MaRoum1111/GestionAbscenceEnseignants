package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.LoginRepository;
import com.example.gestionabscenceenseignants.model.User;

public class LoginViewModel extends ViewModel {

    // Instance du repository pour gérer l'authentification
    private final LoginRepository loginRepository;

    // MutableLiveData qui contiendra le résultat de la connexion
    private final MutableLiveData<User> loginResult = new MutableLiveData<>();

    // Constructeur : initialisation du repository
    public LoginViewModel() {
        loginRepository = new LoginRepository(); // Initialisation du repository de connexion
    }

    /**
     * Getter pour obtenir le résultat de la connexion.
     * @return LiveData contenant l'utilisateur connecté
     */
    public LiveData<User> getLoginResult() {
        return loginResult; // Retourne le LiveData contenant l'utilisateur ou null en cas d'échec
    }

    /**
     * Méthode pour tenter de se connecter en utilisant l'email et le mot de passe.
     * Appelle le repository pour effectuer l'authentification.
     * @param email L'email de l'utilisateur
     * @param password Le mot de passe de l'utilisateur
     */
    public void login(String email, String password) {
        loginRepository.login(email, password, new LoginRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                // Mise à jour du résultat de la connexion en cas de succès
                loginResult.setValue(user); // L'utilisateur connecté est renvoyé en cas de succès
            }

            @Override
            public void onFailure(String errorMessage) {
                // Mise à jour du résultat de la connexion en cas d'échec
                loginResult.setValue(null); // Si la connexion échoue, la valeur est définie à null
            }
        });
    }
}
