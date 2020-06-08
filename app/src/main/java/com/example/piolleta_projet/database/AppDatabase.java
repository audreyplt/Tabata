package com.example.piolleta_projet.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.piolleta_projet.model.Seance;
import com.example.piolleta_projet.model.Travail;

@Database(entities = {Seance.class, Travail.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SeanceDAO seanceDAO();
    public abstract TravailDAO travailDAO();

}