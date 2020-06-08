package com.example.piolleta_projet.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.piolleta_projet.model.Seance;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface SeanceDAO {

    @Query("SELECT * FROM seance")
    List<Seance> getAll();

    @Query("SELECT nom FROM seance WHERE nom = (:nom)")
    String searchNomSeance(String nom);

    @Query("SELECT * FROM seance WHERE id IN (:seanceIds)")
    List<Seance> loadAllByIds(long[] seanceIds);

    @Query("UPDATE seance SET tempsTotal = (:temps) WHERE id = (:id)")
    void updateTempsTotal(int temps, long id);

    @Insert
    long insert(Seance seance);

    @Insert
    long[] insertAll(Seance... seance);

    @Delete
    void delete(Seance seance);

    @Update
    void update(Seance seance);
}
