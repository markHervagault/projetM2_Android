package istic.m2.ila.firefighterapp.dto;

import android.util.Log;

/**
 * Created by hakima on 3/26/18.
 */

public class DroneDTO
{

    private Long id;

    private String nom;

    private String adresseMac;

    private EDroneStatus statut;

    private int battery;

    public EDroneStatus getStatut() {
        return statut;
    }

    public void setStatut(EDroneStatus statut) {
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

    public void Update(DroneInfosDTO dto)
    {
        EDroneStatus status = EDroneStatus.valueOf(dto.status);
        if(status != null)
            this.statut = status;
        else
            Log.e("DroneDTO Update", ("Wrong statuts : " + dto.status));

        setBattery(dto.battery_level);
    }

    public boolean equals(DroneDTO drone2) {
        return this.adresseMac.equals(drone2.adresseMac);
    }

}
