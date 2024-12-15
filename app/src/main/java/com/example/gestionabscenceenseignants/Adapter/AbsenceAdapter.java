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
import java.util.ArrayList;
import java.util.List;

// La classe AbsenceAdapter est un adapter RecyclerView qui sert à afficher la liste des absences.
public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    // Liste des absences à afficher
    private final List<Absence> absenceList;

    // Interface pour gérer le clic sur un élément
    private final OnAbsenceClickListener clickListener;

    // Constructeur de l'adapter
    // Il prend en paramètre une liste d'absences et un listener pour le clic
    public AbsenceAdapter(List<Absence> absenceList, OnAbsenceClickListener clickListener) {
        this.absenceList = new ArrayList<>(absenceList); // Copie de la liste d'absences pour éviter des modifications directes
        this.clickListener = clickListener; // Initialisation du listener de clic
    }

    // Crée un nouveau ViewHolder pour chaque élément dans la RecyclerView
    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate la vue de l'élément de la liste à partir du layout XML
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view); // Retourne un nouveau ViewHolder
    }

    // Lie les données de l'élément à la vue correspondante
    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        // Récupère l'objet Absence à partir de la liste à la position donnée
        Absence absence = absenceList.get(position);

        // Remplir les TextViews avec les données de l'objet Absence
        holder.profName.setText(absence.getProfName());
        holder.absenceCount.setText("Total Absences : " + absence.getAbsenceCount());
        holder.cin.setText(absence.getCin());

        // Gestion du clic sur un élément : on appelle la méthode onAbsenceClick du listener si elle est définie
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

    // Retourne le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    // Méthode pour mettre à jour les données de l'adapter
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Absence> newAbsenceList) {
        absenceList.clear(); // Supprime les anciens éléments de la liste
        absenceList.addAll(newAbsenceList); // Ajoute les nouveaux éléments
        notifyDataSetChanged(); // Notifie l'adapter que les données ont changé, ce qui met à jour l'affichage
    }

    // ViewHolder qui représente chaque élément de la RecyclerView
    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des TextViews pour afficher les informations
        TextView profName, absenceCount, cin;

        // Constructeur du ViewHolder : récupère les références des éléments de la vue
        public AbsenceViewHolder(View itemView) {
            super(itemView);
            profName = itemView.findViewById(R.id.profName); // Récupère le TextView pour le nom du professeur
            absenceCount = itemView.findViewById(R.id.absenceCount); // Récupère le TextView pour le nombre d'absences
            cin = itemView.findViewById(R.id.cin); // Récupère le TextView pour le CIN
        }
    }

    // Interface définissant le comportement lors du clic sur un élément de la liste
    public interface OnAbsenceClickListener {
        // Méthode appelée lors d'un clic sur un élément
        void onAbsenceClick(String cin, String profName, int absenceCount);
    }
}
