package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.ESinistre;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by hakima on 3/28/18.
 */

public interface TypeSinistreDTO {

    /** Récupération des types de sinistre. Ils donnent de l'information sur la topoplogie du sinistre correspondant*/
    @GET(Endpoints.TYPE_SINISTRE)
    Call<List<ESinistre>> getListTypeSinistre(@Header("Authorization")String token);

}
