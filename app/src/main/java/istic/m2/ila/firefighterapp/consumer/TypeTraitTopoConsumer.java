package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by hakima on 3/28/18.
 */

public interface TypeTraitTopoConsumer {
    /** Récupération de tous les types de traits topographiques qu'il est possible d'ajoutersur la SITAC.*/
    @GET(Endpoints.TYPE_TRAIT_TOPO)
    Call<ETypeTraitTopo> getListTypeTraitTopo(@Header("Authorization")String token);
}
