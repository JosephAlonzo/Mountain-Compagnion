package com.td.yassine.zekri.boomboom;


/**
 * Created by light on 08/02/2018.
 */

public class Sessions extends SessionId{

    private String nom;
    private String date;
    private String heure;
    private boolean ouverte;

    public Sessions(){

    }

    public Sessions(String nom, String date, String heure) {

        this.nom = nom;
        this.date = date;
        this.heure = heure;
        this.ouverte = false;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public boolean isOuverte() {
        return ouverte;
    }

    public void setOuverte(boolean ouverte) {
        this.ouverte = ouverte;
    }
}
