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

// ClaimAdapter est un adaptateur RecyclerView qui permet d'afficher une liste de réclamations (Claims)
public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder> {

    // Liste des réclamations à afficher
    private final List<Claim> claimList;

    // Interface qui gère les clics sur les éléments de la liste (suppression et édition)
    private final OnClaimClickListener listener;

    // Constructeur qui initialise la liste des réclamations et le listener
    public ClaimAdapter(List<Claim> claimList, ClaimAdapter.OnClaimClickListener listener) {
        this.claimList = claimList;
        this.listener = listener;
    }

    // Méthode obligatoire de RecyclerView.Adapter qui crée un nouveau ViewHolder pour chaque élément
    @NonNull
    @Override
    public ClaimAdapter.ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate la vue pour chaque item de la RecyclerView à partir du layout XML item_claim
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim, parent, false);
        return new ClaimAdapter.ClaimViewHolder(view); // Retourne un nouveau ViewHolder avec la vue associée
    }

    // Méthode obligatoire de RecyclerView.Adapter qui lie les données (réclamation) à chaque élément de la RecyclerView
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClaimAdapter.ClaimViewHolder holder, int position) {
        // Récupère l'objet Claim à la position donnée dans la liste
        Claim claim = claimList.get(position);

        if (claim != null) {
            // Lier les informations de l'objet Claim aux TextViews respectifs
            holder.classe.setText("Classe: " + claim.getClasse());
            holder.claimDate.setText("Date: " + claim.getClaimDate());
            holder.date.setText("Date de la séance: " + claim.getDate());
            holder.startTime.setText("Heure de début: " + claim.getStartTime());
            holder.endTime.setText("Heure de fin: " + claim.getEndTime());
            holder.status.setText("Status: " + claim.getStatus());

            // Modifier la couleur du statut selon la valeur de 'status'
            if ("Approuvé".equalsIgnoreCase(claim.getStatus())) {
                holder.status.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
            } else if ("Rejeté".equalsIgnoreCase(claim.getStatus())) {
                holder.status.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_red_dark));
            } else {
                holder.status.setTextColor(holder.itemView.getContext().getColor(R.color.yellow));
            }

            // Ajouter des listeners pour gérer les clics sur les icônes de suppression et d'édition
            holder.deleteIcon.setOnClickListener(v -> listener.onDelete(claim));
            holder.editIcon.setOnClickListener(v -> listener.onEdit(claim));
        }
    }

    // Retourner le nombre d'éléments dans la liste (obligatoire)
    @Override
    public int getItemCount() {
        return claimList.size();
    }

    // ViewHolder qui représente chaque élément (réclamation) dans la RecyclerView
    public static class ClaimViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des vues pour les éléments de la réclamation
        TextView classe, claimDate, date, startTime, endTime, status;
        ImageView deleteIcon, editIcon;

        // Constructeur pour initialiser les vues du layout item_claim
        public ClaimViewHolder(View itemView) {
            super(itemView);

            // Liaison des éléments de la vue avec les variables
            classe = itemView.findViewById(R.id.classe);
            claimDate = itemView.findViewById(R.id.claimDate);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            status = itemView.findViewById(R.id.status);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }

    // Interface pour gérer les clics sur les éléments de la RecyclerView (suppression et édition)
    public interface OnClaimClickListener {
        void onDelete(Claim claim); // Méthode appelée lors d'un clic sur l'icône de suppression
        void onEdit(Claim claim);   // Méthode appelée lors d'un clic sur l'icône d'édition
    }
}
