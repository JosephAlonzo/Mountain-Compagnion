package com.example.valentin.moutain_companion.models;

/**
 * Created by Zekri on 26/11/2017.
 */

public class Mountain {

    private int id;
    private float latitude;
    private float longitude;
    private String nom;
    private int idk;


    public Mountain(){

    }

    public Mountain(int id, float latitude, float longitude, String nom, int idk) {

        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.nom = nom;
        this.idk = idk;
    }

    @Override
    public String toString() {
        return "Mountain{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", nom='" + nom + '\'' +
                ", idk=" + idk +
                '}';
    }

    /*****************          GETTERS-SETTERS         *********************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getIdk() {
        return idk;
    }

    public void setIdk(int idk) {
        this.idk = idk;
    }
}