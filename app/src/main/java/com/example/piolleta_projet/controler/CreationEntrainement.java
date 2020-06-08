package com.example.piolleta_projet.controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.piolleta_projet.R;
import com.example.piolleta_projet.database.DatabaseClient;
import com.example.piolleta_projet.model.Seance;
import com.example.piolleta_projet.model.Travail;
import com.example.piolleta_projet.model.recyclerview.TravailsAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CreationEntrainement extends AppCompatActivity implements DialogAjoutExercice.DialogListener {

    private ArrayList<Travail> exoList;
    private RecyclerView recyclerView;
    private TravailsAdapter mAdapter;
    private Seance seance;
    private DatabaseClient mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Chargement du layout
        setContentView(R.layout.activity_creation_entrainement);

        recyclerView = findViewById(R.id.creation_recyclerView);
        Button buttonAjoutExo = findViewById(R.id.creation_ajouterTravail);
        Button buttonAjoutRepos = findViewById(R.id.creation_ajouterRepos);

        exoList = new ArrayList<>();
        //On crée un nouvel Adapter avec la liste exoList
        mAdapter = new TravailsAdapter(exoList);
        mDb = DatabaseClient.getInstance(getApplicationContext());

        //On crée un LinearLayout afin de pouvoir placer les éléments
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        //Attribut à la recyclerView le layout actuelle
        recyclerView.setLayoutManager(mLayoutManager);

        //Pour n'avoir aucune animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        buttonAjoutExo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogExercice();
            }
        });

        buttonAjoutRepos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogRepos();
            }
        });
    }


    //OUVERTURE DES BOITES DE DIALOGUES
    public void openDialogExercice() {
        DialogAjoutExercice dialogAjoutExercice = new DialogAjoutExercice();
        dialogAjoutExercice.show(getSupportFragmentManager(), "Ajout exercice");
    }

    public void openDialogRepos() {
        DialogAjoutRepos dialogAjoutRepos = new DialogAjoutRepos();
        dialogAjoutRepos.show(getSupportFragmentManager(), "Ajout repos");
    }

    public void openDialogManqueInformation() {
        DialogManqueInformation dialogManqueInformation = new DialogManqueInformation();
        dialogManqueInformation.show(getSupportFragmentManager(), "attention");
    }

    public void openDialogNomSeanceExistant() {
        DialogNomSeance dialogNomSeance = new DialogNomSeance();
        dialogNomSeance.show(getSupportFragmentManager(), "nomSeance");
    }

    public void opendDialogInsertSeance() {
        DialogReussiteInsertion dialogReussiteInsertion = new DialogReussiteInsertion();
        dialogReussiteInsertion.show(getSupportFragmentManager(), "insertSeance");
    }

    //Lorsque l'utilisateur décide d'effectuer un entrainement
    public void lancerEntrainement(View view) {

        Intent intent = new Intent(this, Entrainement.class);
        Integer preparation, sequence, reposLong = null;

        EditText editText = findViewById(R.id.creation_nom);
        String nom = editText.getText().toString();

        editText = findViewById(R.id.creation_preparation);
        String strPreparation = editText.getText().toString();

        editText = findViewById(R.id.creation_sequence);
        String strSequence = editText.getText().toString();

        editText = findViewById(R.id.creation_reposLong);
        String strReposLong = editText.getText().toString();

        //Permet de vérifier que les données renseignées sont bien valide
        if (nom.trim().length() < 1 || nom.equals("") || strPreparation.equals("") || Integer.parseInt(strPreparation) == 0 || strSequence.equals("") || Integer.parseInt(strSequence) == 0 || strReposLong.equals("") || Integer.parseInt(strReposLong) == 0) {
            openDialogManqueInformation();  //Affichage d'une boîte de dialogue pour dire que les données ne sont pas bon
        } else {
            //Si tout est bon on lance l'entraînement
            preparation = Integer.parseInt(strPreparation);
            sequence = Integer.parseInt(strSequence);
            reposLong = Integer.parseInt(strReposLong);

            Seance seance = new Seance(nom, preparation, sequence, reposLong);

            intent.putExtra("seance", seance);
            intent.putParcelableArrayListExtra("travails", exoList);
            startActivity(intent);
        }
    }

    public void sauvegarderEntrainement(View view) throws ExecutionException, InterruptedException {

        EditText editText = findViewById(R.id.creation_nom);
        final String nom = editText.getText().toString();

        editText = findViewById(R.id.creation_preparation);
        String strPreparation = editText.getText().toString();

        editText = findViewById(R.id.creation_sequence);
        String strSequence = editText.getText().toString();

        editText = findViewById(R.id.creation_reposLong);
        String strReposLong = editText.getText().toString();

        final String[] searchNom = {""};

        //On cherche dans la base de données le nom de séance qui est égale à celui que l'utilisateur à renseigné
        class getnomSeanceDB extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                searchNom[0] = mDb.getAppDatabase()
                        .seanceDAO()
                        .searchNomSeance(nom);
                return null;
            }
        }
        getnomSeanceDB ns = new getnomSeanceDB();
        ns.execute().get();  //Le .get() permet d'attendre la fin du doInBackground

        String resDBNom = searchNom[0];

        //Permet de vérifier que les données renseignées sont bien valide
        if (nom.trim().length() < 1 || nom.equals("") || strPreparation.equals("") || Integer.parseInt(strPreparation) == 0 || strSequence.equals("") || Integer.parseInt(strSequence) == 0 || strReposLong.equals("") || Integer.parseInt(strReposLong) == 0) {
            openDialogManqueInformation();  //Affichage d'une boîte de dialogue pour dire que les données ne sont pas bon
        } else if (resDBNom != null) {
            openDialogNomSeanceExistant();  //Affichage d'une boîte de dialogue pour dire que le nom de séance existe déjà
        } else {
            seance = new Seance(nom, Integer.parseInt(strPreparation), Integer.parseInt(strSequence), Integer.parseInt(strReposLong));

            class SaveDB extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... voids) {

                    //On rajoute au temps total le temps de préparation et le temps de repos long multiplié par le nombre de séquence
                    int tempsTotal = 0;
                    tempsTotal += seance.getPreparation();
                    tempsTotal += seance.getSequence() * seance.getReposLong();

                    //On insert la séance dans la base de données et on récupère son id
                    long idSeance = mDb.getAppDatabase()
                            .seanceDAO()
                            .insert(seance);

                    //Pour chaque travail on ajoute au temps total le travail multiplié par le nombre de séquence
                    for (Travail travail : exoList) {
                        tempsTotal = tempsTotal + travail.getDuree() * seance.getSequence();
                        travail.setSeanceId(idSeance);  //On set le seanceId de notre travail suivant l'id de la séance inséré précédemment

                        //On ajoute le travail dans la base de données
                        mDb.getAppDatabase()
                                .travailDAO()
                                .insert(travail);
                    }

                    //On modifie le temps total de notre séance avec celui qu'on vient de calculer
                    mDb.getAppDatabase()
                            .seanceDAO()
                            .updateTempsTotal(tempsTotal, idSeance);

                    return null;
                }
            }

            SaveDB sd = new SaveDB();
            sd.execute().get(); //Le .get() permet d'attendre la fin du doInBackground

            opendDialogInsertSeance();  //On affiche une boîte de dialogue pour prévenir que l'intertion a était correctement réalisé
        }
    }

    //Fonction appelé en retour des boîtes de dialogues pour l'ajout d'un travail
    @Override
    public void applyTexts(String duree, String libelle, String typeTravail) {

        Travail exo = new Travail(libelle, Integer.parseInt(duree), typeTravail);
        exoList.add(exo);
        mAdapter.notifyDataSetChanged();
    }

    //Quand on fait le bouton retour du device
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(this, Accueil.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }
}


