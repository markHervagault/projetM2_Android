package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.CreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/21/18.
 */
/** Consumer des services rest, en relation avec les interventions, exposé par le serveur */
public interface InterventionConsumer {

    /**Création d'une intervention */
    @POST(Endpoints.INTERVENTION)
    Call<InterventionDTO> createIntervention(@Header("Authorization") String token
                 , @Body CreateInterventionDTO interventionDto);

    /**Récuperation de toutes les interventions */
    @GET(Endpoints.INTERVENTION)
    Call<List<InterventionDTO>> getIntervention(@Header("Authorization") String token);

    /** Récuperation d'une intervention par id*/
    @GET(Endpoints.INTERVENTION_ID)
    Call<InterventionDTO> getInterventionById(@Header("Authorization") String token,
                                              @Path("id")String id);

    /** Fermer l'intervention en cours */
    @DELETE(Endpoints.INTERVENTION_ID)
    Call<InterventionDTO> closeIntervention(@Header("Authorization") String token
                                            , @Path("id") String id);










}
