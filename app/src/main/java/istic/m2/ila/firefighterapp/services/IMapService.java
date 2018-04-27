package istic.m2.ila.firefighterapp.services;

import java.util.List;

import istic.m2.ila.firefighterapp.dto.CreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;

/**
 * Created by hakima on 3/29/18.
 */

public interface IMapService {
    List<SinistreDTO> getSinistre(final String token,Long id);
    List<TraitTopoDTO> getTraitTopo(final String token, Long id);
    List<TraitTopographiqueBouchonDTO> getTraitTopoFromBouchon(final String token,Long id, final double longitude, final double latitude, final double rayon);
    List<DroneDTO> getDrone(final String token);
    List<DeploiementDTO> getDeploy(final String token, Long id);
    InterventionDTO addIntervention(final String token, CreateInterventionDTO creatInterventionDTO);
    List<InterventionDTO> getInterventions(final String token);
    SinistreDTO addSinistre(final String token, SinistreDTO sinistre);
    TraitTopoDTO addTraitTopo(final String token, TraitTopoDTO traitTopoDTO);
    void majTraitTopo(final String token, TraitTopoDTO traitTopoDTO);
    void majSinistre(final String token, SinistreDTO sinistreDTO);
    void majDeploiement(String token, DeploiementDTO deploiementDTO);
    void removeTraitTopo(final String token, Long id);
    void removeSinistre(final String token, Long id);
    void sendDroneMission(final String token, MissionDTO missionDTO);
    MissionDTO getCurrentDroneMission(final String token, long id);
    List<TypeComposanteDTO> getTypeComposante(final String token);
    void deploiementToAction(final String token, final Long id);
    void deploiementToEngage(final String token, final Long id);
    void deploiementToDesengage(final String token, final Long id);
}
