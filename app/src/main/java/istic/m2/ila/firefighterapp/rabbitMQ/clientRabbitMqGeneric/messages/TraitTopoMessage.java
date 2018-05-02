package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages;

import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;

public class TraitTopoMessage extends MessageGeneric<TraitTopoDTO> {
    public TraitTopoMessage(TraitTopoDTO dto, SyncAction syncAction) {
        super(dto, syncAction);
    }
}
