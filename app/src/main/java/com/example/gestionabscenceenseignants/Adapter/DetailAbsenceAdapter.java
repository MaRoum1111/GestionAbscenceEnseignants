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

    private List<Absence> absenceList; // Liste des absences à afficher
    private final OnAbsenceActionListener listener; // Interface pour gérer les actions (supprimer ou éditer)

    // Constructeur pour initialiser la liste des absences et le listener
    public DetailAbsenceAdapter(List<Absence> absenceList, OnAbsenceActionListener listener) {
        this.absenceList = absenceList;
        this.listener = listener;
    }

    // Méthode pour créer un nouveau ViewHolder pour chaque item
    @NonNull
    @Override
    public AbsenceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout de l'item (item_detail_absence.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_absence, parent, false);
        return new AbsenceDetailViewHolder(view); // Retourne le ViewHolder
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AbsenceDetailViewHolder holder, int position) {
        // Récupérer l'objet Absence à la position donnée
        Absence absence = absenceList.get(position);

        if (absence != null) {

            // Lier les données aux TextViews pour afficher les informations
            holder.salle.setText("Salle: " + absence.getSalle());
            holder.date.setText("Date: " + absence.getDate());
            holder.startTime.setText("Heure de début: " + absence.getStartTime());
            holder.endTime.setText("Heure de fin: " + absence.getEndTime());
            holder.classe.setText("Classe: " + absence.getClasse());
            holder.status.setText("Statut: " + absence.getStatus());

            // Définir la couleur du statut selon sa valeur (justifiée ou non)
            if ("Justifiée".equalsIgnoreCase(absence.getStatus())) {
                holder.status.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_green_dark)); // Couleur verte pour "Justifiée"
            } else {
                holder.status.setTextColor(holder.itemView.getContext().getColor(android.R.color.holo_red_dark)); // Couleur rouge pour un statut autre que "Justifiée"
            }

            // Gérer les actions sur les icônes (supprimer ou éditer)
            holder.deleteIcon.setOnClickListener(v -> listener.onDelete(absence)); // Appeler la méthode onDelete dans l'interface listener
            holder.editIcon.setOnClickListener(v -> listener.onEdit(absence)); // Appeler la méthode onEdit dans l'interface listener
        }
    }

    // Méthode obligatoire : retourne le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return absenceList != null ? absenceList.size() : 0; // Retourne la taille de la liste ou 0 si la liste est nulle
    }

    // ViewHolder pour chaque item de la liste (représente une absence)
    public static class AbsenceDetailViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des vues
        TextView salle, date, startTime, endTime, classe, status;
        ImageView deleteIcon, editIcon;

        // Constructeur du ViewHolder, qui lie les vues avec les éléments de l'item
        public AbsenceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            salle = itemView.findViewById(R.id.salle);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            classe = itemView.findViewById(R.id.classe);
            status = itemView.findViewById(R.id.status);
            deleteIcon = itemView.findViewById(R.id.deleteIcon); // Icône pour supprimer
            editIcon = itemView.findViewById(R.id.editIcon); // Icône pour éditer
        }
    }

    // Interface pour gérer les actions (supprimer ou éditer)
    public interface OnAbsenceActionListener {
        void onDelete(Absence absence); // Méthode pour supprimer une absence
        void onEdit(Absence absence);   // Méthode pour éditer une absence
    }
}
