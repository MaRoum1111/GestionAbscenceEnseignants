package com.example.gestionabscenceenseignants.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Emploi;

import java.util.ArrayList;
import java.util.List;

public class EmploiAdapter extends RecyclerView.Adapter<EmploiAdapter.EmploiViewHolder> {

    private List<Emploi> emploiList = new ArrayList<>();

    @NonNull
    @Override
    public EmploiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emploi, parent, false);
        return new EmploiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmploiViewHolder holder, int position) {
        Emploi emploi = emploiList.get(position);
        holder.tvNom.setText(emploi.getNom());
        holder.tvJour.setText(emploi.getJour());
        holder.tvHeure.setText(emploi.getHeure());
    }
    @Override
    public int getItemCount() {
        return emploiList.size();
    }

    public void submitList(List<Emploi> emplois) {
        emploiList = emplois;
        notifyDataSetChanged();
    }

    static class EmploiViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvJour, tvHeure;

        public EmploiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tv_file_name);
            tvJour = itemView.findViewById(R.id.tv_date);
            tvHeure = itemView.findViewById(R.id.tv_heure);
        }
    }
}
