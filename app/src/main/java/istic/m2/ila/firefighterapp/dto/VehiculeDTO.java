package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/26/18.
 */

public class VehiculeDTO {
    private Long id;

    private String label;

    private TypeVehiculeDTO type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TypeVehiculeDTO getType() {
        return type;
    }

    public void setType(TypeVehiculeDTO type) {
        this.type = type;
    }



}
