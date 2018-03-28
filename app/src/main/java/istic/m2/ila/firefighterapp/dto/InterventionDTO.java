package istic.m2.ila.firefighterapp.dto;

<<<<<<< Updated upstream
import java.io.Serializable;
=======
>>>>>>> Stashed changes
import java.util.Date;

/**
 * Created by hakima on 3/21/18.
 */

public class InterventionDTO implements Serializable{

    private Long id;

<<<<<<< Updated upstream
    private String nom;

=======
>>>>>>> Stashed changes
    private Date dateHeureCreation;

    private Date dateHeureFin;

    private Boolean fini;

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

<<<<<<< Updated upstream
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

=======
>>>>>>> Stashed changes
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
