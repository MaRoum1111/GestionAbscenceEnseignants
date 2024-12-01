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
        holder.date.setText(absence.getDate());
        holder.startTime.setText(absence.getStartTime());
        holder.endTime.setText(absence.getEndTime());
        holder.reason.setText(absence.getReason());
        holder.status.setText(absence.getStatus());
        holder.subjectName.setText(absence.getSubjectName());
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {

        TextView profName, date, startTime, endTime, reason, status, subjectName;

        public AbsenceViewHolder(View itemView) {
            super(itemView);
            // Liaison des TextViews de l'item (CardView)
            profName = itemView.findViewById(R.id.profName);
            date = itemView.findViewById(R.id.date);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            reason = itemView.findViewById(R.id.reason);
            status = itemView.findViewById(R.id.status);
            subjectName = itemView.findViewById(R.id.subjectName);
        }
    }
}
