package com.dev.app.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.app.R;
import com.dev.app.adapter.UserAdapterWithDelete;
import com.dev.app.db.AppDatabase;
import com.dev.app.db.UserDao;
import com.dev.app.model.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class DeleteUserActivity extends AppCompatActivity implements UserAdapterWithDelete.OnUserClickListener {

    private RecyclerView recyclerView;
    private UserAdapterWithDelete userAdapter;
    private SearchBar searchBar;
    private SearchView searchView;
    private UserDao userDao;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        userDao = AppDatabase.getDatabase(this).userDao();

        searchBar = findViewById(R.id.search_bar_delete);
        searchView = findViewById(R.id.search_view_delete);
        recyclerView = findViewById(R.id.recycler_view_delete);

        userAdapter = new UserAdapterWithDelete(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);

        setupSearchListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        filter("");
    }

    private void setupSearchListener() {
        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        String query = "%" + text + "%";
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<User> filteredList = userDao.findByName(query);
            new Handler(Looper.getMainLooper()).post(() -> userAdapter.filterList(filteredList));
        });
    }

    @Override
    public void onEditClick(User user) {
        showEditDialog(user);
    }

    @Override
    public void onDeleteClick(User user) {
        showDeleteConfirmationDialog(user);
    }

    private void showEditDialog(final User user) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_edit_user, null);
        final TextInputEditText editName = view.findViewById(R.id.dialog_edit_name);
        final TextInputEditText editEmail = view.findViewById(R.id.dialog_edit_email);
        final TextInputEditText editTel = view.findViewById(R.id.dialog_edit_tel);

        final boolean isEditing = user != null;
        String dialogTitle = isEditing ? "Editar Usuário" : "Criar Novo Usuário";
        String positiveButtonText = isEditing ? "Salvar Alterações" : "Criar";

        if (isEditing) {
            editName.setText(user.getName());
            editEmail.setText(user.getEmail());
            editTel.setText(user.getTelephone());
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(dialogTitle)
                .setView(view)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    String newName = editName.getText().toString().trim();
                    String newEmail = editEmail.getText().toString().trim();
                    String newTel = editTel.getText().toString().trim();

                    if (TextUtils.isEmpty(newName) || TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newTel)) {
                        Toast.makeText(this, "Todos os campos são obrigatórios.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isEditing) {
                            user.setName(newName);
                            user.setEmail(newEmail);
                            user.setTelephone(newTel);
                            updateUserInDatabase(user);
                        } else {
                            User newUser = new User(newName, newEmail, newTel);
                            insertUserInDatabase(newUser);
                        }
                    }
                })
                .show();
    }

    private void showDeleteConfirmationDialog(final User user) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Tem certeza que deseja remover o usuário " + user.getName() + "?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Remover", (dialog, which) -> deleteUser(user))
                .show();
    }

    private void insertUserInDatabase(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertAll(user);
            filter("");
        });
        Toast.makeText(this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void updateUserInDatabase(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
            filter(searchView.getText().toString());
        });
        Toast.makeText(this, "Usuário atualizado com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void deleteUser(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.delete(user);
            filter(searchView.getText().toString());
        });
        Toast.makeText(this, "Usuário removido com sucesso!", Toast.LENGTH_SHORT).show();
    }
}