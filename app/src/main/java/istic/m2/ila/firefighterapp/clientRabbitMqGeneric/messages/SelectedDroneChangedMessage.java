package istic.m2.ila.firefighterapp.clientRabbitMqGeneric.messages;

import istic.m2.ila.firefighterapp.dto.DroneDTO;

public class SelectedDroneChangedMessage implements MessageBus
{
    public DroneDTO Drone;

    public SelectedDroneChangedMessage(DroneDTO drone)
    {
        Drone = drone;
    }
}
