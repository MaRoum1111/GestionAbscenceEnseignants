package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.UserRepository;
import com.example.gestionabscenceenseignants.model.User;
import java.util.List;

public class UserViewModel extends ViewModel {

    // Instance du repository pour interagir avec les données utilisateur
    private final UserRepository repository;

    // MutableLiveData pour contenir la liste des utilisateurs
    private final MutableLiveData<List<User>> users;

    // MutableLiveData pour contenir les messages de statut
    private final MutableLiveData<String> statusMessage;

    // MutableLiveData pour contenir la liste des enseignants (utilisateurs spécifiques)
    private final MutableLiveData<List<User>> teacherList;

    // MutableLiveData pour le nom de l'utilisateur connecté
    private final MutableLiveData<String> userName;

    // MutableLiveData pour l'email de l'utilisateur connecté
    private final MutableLiveData<String> userEmail;

    // Constructeur : initialisation du repository et des MutableLiveData
    public UserViewModel() {
        repository = new UserRepository();
        users = new MutableLiveData<>();
        statusMessage = new MutableLiveData<>();
        teacherList = new MutableLiveData<>();
        userName = new MutableLiveData<>();
        userEmail = new MutableLiveData<>();

        // Chargement des informations de l'utilisateur connecté
        loadLoggedUserDetails();
    }

    /**
     * Charger les détails de l'utilisateur actuellement connecté.
     * Met à jour les LiveData de nom et d'email de l'utilisateur.
     */
    public void loadLoggedUserDetails() {
        repository.getLoggedUser(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Pas nécessaire ici, la méthode est vide
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas nécessaire ici, la méthode est vide
            }

            @Override
            public void onFailure(String error) {
                // En cas d'échec, on met à jour le message de statut avec l'erreur
                statusMessage.setValue("Erreur : " + error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Si l'utilisateur est trouvé, on met à jour son nom et son email
                if (user != null) {
                    userName.setValue(user.getName());
                    userEmail.setValue(user.getEmail());
                } else {
                    // Si l'utilisateur n'est pas trouvé, on affiche un message d'erreur
                    statusMessage.setValue("Utilisateur non trouvé.");
                }
            }
        });
    }

    /**
     * Charger la liste de tous les utilisateurs.
     */
    public void loadUsers() {
        repository.getUsers(new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String message) {
                // Pas utilisé ici
            }

            @Override
            public void onSuccessUsers(List<User> userList) {
                // Met à jour la liste des utilisateurs
                users.setValue(userList);
            }

            @Override
            public void onFailure(String error) {
                // En cas d'échec, on met à jour le message de statut avec l'erreur
                statusMessage.setValue(error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Cette méthode doit aussi être implémentée mais n'est pas utilisée ici
            }
        });
    }

    /**
     * Ajouter un nouvel utilisateur.
     * @param user L'utilisateur à ajouter
     */
    public void addUser(User user) {
        repository.addUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                // Met à jour le message de statut avec le message de succès
                statusMessage.setValue(successMessage);
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas utilisé ici
            }

            @Override
            public void onFailure(String error) {
                // En cas d'échec, met à jour le message de statut avec l'erreur
                statusMessage.setValue(error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Pas utilisé ici
            }
        });
    }

    /**
     * Supprimer un utilisateur.
     * @param userId L'ID de l'utilisateur à supprimer
     */
    public void deleteUser(String userId) {
        repository.deleteUser(userId, new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                // Met à jour le message de statut avec le message de succès
                statusMessage.setValue(successMessage);
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas utilisé ici
            }

            @Override
            public void onFailure(String error) {
                // En cas d'échec, met à jour le message de statut avec l'erreur
                statusMessage.setValue(error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Pas utilisé ici
            }
        });
    }

    /**
     * Modifier un utilisateur.
     * @param user L'utilisateur avec les informations mises à jour
     */
    public void editUser(User user) {
        repository.editUser(user, new UserRepository.UserCallback() {
            @Override
            public void onSuccessMessage(String successMessage) {
                // Met à jour le message de statut avec le message de succès
                statusMessage.setValue(successMessage);
            }

            @Override
            public void onSuccessUsers(List<User> users) {
                // Pas utilisé ici
            }

            @Override
            public void onFailure(String error) {
                // En cas d'échec, met à jour le message de statut avec l'erreur
                statusMessage.setValue(error);
            }

            @Override
            public void onSuccessUser(User user) {
                // Pas utilisé ici
            }
        });
    }

    // Getters pour les LiveData afin de permettre l'observation par la vue

    public LiveData<List<User>> getUsers() {
        return users; // Retourne la liste des utilisateurs
    }

    public LiveData<List<User>> getTeacherList() {
        return teacherList; // Retourne la liste des enseignants (si utilisée)
    }

    public LiveData<String> getStatusMessage() {
        return statusMessage; // Retourne le message de statut (succès ou erreur)
    }

    public LiveData<String> getUserName() {
        return userName; // Retourne le nom de l'utilisateur connecté
    }

    public LiveData<String> getUserEmail() {
        return userEmail; // Retourne l'email de l'utilisateur connecté
    }
}
