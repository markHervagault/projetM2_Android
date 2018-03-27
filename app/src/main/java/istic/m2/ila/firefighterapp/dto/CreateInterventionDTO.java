package istic.m2.ila.firefighterapp.dto;

import java.util.Set;

/**
 * Created by hakima on 3/26/18.
 */

public class CreateInterventionDTO {
    private String nom;

    private AdresseDTO adresse;

    private Long codeSinistre;

    private Set<DeploiementCreateInterventionDTO> deploiements;


    public Set<DeploiementCreateInterventionDTO> getDeploiements() {
        return deploiements;
    }


    public void setDeploiements(Set<DeploiementCreateInterventionDTO> deploiements) {
        this.deploiements = deploiements;
    }

}
