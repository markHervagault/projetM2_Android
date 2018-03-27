package istic.m2.ila.firefighterapp.dto;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hakima on 3/26/18.
 */

public class InterventionFullDTO {
    private Long id;

    private ZonedDateTime dateHeureCreation;

    private ZonedDateTime dateHeureFin;

    private Boolean fini;

    private Set<MissionDTO> missions = new HashSet<>();

    private Set<DeploiementDTO> deploiements = new HashSet<>();

    private Set<TraitTopoDTO> traits = new HashSet<>();

    private Set<SinistreDTO> sinistres = new HashSet<>();

    private AdresseDTO adresse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateHeureCreation() {
        return dateHeureCreation;
    }

    public void setDateHeureCreation(ZonedDateTime dateHeureCreation) {
        this.dateHeureCreation = dateHeureCreation;
    }

    public ZonedDateTime getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(ZonedDateTime dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }

    public Boolean getFini() {
        return fini;
    }

    public void setFini(Boolean fini) {
        this.fini = fini;
    }

    public Set<MissionDTO> getMissions() {
        return missions;
    }

    public void setMissions(Set<MissionDTO> missions) {
        this.missions = missions;
    }

    public Set<DeploiementDTO> getDeploiements() {
        return deploiements;
    }

    public void setDeploiements(Set<DeploiementDTO> deploiements) {
        this.deploiements = deploiements;
    }

    public Set<TraitTopoDTO> getTraits() {
        return traits;
    }

    public void setTraits(Set<TraitTopoDTO> traits) {
        this.traits = traits;
    }

    public Set<SinistreDTO> getSinistres() {
        return sinistres;
    }

    public void setSinistres(Set<SinistreDTO> sinistres) {
        this.sinistres = sinistres;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }
}
