package istic.m2.ila.firefighterapp.services;

import java.util.List;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;

/**
 * Created by hakima on 3/29/18.
 */

public interface IMapService {
    List<SinistreDTO> getTraitFromBouchon(final String token);
    List<TraitTopoDTO> getTraitTopo(final String token);
    List<TraitTopographiqueBouchonDTO> getTraitTopoFromBouchon(final String token, final double longitude, final double latitude, final double rayon);
    List<DroneDTO> getDrone(final String token);
    List<DeploiementDTO> getDeploy(final String token);
}
