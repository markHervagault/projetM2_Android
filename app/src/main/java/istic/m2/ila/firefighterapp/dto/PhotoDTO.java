package istic.m2.ila.firefighterapp.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Created by hakima on 3/26/18.
 */

public class PhotoDTO {
    private Long id;

    private ZonedDateTime dateHeure;

    private Long iteration;

    private Long pointMissionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateHeure() {
        return dateHeure;
    }

    public void setDateHeure(ZonedDateTime dateHeure) {
        this.dateHeure = dateHeure;
    }

    public Long getIteration() {
        return iteration;
    }

    public void setIteration(Long iteration) {
        this.iteration = iteration;
    }

    public Long getPointMissionId() {
        return pointMissionId;
    }

    public void setPointMissionId(Long pointMissionId) {
        this.pointMissionId = pointMissionId;
    }



}
