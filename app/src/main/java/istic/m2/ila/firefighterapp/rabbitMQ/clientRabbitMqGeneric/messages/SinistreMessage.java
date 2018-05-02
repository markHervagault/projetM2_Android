package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages;

import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;

public class SinistreMessage extends MessageGeneric<SinistreDTO> {
    public SinistreMessage(SinistreDTO dto, SyncAction syncAction) {
        super(dto, syncAction);
    }
}
