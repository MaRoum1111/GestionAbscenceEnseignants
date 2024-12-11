package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;

import com.example.gestionabscenceenseignants.model.Claim;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClaimRepository {
    private final FirebaseFirestore db;  // Instance de Firestore pour accéder à la base de données
    private FirebaseAuth mAuth;

    // Constructeur
    public ClaimRepository() {
        db = FirebaseFirestore.getInstance();  // Initialisation de la connexion à Firestore
        Log.d("Firestore", "Firestore instance initialized");
        mAuth = FirebaseAuth.getInstance();
    }


    // Méthode pour ajouter une absence
    public void addClaim(Claim claim, AuthCallback callback) {

        // Ajouter l'absence à Firestore
        db.collection("reclamations")
                .add(claim)
                .addOnSuccessListener(documentReference -> {
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
                });
    }

    public void getClaimsForCurrentTeacher(AuthCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Récupère l'utilisateur connecté
        if (currentUser == null) {
            Log.e("ClaimRepository", "Utilisateur non connecté.");
            callback.onFailure("Utilisateur non connecté.");
            return;
        }

        // Étape 1 : Récupérer le CIN à partir de l'email de l'utilisateur connecté
        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail()) // Filtrer par email
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Récupérer le CIN
                        String cin = task.getResult().getDocuments().get(0).getString("cin");

                        if (cin != null) {
                            // Étape 2 : Récupérer les réclamations liées à ce CIN
                            db.collection("reclamations") // Nom de la collection des réclamations
                                    .whereEqualTo("cin", cin) // Filtrer par CIN
                                    .orderBy("date", Query.Direction.DESCENDING) // Trier par date décroissante
                                    .get()
                                    .addOnCompleteListener(claimsTask -> {
                                        if (claimsTask.isSuccessful() && claimsTask.getResult() != null) {
                                            List<Claim> claims = new ArrayList<>();
                                            for (QueryDocumentSnapshot document : claimsTask.getResult()) {
                                                // Convertir les documents en objets Claim
                                                Claim claim = document.toObject(Claim.class);
                                                claims.add(claim);
                                            }

                                            // Retourner les réclamations via le callback
                                            callback.onSuccess(claims);
                                        } else {
                                            Log.e("ClaimRepository", "Aucune réclamation trouvée pour ce CIN.");
                                            callback.onFailure("Aucune réclamation trouvée pour cet enseignant.");
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ClaimRepository", "Erreur lors de la récupération des réclamations : " + e.getMessage());
                                        callback.onFailure("Erreur lors de la récupération des réclamations.");
                                    });
                        } else {
                            Log.e("ClaimRepository", "CIN non trouvé pour l'utilisateur.");
                            callback.onFailure("CIN non trouvé pour l'utilisateur connecté.");
                        }
                    } else {
                        Log.e("ClaimRepository", "Erreur lors de la récupération de l'utilisateur : " + task.getException());
                        callback.onFailure("Erreur lors de la récupération de l'utilisateur connecté.");
                    }
                });
    }

    // Interface pour gérer les retours des méthodes
    public interface AuthCallback {
        void onSuccess(List<Claim> claim);
        void onFailure(String errorMessage);
    }

}