package com.example.piolleta_projet.controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import com.example.piolleta_projet.R;

public class Accueil extends AppCompatActivity {

    private MediaPlayer music = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        if (music == null) {
            music = MediaPlayer.create(Accueil.this, R.raw.accueil_music);
            music.start();
        }
    }

    public void creerEntrainement(View view) {
        Intent intent = new Intent(this, CreationEntrainement.class);
        music.stop();
        startActivity(intent);
    }

    public void choisirEntrainement(View view) {
        Intent intent = new Intent(this, SelectionEntrainement.class);
        music.stop();
        startActivity(intent);
    }

    //Pour la rotation de l'écran
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        int position = music.getCurrentPosition();
        outState.putInt("position", position);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        music.stop();
        int position = savedInstanceState.getInt("position");
        music.seekTo(position);

    }

    //Quand on fait le bouton retour du device
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}
