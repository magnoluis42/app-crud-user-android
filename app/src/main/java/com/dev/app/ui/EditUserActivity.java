package com.dev.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.app.R;
import com.dev.app.db.AppDatabase;
import com.dev.app.db.UserDao;
import com.dev.app.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class EditUserActivity extends AppCompatActivity {

    private TextInputEditText inputName;
    private TextInputEditText inputEmail;
    private TextInputEditText inputTel;
    private Button buttonSave;
    private TextView titleTextView;
    private UserDao userDao;
    private User userToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        userDao = AppDatabase.getDatabase(this).userDao();

        inputName = findViewById(R.id.input_create_user_name);
        inputEmail = findViewById(R.id.input_create_email);
        inputTel = findViewById(R.id.input_create_tel);
        buttonSave = findViewById(R.id.button_create_user);

        if (getIntent().hasExtra("USER_TO_EDIT")) {
            userToEdit = (User) getIntent().getSerializableExtra("USER_TO_EDIT");
            setupEditMode();
        } else {
            setupCreateMode();
        }

        buttonSave.setOnClickListener(v -> {
            saveUser();
        });
    }

    private void setupEditMode() {
        buttonSave.setText("Salvar Alterações");

        inputName.setText(userToEdit.getName());
        inputEmail.setText(userToEdit.getEmail());
        inputTel.setText(userToEdit.getTelephone());
    }

    private void setupCreateMode() {
        buttonSave.setText("Criar Usuário");
    }

    private void saveUser() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String telephone = inputTel.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(telephone)) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;

        }

        AppDatabase.databaseWriteExecutor.execute(() -> {
            if (userToEdit != null) {
                userToEdit.setName(name);
                userToEdit.setEmail(email);
                userToEdit.setTelephone(telephone);
                userDao.update(userToEdit);
            } else {
                User newUser = new User(name, email, telephone);
                userDao.insertAll(newUser);
            }

            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(EditUserActivity.this, "Operação bem-sucedida!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}