package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.UserRepository;
import com.example.gestionabscenceenseignants.model.User;
import java.util.List;

public class UserViewModel extends ViewModel {
    private final UserRepository repository; // Instance du UserRepository
    private final MutableLiveData<List<User>> users; // LiveData pour la liste des utilisateurs
    private final MutableLiveData<String> message; // LiveData pour les messages (succès ou erreurs)

    // Constructeur
    public UserViewModel() {
        repository = new UserRepository(); // Instanciation du UserRepository
        users = new MutableLiveData<>(); // LiveData pour les utilisateurs
        message = new MutableLiveData<>(); // LiveData pour les messages
    }

    // Méthode pour charger les utilisateurs
    public void loadUsers() {
        repository.getUsers(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                // Cette méthode n'est pas utilisée ici
            }

            @Override
            public void onSuccessUsers(List<User> userList) {
                users.setValue(userList); // Met à jour la liste des utilisateurs si récupération réussie
            }

            @Override
            public void onFailure(String error) {
                message.setValue(error); // Met à jour le message d'erreur si une erreur survient
            }
        });
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user) {
        repository.addUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                message.setValue(successMessage); // Met à jour le message de succès si ajout réussi
            }

            @Override
            public void onSuccessUsers(List<User> usersList) {
                // Cette méthode n'est pas utilisée ici
            }

            @Override
            public void onFailure(String error) {
                message.setValue(error); // Met à jour le message d'erreur si une erreur survient
            }
        });
    }

    // Getter pour LiveData des utilisateurs
    public LiveData<List<User>> getUsers() {
        return users; // Retourne la liste des utilisateurs sous forme de LiveData
    }

    // Getter pour LiveData des messages
    public LiveData<String> getMessage() {
        return message; // Retourne le message de succès ou d'erreur sous forme de LiveData
    }
}
