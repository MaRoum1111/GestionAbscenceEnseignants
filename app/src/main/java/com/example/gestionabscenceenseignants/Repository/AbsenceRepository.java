package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.Absence;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsenceRepository {
    private final FirebaseFirestore db;  // Instance de Firestore pour accéder à la base de données

    // Constructeur
    public AbsenceRepository() {
        db = FirebaseFirestore.getInstance();  // Initialisation de la connexion à Firestore
        Log.d("Firestore", "Firestore instance initialized");  // Log pour indiquer l'initialisation
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
        db.collection("absences")
                .add(absence)  // Ajout de l'absence à Firestore
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId(); // Récupérer l'ID généré

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
        absenceUpdates.put("reason", updatedAbsence.getReason());
        absenceUpdates.put("subjectName", updatedAbsence.getSubjectName());
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



    // Interface pour les callbacks de récupération des absences
    public interface AuthCallback {
        void onSuccess(List<Absence> absences);  // Callback en cas de succès, avec la liste d'absences
        void onFailure(String errorMessage);  // Callback en cas d'erreur, avec le message d'erreur
    }
}
