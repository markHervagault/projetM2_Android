package istic.m2.ila.firefighterapp.dto;

import java.util.List;

/**
 * Created by hakima on 3/22/18.
 */

public class DroneMissionDTO {

    private Long id;
    private Long droneId;
    private Long interventionId;
    private Boolean boucleFermee;
    private Long iterationNb;
    private List<DronePositionDTO> dronePositions;

    public DroneMissionDTO(Long id, Long droneId, Long interventionId, Boolean boucleFermee,
                           Long iterationNb, List<DronePositionDTO> dronePositions){
        this.id = id;
        this.droneId = droneId;
        this.interventionId = interventionId;
        this.boucleFermee = boucleFermee;
        this.iterationNb = iterationNb;
        this.dronePositions = dronePositions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }

    public Boolean getBoucleFermee() {
        return boucleFermee;
    }

    public void setBoucleFermee(Boolean boucleFermee) {
        this.boucleFermee = boucleFermee;
    }

    public Long getIterationNb() {
        return iterationNb;
    }

    public void setIterationNb(Long iterationNb) {
        this.iterationNb = iterationNb;
    }

    public List<DronePositionDTO> getDronePositions() {
        return dronePositions;
    }

    public void setDronePositions(List<DronePositionDTO> dronePositions) {
        this.dronePositions = dronePositions;
    }
}