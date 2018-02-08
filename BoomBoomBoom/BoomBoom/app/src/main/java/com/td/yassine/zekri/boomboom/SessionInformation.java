package com.td.yassine.zekri.boomboom;

import java.util.Date;

/**
 * Created by light on 08/02/2018.
 */

public class SessionInformation {

    private String nom;
    private Date date;

    public SessionInformation(){

    }

    public SessionInformation(String nom, Date date) {

        this.nom = nom;
        this.date = date;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
