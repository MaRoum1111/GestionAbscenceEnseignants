package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db;

    // Constructeur
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Firestore instance initialized");
    }

    // Méthode pour récupérer les utilisateurs
    public void getUsers(UserCallback callback) {
        db.collection("users")
                .orderBy("name", Query.Direction.ASCENDING) // Tri par nom (modifiable selon vos besoins)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<User> users = new ArrayList<>();
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }
                        Log.d("UserRepository", "Récupération réussie, nombre d'utilisateurs : " + users.size());
                        callback.onSuccessUsers(users);
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

    // Méthode pour ajouter un utilisateur avec le CIN comme ID de document
    public void addUser(User user, UserCallback callback) {
        String cin = user.getCin(); // Assurez-vous que la classe User a un getter pour le champ CIN
        if (cin == null || cin.isEmpty()) {
            callback.onFailure("Le CIN est requis pour ajouter un utilisateur.");
            return;
        }

        db.collection("users")
                .document(cin) // Utilise le CIN comme ID du document
                .set(user) // Ajoute l'utilisateur à la collection "users"
                .addOnSuccessListener(aVoid -> {
                    Log.d("UserRepository", "Utilisateur ajouté avec succès avec CIN comme ID.");
                    callback.onSuccessMessage("Utilisateur ajouté avec succès.");
                })
                .addOnFailureListener(e -> {
                    Log.e("UserRepository", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
                });
    }


    // Interface pour gérer les callbacks
    public interface UserCallback {
        void onSuccessMessage(String message);  // Pour les messages de succès
        void onSuccessUsers(List<User> users);  // Pour les résultats sous forme de liste d'utilisateurs
        void onFailure(String errorMessage);    // Pour gérer les erreurs
    }
}
