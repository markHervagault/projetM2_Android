package istic.m2.ila.firefighterapp.dto;

import java.util.Set;

/**
 * Created by hakima on 3/26/18.
 */

public class MissionDTO {
    private Long id;

    private Integer nbIteration;

    private Boolean boucleFermee;

    //private Date dateHeureCreation;

    //private Date dateHeureFin;

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

    /*public Date getDateHeureCreation() {
        return dateHeureCreation;
    }

    public void setDateHeureCreation(Date dateHeureCreation) {
        this.dateHeureCreation = dateHeureCreation;
    }

    public Date getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(Date dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }*/

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

    public String toString()
    {
        String s = "idMission = " + id + ", nbIteration = "+ nbIteration + ", boucleFermé = "+ boucleFermee
            + ", droneId = " + droneId ;
        if(getDronePositions() != null){
            s = s + " points mission = [ ";
            for(PointMissionDTO dto : getDronePositions())
                s = s + dto.getIndex() + ", ";
            s = s + "] ";
        }


        return s.toString();
    }
}
