package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

/**
 * Created by markh on 29/03/2018.
 */

public class StopMissionMessage implements MessageBus {

    private long droneId;

    public StopMissionMessage(long droneId){
        this.droneId = droneId;
    }

    public long getDroneId() {
        return droneId;
    }

    public void setDroneId(long droneId) {
        this.droneId = droneId;
    }
}
