package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.AbsenceRepository;
import com.example.gestionabscenceenseignants.model.Absence;

import java.util.List;

public class AbsenceViewModel extends ViewModel {

    private final AbsenceRepository repository; // Instance pour gérer les absences
    private final MutableLiveData<List<Absence>> absences; // Liste des absences
    private final MutableLiveData<String> errorMessage; // Messages d'erreur ou de succès
    private final MutableLiveData<Boolean> isAdding; // État d'ajout d'une absence
    private final MutableLiveData<Boolean> isDeleting; // État de suppression d'une absence

    // Constructeur : initialisation des variables
    public AbsenceViewModel() {
        repository = new AbsenceRepository(); // Initialisation du repository
        absences = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isAdding = new MutableLiveData<>(false); // Pas d'ajout en cours
        isDeleting = new MutableLiveData<>(false); // Pas de suppression en cours
    }

    // Charger les absences totales par professeur
    public void loadAbsenceCountsByProf() {
        repository.getAbsencesCountByProf(new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absenceSummary) {
                absences.setValue(absenceSummary); // Met à jour la liste des absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur : " + error); // Met à jour le message d'erreur
            }
        });
    }

    // Charger les absences par CIN du professeur
    public void loadAbsencesByProf(String profCin) {
        repository.getAbsencesByProf(profCin, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Met à jour la liste des absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors du chargement : " + error);
            }
        });
    }

    // Récupérer les absences du professeur actuellement connecté
    public void getAbsencesForCurrentTeacher() {
        repository.getAbsencesForCurrentTeacher(new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Met à jour la liste des absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors de la récupération des absences : " + error); // Message d'erreur
            }
        });
    }

    // Ajouter une absence
    public void addAbsence(Absence absence) {
        isAdding.setValue(true); // Début de l'opération d'ajout
        repository.addAbsence(absence, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                isAdding.setValue(false); // Fin de l'ajout
                absences.setValue(absencesList); // Met à jour la liste des absences
                errorMessage.setValue("Absence ajoutée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isAdding.setValue(false); // Fin de l'ajout en cas d'échec
                errorMessage.setValue("Erreur lors de l'ajout : " + error);
            }
        });
    }

    // Supprimer une absence
    public void deleteAbsence(String documentId, String profCin) {
        isDeleting.setValue(true); // Début de l'opération de suppression
        repository.deleteAbsence(documentId, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                isDeleting.setValue(false); // Fin de la suppression
                loadAbsencesByProf(profCin); // Recharge la liste des absences
                errorMessage.setValue("Absence supprimée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isDeleting.setValue(false); // Fin de la suppression en cas d'échec
                errorMessage.setValue("Erreur lors de la suppression : " + error);
            }
        });
    }

    // Mettre à jour une absence
    public void updateAbsence(String documentId, Absence updatedAbsence) {
        repository.updateAbsence(documentId, updatedAbsence, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Mise à jour des données après succès
                errorMessage.setValue("Absence mise à jour avec succès.");
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors de la mise à jour : " + error);
            }
        });
    }

    // Charger les absences par date
    public void fetchAbsencesByDate(String selectedDate) {
        repository.getAbsencesByDate(selectedDate, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Met à jour la liste des absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors de la récupération par date : " + error);
            }
        });
    }

    // Getters : liste des absences
    public LiveData<List<Absence>> getAbsences() {
        return absences;
    }

    // Getter : message d'erreur ou de succès
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

}
