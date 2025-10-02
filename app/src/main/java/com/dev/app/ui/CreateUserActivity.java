package com.dev.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.dev.app.R;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import com.dev.app.db.AppDatabase;
import com.dev.app.db.UserDao;
import com.dev.app.model.User;
import com.google.android.material.textfield.TextInputEditText;

public class CreateUserActivity extends AppCompatActivity {

    private TextInputEditText inputName;
    private TextInputEditText inputEmail;
    private TextInputEditText inputTel;
    private Button buttonCreateUser;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        userDao = AppDatabase.getDatabase(this).userDao();

        inputName = findViewById(R.id.input_create_user_name);
        inputEmail = findViewById(R.id.input_create_email);
        inputTel = findViewById(R.id.input_create_tel);
        buttonCreateUser = findViewById(R.id.button_create_user);

        buttonCreateUser.setOnClickListener(v -> {
            createUser();
        });
    }

    private void createUser() {
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String telephone = inputTel.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(telephone)) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(name, email, telephone);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            userDao.insertAll(newUser);

            new Handler(Looper.getMainLooper()).post(() -> {
                Toast.makeText(CreateUserActivity.this, "Usuário criado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}