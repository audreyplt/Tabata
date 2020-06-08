package com.example.piolleta_projet.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "travail",
        foreignKeys = @ForeignKey(entity = Seance.class,
                parentColumns = "id",
                childColumns = "seance_id"),
        indices = {@Index("seance_id")})
public class Travail implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "seance_id")
    private long seanceId;

    private String description;

    private int duree;

    private String type;

    public Travail(String description, int duree, String type) {
        this.description = description;
        this.duree = duree;
        this.type = type;
    }

    protected Travail(Parcel in) {
        description = in.readString();
        duree = in.readInt();
        type = in.readString();
    }

    public static final Creator<Travail> CREATOR = new Creator<Travail>() {
        @Override
        public Travail createFromParcel(Parcel in) {
            return new Travail(in);
        }

        @Override
        public Travail[] newArray(int size) {
            return new Travail[size];
        }
    };

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getDuree() {
        return duree;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeInt(duree);
        dest.writeString(type);
    }

    public long getId() {
        return id;
    }

    public long getSeanceId() {
        return seanceId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSeanceId(long seanceId) {
        this.seanceId = seanceId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setType(String type) {
        this.type = type;
    }
}
