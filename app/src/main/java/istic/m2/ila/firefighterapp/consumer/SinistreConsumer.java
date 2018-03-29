package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/26/18.
 */
/** Consumer des services rest, en rapport avec les sinistres, exposé par le serveur*/
public interface SinistreConsumer {

    /** Création d'un nouveau sinistre*/
    @POST(Endpoints.SINISTRE)
    Call<SinistreDTO> createSinistre(@Header("Authorization") String token,
                                     @Body SinistreDTO sinistreDTO);

    /** Mise à jour d'un sinistre*/
    @PUT(Endpoints.SINISTRE)
    Call<SinistreDTO> updateSinistre(@Header("Authorization")String token,
                                    @Body SinistreDTO sinistreDTO);

    /** suppression d'un sinistre */
    @DELETE(Endpoints.SINISTRE_ID)
    Call<Void> deleteSinistre(@Header("Authorization")String token,
                              @Path("id") int id);

}
