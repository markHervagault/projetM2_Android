package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/28/18.
 */

public interface TypeComposanteConsumer {

    /** Récupération de tous les types de composante avec leurs attributs.*/
    @GET(Endpoints.TYPE_COMPOSANTE)
    Call<List<TypeComposanteDTO>> getListTypeComposante(@Header("Authorization") String token);

    /** Récupération d'un type de composante avec son id*/
    @GET(Endpoints.TYPE_COMPOSANTE_ID)
    Call<TypeComposanteDTO> getTypeComposanteById(@Header("Authorization") String token,
                                                  @Path("id") int id);


}
