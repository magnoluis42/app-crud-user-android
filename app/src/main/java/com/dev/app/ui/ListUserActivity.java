package com.dev.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.app.R;
import com.dev.app.adapter.UserAdapter;
import com.dev.app.db.AppDatabase;
import com.dev.app.db.UserDao;
import com.dev.app.model.User;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.List;

public class ListUserActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private SearchBar searchBar;
    private SearchView searchView;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        userDao = AppDatabase.getDatabase(this).userDao();

        searchBar = findViewById(R.id.search_bar);
        searchView = findViewById(R.id.search_view);
        recyclerView = findViewById(R.id.recycler_view_search_results);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.list_users), (v, insets) -> {
            return insets;
        });

        userAdapter = new UserAdapter(new ArrayList<>());
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
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text) {
        String query = "%" + text + "%";

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<User> filteredList = userDao.findByName(query);

            new Handler(Looper.getMainLooper()).post(() -> {
                userAdapter.filterList(filteredList);
            });
        });
    }
}