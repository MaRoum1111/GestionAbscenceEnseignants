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
import com.example.gestionabscenceenseignants.model.Absence;

import java.util.List;

public class DetailAbsenceAdapter extends RecyclerView.Adapter<DetailAbsenceAdapter.AbsenceDetailViewHolder> {

    private List<Absence> absenceList;
    private final OnAbsenceActionListener listener;

    // Constructeur pour initialiser la liste des absences et le listener
    public DetailAbsenceAdapter(List<Absence> absenceList, OnAbsenceActionListener listener) {
        this.absenceList = absenceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AbsenceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout de l'item (item_detail_absence.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_absence, parent, false);
        return new AbsenceDetailViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AbsenceDetailViewHolder holder, int position) {
        // Récupérer l'objet Absence à la position donnée
        Absence absence = absenceList.get(position);

        if (absence != null) {
            // Lier les données aux TextViews
            holder.salle.setText("Salle: "+absence.getSalle());
            holder.date.setText("Date: "+absence.getDate());
            holder.startTime.setText("Heure de début: "+absence.getStartTime());
            holder.endTime.setText("Heure de fin: "+absence.getEndTime());
            holder.classe.setText("Classe: "+absence.getClasse());
            holder.status.setText("Statut: "+absence.getStatus());

            // Définir la couleur du statut selon sa valeur
            if ("Justifiée".equalsIgnoreCase(absence.getStatus())) {
                holder.status.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark));
            } else {
                holder.status.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_red_dark));
            }

            // Actions sur les icônes
            holder.deleteIcon.setOnClickListener(v -> listener.onDelete(absence));
            holder.editIcon.setOnClickListener(v -> listener.onEdit(absence));
        }
    }

    @Override
    public int getItemCount() {
        return absenceList != null ? absenceList.size() : 0;  // Vérifier si absenceList est nul
    }

    // Mettre à jour la liste des absences
    public void setAbsenceList(List<Absence> absenceList) {
        this.absenceList = absenceList;
        notifyDataSetChanged();  // Mettre à jour l'adaptateur avec les nouvelles données
    }

    // ViewHolder pour chaque item de la liste
    public static class AbsenceDetailViewHolder extends RecyclerView.ViewHolder {
        TextView salle, date, startTime, endTime, classe, status;
        ImageView deleteIcon, editIcon;

        public AbsenceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            // Liaison des vues
            salle = itemView.findViewById(R.id.salle);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            classe = itemView.findViewById(R.id.classe);
            status = itemView.findViewById(R.id.status);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);
        }
    }

    // Interface pour gérer les actions (supprimer ou éditer)
    public interface OnAbsenceActionListener {
        void onDelete(Absence absence);
        void onEdit(Absence absence);
    }
}
