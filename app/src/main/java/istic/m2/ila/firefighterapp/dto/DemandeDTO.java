package istic.m2.ila.firefighterapp.dto;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Created by hakima on 3/23/18.
 */

public class DemandeDTO {
    private Long id;
    private Date dateHeureDemande;
    private EEtatDeploiement state;

    private Boolean presenceCRM;

    private GeoPositionDTO geoPosition;
    private TypeComposanteDTO composante;
    private TypeVehiculeDTO typeDemande;
    private Long interventionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateHeureDemande() {
        return dateHeureDemande;
    }

    public void setDateHeureDemande(Date dateHeureDemande) {
        this.dateHeureDemande = dateHeureDemande;
    }

    public EEtatDeploiement getState() {
        return state;
    }

    public void setState(EEtatDeploiement state) {
        this.state = state;
    }

    public Boolean getPresenceCRM() {
        return presenceCRM;
    }

    public void setPresenceCRM(Boolean presenceCRM) {
        this.presenceCRM = presenceCRM;
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

    public TypeVehiculeDTO getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(TypeVehiculeDTO typeDemande) {
        this.typeDemande = typeDemande;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }
}
