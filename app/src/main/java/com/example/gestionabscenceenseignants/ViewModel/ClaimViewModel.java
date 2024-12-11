package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionabscenceenseignants.Repository.ClaimRepository;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.List;

public class ClaimViewModel extends ViewModel {
    private final ClaimRepository repository;
    private final MutableLiveData<List<Claim>> claims ;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isAdding;
    private final MutableLiveData<Boolean> isDeleting;


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
                errorMessage.setValue("Réclamations envoyée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isAdding.setValue(false); // Fin de l'ajout en cas d'échec
                errorMessage.setValue("Erreur lors de l'ajout : " + error);
            }
        });
    }
    // Récupérer les absences du professeur actuellement connecté
    public void getClaimsForCurrentTeacher() {
        repository.getClaimsForCurrentTeacher(new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claim) {
                claims.setValue(claim); // Met à jour la liste des absences
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors de la récupération des absences : " + error); // Message d'erreur
            }
        });
    }


    // Réinitialiser le message d'erreur ou de succès
    public void resetErrorMessage() {
        errorMessage.setValue(null); // Réinitialise le message
    }

    public LiveData <List<Claim>> getClaim(){
        return claims;
    };
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

