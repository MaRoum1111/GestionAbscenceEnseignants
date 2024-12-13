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

    /**
     * Charger les absences totales par professeur.
     */
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

    /**
     * Récupérer les absences du professeur actuellement connecté.
     */
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

    /**
     * Ajouter une absence.
     * @param absence L'absence à ajouter.
     */
    public void addAbsence(Absence absence) {
        isAdding.setValue(true); // Début de l'opération d'ajout
        repository.addAbsence(absence, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                isAdding.setValue(false); // Fin de l'ajout
                absences.setValue(absencesList); // Met à jour la liste des absences
                errorMessage.setValue("Absence ajoutée avec succès.");
                loadAbsenceCountsByProf(); // Recharge les absences
            }

            @Override
            public void onFailure(String error) {
                isAdding.setValue(false); // Fin de l'ajout en cas d'échec
                errorMessage.setValue("Erreur lors de l'ajout : " + error);
            }
        });
    }

    /**
     * Supprimer une absence.
     * @param documentId L'ID du document à supprimer.
     * @param profCin Le CIN du professeur associé.
     */
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

    /**
     * Mettre à jour une absence.
     * @param documentId L'ID du document à mettre à jour.
     * @param updatedAbsence L'absence mise à jour.
     */
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

    /**
     * Charger les absences par date.
     * @param selectedDate La date sélectionnée.
     */
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

    /**
     * Obtenir les absences (Observable).
     * @return Liste observable des absences.
     */
    public LiveData<List<Absence>> getAbsences() {
        return absences;
    }

    /**
     * Obtenir le message d'erreur ou de succès.
     * @return Message observable.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Obtenir l'état d'ajout d'une absence.
     * @return État observable (en cours ou non).
     */
    public LiveData<Boolean> isAdding() {
        return isAdding;
    }

    /**
     * Obtenir l'état de suppression d'une absence.
     * @return État observable (en cours ou non).
     */
    public LiveData<Boolean> isDeleting() {
        return isDeleting;
    }
}