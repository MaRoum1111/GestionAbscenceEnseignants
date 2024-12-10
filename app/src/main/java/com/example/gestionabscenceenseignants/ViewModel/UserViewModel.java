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
    private final MutableLiveData<String> userName;
    private final MutableLiveData<String> userEmail;

    // Constructeur
    public UserViewModel() {
        repository = new UserRepository();
        users = new MutableLiveData<>();
        statusMessage = new MutableLiveData<>();
        teacherList = new MutableLiveData<>();
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
    // Méthode pour supprimer un utilisateur
    public void deleteUser(String userId) {
        repository.deleteUser(userId, new UserRepository.UserCallback() {
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
                // Pas utilisé ici
            }
        });
    }
    // Méthode pour modifier un utilisateur
    public void editUser(User user) {
        repository.editUser(user, new UserRepository.UserCallback() {
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
                // Pas utilisé ici
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

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }
}
