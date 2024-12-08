package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    // Constructeur
    public UserRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Firestore instance initialized");
    }

    // Méthode pour récupérer les utilisateurs
    public void getUsers(UserCallback callback) {
        db.collection("users")
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }
                        Log.d("UserRepository", "Récupération réussie, nombre d'utilisateurs : " + users.size());
                        callback.onSuccessUsers(users);
                    } else {
                        String errorMessage = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "Aucun utilisateur trouvé.";
                        Log.e("UserRepository", "Erreur : " + errorMessage);
                        callback.onFailure(errorMessage);
                    }
                });
    }

    // Méthode pour ajouter un utilisateur
    public void addUser(User user, final UserCallback callback) {
        String cin = user.getCin();
        if (cin == null || cin.isEmpty()) {
            callback.onFailure("Le CIN est requis pour ajouter un utilisateur.");
            return;
        }

        // Créer l'utilisateur dans Firebase Authentication
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid(); // UID généré par Firebase Authentication
                            DocumentReference userRef = db.collection("users").document(uid);
                            user.setCin(cin);
                            userRef.set(user, SetOptions.merge())
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("UserRepository", "Utilisateur ajouté avec succès dans Firestore");
                                        callback.onSuccessMessage("Utilisateur ajouté avec succès.");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("UserRepository", "Erreur lors de l'ajout de l'utilisateur dans Firestore : " + e.getMessage());
                                        callback.onFailure("Erreur lors de l'ajout de l'utilisateur dans Firestore.");
                                    });
                        }
                    } else {
                        Log.e("UserRepository", "Erreur lors de la création de l'utilisateur dans Authentication : " + task.getException());
                        callback.onFailure("Erreur lors de la création de l'utilisateur dans Authentication.");
                    }
                });
    }

    // Méthode pour récupérer les enseignants et leurs CIN
    public void getTeacherNamesAndCIN(UserCallback callback) {
        db.collection("users")
                .whereEqualTo("role", "Enseignant")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> teachers = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User user = doc.toObject(User.class);
                            teachers.add(user);
                        }
                        callback.onSuccessUsers(teachers);
                    } else {
                        callback.onFailure(task.getException() != null
                                ? task.getException().getMessage()
                                : "Erreur inconnue lors de la récupération des enseignants.");
                    }
                });
    }

    // Méthode pour récupérer les informations de l'utilisateur connecté
    public void getLoggedUser(UserCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid(); // Récupérer l'UID de l'utilisateur connecté
            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            callback.onSuccessUser(user);
                        } else {
                            callback.onFailure("Aucun utilisateur trouvé.");
                        }
                    })
                    .addOnFailureListener(e -> callback.onFailure("Erreur de récupération de l'utilisateur : " + e.getMessage()));
        } else {
            callback.onFailure("Utilisateur non connecté.");
        }
    }

    // Interface pour les callbacks
    public interface UserCallback {
        void onSuccessMessage(String message);
        void onSuccessUsers(List<User> users);
        void onSuccessUser(User user);
        void onFailure(String error);
    }
}
