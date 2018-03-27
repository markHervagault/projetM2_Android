package istic.m2.ila.firefighterapp.dto;

import java.time.ZonedDateTime;

/**
 * Created by hakima on 3/21/18.
 */

public class InterventionDTO {
    private Long id;

    private ZonedDateTime dateHeureCreation;

    private ZonedDateTime dateHeureFin;

    private Boolean fini;

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

    public Boolean isFini() {
        return fini;
    }

    public void setFini(Boolean fini) {
        this.fini = fini;
    }

    public AdresseDTO getAdresse() {
        return adresse;
    }

    public void setAdresse(AdresseDTO adresse) {
        this.adresse = adresse;
    }


}
