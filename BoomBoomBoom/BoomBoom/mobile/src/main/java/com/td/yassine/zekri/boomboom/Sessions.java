package com.td.yassine.zekri.boomboom;


/**
 * Created by light on 08/02/2018.
 */

public class Sessions extends SessionId{

    String Nom;
    String Date;
    String Heure;


    public Sessions(){

    }

    public Sessions(String nom, String date, String heure) {

        this.Nom = nom;
        this.Date = date;
        this.Heure = heure;

    }


    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHeure() {
        return Heure;
    }

    public void setHeure(String heure) {
        Heure = heure;
    }
}
