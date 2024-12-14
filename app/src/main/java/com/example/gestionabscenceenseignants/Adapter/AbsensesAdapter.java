package com.example.gestionabscenceenseignants.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.Absence;

import java.util.ArrayList;
import java.util.List;
public class AbsensesAdapter extends RecyclerView.Adapter<AbsensesAdapter.AbsencesViewHolder> {
    private final List<Absence> absenceList;
    private final OnAbsenceClickListener listener;

    public AbsensesAdapter(List<Absence> absenceList, OnAbsenceClickListener listener) {
        this.absenceList = new ArrayList<>(absenceList);
        this.listener = listener;
    }

    @NonNull
    @Override
    public AbsencesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence_teacher, parent, false);

        // Limiter la hauteur de chaque élément à 100dp (ajustez la taille selon vos besoins)
        int itemHeight = (int) (parent.getResources().getDisplayMetrics().density * 180);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, itemHeight));

        return new AbsencesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AbsencesViewHolder holder, int position) {
        Absence absence = absenceList.get(position);

        if (absence != null) {
            holder.startTime.setText("Heure de début: " + absence.getStartTime());
            holder.classe.setText("Classe: " + absence.getClasse());
            holder.endTime.setText("Heure de fin: " + absence.getEndTime());
            holder.status.setText("Statut: " + absence.getStatus());

            holder.recommendButton.setOnClickListener(v -> listener.onRecommend(absence));
        }
    }

    @Override
    public int getItemCount() {
        return absenceList.size();
    }

    public static class AbsencesViewHolder extends RecyclerView.ViewHolder {
        TextView classe, startTime, endTime, status;
        Button recommendButton;

        public AbsencesViewHolder(View itemView) {
            super(itemView);
            classe = itemView.findViewById(R.id.classe);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            status = itemView.findViewById(R.id.status);
            recommendButton = itemView.findViewById(R.id.reclamationButton);
        }
    }

    public interface OnAbsenceClickListener {
        void onRecommend(Absence absence);
    }

    public void updateAbsences(List<Absence> newAbsences) {
        absenceList.clear();
        absenceList.addAll(newAbsences);
        notifyDataSetChanged();
    }
}
