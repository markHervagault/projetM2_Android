package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/26/18.
 */
public enum ETypeTraitTopographiqueBouchon {
    DANGER ("DANGER","Danger"),
    SENSIBLE("SENSIBLE","Sensible"),
    PEP("PEP","Prise d'eau pèrenne"),
    PENP("PENP","Prise d'eau non pèrenne"),
    PDR("PDR","Point de ravitaillement");

    private String code;

    private String description;

    ETypeTraitTopographiqueBouchon(String code, String description){
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code;
    }
}