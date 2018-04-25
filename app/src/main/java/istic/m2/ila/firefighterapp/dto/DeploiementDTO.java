package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hakima on 3/23/18.
 */

public class DeploiementDTO implements Serializable, iDTO {
    private Long id;

    private Date dateHeureDemande;

    private Date dateHeureValidation;

    private Date dateHeureEngagement;

    private Date dateHeureDesengagement;

    private EEtatDeploiement state;

    private Boolean presenceCRM;

    private GeoPositionDTO geoPosition;

    private VehiculeDTO vehicule;

    private TypeVehiculeDTO typeDemande;

    private TypeComposanteDTO composante;

    private Long interventionId;

    public Boolean getPresenceCRM() {
        return presenceCRM;
    }

    public GeoPositionDTO getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPositionDTO geoPosition) {
        this.geoPosition = geoPosition;
    }

    public VehiculeDTO getVehicule() {
        return vehicule;
    }

    public void setVehicule(VehiculeDTO vehicule) {
        this.vehicule = vehicule;
    }

    public TypeVehiculeDTO getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(TypeVehiculeDTO typeDemande) {
        this.typeDemande = typeDemande;
    }

    public TypeComposanteDTO getComposante() {
        return composante;
    }

    public void setComposante(TypeComposanteDTO composante) {
        this.composante = composante;
    }

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

    public Date getDateHeureValidation() {
        return dateHeureValidation;
    }

    public void setDateHeureValidation(Date dateHeureValidation) {
        this.dateHeureValidation = dateHeureValidation;
    }

    public Date getDateHeureEngagement() {
        return dateHeureEngagement;
    }

    public void setDateHeureEngagement(Date dateHeureEngagement) {
        this.dateHeureEngagement = dateHeureEngagement;
    }

    public Date getDateHeureDesengagement() {
        return dateHeureDesengagement;
    }

    public void setDateHeureDesengagement(Date dateHeureDesengagement) {
        this.dateHeureDesengagement = dateHeureDesengagement;
    }

    public EEtatDeploiement getState() {
        return state;
    }

    public void setState(EEtatDeploiement state) {
        this.state = state;
    }

    public Boolean isPresenceCRM() {
        return presenceCRM;
    }

    public void setPresenceCRM(Boolean presenceCRM) {
        this.presenceCRM = presenceCRM;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }

}
