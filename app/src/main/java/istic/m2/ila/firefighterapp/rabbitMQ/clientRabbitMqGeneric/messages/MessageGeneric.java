package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.IDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;

public abstract class MessageGeneric<T> {

    private T dto;
    private SyncAction syncAction;

    public MessageGeneric(T dto, SyncAction syncAction) {
        this.dto = dto;
        this.syncAction = syncAction;
    }

    public T getDto() {
        return dto;
    }

    public SyncAction getSyncAction() {
        return syncAction;
    }

    public static <A extends IDTO, E extends MessageGeneric> E getInstance(A dto, SyncAction syncAction) {
        if (dto instanceof TraitTopoDTO) {
            return (E) new TraitTopoMessage((TraitTopoDTO) dto, syncAction);
        } else if (dto instanceof SinistreDTO) {
            return (E) new SinistreMessage((SinistreDTO) dto, syncAction);
        } else if (dto instanceof DeploiementDTO) {
            return (E) new DeploiementMessage((DeploiementDTO) dto, syncAction);
        } else {
            return null;
        }
    }

}
