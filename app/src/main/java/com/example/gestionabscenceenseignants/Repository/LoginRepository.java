package com.example.gestionabscenceenseignants.Repository;

import com.example.gestionabscenceenseignants.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore db;

    public LoginRepository() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    // Méthode pour authentifier l'utilisateur
    public void login(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getCurrentUser().getUid();
                        fetchUserRole(uid, callback);
                    } else {
                        callback.onFailure("Erreur de connexion.");
                    }
                });
    }

    // Récupérer le rôle de l'utilisateur depuis Firestore
    private void fetchUserRole(String uid, AuthCallback callback) {
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String role = task.getResult().getString("role");
                        String email = task.getResult().getString("email");
                        User user = new User(uid, email, role);
                        callback.onSuccess(user);
                    } else {
                        callback.onFailure("Utilisateur non trouvé.");
                    }
                });
    }

    // Interface pour les callbacks d'authentification
    public interface AuthCallback {
        void onSuccess(User user);
        void onFailure(String errorMessage);
    }
}
