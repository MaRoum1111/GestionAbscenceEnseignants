package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;

import com.example.gestionabscenceenseignants.model.Claim;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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


    // Interface pour gérer les retours des méthodes
    public interface AuthCallback {
        void onSuccess(List<Claim> claim);

        void onFailure(String errorMessage);
    }

}