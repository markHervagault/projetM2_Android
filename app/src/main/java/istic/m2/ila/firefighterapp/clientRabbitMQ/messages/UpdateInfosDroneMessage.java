package istic.m2.ila.firefighterapp.clientRabbitMQ.messages;

/**
 * Created by markh on 28/03/2018.
 */

public class UpdateInfosDroneMessage implements MessageBus {

    private long droneId;
    private double longitude;
    private double latitude;
    private int battery;

    public UpdateInfosDroneMessage(long droneId, double longitude, double latitude,
                                   int battery){
        this.droneId = droneId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.battery = battery;
    }

    public long getDroneId() {
        return droneId;
    }

    public void setDroneId(long droneId) {
        this.droneId = droneId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }
}

  /*
{
      "timestamp" : 1521799522,
        "id_drone": 2,
        "id_mission": 0,
        "battery_level" : 23,
        "localisation": {
        "latitude": 25.34,
        "altitude": 10.0,
        "longitude": -56.23
        },
        "orientation": {
        "roll": 0.343,
        "pitch": 8.780,
        "yaw": -9.343
        },
        "velocity": {
        "x": -0.343,
        "y": 1.780,
        "z": -0.343
        }
        }*/