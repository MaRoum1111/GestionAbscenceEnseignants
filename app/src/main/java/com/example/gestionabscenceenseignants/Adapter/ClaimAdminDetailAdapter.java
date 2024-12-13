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

public class ClaimAdminDetailAdapter extends RecyclerView.Adapter<ClaimAdminDetailAdapter.ClaimViewHolder>{
    private final List<Claim> claimList;// Interface pour le clic sur les éléments
    private final OnClaimDetailClickListener clickListener ;

    public ClaimAdminDetailAdapter(List<Claim> claimList, OnClaimDetailClickListener clickListener) {
        this.claimList = claimList;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override
    public ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_claim_admin, parent, false);
        return new ClaimViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClaimAdminDetailAdapter.ClaimViewHolder holder, int position) {
        // Récupère l'élément à la position donnée
        Claim claim = claimList.get(position);

        if (claim != null) {

            // Lier les données aux TextViews
            holder.classe.setText("Classe: " + claim.getClasse());
            holder.claimDate.setText("Date: " + claim.getClaimDate());
            holder.date.setText("Date de la séance: " + claim.getDate());
            holder.startTime.setText("Heure de début: " + claim.getStartTime());
            holder.endTime.setText("Heure de fin: " + claim.getEndTime());
            holder.status.setText("Statut: " + claim.getStatus());
            holder.claim.setText("Réclamation:"+claim.getClaim());
            holder.accept.setOnClickListener(v -> clickListener.onAccept(claim));
            holder.reject.setOnClickListener(v -> clickListener.onReject(claim));

        }}

    // Méthode obligatoire : retourne le nombre d'éléments
    @Override
    public int getItemCount() {
        return claimList.size();
    }


    // ViewHolder pour représenter chaque item de la RecyclerView
    public static class ClaimViewHolder extends RecyclerView.ViewHolder {
        TextView classe,claimDate,date,startTime,endTime,status,claim;
        ImageView accept,reject;

        public ClaimViewHolder(View itemView) {
            super(itemView);
            classe= itemView.findViewById(R.id.classe);
            claimDate = itemView.findViewById(R.id.claimDate);
            date=itemView.findViewById(R.id.date);
            startTime=itemView.findViewById(R.id.startTime);
            endTime=itemView.findViewById(R.id.endTime);
            status=itemView.findViewById(R.id.status);
            claim=itemView.findViewById(R.id.claim);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);


        }
    }


    // Interface pour gérer les clics sur les éléments de la RecyclerView
    public interface OnClaimDetailClickListener {
        void onAccept(Claim claim);
        void onReject(Claim claim);

    }

}
