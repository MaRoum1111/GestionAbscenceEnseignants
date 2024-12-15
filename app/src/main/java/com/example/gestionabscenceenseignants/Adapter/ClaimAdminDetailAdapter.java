package com.example.gestionabscenceenseignants.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Claim;
import java.util.List;

// ClaimAdminDetailAdapter est un adaptateur RecyclerView utilisé pour afficher les détails d'une réclamation dans l'interface administrateur
public class ClaimAdminDetailAdapter extends RecyclerView.Adapter<ClaimAdminDetailAdapter.ClaimViewHolder>{

    // Liste des réclamations à afficher
    private final List<Claim> claimList;

    // Interface qui gère les actions d'acceptation ou de rejet d'une réclamation
    private final OnClaimDetailClickListener clickListener;

    // Constructeur pour initialiser la liste des réclamations et le listener
    public ClaimAdminDetailAdapter(List<Claim> claimList, OnClaimDetailClickListener clickListener) {
        this.claimList = claimList;
        this.clickListener = clickListener;
    }

    // Méthode obligatoire de RecyclerView.Adapter pour créer un ViewHolder pour chaque élément
    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate la vue pour chaque item de la RecyclerView à partir du layout XML item_detail_claim_admin
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_claim_admin, parent, false);
        return new ClaimViewHolder(view); // Retourne un nouveau ViewHolder avec la vue associée
    }

    // Méthode obligatoire de RecyclerView.Adapter pour lier les données (réclamation) à chaque élément de la RecyclerView
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClaimAdminDetailAdapter.ClaimViewHolder holder, int position) {
        // Récupère l'objet Claim à la position donnée dans la liste
        Claim claim = claimList.get(position);

        if (claim != null) {
            // Lier les informations de l'objet Claim aux TextViews correspondants
            holder.classe.setText("Classe: " + claim.getClasse()); // Affiche la classe
            holder.claimDate.setText("Date: " + claim.getClaimDate()); // Affiche la date de réclamation
            holder.date.setText("Date de la séance: " + claim.getDate()); // Affiche la date de la séance
            holder.startTime.setText("Heure de début: " + claim.getStartTime()); // Affiche l'heure de début
            holder.endTime.setText("Heure de fin: " + claim.getEndTime()); // Affiche l'heure de fin
            holder.status.setText("Statut: " + claim.getStatus()); // Affiche le statut de la réclamation
            holder.claim.setText("Réclamation: " + claim.getClaim()); // Affiche la réclamation proprement dite

            // Change la couleur du texte du statut si la réclamation est en cours
            if ("En cours".equalsIgnoreCase(claim.getStatus())) {
                holder.status.setTextColor(holder.itemView.getContext().getColor(R.color.yellow)); // Met la couleur jaune
            }

            // Ajouter les écouteurs de clic pour accepter ou rejeter la réclamation
            holder.accept.setOnClickListener(v -> clickListener.onAccept(claim)); // Accepte la réclamation
            holder.reject.setOnClickListener(v -> clickListener.onReject(claim)); // Rejette la réclamation
        }
    }

    // Méthode obligatoire de RecyclerView.Adapter pour retourner le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return claimList.size();
    }

    // ViewHolder pour représenter chaque élément (réclamation) dans la RecyclerView
    public static class ClaimViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des vues pour afficher les informations de la réclamation
        TextView classe, claimDate, date, startTime, endTime, status, claim;
        ImageView accept, reject;

        // Constructeur pour initialiser les vues du layout item_detail_claim_admin
        public ClaimViewHolder(View itemView) {
            super(itemView);
            classe = itemView.findViewById(R.id.classe); // Classe de l'enseignant
            claimDate = itemView.findViewById(R.id.claimDate); // Date de la réclamation
            date = itemView.findViewById(R.id.date); // Date de la séance
            startTime = itemView.findViewById(R.id.startTime); // Heure de début
            endTime = itemView.findViewById(R.id.endTime); // Heure de fin
            status = itemView.findViewById(R.id.status); // Statut de la réclamation
            claim = itemView.findViewById(R.id.claim); // Détails de la réclamation
            accept = itemView.findViewById(R.id.accept); // Bouton pour accepter la réclamation
            reject = itemView.findViewById(R.id.reject); // Bouton pour rejeter la réclamation
        }
    }

    // Interface pour gérer les actions d'acceptation ou de rejet d'une réclamation
    public interface OnClaimDetailClickListener {
        void onAccept(Claim claim); // Méthode appelée pour accepter la réclamation
        void onReject(Claim claim); // Méthode appelée pour rejeter la réclamation
    }
}
