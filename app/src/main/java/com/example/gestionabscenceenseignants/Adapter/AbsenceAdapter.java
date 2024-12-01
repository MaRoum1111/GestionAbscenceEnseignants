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

    public AbsenceAdapter(List<Absence> absenceList) {
        this.absenceList = absenceList;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate l'item_absence.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        // Récupère l'absence de la liste et l'affiche dans les vues
        Absence absence = absenceList.get(position);
        holder.profName.setText(absence.getProfName());
        holder.absenceCount.setText(String.valueOf(absence.getAbsenceCount())); // Met à jour le nombre d'absences
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {

        TextView profName, absenceCount;

        public AbsenceViewHolder(View itemView) {
            super(itemView);
            // Liaison des TextViews de l'item (CardView)
            profName = itemView.findViewById(R.id.profName);
            absenceCount = itemView.findViewById(R.id.absenceCount);
        }
    }
}
