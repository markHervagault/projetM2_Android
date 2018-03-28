package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;

/**
 * Created by hakima on 3/26/18.
 */

public class GeoPositionDTO implements Serializable {
    private Double longitude;
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
