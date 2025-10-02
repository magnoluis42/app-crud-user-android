package com.dev.app.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dev.app.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();
    @Query("SELECT * FROM user WHERE name LIKE :query ORDER BY name ASC")
    List<User> findByName(String query);
    @Insert
    void insertAll(User... users);
    @Delete
    void delete(User user);
    @Update
    void update(User user);
}
