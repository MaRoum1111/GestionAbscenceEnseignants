package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionabscenceenseignants.Repository.UserRepository;
import com.example.gestionabscenceenseignants.model.User;

import java.util.List;

public class UserViewModel extends ViewModel {
    private final UserRepository repository;
    private final MutableLiveData<List<User>> users;
    private final MutableLiveData<String> statusMessage;
    private final MutableLiveData<List<User>> teacherList;
    private final MutableLiveData<String> imageUrl;  // Pour stocker l'URL de l'image

    // Constructeur
    public UserViewModel() {
        repository = new UserRepository(); // Instanciation du UserRepository
        users = new MutableLiveData<>(); // LiveData pour la liste des utilisateurs
        statusMessage = new MutableLiveData<>(); // LiveData pour les messages de statut (succès ou erreurs)
        teacherList = new MutableLiveData<>(); // LiveData pour la liste des enseignants
        imageUrl = new MutableLiveData<>(); // LiveData pour l'URL de l'image
    }

    // Méthode pour charger la liste des utilisateurs
    public void loadUsers() {
        repository.getUsers(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Pas utilisé ici
            }

            @Override
            public void onSuccessUsers(List<User> userList) {
                // Mise à jour de la liste des utilisateurs si récupération réussie
                users.setValue(userList);
            }

            @Override
            public void onFailure(String error) {
                // Mise à jour du message d'erreur en cas de problème
                statusMessage.setValue(error);
            }
        });
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user) {
        repository.addUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                // Mise à jour du message de succès si ajout réussi
                statusMessage.setValue(successMessage);
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas utilisé ici
            }

            @Override
            public void onFailure(String error) {
                // Mise à jour du message d'erreur en cas de problème
                statusMessage.setValue(error);
            }
        });
    }

    // Méthode pour charger les enseignants (nom et CIN)
    public void loadTeacherNamesAndCIN() {
        repository.getTeacherNamesAndCIN(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Pas utilisé ici
            }

            @Override
            public void onSuccessUsers(List<User> teachers) {
                // Mise à jour de la liste des enseignants si récupération réussie
                teacherList.setValue(teachers);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Mise à jour du message d'erreur en cas de problème
                statusMessage.setValue(errorMessage);
            }
        });
    }

    // Nouvelle méthode pour récupérer l'URL de l'image en fonction du CIN
    public void getPhotoByCin(String cin) {
        repository.getImageByCin(cin, new UserRepository.OnImageRetrievedListener() {
            @Override
            public void onImageRetrieved(String imageUrlResult) {
                // Mise à jour de l'URL de l'image
                imageUrl.setValue(imageUrlResult);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Mise à jour du message d'erreur en cas de problème
                imageUrl.setValue(errorMessage);
            }
        });
    }

    // Getter pour la liste des utilisateurs
    public LiveData<List<User>> getUsers() {
        return users;
    }

    // Getter pour la liste des enseignants
    public LiveData<List<User>> getTeacherList() {
        return teacherList;
    }

    // Getter pour les messages de statut (succès ou erreur)
    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    // Getter pour l'URL de l'image
    public LiveData<String> getImageUrl() {
        return imageUrl;
    }
}
