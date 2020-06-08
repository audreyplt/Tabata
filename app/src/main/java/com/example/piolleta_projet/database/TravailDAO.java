package com.example.piolleta_projet.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.piolleta_projet.model.Travail;

import java.util.List;

@Dao
public interface TravailDAO {

    @Query("SELECT * FROM travail")
    List<Travail> getAll();

    @Query("SELECT * FROM travail WHERE seance_id = :seanceId")
    List<Travail> findByUserId(long seanceId);

    @Insert
    long insert(Travail travail);

    @Insert
    long[] insertAll(Travail... travails);

    @Delete
    void delete(Travail travail);
}
