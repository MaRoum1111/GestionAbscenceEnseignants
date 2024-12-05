package com.example.gestionabscenceenseignants.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;
import java.util.List;

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private final List<Absence> absenceList;
    private final OnAbsenceClickListener clickListener; // Interface pour le clic sur les éléments

    // Constructeur pour initialiser la liste et le listener
    public AbsenceAdapter(List<Absence> absenceList, OnAbsenceClickListener clickListener) {
        this.absenceList = absenceList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout pour un item (item_absence.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        // Récupère l'élément à la position donnée
        Absence absence = absenceList.get(position);

        // Lie les données de l'absence aux TextViews
        holder.profName.setText(absence.getProfName()); // Nom du professeur
        holder.absenceCount.setText("Total Absences : " + absence.getAbsenceCount()); // Nombre d'absences
        holder.cin.setText(absence.getCin()); // CIN

        // Définir l'action du clic sur l'élément
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onAbsenceClick(
                        absence.getCin(),
                        absence.getProfName(),
                        absence.getAbsenceCount()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        // Retourne la taille de la liste des absences
        return absenceList.size();
    }

    // ViewHolder pour représenter chaque item de la RecyclerView
    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView profName, absenceCount, cin;

        public AbsenceViewHolder(View itemView) {
            super(itemView);

            // Liaison avec les TextViews du layout item_absence.xml
            profName = itemView.findViewById(R.id.profName);
            absenceCount = itemView.findViewById(R.id.absenceCount);
            cin = itemView.findViewById(R.id.cin);
        }
    }

    // Interface pour gérer les clics sur les éléments de la RecyclerView
    public interface OnAbsenceClickListener {
        void onAbsenceClick(String cin, String profName, int absenceCount);
    }
}
