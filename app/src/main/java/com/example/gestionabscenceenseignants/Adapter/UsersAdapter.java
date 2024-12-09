package com.example.gestionabscenceenseignants.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestionabscenceenseignants.R;
import com.example.gestionabscenceenseignants.model.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private final List<User> userList;

    public UsersAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = userList.get(position);
        holder.cin.setText(user.getCin());
        holder.profName.setText(user.getName());
        holder.role.setText(user.getRole());
        holder.Email.setText(user.getEmail()); // Mise à jour de l'email
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView profName, Email, role,cin;

        public UsersViewHolder(View itemView) {
            super(itemView);

            profName = itemView.findViewById(R.id.profName);
            Email = itemView.findViewById(R.id.email);
            role = itemView.findViewById(R.id.role);
            cin=itemView.findViewById((R.id.profCin));

        }
    }
}
