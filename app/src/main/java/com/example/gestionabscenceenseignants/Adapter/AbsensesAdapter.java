package com.example.gestionabscenceenseignants.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;
import java.util.ArrayList;
import java.util.List;

// La classe AbsensesAdapter est un adapter RecyclerView qui sert à afficher la liste des absences des enseignants.
public class AbsensesAdapter extends RecyclerView.Adapter<AbsensesAdapter.AbsencesViewHolder> {

    // Liste des absences à afficher
    private final List<Absence> absenceList;

    // Interface pour gérer le clic sur le bouton "recommandation" (reclamation)
    private final OnAbsenceClickListener listener;

    // Constructeur de l'adapter, prend une liste d'absences et un listener pour les clics
    public AbsensesAdapter(List<Absence> absenceList, OnAbsenceClickListener listener) {
        this.absenceList = new ArrayList<>(absenceList); // Copie la liste d'absences pour éviter des modifications directes
        this.listener = listener; // Initialisation du listener pour la gestion du clic
    }

    // Crée un nouveau ViewHolder pour chaque élément de la RecyclerView
    @NonNull
    @Override
    public AbsencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate la vue de l'élément à partir du layout XML
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence_teacher, parent, false);

        // Limite la hauteur de chaque élément à 180dp (ajustez selon les besoins)
        int itemHeight = (int) (parent.getResources().getDisplayMetrics().density * 180);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, itemHeight));

        return new AbsencesViewHolder(view); // Retourne un nouveau ViewHolder
    }

    // Lie les données de l'élément aux vues correspondantes
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AbsencesViewHolder holder, int position) {
        // Récupère l'objet Absence à partir de la liste à la position donnée
        Absence absence = absenceList.get(position);

        if (absence != null) {
            // Remplir les TextViews avec les informations de l'objet Absence
            holder.startTime.setText("Heure de début: " + absence.getStartTime());
            holder.classe.setText("Classe: " + absence.getClasse());
            holder.endTime.setText("Heure de fin: " + absence.getEndTime());
            holder.status.setText("Statut: " + absence.getStatus());

            // Gestion du clic sur le bouton "recommandation" : on appelle la méthode onRecommend du listener si elle est définie
            holder.recommendButton.setOnClickListener(v -> listener.onRecommend(absence));
        }
    }

    // Retourne le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    // ViewHolder qui représente chaque élément de la RecyclerView
    public static class AbsencesViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des TextViews pour afficher les informations et du bouton pour la recommandation
        TextView classe, startTime, endTime, status;
        Button recommendButton;

        // Constructeur du ViewHolder : récupère les références des éléments de la vue
        public AbsencesViewHolder(View itemView) {
            super(itemView);
            classe = itemView.findViewById(R.id.classe); // Récupère le TextView pour la classe
            startTime = itemView.findViewById(R.id.startTime); // Récupère le TextView pour l'heure de début
            endTime = itemView.findViewById(R.id.endTime); // Récupère le TextView pour l'heure de fin
            status = itemView.findViewById(R.id.status); // Récupère le TextView pour le statut
            recommendButton = itemView.findViewById(R.id.reclamationButton); // Récupère le bouton pour la réclamation
        }
    }

    // Interface définissant le comportement lors du clic sur le bouton "recommandation"
    public interface OnAbsenceClickListener {
        // Méthode appelée lorsqu'un utilisateur clique sur le bouton de recommandation
        void onRecommend(Absence absence);
    }

    // Méthode pour mettre à jour les absences dans l'adapter
    @SuppressLint("NotifyDataSetChanged")
    public void updateAbsences(List<Absence> newAbsences) {
        absenceList.clear(); // Supprime les anciens éléments de la liste
        absenceList.addAll(newAbsences); // Ajoute les nouveaux éléments
        notifyDataSetChanged(); // Notifie l'adapter que les données ont changé, ce qui met à jour l'affichage
    }
}
