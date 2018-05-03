package istic.m2.ila.firefighterapp.dto;

import java.util.Set;

/**
 * Created by markh on 02/05/2018.
 */

public class PointMissionFullDTO {

    private Long id;

    private Long index;

    private Boolean action;

    private Double latitude;

    private Double longitude;

    private Set<PhotoSansPhotoDTO> photos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Set<PhotoSansPhotoDTO> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<PhotoSansPhotoDTO> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PointMissionFullDTO pointMissionDTO = (PointMissionFullDTO) o;
        if(pointMissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return getId()==pointMissionDTO.getId();
    }


    @Override
    public String toString() {
        return "PointMissionDTO{" +
                "id=" + id +
                ", index=" + index +
                ", action=" + action +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
