package com.example.gestionabscenceenseignants.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.R;

import com.example.gestionabscenceenseignants.model.Claim;

import java.util.List;

public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder>{
    private final List<Claim> claimList;
    private final ClaimAdapter.OnClaimClickListener clickListener; // Interface pour le clic sur les éléments

    // Constructeur pour initialiser la liste et le listener
    public ClaimAdapter(List<Claim> claimList, ClaimAdapter.OnClaimClickListener clickListener) {
        this.claimList = claimList;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ClaimAdapter.ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim, parent, false);
        return new ClaimAdapter.ClaimViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimAdapter.ClaimViewHolder holder, int position) {
        // Récupère l'élément à la position donnée
        Claim claim = claimList.get(position);
        holder.claimDate.setText(claim.getDate());

        // Définir l'action du clic sur l'élément
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClaimClick(
                        claim.getDate()
                );
            }
        });
    }
    // Méthode obligatoire : retourne le nombre d'éléments
    @Override
    public int getItemCount() {
        return claimList.size();
    }


    // ViewHolder pour représenter chaque item de la RecyclerView
    public static class ClaimViewHolder extends RecyclerView.ViewHolder {
        TextView claimDate;

        public ClaimViewHolder(View itemView) {
            super(itemView);

            // Liaison avec les TextViews du layout item_absence.xml
            claimDate = itemView.findViewById(R.id.claimDate);

        }
    }

    // Interface pour gérer les clics sur les éléments de la RecyclerView
    public interface OnClaimClickListener {
        void onClaimClick(String claimDate);
    }
}
