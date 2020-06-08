package com.example.piolleta_projet.controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.piolleta_projet.model.Seance;
import com.example.piolleta_projet.model.Travail;
import com.example.piolleta_projet.model.compteur.Compteur;
import com.example.piolleta_projet.R;
import com.example.piolleta_projet.model.compteur.OnUpdateListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class Entrainement extends AppCompatActivity implements OnUpdateListener {

    private TextView timerValue;
    private Compteur compteur;
    private Seance seance;
    private ArrayList<Travail> travails;
    private LinkedList<String> types;
    private LinkedList<String> libelles;
    private LinkedList<Float> temps;
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrainement);

        timerValue = findViewById(R.id.entrainement_timerValue);
        types = new LinkedList<>();
        libelles = new LinkedList<>();
        temps = new LinkedList<>();

        //On récupère ce que la précédente activité nous a passé
        seance = getIntent().getParcelableExtra("seance");
        travails = getIntent().getParcelableArrayListExtra("travails");

        //On met à jour l'affichage de l'activité pour le premier temps
        TextView txtle = findViewById(R.id.entrainement_etape);
        txtle.setText("Préparation !");
        View root = txtle.getRootView();
        root.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));


        initialisationCompteur();   //On crée le premier compteur
        preparationEtapes();        //On prépare la liste des temps pour faire par la suite les différents compteur
    }

    public void initialisationCompteur() {
        // Initialiser l'objet Compteur
        compteur = new Compteur(seance.getPreparation());

        // Abonner l'activité au compteur pour "suivre" les événements
        compteur.addOnUpdateListener(this);
    }

    //Fonction appelé suite au onTick du Compteur
    @Override
    public void onUpdate()
    {
        String miliseconde = String.format("%03d", compteur.getMillisecondes());
        miliseconde = miliseconde.substring(0, 1);

        String str = String.format("%01d", compteur.getSecondes()) + "," + miliseconde;

//      Affichage des informations du compteur
        timerValue.setText(str);
    }

    //Appelé à la fin d'un chrono
    @Override
    public void onFinEtape() {

        //On enlève les infos de l'étape qui vient de se finir des LinkedList
        types.poll();
        libelles.poll();
        temps.poll();

        compteur.cancel();

        View someView = findViewById(R.id.entrainement_etape);
        View root = someView.getRootView();
        TextView txtle = findViewById(R.id.entrainement_etape);

        //S'il y a d'autres étapes encore à réaliser
        if (temps.size() != 0) {

            //On crée un compteur avec le nouveau temps
            compteur = new Compteur(temps.get(0));

            // Abonner l'activité au compteur pour "suivre" les événements
            compteur.addOnUpdateListener(this);

            //On récupète le type et le libelle de la nouvelle étape
            String type = types.get(0);
            String libelle = libelles.get(0);

            //Pour modifier la couleur
            switch (type) {
                case "Repos long":
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                case "Exercice":
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    break;
                case "Repos":
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    break;
            }

            //Pour modifier le text
            txtle.setText(libelle);

            music = MediaPlayer.create(Entrainement.this,R.raw.coup_sifflet);
            music.start();
            //lancerChrono(temps.poll());
            compteur.start();

        } else {
            //Sinon on fait l'affichage pour l'écran de fin
            root.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            txtle.setText("FIN");
            txtle = findViewById(R.id.entrainement_timerValue);
            txtle.setText("");
            Button btn = findViewById(R.id.entrainement_btnEtat);
            btn.setVisibility(View.INVISIBLE);

        }
    }


    //Fonction appelé au lancement d'une activité
    public void preparationEtapes() {

        //On ajoute à la liste des temps le temps de préparation
        temps.add((float)seance.getPreparation());
        libelles.add("Préparation");
        types.add("preparation");

        //On ajoute ensuite les repos et exercices avec aussi les repos longs
        for (int i = 0; i < seance.getSequence(); i++) {
            for (int j = 0; j < travails.size(); j++) {
                types.add(travails.get(j).getType());
                libelles.add(travails.get(j).getDescription());
                temps.add((float)travails.get(j).getDuree());
            }

            types.add("Repos long");
            libelles.add("Repos long");
            temps.add((float)seance.getReposLong());
        }

        music = MediaPlayer.create(Entrainement.this,R.raw.coup_sifflet);
        music.start();
        //On lance le chrono
        compteur.start();

    }

    //Appelé lors du clique sur le bouton Pause/Relancer
    public void changementEtat(View view) {

        Button btn = findViewById(R.id.entrainement_btnEtat);

        if (btn.getText().equals("Pause")) {
            compteur.isInPause(false);
            btn.setText("Relancer");
        } else {
            compteur.isInPause(true);
            btn.setText("Pause");
        }

    }

    //Pour la rotation de l'écran
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Button btn = findViewById(R.id.entrainement_btnEtat);

        //Boolean pour dire si le chrono tourne avant le clique sur le bouton Pause/Relancer
        if(btn.getText().equals("Pause")) {
            outState.putBoolean("timerRunning", true);
        } else {
            outState.putBoolean("timerRunning", false);
        }

        //Float pour dire le temps qu'il reste jusqu'à la fin du compteur
        outState.putFloat("endTime", compteur.getEndTime());

        //On transforme notre LinkedList en array float pour pouvoir la passer
        float[] tmpArray = new float[temps.size()];
        for (int j = 0; j<temps.size(); j++) {
            tmpArray[j] = temps.get(j);
        }
        outState.putFloatArray("listTemps", tmpArray);

        ArrayList<String> listLibelles = new ArrayList<>();
        listLibelles.addAll(libelles);
        outState.putStringArrayList("listLibelles",listLibelles);

        ArrayList<String> listTypes = new ArrayList<>();
        listTypes.addAll(types);
        outState.putStringArrayList("listTypes",listTypes);

        compteur.cancel();

        outState.putFloat("millisLeft", compteur.getmTimeLeftInMillis()/1000);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //On vide nos LinkedList
        temps.clear();
        libelles.clear();
        types.clear();

        //On reremplit la LinkedList
        int nbf = 0;
        for (Float f : savedInstanceState.getFloatArray("listTemps")) {
            temps.add(nbf++,f);
        }
        libelles.addAll(savedInstanceState.getStringArrayList("listLibelles"));
        types.addAll(savedInstanceState.getStringArrayList("listTypes"));

        //S'il reste encore des compteurs à exister
        if (temps.size() != 0) {

            temps.set(0,savedInstanceState.getFloat("millisLeft"));

            compteur.cancel();
            //On crée un compteur avec le nouveau temps
            compteur = new Compteur(temps.get(0));

            // Abonner l'activité au compteur pour "suivre" les événements
            compteur.addOnUpdateListener(this);

            //On récupète le type et le libelle de la nouvelle étape
            String type = types.get(0);
            String libelle = libelles.get(0);

            //Pour modifier la couleur
            View someView = findViewById(R.id.entrainement_etape);
            View root = someView.getRootView();

            switch (type) {
                case "Repos long":
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                case "Exercice":
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    break;
                case "Repos":
                    root.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                    break;
            }

            //Pour modifier le text
            TextView txtle = findViewById(R.id.entrainement_etape);
            txtle.setText(libelle);

            Button btn = findViewById(R.id.entrainement_btnEtat);

            // Si le timer n'est pas en pause
            if(savedInstanceState.getBoolean("timerRunning")) {
                compteur.isInPause(true);
                btn.setText("Pause");
            } else {
                //On met à jour l'affichage du temps
                String temps = Float.toString(savedInstanceState.getFloat("millisLeft"));
                int occurenceVirgule = temps.indexOf('.');
                String res = temps.substring(0,occurenceVirgule)+","+temps.substring(occurenceVirgule+1,occurenceVirgule+2);
                timerValue.setText(res);
                compteur.isInPause(false);
                btn.setText("Relancer");
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        music.stop();
    }

    //Quand on fait le bouton retour du device
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }
}