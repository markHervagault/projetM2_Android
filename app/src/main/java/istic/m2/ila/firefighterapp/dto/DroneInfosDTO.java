package istic.m2.ila.firefighterapp.dto;

/**
 * Created by markh on 28/03/2018.
 */

public class DroneInfosDTO {

    public long timestamp;
    public int id_drone;
    public int id_mission;
    public int battery_level;
    public LocalisationDroneDTO localisation;
    public OrientationDroneDTO orientation;
    public VelocityDroneDTO velocity;

}