package istic.m2.ila.firefighterapp.dto;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by hakima on 3/26/18.
 */

public class InterventionFullDTO {

    private Long id;

    private String nom;

    private Date dateHeureCreation;

    private Date dateHeureFin;

    private Boolean fini;

    private Set<MissionDTO> missions = new HashSet<>();

    private Set<DeploiementDTO> deploiements = new HashSet<>();

    private Set<TraitTopoDTO> traits = new HashSet<>();

    private Set<SinistreDTO> sinistres = new HashSet<>();

    private AdresseDTO adresse;

    private CodeSinistreDTO codeSinistre;

    public CodeSinistreDTO getCodeSinistre() {
        return codeSinistre;
    }

    public void setCodeSinistre(CodeSinistreDTO codeSinistre) {
        this.codeSinistre = codeSinistre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateHeureCreation() {
        return dateHeureCreation;
    }

    public void setDateHeureCreation(Date dateHeureCreation) {
        this.dateHeureCreation = dateHeureCreation;
    }

    public Date getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(Date dateHeureFin) {
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
