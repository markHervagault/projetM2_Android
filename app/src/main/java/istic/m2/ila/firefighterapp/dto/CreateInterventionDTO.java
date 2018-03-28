package istic.m2.ila.firefighterapp.dto;

import java.util.Set;

/**
 * Created by hakima on 3/26/18.
 */

public class CreateInterventionDTO {
    private String nom;

    private AdresseDTO adresse;

    private Long idCodeSinistre;

    private Set<DeploiementCreateInterventionDTO> deploiements;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }

    public Long getIdCodeSinistre() {
        return idCodeSinistre;
    }

    public void setIdCodeSinistre(Long idCodeSinistre) {
        this.idCodeSinistre = idCodeSinistre;
    }

    public Set<DeploiementCreateInterventionDTO> getDeploiements() {
        return deploiements;
    }

    public void setDeploiements(Set<DeploiementCreateInterventionDTO> deploiements) {
        this.deploiements = deploiements;
    }
}
