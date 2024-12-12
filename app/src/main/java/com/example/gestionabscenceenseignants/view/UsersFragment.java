package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gestionabscenceenseignants.Adapter.UsersAdapter;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;

import java.util.List;

public class UsersFragment extends Fragment implements UsersAdapter.OnUserClickListener {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;
    private UserViewModel userViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser le ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observer la liste des utilisateurs
        userViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    adapter = new UsersAdapter(users, UsersFragment.this); // Passer l'interface ici
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        // Gérer le clic du bouton flottant
        FloatingActionButton boutonadd = view.findViewById(R.id.btnFloatingAction);
        boutonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir le fragment AddUserFragment
                Fragment addUserFragment = new AddUserFragment(); // Assure-toi que le nom du fragment est correct

                // Remplacer le fragment actuel par le nouveau
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, addUserFragment)
                        .addToBackStack(null) // Ajoute à la pile pour pouvoir revenir
                        .commit();
            }
        });

        // Charger la liste des utilisateurs
        userViewModel.loadUsers();  // Ajouté pour charger les utilisateurs dès que le fragment est créé

        return view;
    }

    @Override
    public void onEditClick(User user) {
        // Gestion de l'édition de l'absence
        Log.d("DetailuserFragment", "Modification de l'user : " + user.toString());

        // Créer un nouveau fragment EditAbsenceFragment
        EditUserFragment editFragment = new EditUserFragment();

        // Passer les données de l'absence sélectionnée en tant qu'arguments
        Bundle args = new Bundle();
        args.putString("cin", user.getCin());
        args.putString("name",user.getName());
        args.putString("email",user.getEmail());
        args.putString("pass", user.getPassword());
        args.putString("role", user.getRole());
        editFragment.setArguments(args);

        // Remplacer le fragment actuel par EditAbsenceFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editFragment) // ID du conteneur de fragments
                .addToBackStack(null) // Ajouter à la pile pour permettre un retour en arrière
                .commit();
    }

    // Méthode pour gérer le clic sur le bouton "Supprimer"
    @Override
    public void onDeleteClick(String userId) {
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.deleteUser(userId);
        // Observer le message de statut de suppression
        userViewModel.getStatusMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                userViewModel.loadUsers(); // Recharger la liste après suppression
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("UsersFragment", "onStart appelé");
        // Code spécifique à l'initialisation de l'activité ou à la reprise de l'activité
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("UsersFragment", "onResume appelé");
        // Code pour mettre à jour l'UI ou reprendre des tâches
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("UsersFragment", "onPause appelé");
        // Code pour sauvegarder l'état ou libérer des ressources
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("UsersFragment", "onStop appelé");
        // Code pour arrêter les processus ou libérer des ressources
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("UsersFragment", "onDestroyView appelé");
        // Code pour nettoyer la vue (par exemple, détacher les observateurs)
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("UsersFragment", "onDestroy appelé");
        // Code pour nettoyer les ressources avant la destruction du fragment
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("UsersFragment", "onDetach appelé");
        // Code pour nettoyer les ressources associées au fragment
    }
}
