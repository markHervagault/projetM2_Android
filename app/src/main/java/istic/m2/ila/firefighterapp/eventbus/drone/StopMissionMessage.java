package istic.m2.ila.firefighterapp.eventbus.drone;

/**
 * Created by markh on 24/04/2018.
 */

public class StopMissionMessage {

    private Long droneId;

    public StopMissionMessage(Long droneId){
        this.droneId = droneId;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }
}
