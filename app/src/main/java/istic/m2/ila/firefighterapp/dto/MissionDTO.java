package istic.m2.ila.firefighterapp.dto;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Created by hakima on 3/26/18.
 */

public class MissionDTO {
    private Long id;

    private Integer nbIteration;

    private Boolean boucleFermee;

    private ZonedDateTime dateHeureCreation;

    private ZonedDateTime dateHeureFin;

    private Long interventionId;

    private Boolean archive;

    private Long droneId;

    private Set<PointMissionDTO> dronePositions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbIteration() {
        return nbIteration;
    }

    public void setNbIteration(Integer nbIteration) {
        this.nbIteration = nbIteration;
    }

    public Boolean getBoucleFermee() {
        return boucleFermee;
    }

    public void setBoucleFermee(Boolean boucleFermee) {
        this.boucleFermee = boucleFermee;
    }

    public ZonedDateTime getDateHeureCreation() {
        return dateHeureCreation;
    }

    public void setDateHeureCreation(ZonedDateTime dateHeureCreation) {
        this.dateHeureCreation = dateHeureCreation;
    }

    public ZonedDateTime getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(ZonedDateTime dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public Set<PointMissionDTO> getDronePositions() {
        return dronePositions;
    }

    public void setDronePositions(Set<PointMissionDTO> dronePositions) {
        this.dronePositions = dronePositions;
    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }



}
