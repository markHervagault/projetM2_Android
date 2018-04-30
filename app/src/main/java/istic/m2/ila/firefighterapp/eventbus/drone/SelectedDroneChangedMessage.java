package istic.m2.ila.firefighterapp.eventbus.drone;

import istic.m2.ila.firefighterapp.dto.DroneDTO;

public class SelectedDroneChangedMessage {
    public DroneDTO Drone;

    public SelectedDroneChangedMessage(DroneDTO drone)
    {
        Drone = drone;
    }
}
