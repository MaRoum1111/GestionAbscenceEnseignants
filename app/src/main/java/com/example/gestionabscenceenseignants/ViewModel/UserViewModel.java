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
    private final MutableLiveData<String> imageUrl;
    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> userEmail;

    // Constructeur
    public UserViewModel() {
        repository = new UserRepository();
        users = new MutableLiveData<>();
        statusMessage = new MutableLiveData<>();
        teacherList = new MutableLiveData<>();
        imageUrl = new MutableLiveData<>();
        userName = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();
        loadLoggedUserDetails();
    }

    // Charger les données de l'utilisateur connecté
    public void loadLoggedUserDetails() {
        repository.getLoggedUser(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Pas nécessaire ici
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas nécessaire ici
            }

            @Override
            public void onFailure(String error) {
                statusMessage.setValue("Erreur : " + error);
            }

            @Override
            public void onSuccessUser(User user) {
                if (user != null) {
                    userName.setValue(user.getName());
                    userEmail.setValue(user.getEmail());
                } else {
                    statusMessage.setValue("Utilisateur non trouvé.");
                }
            }
        });
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
                users.setValue(userList);
            }

            @Override
            public void onFailure(String error) {
                statusMessage.setValue(error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Cette méthode doit aussi être implémentée
            }
        });
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user) {
        repository.addUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                statusMessage.setValue(successMessage);
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas utilisé ici
            }

            @Override
            public void onFailure(String error) {
                statusMessage.setValue(error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Cette méthode doit aussi être implémentée
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
                teacherList.setValue(teachers);
            }

            @Override
            public void onFailure(String errorMessage) {
                statusMessage.setValue(errorMessage);
            }

            @Override
            public void onSuccessUser(User user) {
                // Cette méthode doit aussi être implémentée
            }
        });
    }

    // Getters
    public LiveData<List<User>> getUsers() {
        return users;
    }

    public LiveData<List<User>> getTeacherList() {
        return teacherList;
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage;
    }

    public LiveData<String> getImageUrl() {
        return imageUrl;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }
}
