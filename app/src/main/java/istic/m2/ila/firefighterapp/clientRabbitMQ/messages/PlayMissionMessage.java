package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

/**
 * Created by markh on 24/04/2018.
 */

public class PlayMissionMessage {

    private Long droneId;

    public PlayMissionMessage(Long droneId){
        this.droneId = droneId;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }
}
