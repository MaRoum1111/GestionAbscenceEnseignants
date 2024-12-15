package com.example.gestionabscenceenseignants.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.gestionabscenceenseignants.Repository.ClaimRepository;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.List;

public class ClaimViewModel extends ViewModel {

    private final ClaimRepository repository; // Instance pour gérer les réclamations
    private final MutableLiveData<List<Claim>> claims; // Liste des réclamations
    private final MutableLiveData<String> errorMessage; // Messages d'erreur ou de succès
    private final MutableLiveData<Boolean> isAdding; // État d'ajout d'une réclamation
    private final MutableLiveData<Boolean> isDeleting; // État de suppression d'une réclamation

    // Constructeur : initialisation des variables
    public ClaimViewModel() {
        repository = new ClaimRepository(); // Initialisation du repository
        claims = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isAdding = new MutableLiveData<>(false); // Pas d'ajout en cours
        isDeleting = new MutableLiveData<>(false); // Pas de suppression en cours
    }

    /**
     * Récupérer toutes les réclamations.
     */
    public void getClaims() {
        repository.getClaims(new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                claims.setValue(claimList); // Met à jour la liste des réclamations
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors du chargement : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Récupérer les réclamations d'un professeur spécifique.
     * @param profCin Le CIN du professeur.
     */
    public void getClaimByProf(String profCin) {
        repository.getClaimByProf(profCin, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                claims.setValue(claimList); // Met à jour la liste des réclamations
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors du chargement : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Ajouter une réclamation.
     * @param claim La réclamation à ajouter.
     */
    public void addClaim(Claim claim) {
        isAdding.setValue(true); // Début de l'opération d'ajout
        repository.addClaim(claim, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                isAdding.setValue(false); // Fin de l'ajout
                claims.setValue(claimList); // Met à jour la liste des réclamations
                errorMessage.setValue("Réclamation envoyée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isAdding.setValue(false); // Fin de l'ajout en cas d'échec
                errorMessage.setValue("Erreur lors de l'ajout : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Récupérer les réclamations du professeur actuellement connecté.
     */
    public void getClaimsForCurrentTeacher() {
        repository.getClaimsForCurrentTeacher(new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                claims.setValue(claimList); // Met à jour la liste des réclamations
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors de la récupération des réclamations : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Supprimer une réclamation.
     * @param documentId L'ID du document à supprimer.
     */
    public void deleteClaim(String documentId) {
        isDeleting.setValue(true); // Début de l'opération de suppression
        repository.deleteClaim(documentId, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                isDeleting.setValue(false); // Fin de la suppression
                getClaimsForCurrentTeacher(); // Recharge la liste des réclamations
                errorMessage.setValue("Réclamation supprimée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isDeleting.setValue(false); // Fin de la suppression en cas d'échec
                errorMessage.setValue("Erreur lors de la suppression : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Accepter une réclamation.
     * @param documentId L'ID de la réclamation.
     */
    public void onAccept(String documentId) {
        isDeleting.setValue(true); // Début de l'opération d'acceptation
        repository.approuverReclamation(documentId, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                isDeleting.setValue(false); // Fin de l'acceptation
                getClaimsForCurrentTeacher(); // Recharge la liste des réclamations
                errorMessage.setValue("Réclamation acceptée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isDeleting.setValue(false); // Fin de l'acceptation en cas d'échec
                errorMessage.setValue("Erreur lors de l'acceptation : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Rejeter une réclamation.
     * @param documentId L'ID de la réclamation.
     */
    public void onReject(String documentId) {
        isDeleting.setValue(true); // Début de l'opération de rejet
        repository.rejeterReclamation(documentId, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                isDeleting.setValue(false); // Fin du rejet
                getClaimsForCurrentTeacher(); // Recharge la liste des réclamations
                errorMessage.setValue("Réclamation rejetée avec succès.");
            }

            @Override
            public void onFailure(String error) {
                isDeleting.setValue(false); // Fin du rejet en cas d'échec
                errorMessage.setValue("Erreur lors du rejet : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Mettre à jour une réclamation.
     * @param documentId L'ID du document à mettre à jour.
     * @param updatedClaim La réclamation mise à jour.
     */
    public void updateClaim(String documentId, Claim updatedClaim) {
        repository.updateClaim(documentId, updatedClaim, new ClaimRepository.AuthCallback() {
            @Override
            public void onSuccess(List<Claim> claimList) {
                claims.setValue(claimList); // Mise à jour de la liste des réclamations
                errorMessage.setValue("Réclamation mise à jour avec succès.");
            }

            @Override
            public void onFailure(String error) {
                errorMessage.setValue("Erreur lors de la mise à jour : " + error); // Message d'erreur
            }
        });
    }

    /**
     * Réinitialiser le message d'erreur ou de succès.
     */
    public void resetErrorMessage() {
        errorMessage.setValue(null); // Réinitialisation du message
    }

    // Getter : obtenir la liste des réclamations
    public LiveData<List<Claim>> getClaim() {
        return claims;
    }

    // Getter : obtenir le message d'erreur ou de succès
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // Getter : obtenir l'état de l'ajout d'une réclamation
    public LiveData<Boolean> isAdding() {
        return isAdding;
    }

    // Getter : obtenir l'état de la suppression d'une réclamation
    public LiveData<Boolean> isDeleting() {
        return isDeleting;
    }
}
