package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/26/18.
 */

public class DeploiementCreateInterventionDTO {

    Long idVehicule;
    Long idTypeComposante;

    public Long getIdVehicule() {
        return idVehicule;
    }
    public void setIdVehicule(Long idVehicule) {
        this.idVehicule = idVehicule;
    }
    public Long getIdTypeComposante() {
        return idTypeComposante;
    }
    public void setIdTypeComposante(Long idTypeComposante) {
        this.idTypeComposante = idTypeComposante;
    }
}
