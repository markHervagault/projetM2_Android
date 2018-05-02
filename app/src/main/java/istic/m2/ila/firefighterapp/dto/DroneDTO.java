package istic.m2.ila.firefighterapp.dto;

import android.util.Log;

/**
 * Created by hakima on 3/26/18.
 */

public class DroneDTO
{
    private static final String TAG = "DroneDTO";

    //region members
    private Long id;
    private String nom;
    private String adresseMac;
    private EDroneStatus statut;
    private int battery;
    //endregion
    //region Localisation (utilisée pour l'affichage de l'altitude du drone dans la liste)
    private LocalisationDroneDTO localisation;

    //region Constructor
    public DroneDTO() {
        localisation = new LocalisationDroneDTO();
        localisation.altitude = 0;
        localisation.latitude = 0;
        localisation.longitude = 0;
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
    //endregion

    //region Getter / Setters
    public EDroneStatus getStatut() {
        return statut;
    }

    public void setStatut(EDroneStatus statut) {
        this.statut = statut;
    }

    public LocalisationDroneDTO getLocalisation() {
        return localisation;
    }
    //endregion

    public void setLocalisation(LocalisationDroneDTO loc) {
        localisation = loc;
    }
    //endregion

    //Update
    public void Update(DroneInfosDTO dto) {
        //Nouveau statut
        EDroneStatus status = null;
        try {
            status = EDroneStatus.valueOf(dto.status);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Wrong status received");
        } finally {
            if (status != null)
                statut = status;
        }

        //Update de la batterie
        setBattery(dto.battery_level);
        setLocalisation(dto.position);
    }

    public boolean equals(DroneDTO drone2) {
        return this.adresseMac.equals(drone2.adresseMac);
    }

}
