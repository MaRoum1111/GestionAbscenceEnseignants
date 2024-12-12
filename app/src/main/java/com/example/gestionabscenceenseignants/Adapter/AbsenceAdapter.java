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

public class AbsenceAdapter extends RecyclerView.Adapter<AbsenceAdapter.AbsenceViewHolder> {

    private final List<Absence> absenceList;
    private final OnAbsenceClickListener clickListener;

    // Constructeur
    public AbsenceAdapter(List<Absence> absenceList, OnAbsenceClickListener clickListener) {
        this.absenceList = new ArrayList<>(absenceList); // Copie pour éviter les modifications directes
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AbsenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absence, parent, false);
        return new AbsenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenceViewHolder holder, int position) {
        Absence absence = absenceList.get(position);

        holder.profName.setText(absence.getProfName());
        holder.absenceCount.setText("Total Absences : " + absence.getAbsenceCount());
        holder.cin.setText(absence.getCin());

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
        return absenceList.size();
    }

    // Méthode pour mettre à jour les données
    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<Absence> newAbsenceList) {
        absenceList.clear(); // Supprime les anciens éléments
        absenceList.addAll(newAbsenceList); // Ajoute les nouveaux éléments
        notifyDataSetChanged(); // Rafraîchit l'affichage
    }

    public static class AbsenceViewHolder extends RecyclerView.ViewHolder {
        TextView profName, absenceCount, cin;

        public AbsenceViewHolder(View itemView) {
            super(itemView);
            profName = itemView.findViewById(R.id.profName);
            absenceCount = itemView.findViewById(R.id.absenceCount);
            cin = itemView.findViewById(R.id.cin);
        }
    }

    public interface OnAbsenceClickListener {
        void onAbsenceClick(String cin, String profName, int absenceCount);
    }
}
