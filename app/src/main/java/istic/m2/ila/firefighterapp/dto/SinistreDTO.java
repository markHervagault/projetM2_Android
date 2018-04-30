package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;

/**
 * Created by hakima on 3/26/18.
 */

public class SinistreDTO implements Serializable, IDTO {
    private Long id;

    private ESinistre type;

    private GeoPositionDTO geoPosition;

    private TypeComposanteDTO composante;

    private Long interventionId;

    public Long getId() {
        return id;
    }

    @Override
    public String menuTitle() {
        if(getId() == null){
            return "Creation d'un sinistre";
        } else {
            return "Sinistre de composante " + getComposante().getLabel();
        }
    }

    @Override
    public String menuColor() {
        if(composante == null){
            return "#D0D0D0";
        } else {
            return composante.getCouleur();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ESinistre getType() {
        return type;
    }

    public void setType(ESinistre type) {
        this.type = type;
    }

    public GeoPositionDTO getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPositionDTO geoPosition) {
        this.geoPosition = geoPosition;
    }

    public TypeComposanteDTO getComposante() {
        return composante;
    }

    public void setComposante(TypeComposanteDTO composante) {
        this.composante = composante;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }


}
