package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/26/18.
 */

public class DronePositionDTO {
    private Long index;
    private Boolean action;
    private Double latitude;
    private Double longitude;

    public DronePositionDTO(Long index, Boolean action, Double latitude, Double longitude){
        this.index = index;
        this.action = action;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
