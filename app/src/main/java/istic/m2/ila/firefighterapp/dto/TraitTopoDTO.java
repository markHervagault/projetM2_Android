package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/22/18.
 */

public class TraitTopoDTO implements ITraitTopo, IRabbitDTO {
    private Long id;

    private ETypeTraitTopo type;

    private Long interventionId;

    private GeoPositionDTO position;

    private TypeComposanteDTO composante;

    public Long getId() {
        return id;
    }

    @Override
    public String menuTitle() {
        if (getId() == null) {
            return "Creation d'un Trait topographique";
        } else {
            return "Trait topographique de composante " + getComposante().getLabel();
        }
    }

    @Override
    public String menuColor() {
        if (composante == null) {
            return "#D0D0D0";
        } else {
            return composante.getCouleur();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ETypeTraitTopo getType() {
        return type;
    }

    public void setType(ETypeTraitTopo type) {
        this.type = type;
    }

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
        this.interventionId = interventionId;
    }

    public GeoPositionDTO getPosition() {
        return position;
    }

    public void setPosition(GeoPositionDTO position) {
        this.position = position;
    }

    public TypeComposanteDTO getComposante() {
        return composante;
    }

    public void setComposante(TypeComposanteDTO composante) {
        this.composante = composante;
    }
}
