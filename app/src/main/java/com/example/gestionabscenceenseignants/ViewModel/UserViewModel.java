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
    private final MutableLiveData<String> errorMessage; // LiveData pour le message d'erreur

    // Constructeur
    public UserViewModel() {
        repository = new UserRepository(); // Instanciation du UserRepository
        users = new MutableLiveData<>(); // LiveData pour les utilisateurs
        errorMessage = new MutableLiveData<>(); // LiveData pour les erreurs
    }

    // Méthode pour charger les utilisateurs
    public void loadUsers() {
        repository.getUsers(new UserRepository.UserCallback() {
            @Override
            public void onSuccess(List<User> userList) {
                users.setValue(userList); // Si la récupération réussit, mettre à jour la liste des utilisateurs dans LiveData
            }

            @Override
            public void onSuccess(String message) {
                // Cette méthode ne sera pas utilisée ici
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Si une erreur se produit, mettre à jour le message d'erreur
            }
        });
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user) {
        repository.addUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccess(List<User> usersList) {
                // Cette méthode ne sera pas utilisée dans le cas de l'ajout d'un utilisateur
            }

            @Override
            public void onSuccess(String message) {
                errorMessage.setValue(message); // Si l'ajout réussit, envoyer un message de succès
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Si une erreur se produit, envoyer un message d'erreur
            }
        });
    }

    // Getter pour LiveData des utilisateurs
    public LiveData<List<User>> getUsers() {
        return users; // Retourne la liste des utilisateurs
    }

    // Getter pour LiveData des messages d'erreur
    public LiveData<String> getErrorMessage() {
        return errorMessage; // Retourne le message d'erreur
    }
}
