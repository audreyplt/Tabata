package com.example.piolleta_projet.model.compteur;

import android.os.CountDownTimer;

public class Compteur extends UpdateSource {

    private float mTimeLeftInMillis;
    private CountDownTimer timer;
    private boolean mTimerRunning;
    private float mEndTime;
    private long currentTimeMilis;

    //Constructeur de compteur prenant un nombre de secondes, permet d'avoir le temps en milisecondes
    public Compteur(float value) {
        mTimeLeftInMillis = value * 1000;
    }


    //Retourne le nombre de secondes
    public int getSecondes() {
        int secs = (int) (mTimeLeftInMillis / 1000);
        return secs;
    }

    //Retourne les milisecondes du temps
    public int getMillisecondes() {
        return (int) (mTimeLeftInMillis % 1000);
    }

    // Lancer le compteur
    public void start() {

        //On récupère les milisecondes qui nous sépare du 1 janvier 1970 à minuit
        currentTimeMilis = System.currentTimeMillis();

        //La fin du chrono sera le temps jusqu'à présent + la durée qui reste du chrono
        mEndTime = currentTimeMilis + mTimeLeftInMillis;

        if (timer != null) {
            timer.cancel();
        }
        // Créer le CountDownTimer avec comme paramètre : le temps en miliseconde qui reste, tous les combien où on déclenche onTick
        timer = new CountDownTimer((long) mTimeLeftInMillis, 10) {

            //Fonction appelé tous les x temps
            public void onTick(long millisUntilFinished) {
                //On met à jour le temps qu'il reste
                mTimeLeftInMillis = millisUntilFinished;

                // Mise à jour de l'affichage
                update();
            }

            // Fonction appelé à la fin du chrono
            public void onFinish() {

                //On met le temps qu'il reste à 0
                mTimeLeftInMillis = 0;

                // Mise à jour de l'apparence
                update();
                etatFini();
            }
        }.start();   //Lance le compteur
        mTimerRunning = true;
    }

    public void isInPause(Boolean inPause) {

        timer.cancel();

        //S'il n'y a pas de timer ou qu'il est en pause
        if (timer == null || inPause) {
            timer = new CountDownTimer((long) mTimeLeftInMillis, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    update();
                }

                @Override
                public void onFinish() {
                    mTimeLeftInMillis = 0;
                    mTimerRunning = false;
                    // Mise à jour
                    update();
                    etatFini();
                }
            }.start();
        }
    }

    //Les getters et setters
    public float getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public float getEndTime() {
        return mEndTime;
    }

    public void cancel() {
        timer.cancel();
    }
}

