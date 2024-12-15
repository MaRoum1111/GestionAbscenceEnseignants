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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Observe the list of users from the ViewModel
        userViewModel.getUsers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null) {
                    // If users are fetched, set up the adapter and RecyclerView
                    adapter = new UsersAdapter(users, UsersFragment.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        // Handle the FloatingActionButton click
        FloatingActionButton boutonadd = view.findViewById(R.id.btnFloatingAction);
        boutonadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the AddUserFragment
                Fragment addUserFragment = new AddUserFragment(); // Make sure fragment name is correct

                // Replace current fragment with AddUserFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, addUserFragment)
                        .addToBackStack(null) // Add fragment to back stack to allow navigation back
                        .commit();
            }
        });

        // Load users when the fragment is created
        userViewModel.loadUsers();

        return view;
    }

    @Override
    public void onEditClick(User user) {
        // Handle the editing of a user
        Log.d("DetailuserFragment", "Editing user: " + user.toString());

        // Create a new EditUserFragment
        EditUserFragment editFragment = new EditUserFragment();

        // Pass user details as arguments to the EditUserFragment
        Bundle args = new Bundle();
        args.putString("cin", user.getCin());
        args.putString("name", user.getName());
        args.putString("email", user.getEmail());
        args.putString("pass", user.getPassword());
        args.putString("role", user.getRole());
        editFragment.setArguments(args);

        // Replace current fragment with EditUserFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, editFragment)
                .addToBackStack(null)
                .commit();
    }

    // Handle the click on the "Delete" button
    @Override
    public void onDeleteClick(String userId) {
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.deleteUser(userId); // Call the deleteUser method in the ViewModel

        // Observe the status message after deletion
        userViewModel.getStatusMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String message) {
                // Show a Toast message for the deletion status
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                userViewModel.loadUsers(); // Reload the users list after deletion
            }
        });
    }

}
