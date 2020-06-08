package com.example.piolleta_projet.controler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.piolleta_projet.R;
import com.example.piolleta_projet.database.AppDatabase;
import com.example.piolleta_projet.database.DatabaseClient;
import com.example.piolleta_projet.database.SeanceAdapter;
import com.example.piolleta_projet.database.SeanceDAO;
import com.example.piolleta_projet.model.Seance;
import com.example.piolleta_projet.model.Travail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SelectionEntrainement extends AppCompatActivity {

    private DatabaseClient mDb;
    private SeanceAdapter adapter;
    private ListView listSeance;
    private static final int REQUEST_CODE_ADD = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_seance);

        // Récupération du DatabaseClient
        mDb = DatabaseClient.getInstance(getApplicationContext());

        listSeance = findViewById(R.id.selectionSeance_list);

        // Lier l'adapter au listView
        adapter = new SeanceAdapter(this, new ArrayList<Seance>());
        listSeance.setAdapter(adapter);
        try {
            getSeances();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getSeances() throws ExecutionException, InterruptedException {
        class GetSeances extends AsyncTask<Void, Void, List<Seance>> {

            @Override
            protected List<Seance> doInBackground(Void... voids) {
                //On récupère toutes les séances qui sont en base de données
                List<Seance> seances = mDb.getAppDatabase().seanceDAO().getAll();

                //On récupère les travails de chaque séance et on l'assigne
                for(Seance seance: seances) {
                    List<Travail> travails = mDb
                            .getAppDatabase()
                            .travailDAO()
                            .findByUserId(seance.getId());
                    seance.setTravails(travails);
                }

                return seances;
            }

            @Override
            protected void onPostExecute(List<Seance> seances) {
                super.onPostExecute(seances);

                // Mettre à jour l'adapter avec la liste de taches
                adapter.clear();
                adapter.addAll(seances);

                // Now, notify the adapter of the change in source
                adapter.notifyDataSetChanged();
            }
        }

        GetSeances gs = new GetSeances();
        gs.execute().get(); //Le .get() permet d'attendre la fin du doInBackground
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {

            // Mise à jour des taches
            try {
                getSeances();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Quand on fait le bouton retour du device
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(this,Accueil.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }

}
