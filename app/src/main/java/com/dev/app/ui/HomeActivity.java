package com.dev.app.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dev.app.R;
import com.dev.app.utils.NavigationUtils;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonCreateUser = findViewById(R.id.btnAddUser);
        Button btnListUser = findViewById(R.id.btnListUser);
        Button btnRemoveUser = findViewById(R.id.btnRemoveUser);
//        Button btnUpdateUser = findViewById(R.id.btnUpdateUser);

        buttonCreateUser.setOnClickListener(v -> {
            NavigationUtils.redirectToActivity(this, CreateUserActivity.class, null);
        });

        btnListUser.setOnClickListener(v -> {
            NavigationUtils.redirectToActivity(this, ListUserActivity.class, null);
        });

        btnRemoveUser.setOnClickListener(v -> {
            NavigationUtils.redirectToActivity(this, DeleteUserActivity.class, null);
        });

//        btnUpdateUser.setOnClickListener(v -> {
//            NavigationUtils.redirectToActivity(this, EditUserActivity.class, null);
//        });

    }
}