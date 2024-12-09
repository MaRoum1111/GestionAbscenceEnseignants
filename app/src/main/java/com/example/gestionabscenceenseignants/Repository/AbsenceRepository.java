package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.Absence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsenceRepository {
    private final FirebaseFirestore db;  // Instance de Firestore pour accéder à la base de données
    private FirebaseAuth mAuth;

    // Constructeur
    public AbsenceRepository() {
        db = FirebaseFirestore.getInstance();  // Initialisation de la connexion à Firestore
        Log.d("Firestore", "Firestore instance initialized");
        mAuth = FirebaseAuth.getInstance();
    }

    // Méthode pour récupérer le nombre d'absences par professeur
    public void getAbsencesCountByProf(AuthCallback callback) {
        db.collection("absences")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Map<String, Map<String, Integer>> absenceCounts = new HashMap<>(); // Map pour stocker le CIN et le nombre d'absences par professeur
                        // Parcours des absences pour compter celles de chaque professeur
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);
                            String profCin = absence.getCin(); // Récupère le CIN du professeur
                            absenceCounts.putIfAbsent(profCin, new HashMap<>()); // Initialisation de la Map pour chaque CIN
                            String profName = absence.getProfName();
                            absenceCounts.get(profCin).put(profName, absenceCounts.get(profCin).getOrDefault(profName, 0) + 1);  // Incrémente le nombre d'absences
                        }

                        List<Absence> absenceSummary = new ArrayList<>();
                        // Création d'un résumé des absences pour chaque professeur
                        for (Map.Entry<String, Map<String, Integer>> entry : absenceCounts.entrySet()) {
                            String profCin = entry.getKey();
                            for (Map.Entry<String, Integer> nameEntry : entry.getValue().entrySet()) {
                                Absence summary = new Absence();
                                summary.setCin(profCin); // Ajouter le CIN dans le résumé
                                summary.setProfName(nameEntry.getKey()); // Ajouter le nom du professeur
                                summary.setAbsenceCount(nameEntry.getValue()); // Ajouter le nombre d'absences
                                absenceSummary.add(summary);
                            }
                        }
                        callback.onSuccess(absenceSummary);  // Appel du callback avec le résumé des absences
                    } else {
                        callback.onFailure("Erreur lors de la récupération des absences.");
                    }
                });
    }

    // Méthode pour récupérer les absences d'un professeur en fonction de son CIN
    public void getAbsencesByProf(String profCin, AuthCallback callback) {
        db.collection("absences")
                .whereEqualTo("cin", profCin)  // Filtre par CIN du professeur
                .orderBy("date", Query.Direction.DESCENDING)  // Tri par date décroissante
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<Absence> absences = new ArrayList<>();
                        // Parcours des résultats et conversion en objets Absence
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);
                            absences.add(absence);
                        }
                        callback.onSuccess(absences);  // Appel du callback avec la liste des absences
                    } else {
                        callback.onFailure("Erreur lors de la récupération des absences.");
                    }
                });
    }

    // Méthode pour ajouter une absence
    public void addAbsence(Absence absence, AuthCallback callback) {
        // Récupérer le CIN de l'utilisateur connecté
        getCinForUser(new AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absences) {
                // Assurer que nous avons bien récupéré un CIN
                if (absences != null && !absences.isEmpty()) {
                    String cin = absences.get(0).getIdAgent();  // Récupérer le CIN

                    // Assigner le CIN à l'objet Absence
                    absence.setIdAgent(cin);

                    // Ajouter l'absence à Firestore
                    db.collection("absences")
                            .add(absence)
                            .addOnSuccessListener(documentReference -> {
                                String documentId = documentReference.getId();  // Récupérer l'ID généré

                                // Mise à jour du champ idAbsence avec l'ID du document
                                documentReference.update("idAbsence", documentId)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("AbsenceRepository", "Absence ajoutée et mise à jour avec l'ID : " + documentId);
                                            callback.onSuccess(null); // Appel du callback avec un succès
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("AbsenceRepository", "Erreur lors de la mise à jour de l'ID : " + e.getMessage(), e);
                                            callback.onFailure("Erreur lors de la mise à jour de l'ID : " + e.getMessage());
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("AbsenceRepository", "Erreur lors de l'ajout de l'absence : " + e.getMessage(), e);
                                callback.onFailure("Erreur lors de l'ajout de l'absence : " + e.getMessage());
                            });
                } else {
                    callback.onFailure("CIN non trouvé pour l'utilisateur connecté.");
                }
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error); // Propager l'erreur du callback
            }
        });
    }

    // Méthode pour supprimer une absence
    public void deleteAbsence(String documentId, AuthCallback callback) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("AbsenceRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");  // Vérification si l'ID est valide
            return;
        }

        db.collection("absences")
                .document(documentId)  // Spécification du document à supprimer
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("AbsenceRepository", "Absence supprimée avec succès.");
                    callback.onSuccess(null);  // Appel du callback avec un succès, pas de données supplémentaires
                })
                .addOnFailureListener(e -> {
                    Log.e("AbsenceRepository", "Erreur lors de la suppression de l'absence : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la suppression de l'absence : " + e.getMessage());  // Appel du callback avec le message d'erreur
                });
    }

    // Méthode pour mettre à jour une absence
    public void updateAbsence(String documentId, Absence updatedAbsence, AuthCallback callback) {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("AbsenceRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");
            return;
        }
        // Créez un Map pour les champs à mettre à jour dans le document
        Map<String, Object> absenceUpdates = new HashMap<>();
        absenceUpdates.put("profName", updatedAbsence.getProfName());
        absenceUpdates.put("date", updatedAbsence.getDate());
        absenceUpdates.put("startTime", updatedAbsence.getStartTime());
        absenceUpdates.put("endTime", updatedAbsence.getEndTime());
        absenceUpdates.put("classe", updatedAbsence.getClasse());
        absenceUpdates.put("salle", updatedAbsence.getSalle());
        absenceUpdates.put("status", updatedAbsence.getStatus());
        absenceUpdates.put("cin", updatedAbsence.getCin());

        db.collection("absences")
                .document(documentId) // Spécifie le document à mettre à jour
                .update(absenceUpdates) // Met à jour les champs spécifiés
                .addOnSuccessListener(aVoid -> {
                    Log.d("AbsenceRepository", "Absence mise à jour avec succès : " + documentId);
                    callback.onSuccess(null); // Appel du callback en cas de succès
                })
                .addOnFailureListener(e -> {
                    Log.e("AbsenceRepository", "Erreur lors de la mise à jour de l'absence : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la mise à jour de l'absence : " + e.getMessage());
                });
    }

    public void getCinForUser(AuthCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("AbsenceRepository", "Utilisateur non connecté.");
            callback.onFailure("Utilisateur non connecté.");
            return;
        }

        db.collection("users")  // Utilise la collection "users" pour récupérer les informations de l'utilisateur
                .whereEqualTo("email", currentUser.getEmail())  // Filtre par email de l'utilisateur connecté
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String cin = document.getString("cin");
                            if (cin != null) {
                                // Créer un objet Absence avec le CIN
                                Absence absence = new Absence();
                                absence.setIdAgent(cin);  // Définir le CIN dans l'objet Absence

                                // Passer l'objet Absence au callback
                                callback.onSuccess(Collections.singletonList(absence));  // Envoie l'objet Absence dans le callback
                                return;  // Quitter la boucle dès que l'on a trouvé le CIN
                            }
                        }
                        // Si le CIN est manquant
                        Log.e("AbsenceRepository", "CIN non trouvé dans les données utilisateur.");
                        callback.onFailure("CIN non trouvé dans les données utilisateur.");
                    } else {
                        Log.e("AbsenceRepository", "Erreur lors de la récupération du CIN : " + task.getException());
                        callback.onFailure("Erreur lors de la récupération du CIN.");
                    }
                });
    }

    // Méthode pour récupérer les absences de l'utilisateur connecté
    public void getConnectedProfAbsences(AuthCallback callback) {
        // Étape 1 : Récupérer le CIN de l'utilisateur connecté
        getCinForUser(new AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absences) {
                // Vérifier que nous avons récupéré un CIN valide
                if (absences != null && !absences.isEmpty()) {
                    String cin = absences.get(0).getIdAgent(); // Récupérer le CIN

                    // Étape 2 : Récupérer les absences de Firestore pour ce CIN
                    db.collection("absences")
                            .whereEqualTo("cin", cin) // Filtrer les absences par CIN
                            .orderBy("date", Query.Direction.DESCENDING) // Trier par date décroissante
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                                    List<Absence> userAbsences = new ArrayList<>();
                                    // Parcourir les résultats et les convertir en objets Absence
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Absence absence = document.toObject(Absence.class);
                                        userAbsences.add(absence);
                                    }
                                    callback.onSuccess(userAbsences); // Retourner la liste des absences
                                } else {
                                    callback.onFailure("Aucune absence trouvée pour cet utilisateur.");
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("AbsenceRepository", "Erreur lors de la récupération des absences : " + e.getMessage(), e);
                                callback.onFailure("Erreur lors de la récupération des absences.");
                            });
                } else {
                    callback.onFailure("Impossible de récupérer le CIN de l'utilisateur connecté.");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                callback.onFailure(errorMessage); // Propager l'erreur du CIN
            }
        });
    }

    // Interface pour gérer les retours des méthodes
    public interface AuthCallback {
        void onSuccess(List<Absence> absences);
        void onFailure(String errorMessage);
    }
}
