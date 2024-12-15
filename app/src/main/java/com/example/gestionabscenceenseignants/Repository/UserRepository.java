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
import java.util.Arrays;
import java.util.List;

public class UserRepository {
    // Déclaration des instances Firebase pour Firestore et FirebaseAuth
    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    // Constructeur pour initialiser FirebaseAuth et FirebaseFirestore
    public UserRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Firestore instance initialized");
    }

    // Méthode pour récupérer les utilisateurs ayant les rôles "Enseignant" et "Agent"
    public void getUsers(UserCallback callback) {
        db.collection("users")
                .whereIn("role", Arrays.asList("Enseignant", "Agent"))  // Filtre les utilisateurs par rôle
                .orderBy("name", Query.Direction.ASCENDING)  // Trie par nom
                .get()  // Exécute la requête Firestore
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Si la requête réussit, on crée une liste d'utilisateurs à partir des résultats
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);  // Convertit le document Firestore en objet User
                            users.add(user);  // Ajoute l'utilisateur à la liste
                        }
                        Log.d("UserRepository", "Récupération réussie, nombre d'utilisateurs : " + users.size());
                        callback.onSuccessUsers(users);  // Appelle le callback avec les utilisateurs récupérés
                    } else {
                        // En cas d'erreur, on récupère le message d'erreur et on le passe au callback
                        String errorMessage = (task.getException() != null)
                                ? task.getException().getMessage()
                                : "Aucun utilisateur trouvé.";
                        Log.e("UserRepository", "Erreur : " + errorMessage);
                        callback.onFailure(errorMessage);  // Appelle le callback en cas d'échec
                    }
                });
    }

    // Méthode pour ajouter un utilisateur dans Firebase Authentication et Firestore
    public void addUser(User user, final UserCallback callback) {
        String cin = user.getCin();  // Récupère le CIN de l'utilisateur
        if (cin == null || cin.isEmpty()) {  // Vérifie que le CIN n'est pas vide
            callback.onFailure("Le CIN est requis pour ajouter un utilisateur.");
            return;
        }
        // Crée un utilisateur dans Firebase Authentication
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Si l'utilisateur est créé avec succès dans Authentication
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();  // Récupère l'UID généré par Firebase Authentication
                            DocumentReference userRef = db.collection("users").document(uid);  // Référence au document utilisateur dans Firestore
                            user.setCin(cin);  // Associe le CIN à l'utilisateur
                            userRef.set(user, SetOptions.merge())  // Ajoute ou met à jour l'utilisateur dans Firestore
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
                        // En cas d'erreur lors de la création de l'utilisateur dans Firebase Authentication
                        Log.e("UserRepository", "Erreur lors de la création de l'utilisateur dans Authentication : " + task.getException());
                        callback.onFailure("Erreur lors de la création de l'utilisateur dans Authentication.");
                    }
                });
    }

    // Méthode pour récupérer les enseignants et leurs CIN
    public void getTeacherNamesAndCIN(UserCallback callback) {
        db.collection("users")
                .whereEqualTo("role", "Enseignant")  // Filtre les utilisateurs ayant le rôle "Enseignant"
                .get()  // Exécute la requête Firestore
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Si la requête réussit, on crée une liste d'enseignants
                        List<User> teachers = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            User user = doc.toObject(User.class);  // Convertit le document Firestore en objet User
                            teachers.add(user);  // Ajoute l'enseignant à la liste
                        }
                        callback.onSuccessUsers(teachers);  // Appelle le callback avec les enseignants récupérés
                    } else {
                        callback.onFailure(task.getException() != null
                                ? task.getException().getMessage()
                                : "Erreur inconnue lors de la récupération des enseignants.");
                    }
                });
    }

    // Méthode pour récupérer les informations de l'utilisateur connecté
    public void getLoggedUser(UserCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();  // Récupère l'utilisateur actuellement connecté
        if (currentUser != null) {
            String uid = currentUser.getUid();  // Récupère l'UID de l'utilisateur connecté
            db.collection("users").document(uid)  // Référence au document utilisateur dans Firestore
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);  // Convertit le document en objet User
                            callback.onSuccessUser(user);  // Appelle le callback avec l'utilisateur récupéré
                        } else {
                            callback.onFailure("Aucun utilisateur trouvé.");
                        }
                    })
                    .addOnFailureListener(e -> callback.onFailure("Erreur de récupération de l'utilisateur : " + e.getMessage()));
        } else {
            callback.onFailure("Utilisateur non connecté.");
        }
    }

    // Méthode pour supprimer un utilisateur en utilisant son CIN
    public void deleteUser(String cin, final UserCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();  // Récupère l'utilisateur connecté
        if (currentUser != null) {
            // Recherche l'utilisateur avec le CIN spécifié
            db.collection("users")
                    .whereEqualTo("cin", cin)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            String userId = task.getResult().getDocuments().get(0).getId();  // Récupère l'ID du document utilisateur
                            Log.d("UserRepository", "Document trouvé avec ID : " + userId);

                            // Supprime l'utilisateur de Firestore
                            db.collection("users").document(userId)
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("UserRepository", "Utilisateur supprimé avec succès.");
                                        callback.onSuccessMessage("Utilisateur supprimé avec succès.");
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("UserRepository", "Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
                                        callback.onFailure("Erreur lors de la suppression de l'utilisateur.");
                                    });
                        } else {
                            callback.onFailure("Aucun utilisateur trouvé avec ce CIN.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("UserRepository", "Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
                        callback.onFailure("Erreur lors de la récupération de l'utilisateur.");
                    });
        } else {
            callback.onFailure("Utilisateur non connecté.");
        }
    }

    // Méthode pour modifier un utilisateur en utilisant son CIN
    public void editUser(User user, final UserCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();  // Récupère l'utilisateur connecté
        if (currentUser != null) {
            // Recherche l'utilisateur par CIN
            db.collection("users")
                    .whereEqualTo("cin", user.getCin())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                String userId = task.getResult().getDocuments().get(0).getId();  // Récupère l'ID du document utilisateur
                                DocumentReference userRef = db.collection("users").document(userId);  // Référence au document utilisateur

                                // Met à jour l'utilisateur dans Firestore
                                userRef.set(user, SetOptions.merge())
                                        .addOnSuccessListener(aVoid -> {
                                            callback.onSuccessMessage("Utilisateur mis à jour avec succès.");
                                        })
                                        .addOnFailureListener(e -> {
                                            callback.onFailure("Erreur lors de la mise à jour de l'utilisateur.");
                                        });
                            } else {
                                callback.onFailure("Utilisateur non trouvé pour la mise à jour.");
                            }
                        } else {
                            callback.onFailure("Erreur lors de la récupération de l'utilisateur.");
                        }
                    })
                    .addOnFailureListener(e -> callback.onFailure("Erreur lors de l'appel à Firestore."));
        } else {
            callback.onFailure("Utilisateur non connecté.");
        }
    }
    // Interface pour les callbacks qui permettent de gérer les résultats des opérations Firebase
    public interface UserCallback {
        void onSuccessMessage(String message);  // Appelé lorsqu'une opération réussit
        void onSuccessUsers(List<User> users);  // Appelé avec une liste d'utilisateurs
        void onSuccessUser(User user);  // Appelé avec un utilisateur
        void onFailure(String error);  // Appelé en cas d'échec
    }
}
