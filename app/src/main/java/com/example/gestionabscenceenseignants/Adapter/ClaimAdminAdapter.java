package com.example.gestionabscenceenseignants.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.model.Claim;

import java.util.ArrayList;
import java.util.List;

public class ClaimAdminAdapter extends RecyclerView.Adapter<ClaimAdminAdapter.ClaimAdminViewHolder> {

    private final List<Claim> claimList;
    private final OnClaimClickListener clickListener;

    // Constructeur
    public ClaimAdminAdapter(List<Claim> claimList, OnClaimClickListener clickListener) {
        this.claimList = new ArrayList<>(claimList); // Copie pour éviter les modifications directes
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ClaimAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim_admin, parent, false);
        return new ClaimAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimAdminViewHolder holder, int position) {
        Claim claim = claimList.get(position);

        holder.profName.setText(claim.getProfName());
        holder.cin.setText(claim.getCin());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClaimClick(
                        claim.getCin(),
                        claim.getProfName()

                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return claimList.size();
    }

    // Méthode pour mettre à jour les données
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Claim> newClaimList) {
        claimList.clear(); // Supprime les anciens éléments
        claimList.addAll(newClaimList); // Ajoute les nouveaux éléments
        notifyDataSetChanged(); // Rafraîchit l'affichage
    }

    public static class ClaimAdminViewHolder extends RecyclerView.ViewHolder {
        TextView profName, cin;

        public ClaimAdminViewHolder(View itemView) {
            super(itemView);
            profName = itemView.findViewById(R.id.profName);
            cin = itemView.findViewById(R.id.cin);
        }
    }

    public interface OnClaimClickListener {
        void onClaimClick(String cin, String profName);
    }
}
