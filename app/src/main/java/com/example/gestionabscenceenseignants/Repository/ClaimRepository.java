package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;

import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.model.Claim;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Récupère l'utilisateur connecté
        if (currentUser == null) {
            Log.e("ClaimRepository", "Utilisateur non connecté.");
            callback.onFailure("Utilisateur non connecté.");
            return;
        }
        // Étape 1 : Récupérer le CIN et le nom de l'enseignant à partir de l'email de l'utilisateur connecté
        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail()) // Filtrer par email
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Récupérer le CIN
                        String cin = task.getResult().getDocuments().get(0).getString("cin");
                        String profName = task.getResult().getDocuments().get(0).getString("name");

                        if ((cin != null) && (profName !=null)) {
                            // Créer un objet Claim avec le CIN et profName
                            claim.setCin(cin);
                            claim.setProfName(profName);
                            // Ajouter l'absence à Firestore
                            db.collection("reclamations")
                                    .add(claim)
                                    .addOnSuccessListener(documentReference -> {
                                        String documentId = documentReference.getId();  // Récupérer l'ID généré

                                        // Mise à jour du champ idAbsence avec l'ID du document
                                        documentReference.update("idClaim", documentId)
                                                .addOnSuccessListener(aVoid -> {
                                                    callback.onSuccess(null);
                                                })

                                                .addOnFailureListener(e -> {
                                                    callback.onFailure("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
                                                });
                                    });

                        }}});}
    // Méthode pour supprimer une réclamation
    public void deleteClaim(String documentId, AuthCallback callback) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("ClaimRepositroy", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");  // Vérification si l'ID est valide
            return;
        }

        db.collection("reclamations")
                .document(documentId)  // Spécification du document à supprimer
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("ClaimRepository", "Réclamation supprimée avec succès.");
                    callback.onSuccess(null);  // Appel du callback avec un succès, pas de données supplémentaires
                })
                .addOnFailureListener(e -> {
                    Log.e("AbsenceRepository", "Erreur lors de la suppression de l'absence : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la suppression de l'absence : " + e.getMessage());  // Appel du callback avec le message d'erreur
                });
    }
    public void updateClaim(String documentId, Claim updateClaim, AuthCallback callback) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("ClaimRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");
            return;
        }
        // Créez un Map pour les champs à mettre à jour dans le document
        Map<String, Object> claimUpdates = new HashMap<>();
        claimUpdates.put("date", updateClaim.getDate());
        claimUpdates.put("startTime", updateClaim.getStartTime());
        claimUpdates.put("endTime", updateClaim.getEndTime());
        claimUpdates.put("claim", updateClaim.getClaim());
        claimUpdates.put("classe", updateClaim.getClasse());
        claimUpdates.put("claimDate", updateClaim.getClaimDate());


        db.collection("reclamations")
                .document(documentId) // Spécifie le document à mettre à jour
                .update(claimUpdates) // Met à jour les champs spécifiés
                .addOnSuccessListener(aVoid -> {
                    Log.d("ClaimRepository", "Réclamation mise à jour avec succès : " + documentId);
                    callback.onSuccess(null); // Appel du callback en cas de succès
                })
                .addOnFailureListener(e -> {
                    Log.e("ClaimRepository", "Erreur lors de la mise à jour de la réclamation : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
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
                                    .orderBy("claimDate", Query.Direction.DESCENDING) // Trier par date décroissante
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