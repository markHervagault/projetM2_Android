package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

import istic.m2.ila.firefighterapp.dto.DroneDTO;

/**
 * Created by markh on 28/03/2018.
 */

public class DeclareDroneMessage implements MessageBus
{
    //region Properties

    private DroneDTO _droneDTO;
    public DroneDTO getDroneDTO() {
        return _droneDTO;
    }

    //endregion

    //region Constructor
    /**
     * Constructeur
     * @param droneDTO  le DTO du nouveau drone a déclarer
     */
    public DeclareDroneMessage(DroneDTO droneDTO) {
        _droneDTO = droneDTO;
    }

    //endregion
}
