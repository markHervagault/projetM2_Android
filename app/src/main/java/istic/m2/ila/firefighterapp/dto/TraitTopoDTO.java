package istic.m2.ila.firefighterapp.dto;

import java.util.Objects;

/**
 * Created by hakima on 3/22/18.
 */

public class TraitTopoDTO {
    private Long id;

    private ETypeTraitTopo type;

    private Long interventionId;

    private GeoPositionDTO position;

    private TypeComposanteDTO composante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ETypeTraitTopo getType() {
        return type;
    }

    public void setType(ETypeTraitTopo type) {
        this.type = type;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }

    public GeoPositionDTO getPosition() {
        return position;
    }

    public void setPosition(GeoPositionDTO position) {
        this.position = position;
    }

    public TypeComposanteDTO getComposante() {
        return composante;
    }

    public void setComposante(TypeComposanteDTO composante) {
        this.composante = composante;
    }
}
