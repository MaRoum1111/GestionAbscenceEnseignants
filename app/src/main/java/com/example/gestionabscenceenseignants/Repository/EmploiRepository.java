package com.example.gestionabscenceenseignants.Repository;

import android.net.Uri;
import android.util.Log;

import com.example.gestionabscenceenseignants.model.Emploi;
import com.google.firebase.firestore.FirebaseFirestore;
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
                String nom = row.getCell(0).getStringCellValue();
                String jour = row.getCell(1).getStringCellValue();
                String heure = row.getCell(2).getStringCellValue();
                emploisList.add(new Emploi(nom, jour, heure));
            }
            workbook.close();
        } catch (Exception e) {
            Log.e("EmploiRepository", "Erreur lors de la lecture du fichier Excel", e);
        }
        return emploisList;
    }

    public void saveEmploisToFirestore(List<Emploi> emplois) {
        for (Emploi emploi : emplois) {
            firestore.collection("emplois")
                    .add(emploi)
                    .addOnSuccessListener(docRef ->
                            Log.d("Firestore", "Données ajoutées : " + docRef.getId()))
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Erreur lors de l'ajout", e));
        }
    }
}

