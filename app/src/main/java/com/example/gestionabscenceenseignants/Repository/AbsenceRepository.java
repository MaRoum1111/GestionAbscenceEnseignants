package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.Absence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        mAuth = FirebaseAuth.getInstance();  // Initialisation de FirebaseAuth pour gérer l'authentification
    }

    // Méthode pour récupérer le nombre d'absences par professeur
    public void getAbsencesCountByProf(AuthCallback callback) {
        db.collection("absences")  // Accéder à la collection "absences"
                .get()  // Récupérer tous les documents
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Map<String, Map<String, Integer>> absenceCounts = new HashMap<>(); // Map pour stocker le CIN et le nombre d'absences par professeur

                        // Parcours des absences pour compter celles de chaque professeur
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);  // Conversion du document en objet Absence
                            String profCin = absence.getCin(); // Récupère le CIN du professeur
                            absenceCounts.putIfAbsent(profCin, new HashMap<>()); // Initialisation de la Map pour chaque CIN
                            String profName = absence.getProfName();  // Récupère le nom du professeur
                            // Incrémente le nombre d'absences pour chaque professeur
                            absenceCounts.get(profCin).put(profName, absenceCounts.get(profCin).getOrDefault(profName, 0) + 1);
                        }

                        List<Absence> absenceSummary = new ArrayList<>();  // Liste pour stocker le résumé des absences

                        // Création d'un résumé des absences pour chaque professeur
                        for (Map.Entry<String, Map<String, Integer>> entry : absenceCounts.entrySet()) {
                            String profCin = entry.getKey();  // Récupère le CIN du professeur
                            for (Map.Entry<String, Integer> nameEntry : entry.getValue().entrySet()) {
                                Absence summary = new Absence();
                                summary.setCin(profCin); // Ajouter le CIN dans le résumé
                                summary.setProfName(nameEntry.getKey()); // Ajouter le nom du professeur
                                summary.setAbsenceCount(nameEntry.getValue()); // Ajouter le nombre d'absences
                                absenceSummary.add(summary);  // Ajouter le résumé à la liste
                            }
                        }
                        callback.onSuccess(absenceSummary);  // Appel du callback avec le résumé des absences
                    } else {
                        callback.onFailure("Erreur lors de la récupération des absences.");  // Erreur si la récupération échoue
                    }
                });
    }

    // Méthode pour récupérer les absences d'un professeur en fonction de son CIN
    public void getAbsencesByProf(String profCin, AuthCallback callback) {
        db.collection("absences")  // Accéder à la collection "absences"
                .whereEqualTo("cin", profCin)  // Filtre par CIN du professeur
                .orderBy("date", Query.Direction.DESCENDING)  // Tri par date décroissante
                .get()  // Récupérer les documents filtrés
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<Absence> absences = new ArrayList<>();
                        // Parcours des résultats et conversion en objets Absence
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);  // Conversion du document en objet Absence
                            absences.add(absence);  // Ajouter à la liste des absences
                        }
                        callback.onSuccess(absences);  // Retourner la liste des absences au callback
                    } else {
                        callback.onFailure("Erreur lors de la récupération des absences.");  // Erreur si la récupération échoue
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
                    absence.setIdAgent(cin);  // Assigner le CIN à l'objet Absence
                    // Ajouter l'absence à Firestore
                    db.collection("absences")
                            .add(absence)  // Ajouter l'objet Absence dans la collection "absences"
                            .addOnSuccessListener(documentReference -> {
                                String documentId = documentReference.getId();  // Récupérer l'ID généré du document

                                // Mise à jour du champ idAbsence avec l'ID du document
                                documentReference.update("idAbsence", documentId)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("AbsenceRepository", "Absence ajoutée et mise à jour avec l'ID : " + documentId);
                                            callback.onSuccess(null);  // Appel du callback avec un succès
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("AbsenceRepository", "Erreur lors de la mise à jour de l'ID : " + e.getMessage(), e);
                                            callback.onFailure("Erreur lors de la mise à jour de l'ID : " + e.getMessage());  // Erreur de mise à jour de l'ID
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("AbsenceRepository", "Erreur lors de l'ajout de l'absence : " + e.getMessage(), e);
                                callback.onFailure("Erreur lors de l'ajout de l'absence : " + e.getMessage());  // Erreur d'ajout
                            });
                } else {
                    callback.onFailure("CIN non trouvé pour l'utilisateur connecté.");  // Erreur si le CIN n'est pas trouvé
                }
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);  // Propager l'erreur du callback
            }
        });
    }

    // Méthode pour rechercher des absences par professeur en fonction d'une requête de recherche
    public void searchAbsencesByTeacher(String searchQuery, AuthCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();  // Initialisation Firestore
        db.collection("absences")
                .whereGreaterThanOrEqualTo("cin", searchQuery)  // Rechercher par CIN avec un préfixe
                .whereLessThanOrEqualTo("cin", searchQuery + '\uf8ff')  // Recherche par CIN avec match partiel
                .get()  // Récupérer les résultats filtrés
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        // Map pour regrouper les absences par CIN et calculer le total
                        Map<String, Map<String, Integer>> absenceCounts = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);  // Conversion en objet Absence
                            String profCin = absence.getCin();  // Récupère le CIN du professeur
                            absenceCounts.putIfAbsent(profCin, new HashMap<>());  // Initialiser la Map pour chaque CIN
                            String profName = absence.getProfName();  // Récupère le nom du professeur
                            // Incrémente le nombre d'absences pour chaque professeur
                            absenceCounts.get(profCin).put(profName,
                                    absenceCounts.get(profCin).getOrDefault(profName, 0) + 1);
                        }

                        List<Absence> absenceSummary = new ArrayList<>();  // Liste pour stocker le résumé des absences
                        for (Map.Entry<String, Map<String, Integer>> entry : absenceCounts.entrySet()) {
                            String profCin = entry.getKey();  // Récupère le CIN du professeur
                            for (Map.Entry<String, Integer> nameEntry : entry.getValue().entrySet()) {
                                Absence summary = new Absence();
                                summary.setCin(profCin);  // Ajouter le CIN
                                summary.setProfName(nameEntry.getKey());  // Ajouter le nom
                                summary.setAbsenceCount(nameEntry.getValue());  // Ajouter le total d'absences
                                absenceSummary.add(summary);  // Ajouter au résumé
                            }
                        }

                        callback.onSuccess(absenceSummary);  // Retourner les résultats agrégés
                    } else {
                        callback.onFailure("Erreur lors de la recherche des absences.");  // Erreur lors de la recherche
                    }
                });
    }// Méthode pour supprimer une absence

    public void deleteAbsence(String documentId, AuthCallback callback) {
        // Vérifie si l'ID du document est valide
        if (documentId == null || documentId.isEmpty()) {
            Log.e("AbsenceRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");  // Retourne une erreur si l'ID est invalide
            return;
        }
        // Appel à Firestore pour supprimer le document correspondant à l'ID
        db.collection("absences")
                .document(documentId)  // Spécifie le document à supprimer
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Si la suppression réussit
                    Log.d("AbsenceRepository", "Absence supprimée avec succès.");
                    callback.onSuccess(null);  // Appel du callback avec succès
                })
                .addOnFailureListener(e -> {
                    // Si la suppression échoue
                    Log.e("AbsenceRepository", "Erreur lors de la suppression de l'absence : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la suppression de l'absence : " + e.getMessage());  // Retourne l'erreur
                });
    }

    // Méthode pour mettre à jour une absence
    public void updateAbsence(String documentId, Absence updatedAbsence, AuthCallback callback) {
        // Vérifie si l'ID du document est valide
        if (documentId == null || documentId.isEmpty()) {
            Log.e("AbsenceRepository", "L'ID du document est nul ou vide.");
            callback.onFailure("ID du document invalide.");
            return;
        }

        // Crée une Map pour stocker les champs à mettre à jour dans le document
        Map<String, Object> absenceUpdates = new HashMap<>();
        absenceUpdates.put("profName", updatedAbsence.getProfName());
        absenceUpdates.put("date", updatedAbsence.getDate());
        absenceUpdates.put("startTime", updatedAbsence.getStartTime());
        absenceUpdates.put("endTime", updatedAbsence.getEndTime());
        absenceUpdates.put("classe", updatedAbsence.getClasse());
        absenceUpdates.put("salle", updatedAbsence.getSalle());
        absenceUpdates.put("status", updatedAbsence.getStatus());
        absenceUpdates.put("cin", updatedAbsence.getCin());

        // Appel à Firestore pour mettre à jour le document
        db.collection("absences")
                .document(documentId) // Spécifie le document à mettre à jour
                .update(absenceUpdates) // Applique les mises à jour
                .addOnSuccessListener(aVoid -> {
                    // Si la mise à jour réussit
                    Log.d("AbsenceRepository", "Absence mise à jour avec succès : " + documentId);
                    callback.onSuccess(null); // Retourne un succès
                })
                .addOnFailureListener(e -> {
                    // Si la mise à jour échoue
                    Log.e("AbsenceRepository", "Erreur lors de la mise à jour de l'absence : " + e.getMessage(), e);
                    callback.onFailure("Erreur lors de la mise à jour de l'absence : " + e.getMessage());  // Retourne l'erreur
                });
    }

    // Méthode pour récupérer les absences d'une date spécifique
    public void getAbsencesByDate(String selectedDate, AuthCallback callback) {
        // Requête pour récupérer les absences pour une date donnée
        db.collection("absences")
                .whereEqualTo("date", selectedDate) // Filtrer par date
                .orderBy("startTime", Query.Direction.ASCENDING) // Trier par heure de début
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Absence> absences = new ArrayList<>();
                        // Convertir les documents en objets Absence
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);
                            absences.add(absence);
                        }
                        callback.onSuccess(absences); // Retourne les absences trouvées
                    } else {
                        callback.onFailure("Aucune absence trouvée pour la date sélectionnée."); // Retourne une erreur si aucune absence n'est trouvée
                    }
                });
    }

    // Méthode pour récupérer le CIN de l'utilisateur connecté
    public void getCinForUser(AuthCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("AbsenceRepository", "Utilisateur non connecté.");
            callback.onFailure("Utilisateur non connecté."); // Si l'utilisateur n'est pas connecté, retourne une erreur
            return;
        }
        // Requête pour récupérer le CIN de l'utilisateur basé sur son email
        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String cin = document.getString("cin");
                            if (cin != null) {
                                Absence absence = new Absence();
                                absence.setIdAgent(cin);  // Définir le CIN dans l'objet Absence
                                callback.onSuccess(Collections.singletonList(absence));  // Retourner l'absence dans le callback
                                return;  // Quitte la boucle dès que l'on a trouvé le CIN
                            }
                        }
                        callback.onFailure("CIN non trouvé dans les données utilisateur."); // Si CIN est absent
                    } else {
                        callback.onFailure("Erreur lors de la récupération du CIN."); // Si l'opération échoue
                    }
                });
    }

    // Méthode pour récupérer les absences de l'enseignant connecté
    public void getAbsencesForCurrentTeacher(AuthCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("AbsenceRepository", "Utilisateur non connecté.");
            callback.onFailure("Utilisateur non connecté."); // Si l'utilisateur n'est pas connecté, retourne une erreur
            return;
        }

        // Récupérer le CIN de l'utilisateur connecté
        db.collection("users")
                .whereEqualTo("email", currentUser.getEmail()) // Filtrer par email
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        String cin = task.getResult().getDocuments().get(0).getString("cin");

                        if (cin != null) {
                            // Requête pour récupérer les absences de l'enseignant basé sur son CIN
                            db.collection("absences")
                                    .whereEqualTo("cin", cin) // Filtrer par CIN
                                    .orderBy("date", Query.Direction.DESCENDING) // Trier par date décroissante
                                    .get()
                                    .addOnCompleteListener(absencesTask -> {
                                        if (absencesTask.isSuccessful() && absencesTask.getResult() != null) {
                                            List<Absence> absences = new ArrayList<>();
                                            for (QueryDocumentSnapshot document : absencesTask.getResult()) {
                                                Absence absence = document.toObject(Absence.class);
                                                absences.add(absence);
                                            }
                                            callback.onSuccess(absences); // Retourne les absences trouvées
                                        } else {
                                            callback.onFailure("Aucune absence trouvée pour cet enseignant."); // Retourne une erreur si aucune absence n'est trouvée
                                        }
                                    });
                        } else {
                            callback.onFailure("CIN non trouvé pour l'utilisateur connecté.");
                        }
                    } else {
                        callback.onFailure("Erreur lors de la récupération de l'utilisateur connecté.");
                    }
                });
    }

    // Méthode pour récupérer les absences de l'enseignant pour la journée actuelle
    public void getAbsencesForCurrentDay(AuthCallback callback) {
        String uid = mAuth.getCurrentUser().getUid(); // Récupère l'UID de l'utilisateur
        if (uid == null) {
            callback.onFailure("Utilisateur non connecté");
            return;
        }

        // Récupérer le CIN basé sur l'UID
        db.collection("users")
                .document(uid) // Recherche l'utilisateur par son UID
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String profCin = task.getResult().getString("cin");

                        if (profCin != null) {
                            // Récupérer les absences pour la date d'aujourd'hui
                            LocalDate today = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String todayDate = today.format(formatter); // Format de la date
                            db.collection("absences")
                                    .whereEqualTo("cin", profCin) // Filtrer par CIN
                                    .whereEqualTo("date", todayDate) // Filtrer par date
                                    .get()
                                    .addOnCompleteListener(absencesTask -> {
                                        if (absencesTask.isSuccessful() && absencesTask.getResult() != null && !absencesTask.getResult().isEmpty()) {
                                            List<Absence> absences = new ArrayList<>();
                                            for (QueryDocumentSnapshot document : absencesTask.getResult()) {
                                                Absence absence = document.toObject(Absence.class);
                                                absences.add(absence);
                                            }
                                            callback.onSuccess(absences); // Retourner les absences de la journée
                                        } else {
                                            callback.onFailure("Aucune absence trouvée pour aujourd'hui.");
                                        }
                                    });
                        } else {
                            callback.onFailure("CIN introuvable pour cet utilisateur.");
                        }
                    } else {
                        callback.onFailure("Échec de la récupération du CIN.");
                    }
                });
    }

    // Interface pour gérer les retours des méthodes
    public interface AuthCallback {
        void onSuccess(List<Absence> absences); // Méthode appelée en cas de succès

        void onFailure(String errorMessage); // Méthode appelée en cas d'échec
    }
}