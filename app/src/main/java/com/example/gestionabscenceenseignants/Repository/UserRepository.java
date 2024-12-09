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
    }// Méthode pour supprimer un utilisateur
    public void deleteUser(String cin, final UserCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Rechercher le document utilisateur avec le CIN
            db.collection("users")
                    .whereEqualTo("cin", cin)  // Filtrer par CIN
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Le document existe, récupérer son ID
                            String userId = task.getResult().getDocuments().get(0).getId(); // Prendre l'ID du premier document trouvé
                            Log.d("UserRepository", "Document trouvé avec ID : " + userId);

                            // Supprimer l'utilisateur
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
                            // Aucun utilisateur trouvé avec ce CIN
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


    // Méthode pour modifier un utilisateur sans ID explicite
    public void editUser(User user, final UserCallback callback) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Log.d("UserRepository", "Utilisateur connecté : " + currentUser.getUid());

            // Utiliser le CIN pour identifier l'utilisateur à mettre à jour
            db.collection("users")
                    .whereEqualTo("cin", user.getCin()) // Assurez-vous que le CIN est unique
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("UserRepository", "Recherche réussie, nombre de résultats : " + task.getResult().size());

                            if (!task.getResult().isEmpty()) {
                                // Récupérer l'ID du document correspondant
                                String userId = task.getResult().getDocuments().get(0).getId();
                                Log.d("UserRepository", "Utilisateur trouvé avec ID : " + userId);

                                DocumentReference userRef = db.collection("users").document(userId);

                                // Mettre à jour l'utilisateur
                                userRef.set(user, SetOptions.merge()) // Utilisation de merge pour ne pas écraser d'autres champs non spécifiés
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("UserRepository", "Utilisateur mis à jour avec succès");
                                            callback.onSuccessMessage("Utilisateur mis à jour avec succès.");
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("UserRepository", "Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
                                            callback.onFailure("Erreur lors de la mise à jour de l'utilisateur.");
                                        });
                            } else {
                                // Si aucun utilisateur n'est trouvé avec le CIN
                                Log.d("UserRepository", "Aucun utilisateur trouvé avec le CIN : " + user.getCin());
                                callback.onFailure("Utilisateur non trouvé pour la mise à jour.");
                            }
                        } else {
                            Log.e("UserRepository", "Erreur lors de la récupération de l'utilisateur : " + task.getException().getMessage());
                            callback.onFailure("Erreur lors de la récupération de l'utilisateur.");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("UserRepository", "Erreur lors de l'appel à Firestore : " + e.getMessage());
                        callback.onFailure("Erreur lors de l'appel à Firestore.");
                    });
        } else {
            Log.d("UserRepository", "Aucun utilisateur connecté");
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
