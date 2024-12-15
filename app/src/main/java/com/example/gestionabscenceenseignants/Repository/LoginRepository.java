package com.example.gestionabscenceenseignants.Repository;

import com.example.gestionabscenceenseignants.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRepository {
    private final FirebaseAuth auth;  // Instance de FirebaseAuth pour gérer l'authentification
    private final FirebaseFirestore db;  // Instance de FirebaseFirestore pour interagir avec la base de données Firestore

    // Constructeur de LoginRepository
    public LoginRepository() {
        auth = FirebaseAuth.getInstance();  // Initialisation de l'instance FirebaseAuth pour l'authentification
        db = FirebaseFirestore.getInstance();  // Initialisation de l'instance Firestore pour accéder à la base de données
    }

    // Méthode pour authentifier l'utilisateur
    public void login(String email, String password, AuthCallback callback) {
        // Tentative de connexion avec l'email et le mot de passe
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {  // Si la connexion est réussie
                        // Récupération de l'UID de l'utilisateur connecté
                        String uid = auth.getCurrentUser().getUid();
                        // Appel de la méthode pour récupérer le rôle de l'utilisateur depuis Firestore
                        fetchUserRole(uid, callback);
                    } else {  // Si la connexion échoue
                        // Appel du callback avec le message d'erreur
                        callback.onFailure("Erreur de connexion.");
                    }
                });
    }

    // Méthode pour récupérer le rôle de l'utilisateur depuis Firestore
    private void fetchUserRole(String uid, AuthCallback callback) {
        // Récupération du document utilisateur dans la collection "users" en utilisant l'UID
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {  // Si l'utilisateur existe dans la base de données
                        // Récupération des données de l'utilisateur (rôle et email)
                        String role = task.getResult().getString("role");
                        String email = task.getResult().getString("email");
                        // Création d'un objet User avec les données récupérées
                        User user = new User(uid, email, role);
                        // Appel du callback avec l'utilisateur
                        callback.onSuccess(user);
                    } else {  // Si l'utilisateur n'existe pas dans Firestore
                        // Appel du callback avec le message d'erreur
                        callback.onFailure("Utilisateur non trouvé.");
                    }
                });
    }

    // Interface pour les callbacks d'authentification
    public interface AuthCallback {
        // Méthode appelée en cas de succès de l'authentification
        void onSuccess(User user);

        // Méthode appelée en cas d'échec de l'authentification
        void onFailure(String errorMessage);
    }
}
