package com.example.gestionabscenceenseignants.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private final List<User> userList; // Liste des utilisateurs à afficher
    private final OnUserClickListener onUserClickListener; // Interface pour gérer les clics sur les boutons de modification et suppression

    // Interface pour gérer les événements de clics sur les boutons de suppression et modification
    public interface OnUserClickListener {
        void onEditClick(User user); // Méthode pour l'édition d'un utilisateur
        void onDeleteClick(String userId); // Méthode pour la suppression d'un utilisateur
    }

    // Constructeur de l'adaptateur pour initialiser la liste des utilisateurs et le listener
    public UsersAdapter(List<User> userList, OnUserClickListener onUserClickListener) {
        this.userList = userList;
        this.onUserClickListener = onUserClickListener;
    }

    // Méthode pour créer un nouveau ViewHolder pour chaque item de la liste
    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gonfler le layout de l'item (item_user.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UsersViewHolder(view); // Retourner le ViewHolder
    }

    // Méthode pour lier les données de l'utilisateur aux vues
    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = userList.get(position); // Récupérer l'utilisateur à la position donnée

        // Lier les données aux TextViews
        holder.cin.setText(user.getCin());
        holder.profName.setText(user.getName());
        holder.role.setText(user.getRole());
        holder.Email.setText(user.getEmail());

        // Gestion des clics sur les icônes de modification et suppression
        holder.editIcon.setOnClickListener(v -> onUserClickListener.onEditClick(user)); // Clic sur le bouton d'édition
        holder.deleteIcon.setOnClickListener(v -> onUserClickListener.onDeleteClick(user.getCin())); // Clic sur le bouton de suppression
    }

    // Retourner le nombre d'éléments dans la liste
    @Override
    public int getItemCount() {
        return userList.size(); // Retourne la taille de la liste des utilisateurs
    }

    // ViewHolder pour chaque utilisateur de la liste
    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        // Déclaration des vues pour chaque champ d'utilisateur
        TextView profName, Email, role, cin;
        ImageView editIcon, deleteIcon;

        // Constructeur du ViewHolder qui lie les vues avec les éléments de l'item
        public UsersViewHolder(View itemView) {
            super(itemView);
            profName = itemView.findViewById(R.id.profName); // Nom de l'utilisateur
            Email = itemView.findViewById(R.id.email); // Email de l'utilisateur
            role = itemView.findViewById(R.id.role); // Rôle de l'utilisateur
            cin = itemView.findViewById(R.id.profCin); // CIN de l'utilisateur
            editIcon = itemView.findViewById(R.id.editIcon); // Icône d'édition
            deleteIcon = itemView.findViewById(R.id.deleteIcon); // Icône de suppression
        }
    }
}
