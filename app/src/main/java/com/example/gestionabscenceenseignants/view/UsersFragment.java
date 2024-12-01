package com.example.gestionabscenceenseignants.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gestionabscenceenseignants.Adapter.UsersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton; // Correct import for FloatingActionButton
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.ViewModel.UserViewModel;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsersAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Charger la vue
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialiser le RecyclerView

        // Initialiser le ViewModel
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.loadUsers();

        // Gérer le clic du bouton flottant
        FloatingActionButton boutonadd = view.findViewById(R.id.btnFloatingAction); // Corrected to FloatingActionButton
        boutonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir le fragment AddUserFragment
                Fragment addUserFragment = new addUserFragment();

                // Remplacer le fragment actuel par le nouveau
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, addUserFragment)
                        .addToBackStack(null) // Ajoute à la pile pour pouvoir revenir
                        .commit();
            }
        });

        return view;
    }
}
