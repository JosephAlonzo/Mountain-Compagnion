package com.td.yassine.zekri.boomboom.adapterUsers;

/**
 * Created by Zekri on 23/02/2018.
 */

public class User {

    private String Nom;
    private String Frequence;

    public User() {

    }

    public User(String nom, String frequence) {

        this.Nom = nom;
        this.Frequence = frequence;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getFrequence() {
        return Frequence;
    }

    public void setFrequence(String frequence) {
        this.Frequence = frequence;
    }
}
