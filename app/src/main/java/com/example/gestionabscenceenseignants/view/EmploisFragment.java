package com.example.gestionabscenceenseignants.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.EmploiViewModel;
import com.example.gestionabscenceenseignants.Adapter.EmploiAdapter;
import com.example.gestionabscenceenseignants.model.Emploi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.List;

public class EmploisFragment extends Fragment {

    private EmploiViewModel emploiViewModel;
    private RecyclerView recyclerView;
    private EmploiAdapter emploiAdapter;
    private FloatingActionButton fabImportFile;

    // ActivityResultLauncher for file selection
    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri fileUri = data.getData();
                        handleFileImport(fileUri);
                    }
                } else {
                    Toast.makeText(requireContext(), "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("EmploisFragment", "onCreateView: Fragment created");
        View view = inflater.inflate(R.layout.fragment_emplois, container, false);

        // Initialize ViewModel
        emploiViewModel = new ViewModelProvider(this).get(EmploiViewModel.class);

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.rv_emplois);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        emploiAdapter = new EmploiAdapter();
        recyclerView.setAdapter(emploiAdapter);

        // Setup FloatingActionButton
        fabImportFile = view.findViewById(R.id.fab_import_file);
        fabImportFile.setOnClickListener(v -> openFilePicker());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("EmploisFragment", "onViewCreated: View created");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("EmploisFragment", "onStart: Fragment started");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("EmploisFragment", "onResume: Fragment resumed");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("EmploisFragment", "onPause: Fragment paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("EmploisFragment", "onStop: Fragment stopped");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("EmploisFragment", "onDestroyView: View destroyed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("EmploisFragment", "onDestroy: Fragment destroyed");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("EmploisFragment", "onDetach: Fragment detached");
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // MIME type for Excel files
        filePickerLauncher.launch(intent);
    }

    private void handleFileImport(Uri fileUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(fileUri);

            // Use ViewModel to process the file
            List<Emploi> emplois = emploiViewModel.traiterFichierExcel(inputStream);

            if (emplois != null && !emplois.isEmpty()) {
                // Update RecyclerView with new data
                emploiAdapter.submitList(emplois);

                // Save to Firestore
                emploiViewModel.enregistrerEmplois(emplois);

                Toast.makeText(requireContext(), "Fichier importé avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Fichier vide ou invalide", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("EmploisFragment", "Erreur lors de l'importation du fichier : ", e);
            Toast.makeText(requireContext(), "Erreur lors de l'importation du fichier", Toast.LENGTH_SHORT).show();
        }
    }
}
