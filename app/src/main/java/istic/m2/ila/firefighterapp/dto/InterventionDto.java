package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/21/18.
 */

public class InterventionDto {
    private String adressIntervention;
    private int codeSinistre;
    private enum etat {ongoing,closed};
    private MoyenIntervention[] moyenIntervention;

    public String getAdressIntervention() {
        return adressIntervention;
    }

    public void setAdressIntervention(String adressIntervention) {
        this.adressIntervention = adressIntervention;
    }

    public int getCodeSinistre() {
        return codeSinistre;
    }

    public void setCodeSinistre(int codeSinistre) {
        this.codeSinistre = codeSinistre;
    }

    public MoyenIntervention[] getMoyenIntervention() {
        return moyenIntervention;
    }

    public void setMoyenIntervention(MoyenIntervention[] moyenIntervention) {
        this.moyenIntervention = moyenIntervention;
    }
}
