package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages;

import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;

/**
 * Created by bob on 02/05/18.
 */

public class InterventionMessage extends MessageGeneric<InterventionDTO> {
    public InterventionMessage(InterventionDTO dto, SyncAction syncAction) {
        super(dto, syncAction);
    }
}
