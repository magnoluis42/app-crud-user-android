package com.dev.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.app.R;
import com.dev.app.model.User;

import java.util.List;

public class UserAdapterWithDelete extends RecyclerView.Adapter<UserAdapterWithDelete.UserViewHolder> {

    private List<User> userList;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onDeleteClick(User user);
        void onEditClick(User user);
    }

    public UserAdapterWithDelete(List<User> userList, OnUserClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    public void filterList(List<User> filteredList) {
        this.userList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_with_delete, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView emailTextView;
        TextView telephoneTextView;
        ImageButton deleteButton, editButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            emailTextView = itemView.findViewById(R.id.textViewEmail);
            telephoneTextView = itemView.findViewById(R.id.textViewTelephone);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
            editButton = itemView.findViewById(R.id.buttonEdit);
        }

        public void bind(final User user, final OnUserClickListener listener) {
            nameTextView.setText(user.getName());
            emailTextView.setText(user.getEmail());
            telephoneTextView.setText(user.getTelephone());
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(user));
            editButton.setOnClickListener(v -> listener.onEditClick(user));
            //itemView.setOnClickListener(v -> listener.onUserClick(user));
        }
    }
}
