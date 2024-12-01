package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
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

    // Méthode pour ajouter un utilisateur
    public void addUser(User user, UserCallback callback) {
        db.collection("users")
                .add(user)  // Ajoute l'utilisateur dans la collection "users"
                .addOnSuccessListener(documentReference -> {
                    Log.d("UserRepository", "Utilisateur ajouté avec succès");
                    callback.onSuccessMessage(null);  // Pas besoin de retour spécifique ici, on passe null
                })
                .addOnFailureListener(e -> {
                    Log.e("UserRepository", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
                });
    }

    // Méthode pour récupérer les utilisateurs
    public void getUsers(UserCallback callback) {
        db.collection("users")
                .orderBy("name", Query.Direction.ASCENDING)  // Tri par nom (modifiable selon vos besoins)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }

                        if (users.isEmpty()) {
                            Log.w("UserRepository", "Aucun utilisateur trouvé.");
                            callback.onFailure("Aucun utilisateur trouvé.");
                        } else {
                            Log.d("UserRepository", "Récupération réussie, nombre d'utilisateurs : " + users.size());
                            callback.onSuccessUsers(users);
                        }
                    } else {
                        String errorMessage = (task.getException() != null)
                                ? "Erreur lors de la récupération : " + task.getException().getMessage()
                                : "Résultat vide ou null.";
                        Log.e("UserRepository", errorMessage);
                        callback.onFailure(errorMessage);
                    }
                });
    }
    // Interface pour gérer les callbacks
    public interface UserCallback {
        void onSuccessMessage(String message);  // Pour les messages de succès
        void onSuccessUsers(List<User> users);  // Pour les résultats sous forme de liste d'utilisateurs
        void onFailure(String errorMessage);    // Pour gérer les erreurs
    }
}
