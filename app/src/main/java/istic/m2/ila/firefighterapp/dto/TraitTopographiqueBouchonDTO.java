package istic.m2.ila.firefighterapp.dto;

/**
 * Created by hakima on 3/26/18.
 */

public class TraitTopographiqueBouchonDTO implements ITraitTopo {
    private Long id;

    private ETypeTraitTopographiqueBouchon type;

    private String label;

    private TypeComposanteDTO composante;

    private GeoPositionDTO geoPosition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ETypeTraitTopographiqueBouchon getType() {
        return type;
    }

    public void setType(ETypeTraitTopographiqueBouchon type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public GeoPositionDTO getPosition() {
        return geoPosition;
    }

    public TypeComposanteDTO getComposante() {
        return composante;
    }

    public void setComposante(TypeComposanteDTO composante) {
        this.composante = composante;
    }

    public GeoPositionDTO getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPositionDTO geoPosition) {
        this.geoPosition = geoPosition;
    }


}
