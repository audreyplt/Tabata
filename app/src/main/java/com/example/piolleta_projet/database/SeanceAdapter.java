package com.example.piolleta_projet.database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.piolleta_projet.R;
import com.example.piolleta_projet.model.Seance;

import java.util.List;

public class SeanceAdapter extends ArrayAdapter<Seance> {

    public SeanceAdapter(Context context, List<Seance> seanceList) {
        super(context, R.layout.template_seance,seanceList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Seance seance = getItem(position);

        // Charge le template XML, permet par la suite pour chaque row d'afficher les données
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.template_seance, parent, false);

        TextView textViewNom = rowView.findViewById(R.id.templateSeance_nom);
        TextView textViewTemps = rowView.findViewById(R.id.templateSeance_tmpTotal);

        textViewNom.setText(seance.getNom());
        textViewTemps.setText(String.valueOf(seance.getTempsTotal()));

        return rowView;
    }
}
