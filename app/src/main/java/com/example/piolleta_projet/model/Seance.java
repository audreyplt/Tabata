package com.example.piolleta_projet.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "seance",
        indices = {@Index(value = {"nom"},
                unique = true)})
public class Seance implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "preparation")
    private Integer preparation;

    @ColumnInfo(name = "sequence")
    private Integer sequence;

    @ColumnInfo(name = "reposLong")
    private Integer reposLong;

    @ColumnInfo(name = "tempsTotal")
    private Integer tempsTotal;

    @Ignore
    private List<Travail> travails;

    public Seance(String nom, Integer preparation, Integer sequence, Integer reposLong) {
        this.nom = nom;
        this.preparation = preparation;
        this.sequence = sequence;
        this.reposLong = reposLong;
    }

    protected Seance(Parcel in) {
        nom = in.readString();
        if (in.readByte() == 0) {
            preparation = null;
        } else {
            preparation = in.readInt();
        }
        if (in.readByte() == 0) {
            sequence = null;
        } else {
            sequence = in.readInt();
        }
        if (in.readByte() == 0) {
            reposLong = null;
        } else {
            reposLong = in.readInt();
        }
        travails = in.createTypedArrayList(Travail.CREATOR);
    }

    public static final Creator<Seance> CREATOR = new Creator<Seance>() {
        @Override
        public Seance createFromParcel(Parcel in) {
            return new Seance(in);
        }

        @Override
        public Seance[] newArray(int size) {
            return new Seance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        if (preparation == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(preparation);
        }
        if (sequence == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sequence);
        }
        if (reposLong == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(reposLong);
        }
        dest.writeTypedList(travails);
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Integer getPreparation() {
        return preparation;
    }

    public Integer getSequence() {
        return sequence;
    }

    public Integer getReposLong() {
        return reposLong;
    }

    public List<Travail> getTravails() {
        return travails;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTravails(List<Travail> travails) {
        this.travails = travails;
    }

    public Integer getTempsTotal() {
        return tempsTotal;
    }

    public void setTempsTotal(Integer tempsTotal) {
        this.tempsTotal = tempsTotal;
    }
}