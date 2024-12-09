package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.ViewModel;

import com.example.gestionabscenceenseignants.Repository.EmploiRepository;
import com.example.gestionabscenceenseignants.model.Emploi;

import java.io.InputStream;
import java.util.List;

public class EmploiViewModel extends ViewModel {
    private final EmploiRepository repository;

    public EmploiViewModel() {
        repository = new EmploiRepository();
    }

    public List<Emploi> traiterFichierExcel(InputStream inputStream) {
        return repository.readExcelFile(inputStream);
    }

    public void enregistrerEmplois(List<Emploi> emplois) {
        repository.saveEmploisToFirestore(emplois);
    }
}