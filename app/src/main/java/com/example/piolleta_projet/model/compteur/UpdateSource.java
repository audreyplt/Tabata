package com.example.piolleta_projet.model.compteur;

import java.util.ArrayList;

public class UpdateSource {
    // Liste des auditeurs
    private ArrayList<OnUpdateListener> listeners = new ArrayList<OnUpdateListener>();

    // Méthode d'abonnement
    public void addOnUpdateListener(OnUpdateListener listener) {
        listeners.add(listener);
    }

    // Méthode activée par la source pour prévenir les auditeurs de l'événement update
    public void update() {

        // Notify everybody that may be interested.
        for (OnUpdateListener listener : listeners) {
            listener.onUpdate();
        }

    }

    // Méthode activée par la source pour prévenir les auditeurs de l'événement update
    public void etatFini() {

        // Notify everybody that may be interested.
        for (OnUpdateListener listener : listeners) {
            listener.onFinEtape();
        }

    }
}
