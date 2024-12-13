package com.example.gestionabscenceenseignants.Repository;


import android.util.Log;
import com.example.gestionabscenceenseignants.model.Emploi;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EmploiRepository {
    private final FirebaseFirestore firestore;

    public EmploiRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }
    public List<Emploi> readExcelFile(InputStream inputStream) {
        List<Emploi> emploisList = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Première feuille
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Ignorer l'en-tête

                // Lecture des cellules avec gestion des valeurs nulles
                String nom = getStringCellValue(row, 0);
                String jour = getStringCellValue(row, 1);
                String heure = getStringCellValue(row, 2);

                emploisList.add(new Emploi(nom, jour, heure));
            }
            workbook.close();
        } catch (Exception e) {
            Log.e("EmploiRepository", "Erreur lors de la lecture du fichier Excel", e);
        }
        return emploisList;
    }

    private String getStringCellValue(Row row, int cellIndex) {
        if (row.getCell(cellIndex) != null) {
            return row.getCell(cellIndex).toString(); // Pour gérer tous les types de cellules
        }
        return ""; // Retourner une chaîne vide si la cellule est nulle
    }

    public void saveEmploisToFirestore(List<Emploi> emplois) {
        WriteBatch batch = firestore.batch(); // Utilisation du WriteBatch pour des écritures groupées

        for (Emploi emploi : emplois) {
            // Créer un document avec un identifiant auto-généré
            firestore.collection("emplois")
                    .add(emploi) // Ajouter l'emploi à Firestore
                    .addOnSuccessListener(docRef ->
                            Log.d("Firestore", "Données ajoutées : " + docRef.getId()))
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Erreur lors de l'ajout", e));

            // Tu peux également ajouter un contrôle d'erreur ici ou d'autres fonctionnalités
        }

        // Commit du batch une fois toutes les écritures prêtes
        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Batch commit success");
            } else {
                Log.e("Firestore", "Batch commit failed", task.getException());
            }
        });
    }
}
