package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/26/18.
 */

public class DroneDTO {
    private Long id;

    private String nom;

    private String adresseMac;

    private EDroneStatut statut;

    private int battery;

    public EDroneStatut getStatut() {
        return statut;
    }

    public void setStatut(EDroneStatut statut) {
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresseMac() {
        return adresseMac;
    }

    public void setAdresseMac(String adresseMac) {
        this.adresseMac = adresseMac;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}
