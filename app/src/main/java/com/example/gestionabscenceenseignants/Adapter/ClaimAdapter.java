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

public class ClaimAdapter extends RecyclerView.Adapter<ClaimAdapter.ClaimViewHolder>{
    private final List<Claim> claimList;
    private final OnClaimClickListener listener; // Interface pour le clic sur les éléments

    // Constructeur pour initialiser la liste et le listener
    public ClaimAdapter(List<Claim> claimList, ClaimAdapter.OnClaimClickListener listener) {
        this.claimList = claimList;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ClaimAdapter.ClaimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim, parent, false);
        return new ClaimAdapter.ClaimViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ClaimAdapter.ClaimViewHolder holder, int position) {
        // Récupère l'élément à la position donnée
        Claim claim = claimList.get(position);

        if (claim != null) {

            // Lier les données aux TextViews
            holder.classe.setText("Classe: " + claim.getClasse());
            holder.claimDate.setText("Date: " + claim.getClaimDate());
            holder.date.setText("Date de la séance: " + claim.getDate());
            holder.startTime.setText("Heure de début: " + claim.getStartTime());
            holder.endTime.setText("Heure de fin: " + claim.getEndTime());
            holder.deleteIcon.setOnClickListener(v -> listener.onDelete(claim));
            holder.editIcon.setOnClickListener(v -> listener.onEdit(claim));

        }}

    // Méthode obligatoire : retourne le nombre d'éléments
    @Override
    public int getItemCount() {
        return claimList.size();
    }


    // ViewHolder pour représenter chaque item de la RecyclerView
    public static class ClaimViewHolder extends RecyclerView.ViewHolder {
        TextView classe,claimDate,date,startTime,endTime;
        ImageView deleteIcon,editIcon;

        public ClaimViewHolder(View itemView) {
            super(itemView);

            classe= itemView.findViewById(R.id.classe);
            claimDate = itemView.findViewById(R.id.claimDate);
            date=itemView.findViewById(R.id.date);
            startTime=itemView.findViewById(R.id.startTime);
            endTime=itemView.findViewById(R.id.endTime);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);


        }
    }

    // Interface pour gérer les clics sur les éléments de la RecyclerView
    public interface OnClaimClickListener {
        void onDelete(Claim claim);
        void onEdit(Claim claim);

    }


}
