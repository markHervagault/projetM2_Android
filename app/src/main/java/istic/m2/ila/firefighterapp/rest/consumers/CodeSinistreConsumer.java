package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.CodeSinistreDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/28/18.
 */

public interface CodeSinistreConsumer {
    /** Récupération de tout les codes sinistre */
    @GET(Endpoints.CODE_SINISTRE)
    Call<List<CodeSinistreDTO>> getAllCodeSinistre(@Header("Authorization") String token );

    /** Récupération des sinistres par id*/
    @GET(Endpoints.CODE_SINISTRE_BY_ID)
    Call<CodeSinistreDTO> getCodeSinistreById(@Header("Authorization") String token,
                                                @Path("id") int id);

}
