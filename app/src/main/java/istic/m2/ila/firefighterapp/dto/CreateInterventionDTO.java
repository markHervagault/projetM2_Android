package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the create Intervention for CODIS.
 */
public class CreateInterventionDTO implements Serializable {

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

    @Override
    public String toString() {
        return "CreateInterventionDTO [nom=" + nom + ", adresse=" + adresse + ", idCodeSinistre=" + idCodeSinistre
                + ", deploiements=" + deploiements + "]";
    }


}
