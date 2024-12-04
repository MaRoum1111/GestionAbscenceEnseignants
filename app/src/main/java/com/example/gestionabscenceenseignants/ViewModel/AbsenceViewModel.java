package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.AbsenceRepository;
import com.example.gestionabscenceenseignants.model.Absence;

import java.util.List;

public class AbsenceViewModel extends ViewModel {
    private final AbsenceRepository repository; // Instance de AbsenceRepository
    private final MutableLiveData<List<Absence>> absences; // LiveData contenant la liste des absences
    private final MutableLiveData<String> errorMessage; // LiveData contenant les messages d'erreur
    private final MutableLiveData<Boolean> isAdding; // LiveData pour l'état de l'ajout d'une absence

    public AbsenceViewModel() {
        repository = new AbsenceRepository(); // Initialisation du repository
        absences = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isAdding = new MutableLiveData<>(false);
    }

    // Méthode pour charger les absences
    public void loadAbsences() {
        repository.getAbsences(new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Si réussi, on met à jour le LiveData avec la liste d'absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Si échec, on met à jour le LiveData avec le message d'erreur
            }
        });
    }
    public void loadAbsenceCountsByProf() {
        repository.getAbsencesCountByProf(new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absenceSummary) {
                absences.setValue(absenceSummary); // Met à jour la liste des absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Met à jour le message d'erreur
            }
        });
    }

    // Méthode pour charger les absences par professeur (en fonction de son CIN)
    public void loadAbsencesByProf(String profCin) {
        repository.getAbsencesByProf(profCin, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // Si réussi, on met à jour le LiveData avec la liste d'absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // Si échec, on met à jour le LiveData avec le message d'erreur
            }
        });
    }
    // Getter pour LiveData des absences
    public LiveData<List<Absence>> getAbsences() {
        return absences;
    }

    // Getter pour LiveData des messages d'erreur
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Getter pour LiveData de l'état de l'ajout
    public LiveData<Boolean> isAdding() {
        return isAdding;
    }

    // Méthode pour ajouter une absence
    public void addAbsence(Absence absence) {
        isAdding.setValue(true); // Mettre l'état à "en cours d'ajout"
        repository.addAbsence(absence, new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                // Si l'ajout est réussi
                isAdding.setValue(false); // Terminer l'état "en cours d'ajout"
            }
            @Override
            public void onFailure(String error) {
                // Si l'ajout échoue
                isAdding.setValue(false); // Terminer l'état "en cours d'ajout"
                errorMessage.setValue(error); // Mettre à jour le message d'erreur
            }
        });
    }
}
