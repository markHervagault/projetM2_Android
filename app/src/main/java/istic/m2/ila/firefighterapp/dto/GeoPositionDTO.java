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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoPositionDTO that = (GeoPositionDTO) o;

        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null)
            return false;
        return latitude != null ? latitude.equals(that.latitude) : that.latitude == null;
    }

    @Override
    public int hashCode() {
        int result = longitude != null ? longitude.hashCode() : 0;
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        return result;
    }
}
