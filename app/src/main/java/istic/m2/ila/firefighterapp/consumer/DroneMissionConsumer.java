package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/22/18.
 */
/** Consumer les services concernant les mission du drone  exposé par le serveur  */
public interface DroneMissionConsumer {

    /** Création d'une mission */
    @POST(Endpoints.MISSION)
    Call<MissionDTO> createMission(@Header("Authorization")String token,
                                   @Body MissionDTO droneMission);
    /** Modification d'une mission*/
    @PUT(Endpoints.MISSION_ID)
    Call<MissionDTO> updateMission(@Header("Authorization")String token,
                                        @Path("id")String id,
                                        @Body MissionDTO droneMissionDTO);

    /** Récuperation de la mission avec la liste des point    not sure */
    @GET(Endpoints.MISSION_ID)
    Call<MissionDTO> getMission(@Header("Authorization")String token,
                                     @Path("id")String id);

    /** Archiver une mission*/
    @DELETE(Endpoints.MISSION_ID)
    Call<MissionDTO> deleteMission(@Header("Authorization")String token,
                                        @Path("id")String id );


}


