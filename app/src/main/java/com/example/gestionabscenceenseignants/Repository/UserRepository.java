package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db;

    // Constructeur
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Firestore instance initialized");
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user, UserCallback callback) {
        db.collection("users")
                .document(user.getUid())  // Utilise l'UID de l'utilisateur comme identifiant
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("UserRepository", "Utilisateur ajouté avec succès");
                    callback.onSuccess("Utilisateur ajouté avec succès");
                })
                .addOnFailureListener(e -> {
                    Log.e("UserRepository", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
                });
    }

    // Méthode pour récupérer les utilisateurs
    public void getUsers(UserCallback callback) {
        db.collection("users")
                .orderBy("name", Query.Direction.ASCENDING)  // Tri par nom, peut être modifié en fonction de la logique
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<User> users = new ArrayList<>();
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }
                        Log.d("UserRepository", "Récupération réussie, nombre d'utilisateurs : " + users.size());
                        callback.onSuccess(users);
                    } else {
                        if (task.getException() != null) {
                            FirebaseFirestoreException e = (FirebaseFirestoreException) task.getException();
                            Log.e("UserRepository", "Erreur lors de la récupération des utilisateurs : " + e.getMessage(), e);
                            callback.onFailure("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
                        } else {
                            Log.e("UserRepository", "La requête est terminée, mais la réponse est vide.");
                            callback.onFailure("Aucun utilisateur trouvé.");
                        }
                    }
                });
    }

    // Interface pour les callbacks de récupération des utilisateurs
    public interface UserCallback {
        void onSuccess(String message);
        void onSuccess(List<User> users);
        void onFailure(String errorMessage);
    }
}
