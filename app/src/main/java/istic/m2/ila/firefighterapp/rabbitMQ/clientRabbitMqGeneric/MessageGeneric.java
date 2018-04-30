package istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric;

public class MessageGeneric<T> {

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
}
