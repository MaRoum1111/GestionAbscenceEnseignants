package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.Claim;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimRepository {
    private final FirebaseFirestore db;  // Instance de Firestore pour accéder à la base de données
    private FirebaseAuth mAuth;  // Instance d'authentification Firebase

    // Constructeur
    public ClaimRepository() {
        db = FirebaseFirestore.getInstance();  // Initialisation de la connexion à Firestore
        Log.d("Firestore", "Firestore instance initialized");
        mAuth = FirebaseAuth.getInstance();  // Initialisation de l'authentification
    }

    // Méthode pour ajouter une réclamation
    public void addClaim(Claim claim, AuthCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();  // Récupère l'utilisateur connecté
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
                        // Récupérer le CIN et le nom de l'enseignant
                        String cin = task.getResult().getDocuments().get(0).getString("cin");
                        String profName = task.getResult().getDocuments().get(0).getString("name");

                        if (cin != null && profName != null) {
                            // Mettre à jour l'objet claim avec CIN et nom de l'enseignant
                            claim.setCin(cin);
                            claim.setProfName(profName);
                            claim.setStatus("En cours");  // Statut de la réclamation

                            // Ajouter la réclamation à Firestore
                            db.collection("reclamations")
                                    .add(claim)
                                    .addOnSuccessListener(documentReference -> {
                                        String documentId = documentReference.getId();  // Récupérer l'ID généré

                                        // Mise à jour du champ idClaim avec l'ID du document
                                        documentReference.update("idClaim", documentId)
                                                .addOnSuccessListener(aVoid -> {
                                                    callback.onSuccess(null);
                                                })
                                                .addOnFailureListener(e -> {
                                                    callback.onFailure("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
                                                });
                                    });
                        }
                    }
                });
    }

    // Méthode pour supprimer une réclamation
    public void deleteClaim(String documentId, AuthCallback callback) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("ClaimRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");
            return;
        }

        // Suppression de la réclamation dans Firestore
        db.collection("reclamations")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("ClaimRepository", "Réclamation supprimée avec succès.");
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e("ClaimRepository", "Erreur lors de la suppression de la réclamation : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la suppression de la réclamation : " + e.getMessage());
                });
    }

    // Méthode pour mettre à jour une réclamation
    public void updateClaim(String documentId, Claim updateClaim, AuthCallback callback) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("ClaimRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");
            return;
        }

        // Crée un Map avec les données à mettre à jour
        Map<String, Object> claimUpdates = new HashMap<>();
        claimUpdates.put("date", updateClaim.getDate());
        claimUpdates.put("startTime", updateClaim.getStartTime());
        claimUpdates.put("endTime", updateClaim.getEndTime());
        claimUpdates.put("claim", updateClaim.getClaim());
        claimUpdates.put("classe", updateClaim.getClasse());
        claimUpdates.put("claimDate", updateClaim.getClaimDate());

        // Mise à jour de la réclamation dans Firestore
        db.collection("reclamations")
                .document(documentId)
                .update(claimUpdates)
                .addOnSuccessListener(aVoid -> {
                    Log.d("ClaimRepository", "Réclamation mise à jour avec succès : " + documentId);
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e("ClaimRepository", "Erreur lors de la mise à jour de la réclamation : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
                });
    }

    // Méthode pour récupérer toutes les réclamations de l'enseignant connecté
    public void getClaimsForCurrentTeacher(AuthCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();  // Récupère l'utilisateur connecté
        if (currentUser == null) {
            Log.e("ClaimRepository", "Utilisateur non connecté.");
            callback.onFailure("Utilisateur non connecté.");
            return;
        }

        // Étape 1 : Récupérer le CIN à partir de l'email de l'utilisateur connecté
        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        String cin = task.getResult().getDocuments().get(0).getString("cin");

                        if (cin != null) {
                            // Étape 2 : Récupérer les réclamations liées au CIN de l'utilisateur
                            db.collection("reclamations")
                                    .whereEqualTo("cin", cin)
                                    .orderBy("claimDate", Query.Direction.DESCENDING)
                                    .get()
                                    .addOnCompleteListener(claimsTask -> {
                                        if (claimsTask.isSuccessful() && claimsTask.getResult() != null) {
                                            List<Claim> claims = new ArrayList<>();
                                            for (QueryDocumentSnapshot document : claimsTask.getResult()) {
                                                // Convertir chaque document en objet Claim
                                                Claim claim = document.toObject(Claim.class);
                                                claims.add(claim);
                                            }

                                            // Retourner la liste des réclamations via le callback
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

    // Méthode pour récupérer toutes les réclamations en cours
    public void getClaims(ClaimRepository.AuthCallback callback) {
        // Recherche dans la collection "reclamations" avec le statut "En cours"
        db.collection("reclamations")
                .whereEqualTo("status", "En cours")  // Filtrage des réclamations avec le statut "En cours"
                .orderBy("date", Query.Direction.DESCENDING)  // Tri par date décroissante (les plus récentes en premier)
                .get()
                .addOnCompleteListener(task -> {
                    // Vérification si la tâche a réussi
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<Claim> claims = new ArrayList<>();
                        // Parcours des résultats retournés par Firestore et conversion en objets Claim
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Claim claim = document.toObject(Claim.class);  // Conversion du document Firestore en objet Claim
                            claims.add(claim);  // Ajout de l'objet Claim à la liste
                        }
                        // Appel du callback en cas de succès avec la liste des réclamations
                        callback.onSuccess(claims);
                    } else {
                        // Appel du callback en cas d'échec de la récupération des réclamations
                        callback.onFailure("Erreur lors de la récupération des absences.");
                    }
                });
    }

    // Méthode pour récupérer les réclamations d'un professeur en fonction de son CIN
    public void getClaimByProf(String profCin, AuthCallback callback) {
        // Recherche dans la collection "reclamations" avec le CIN du professeur et le statut "En cours"
        db.collection("reclamations")
                .whereEqualTo("cin", profCin)  // Filtrage par le CIN du professeur
                .whereEqualTo("status", "En cours")  // Filtrage par statut "En cours"
                .orderBy("date", Query.Direction.DESCENDING)  // Tri par date décroissante
                .get()
                .addOnCompleteListener(task -> {
                    // Vérification si la tâche a réussi
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<Claim> claims = new ArrayList<>();
                        // Parcours des résultats et conversion en objets Claim
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Claim claim = document.toObject(Claim.class);  // Conversion du document Firestore en objet Claim
                            claims.add(claim);  // Ajout de l'objet Claim à la liste
                        }
                        // Appel du callback en cas de succès avec la liste des réclamations
                        callback.onSuccess(claims);
                    } else {
                        // Appel du callback en cas d'échec
                        callback.onFailure("Erreur lors de la récupération des absences.");
                    }
                });
    }

    // Fonction pour approuver une réclamation
    public void approuverReclamation(String reclamationId, AuthCallback callback) {
        // Récupération du document de réclamation à partir de son ID
        DocumentReference docRef = db.collection("reclamations").document(reclamationId);
        // Mise à jour du statut de la réclamation à "Approuvé"
        docRef.update("status", "Approuvé")
                .addOnSuccessListener(aVoid -> {
                    // Appel du callback en cas de succès
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    // Appel du callback en cas d'échec avec un message d'erreur
                    callback.onFailure("Erreur lors de l'acceptation de la réclamation : " + e.getMessage());
                });
    }

    // Fonction pour rejeter une réclamation
    public void rejeterReclamation(String reclamationId, AuthCallback callback) {
        // Récupération du document de réclamation à partir de son ID
        DocumentReference docRef = db.collection("reclamations").document(reclamationId);
        // Mise à jour du statut de la réclamation à "Rejeté"
        docRef.update("status", "Rejeté")
                .addOnSuccessListener(aVoid -> {
                    // Appel du callback en cas de succès
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    // Appel du callback en cas d'échec avec un message d'erreur
                    callback.onFailure("Erreur lors du rejet de la réclamation : " + e.getMessage());
                });
    }

    // Interface pour gérer les retours des méthodes de réclamation
    public interface AuthCallback {
        // Méthode appelée en cas de succès, renvoie la liste des réclamations
        void onSuccess(List<Claim> claim);

        // Méthode appelée en cas d'échec, renvoie un message d'erreur
        void onFailure(String errorMessage);
    }
}