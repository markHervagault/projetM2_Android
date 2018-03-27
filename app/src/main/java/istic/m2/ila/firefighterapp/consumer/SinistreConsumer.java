package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by hakima on 3/26/18.
 */
/** Consumer des services rest, en rapport avec les sinistres, exposé par le serveur*/
public interface SinistreConsumer {
    /** Ajout d'un sinistre à l'intervention*/
    @POST(Endpoints.SINISTRE)
    Call<SinistreDTO> addSinistre(@Header("Authorization")String token,
                                  @Body SinistreDTO sinistreDto);

    /** Récuperation de la liste des sinistres */
    @GET(Endpoints.TYPE_SINISTRE)
    Call<List<SinistreDTO>> getListSinistre(@Header("Authorization")String token);

}
