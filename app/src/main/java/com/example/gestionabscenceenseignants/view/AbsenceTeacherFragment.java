package com.example.gestionabscenceenseignants.view;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.Adapter.DetailAbsenceAdapter;
import com.example.gestionabscenceenseignants.model.Absence;
import com.example.gestionabscenceenseignants.ViewModel.AbsenceViewModel;
import java.util.List;

public class AbsenceTeacherFragment extends Fragment implements DetailAbsenceAdapter.OnAbsenceActionListener {
    private RecyclerView recyclerView;
    private DetailAbsenceAdapter adapter;
    private AbsenceViewModel absenceViewModel;
    private List<Absence> absenceList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Initialisation des composants globaux pour AbsenceTeacherFragment");
        absenceViewModel = new ViewModelProvider(this).get(AbsenceViewModel.class);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Création de la vue pour AbsenceTeacherFragment");
        View rootView = inflater.inflate(R.layout.fragment_detail_absence, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView name = rootView.findViewById(R.id.teacher_name);
        TextView cin = rootView.findViewById(R.id.teacher_cin);
        TextView nb = rootView.findViewById(R.id.total_absences);

        absenceViewModel.getAbsencesForCurrentTeacher();
        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            if (absences != null && !absences.isEmpty()) {
                absenceList = absences;
                Absence firstAbsence = absences.get(0);
                name.setText(firstAbsence.getProfName());
                cin.setText(firstAbsence.getCin());
                nb.setText("Total des absences : " + absences.size());
            } else {
                name.setText("Nom indisponible");
                cin.setText("CIN indisponible");
                nb.setText("Aucune absence trouvée");
            }
        });

        absenceViewModel.getAbsences().observe(getViewLifecycleOwner(), absences -> {
            Log.d(TAG, "Absences reçues : " + (absences != null ? absences.size() : "null"));
            if (absences != null && !absences.isEmpty()) {
                absenceList = absences;
                adapter = new DetailAbsenceAdapter(absenceList, this);
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setAdapter(null);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: La vue est créée pour AbsenceTeacherFragment");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Fragment visible à l'utilisateur");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Fragment interactif");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Fragment perd le focus");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Fragment n'est plus visible");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: Libération des ressources de la vue");
        adapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Fragment détruit");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Fragment détaché de l'activité");
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onDelete(Absence absence) {
        if (absenceList != null) {
            absenceList.remove(absence);
            adapter.notifyDataSetChanged();
            absenceViewModel.deleteAbsence(absence.getIdAbsence(), absence.getCin());
            Log.d(TAG, "Absence supprimée : " + absence);
            Toast.makeText(getContext(), "Absence supprimée avec succès", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEdit(Absence absence) {
        Log.d(TAG, "Modification de l'absence : " + absence);
        EditAbsenceFragment editFragment = new EditAbsenceFragment();

        Bundle args = new Bundle();
        args.putString("idAbsence", absence.getIdAbsence());
        args.putString("profName", absence.getProfName());
        args.putString("cin", absence.getCin());
        args.putString("salle", absence.getSalle());
        args.putString("date", absence.getDate());
        args.putString("startTime", absence.getStartTime());
        args.putString("endTime", absence.getEndTime());
        args.putString("classe", absence.getClasse());
        args.putString("status", absence.getStatus());
        editFragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editFragment)
                .addToBackStack(null)
                .commit();
    }
}
