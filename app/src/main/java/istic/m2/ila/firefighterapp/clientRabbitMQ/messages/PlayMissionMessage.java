package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

/**
 * Created by markh on 29/03/2018.
 */

public class PlayMissionMessage implements MessageBus {

    private long droneId;

    public PlayMissionMessage(long droneId){
        this.droneId = droneId;
    }

    public long getDroneId() {
        return droneId;
    }

    public void setDroneId(long droneId) {
        this.droneId = droneId;
    }
}
