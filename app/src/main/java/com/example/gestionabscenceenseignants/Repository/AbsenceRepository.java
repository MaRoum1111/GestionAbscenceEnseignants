package com.example.gestionabscenceenseignants.Repository;

import android.util.Log;
import com.example.gestionabscenceenseignants.model.Absence;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.util.ArrayList;
import java.util.List;

public class AbsenceRepository {
    private final FirebaseFirestore db;

    // Constructeur
    public AbsenceRepository() {
        db = FirebaseFirestore.getInstance();
        Log.d("Firestore", "Firestore instance initialized");
    }

    // Méthode pour récupérer les absences
    public void getAbsences(AuthCallback callback) {
        db.collection("absences")
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        List<Absence> absences = new ArrayList<>();
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : task.getResult()) {
                            Absence absence = document.toObject(Absence.class);
                            absences.add(absence);
                        }
                        Log.d("AbsenceRepository", "Récupération réussie, nombre d'absences : " + absences.size());
                        callback.onSuccess(absences);
                    } else {
                        // Vérification si une exception a été lancée
                        if (task.getException() != null) {
                            FirebaseFirestoreException e = (FirebaseFirestoreException) task.getException();
                            Log.e("AbsenceRepository", "Erreur lors de la récupération des absences : " + e.getMessage(), e);
                            callback.onFailure("Erreur lors de la récupération des absences : " + e.getMessage());
                        } else {
                            Log.e("AbsenceRepository", "La requête est terminée, mais la réponse est vide.");
                            callback.onFailure("Aucune donnée trouvée.");
                        }
                    }
                });
    }

    // Interface pour les callbacks de récupération des absences
    public interface AuthCallback {
        void onSuccess(List<Absence> absences);
        void onFailure(String errorMessage);
    }
}
