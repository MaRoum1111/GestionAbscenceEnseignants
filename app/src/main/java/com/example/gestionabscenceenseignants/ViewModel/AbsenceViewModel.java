package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.AbsenceRepository;
import com.example.gestionabscenceenseignants.model.Absence;

import java.util.List;

public class AbsenceViewModel extends ViewModel {
    private final AbsenceRepository repository; // Instance de AbsenceRepository pour accéder aux données
    private final MutableLiveData<List<Absence>> absences; // LiveData contenant la liste des absences
    private final MutableLiveData<String> errorMessage; // LiveData pour contenir les messages d'erreur
    private final MutableLiveData<Boolean> isAdding; // LiveData pour l'état de l'ajout d'une absence
    private final MutableLiveData<Boolean> isDeleting; // LiveData pour l'état de la suppression d'une absence

    // Constructeur du ViewModel, initialisation des variables
    public AbsenceViewModel() {
        repository = new AbsenceRepository(); // Initialisation du repository pour récupérer et manipuler les données des absences
        absences = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isAdding = new MutableLiveData<>(false); // Initialisation de l'état d'ajout à "false" (pas d'ajout en cours)
        isDeleting = new MutableLiveData<>(false); // Initialisation de l'état de suppression à "false" (pas de suppression en cours)
    }

    // Méthode pour charger le nb d'absences par professeur
    public void loadAbsenceCountsByProf() {
        repository.getAbsencesCountByProf(new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absenceSummary) {
                absences.setValue(absenceSummary); // Si la récupération des absences est réussie, on met à jour les absences dans le LiveData
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Si la récupération échoue, on met à jour le message d'erreur dans le LiveData
            }
        });
    }

    // Méthode pour charger les absences d'un professeur spécifique, basé sur son CIN
    public void loadAbsencesByProf(String profCin) {
        repository.getAbsencesByProf(profCin, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Si la récupération des absences est réussie, on met à jour les absences dans le LiveData
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Si la récupération échoue, on met à jour le message d'erreur dans le LiveData
            }
        });
    }

    // Méthode pour supprimer une absence en utilisant son ID et recharger les absences d'un professeur spécifique après la suppression
    public void deleteAbsence(String documentId, String profCin) {
        isDeleting.setValue(true); // On met à jour l'état de la suppression à "en cours de suppression"
        repository.deleteAbsence(documentId, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                isDeleting.setValue(false); // On met à jour l'état de la suppression à "terminée" après la suppression
                loadAbsencesByProf(profCin); // Recharger les absences du professeur après la suppression
            }
            @Override
            public void onFailure(String error) {
                isDeleting.setValue(false); // On met à jour l'état de la suppression à "terminée" même si la suppression échoue
                errorMessage.setValue(error); // Mettre à jour le message d'erreur dans le LiveData
            }
        });
    }

    // Méthode pour ajouter une absence
    public void addAbsence(Absence absence) {
        isAdding.setValue(true); // On met à jour l'état d'ajout à "en cours d'ajout"
        repository.addAbsence(absence, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                isAdding.setValue(false); // Terminer l'état "en cours d'ajout" après l'ajout réussi
            }

            @Override
            public void onFailure(String error) {
                isAdding.setValue(false); // Terminer l'état "en cours d'ajout" même si l'ajout échoue
                errorMessage.setValue(error); // Mettre à jour le message d'erreur dans le LiveData
            }
        });
    }
    // Méthode pour mettre à jour une absence
    public void updateAbsence(String documentId, Absence updatedAbsence) {
        // Utilisation du repository pour effectuer la mise à jour
        repository.updateAbsence(documentId, updatedAbsence, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                // Mise à jour réussie, recharge les absences
                loadAbsencesByProf(updatedAbsence.getCin());
            }

            @Override
            public void onFailure(String error) {
                // En cas d'erreur, met à jour le message d'erreur dans le LiveData
                errorMessage.setValue(error);
            }
        });
    }

    // Getter pour obtenir la LiveData contenant la liste des absences
    public LiveData<List<Absence>> getAbsences() {
        return absences;
    }

    // Getter pour obtenir la LiveData contenant les messages d'erreur
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Getter pour obtenir la LiveData contenant l'état d'ajout
    public LiveData<Boolean> isAdding() {
        return isAdding;
    }
}