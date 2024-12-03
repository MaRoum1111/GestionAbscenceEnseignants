package com.example.gestionabscenceenseignants.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestionabscenceenseignants.Adapter.UsersAdapter;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;
import com.example.gestionabscenceenseignants.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Correct import for FloatingActionButton

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;

import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser le ViewModel
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observer la liste des utilisateurs
        userViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    adapter = new UsersAdapter(users);
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
                Fragment addUserFragment = new addUserFragment(); // Assure-toi que le nom du fragment est correct

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
}
