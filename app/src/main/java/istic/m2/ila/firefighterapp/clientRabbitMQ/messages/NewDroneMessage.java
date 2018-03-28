package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

/**
 * Created by markh on 28/03/2018.
 */

public class NewDroneMessage implements MessageBus{

    private long droneId;

    /**
     * Constructeur
     * @param droneId
     *      id du nouveau drone
     */
    public NewDroneMessage(long droneId) {
        this.droneId = droneId;
    }

    public long getDroneId() {
        return droneId;
    }

    public void setDroneId(long droneId) {
        this.droneId = droneId;
    }

}
