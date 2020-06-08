package com.example.piolleta_projet.controler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.piolleta_projet.R;

public class DialogAjoutExercice extends AppCompatDialogFragment {

    private EditText editTextDuree;
    private EditText editTextLibelle;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builer = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_ajout_travail, null);

        builer.setView(view)
                .setTitle("Ajout exercice")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //voir après
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String duree = editTextDuree.getText().toString();
                        String libelle = editTextLibelle.getText().toString();

                        if(!duree.equals("") && !libelle.equals("") && !duree.equals("0") && libelle.trim().length()>0) {
                            listener.applyTexts(duree,libelle,"Exercice");
                        }
                    }
                });

        editTextDuree = view.findViewById(R.id.ajoutExercice_duree);
        editTextLibelle = view.findViewById(R.id.ajoutExercice_libelle);

        return builer.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" devrait afficher dialog");
        }
    }

    public interface DialogListener {
        void applyTexts(String duree, String libelle, String typeTravail);
    }
}
