package istic.m2.ila.firefighterapp.dto;

import java.io.Serializable;

/**
 * Created by bob on 25/04/18.
 */

public interface ITraitTopo extends Serializable{

    Long getId();
    GeoPositionDTO getPosition();
    TypeComposanteDTO getComposante();
    Enum getType();
}
