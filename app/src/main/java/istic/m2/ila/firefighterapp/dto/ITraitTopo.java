package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;

/**
 * Created by bob on 25/04/18.
 */

public interface ITraitTopo extends Serializable, IDTO{

    Long getId();
    GeoPositionDTO getPosition();
    void setPosition(GeoPositionDTO position);
    TypeComposanteDTO getComposante();
    Enum getType();
}
