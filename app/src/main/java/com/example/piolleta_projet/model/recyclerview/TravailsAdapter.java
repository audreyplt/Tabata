package com.example.piolleta_projet.model.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piolleta_projet.R;
import com.example.piolleta_projet.model.Travail;

import java.util.List;


public class TravailsAdapter extends RecyclerView.Adapter<TravailsAdapter.TravailViewHolder> {

    private List<Travail> travails;

    public TravailsAdapter(List<Travail> travails) {
        this.travails = travails;
    }

    @NonNull
    @Override
    public TravailsAdapter.TravailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_travail, parent, false);

        return new TravailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TravailViewHolder holder, int position) {
        holder.description.setText(travails.get(position).getDescription());
        holder.duree.setText(String.valueOf(travails.get(position).getDuree()));
    }

    @Override
    public int getItemCount() {
        return travails.size();
    }

    public class TravailViewHolder extends RecyclerView.ViewHolder {
        public TextView duree;
        public TextView description;

        public TravailViewHolder(View view) {
            super(view);
            duree = view.findViewById((R.id.item_duree));
            description = view.findViewById((R.id.item_description));
        }
    }
}