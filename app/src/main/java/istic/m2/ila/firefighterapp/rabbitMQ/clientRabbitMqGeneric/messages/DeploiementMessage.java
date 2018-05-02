package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;

public class DeploiementMessage extends MessageGeneric<DeploiementDTO> {
    public DeploiementMessage(DeploiementDTO dto, SyncAction syncAction) {
        super(dto, syncAction);
    }
}
