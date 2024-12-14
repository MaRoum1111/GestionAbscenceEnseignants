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
                        if (isExcelFile(fileUri)) {
                            handleFileImport(fileUri); // Handle the file import process
                        } else {
                            Toast.makeText(requireContext(), "Veuillez sélectionner un fichier Excel", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Aucun fichier sélectionné", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emplois, container, false);

        // Initialize the ViewModel and RecyclerView
        initializeViewModelAndRecyclerView(view);

        // Set up the FloatingActionButton to open file picker
        fabImportFile = view.findViewById(R.id.fab_import_file);
        fabImportFile.setOnClickListener(v -> openFilePicker());

        return view;
    }

    private void initializeViewModelAndRecyclerView(View view) {
        // Initialize the ViewModel
        emploiViewModel = new ViewModelProvider(this).get(EmploiViewModel.class);

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.rv_emplois);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        emploiAdapter = new EmploiAdapter();
        recyclerView.setAdapter(emploiAdapter);
    }

    // Method to open the file picker for Excel files
    // Méthode pour ouvrir le sélecteur de fichier
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Ajouter plusieurs types MIME pour couvrir différents types de fichiers Excel
        intent.setType("*/*"); // Accepter tous les types de fichiers (ex. *.xls, *.xlsx)
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // Pour .xlsx
                "application/vnd.ms-excel" // Pour .xls
        });
        filePickerLauncher.launch(intent); // Lance l'activité de sélection de fichier
    }
    // Method to check if the selected file is an Excel file
    private boolean isExcelFile(Uri fileUri) {
        String fileExtension = getFileExtension(fileUri);
        return "xlsx".equalsIgnoreCase(fileExtension); // Check if the file has .xlsx extension
    }

    // Method to get the file extension from Uri
    private String getFileExtension(Uri uri) {
        String fileName = uri.getLastPathSegment();
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return "";
    }

    // Method to handle file import and process the selected Excel file
    private void handleFileImport(Uri fileUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(fileUri);
            List<Emploi> emplois = emploiViewModel.traiterFichierExcel(inputStream); // Process the file using ViewModel

            if (emplois != null && !emplois.isEmpty()) {
                // Update RecyclerView with new data
                emploiAdapter.submitList(emplois);

                // Save imported emplois to Firestore
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
