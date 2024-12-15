package com.example.gestionabscenceenseignants.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Claim;
import java.util.ArrayList;
import java.util.List;

// ClaimAdminAdapter est un adaptateur RecyclerView utilisé pour afficher une liste de réclamations dans l'interface de l'administrateur
public class ClaimAdminAdapter extends RecyclerView.Adapter<ClaimAdminAdapter.ClaimAdminViewHolder> {

    // Liste des réclamations à afficher
    private final List<Claim> claimList;

    // Interface qui gère les clics sur les éléments de la liste
    private final OnClaimClickListener clickListener;

    // Constructeur pour initialiser la liste des réclamations et le listener
    public ClaimAdminAdapter(List<Claim> claimList, OnClaimClickListener clickListener) {
        this.claimList = new ArrayList<>(claimList); // Crée une copie de la liste pour éviter les modifications directes
        this.clickListener = clickListener;
    }

    // Méthode obligatoire de RecyclerView.Adapter qui crée un nouveau ViewHolder pour chaque élément
    @NonNull
    @Override
    public ClaimAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate la vue pour chaque item de la RecyclerView à partir du layout XML item_claim_admin
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim_admin, parent, false);
        return new ClaimAdminViewHolder(view); // Retourne un nouveau ViewHolder avec la vue associée
    }

    // Méthode obligatoire de RecyclerView.Adapter qui lie les données (réclamation) à chaque élément de la RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ClaimAdminViewHolder holder, int position) {
        // Récupère l'objet Claim à la position donnée dans la liste
        Claim claim = claimList.get(position);

        // Lier les informations de l'objet Claim aux TextViews respectifs
        holder.profName.setText(claim.getProfName()); // Affiche le nom du professeur
        holder.cin.setText(claim.getCin()); // Affiche le CIN du professeur

        // Ajoute un listener de clic pour gérer l'action lorsqu'un élément est sélectionné
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                // Appelle la méthode onClaimClick() du listener avec les informations de la réclamation
                clickListener.onClaimClick(
                        claim.getCin(),
                        claim.getProfName()
                );
            }
        });
    }

    // Retourner le nombre d'éléments dans la liste (obligatoire)
    @Override
    public int getItemCount() {
        return claimList.size();
    }

    // Méthode pour mettre à jour les données de la liste et rafraîchir l'affichage
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Claim> newClaimList) {
        claimList.clear(); // Supprime les anciens éléments de la liste
        claimList.addAll(newClaimList); // Ajoute les nouveaux éléments à la liste
        notifyDataSetChanged(); // Rafraîchit l'affichage de la RecyclerView pour refléter les nouvelles données
    }

    // ViewHolder qui représente chaque élément (réclamation) dans la RecyclerView
    public static class ClaimAdminViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des vues pour afficher les informations du professeur (nom et CIN)
        TextView profName, cin;

        // Constructeur pour initialiser les vues du layout item_claim_admin
        public ClaimAdminViewHolder(View itemView) {
            super(itemView);

            // Liaison des éléments de la vue avec les variables
            profName = itemView.findViewById(R.id.profName); // Affiche le nom du professeur
            cin = itemView.findViewById(R.id.cin); // Affiche le CIN du professeur
        }
    }

    // Interface pour gérer les clics sur les éléments de la RecyclerView (détails de la réclamation)
    public interface OnClaimClickListener {
        // Méthode appelée lorsqu'une réclamation est cliquée, avec les informations du professeur
        void onClaimClick(String cin, String profName);
    }
}
