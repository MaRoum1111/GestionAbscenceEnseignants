package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionabscenceenseignants.Repository.ClaimRepository;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.List;

public class ClaimViewModel extends ViewModel {
    private final ClaimRepository repository; // Instance pour gérer les absences
    private final MutableLiveData<List<Claim>> claims; // Liste des absences
    private final MutableLiveData<String> errorMessage; // Messages d'erreur ou de succès
    private final MutableLiveData<Boolean> isAdding; // État d'ajout d'une absence
    private final MutableLiveData<Boolean> isDeleting; // État de suppression d'une absence

    // Constructeur : initialisation des variables
    public ClaimViewModel(){
        repository = new ClaimRepository(); // Initialisation du repository
        claims = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isAdding = new MutableLiveData<>(false); // Pas d'ajout en cours
        isDeleting = new MutableLiveData<>(false); // Pas de suppression en cours
    }

    public void addClaim(Claim claim) {
        isAdding.setValue(true); // Début de l'opération d'ajout
        repository.addClaim(claim, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                isAdding.setValue(false); // Fin de l'ajout
                claims.setValue(claimList); // Met à jour la liste des absences
                errorMessage.setValue("Absence ajoutée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isAdding.setValue(false); // Fin de l'ajout en cas d'échec
                errorMessage.setValue("Erreur lors de l'ajout : " + error);
            }
        });
    }



    // Réinitialiser le message d'erreur ou de succès
    public void resetErrorMessage() {
        errorMessage.setValue(null); // Réinitialise le message
    }

    // Getter : message d'erreur ou de succès
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Getter : état d'ajout d'une absence
    public LiveData<Boolean> isAdding() {
        return isAdding;
    }

    // Getter : état de suppression d'une absence
    public LiveData<Boolean> isDeleting() {
        return isDeleting;
    }
}
