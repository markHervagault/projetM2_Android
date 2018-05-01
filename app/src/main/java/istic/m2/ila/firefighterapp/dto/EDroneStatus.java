package istic.m2.ila.firefighterapp.dto;

/**
 * Created by markh on 28/03/2018.
 */

public enum EDroneStatus {
    EN_MISSION("EN_MISSION"),
    DISPONIBLE("DISPONIBLE"),
    EN_PAUSE("EN_PAUSE"),
    RETOUR_BASE("RETOUR_BASE"),
    DECONNECTE("DECONNECTE"),
    EN_PAUSE_RETOUR_BASE("EN_PAUSE_RETOUR_BASE");

    EDroneStatus(String droneStatus) {}
}
