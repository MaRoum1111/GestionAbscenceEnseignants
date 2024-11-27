package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.gestionabscenceenseignants.Repository.AbsenceRepository;
import com.example.gestionabscenceenseignants.model.Absence;
import java.util.List;

public class AbsenceViewModel extends ViewModel {
    private final AbsenceRepository repository; // n7otou l'instance mta3 AbsenceRepository
    private final MutableLiveData<List<Absence>> absences; // mta3 LiveData li bech nb3tou les absences
    private final MutableLiveData<String> errorMessage; // LiveData mta3 message d'erreur

    // Constructeur
    public AbsenceViewModel() {
        repository = new AbsenceRepository(); // on instancie l'AbsenceRepository
        absences = new MutableLiveData<>(); // n7otou liveData li t3ayet 3la les absences
        errorMessage = new MutableLiveData<>(); // hna li7bina njibu message d'erreur
    }

    // Méthode pour charger les absences
    public void loadAbsences() {
        repository.getAbsences(new AbsenceRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Absence> absencesList) {
                absences.setValue(absencesList); // ken l'operation t3addat mlih, n7otou l'absences fi liveData
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue(error); // ken fama erreur, n7otou message fi liveData
            }
        });
    }

    // Getter pour LiveData
    public LiveData<List<Absence>> getAbsences() {
        return absences; // Retourner les absences
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage; // Retourner le message d'erreur
    }
}
