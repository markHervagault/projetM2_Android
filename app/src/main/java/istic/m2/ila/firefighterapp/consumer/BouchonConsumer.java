package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/28/18.
 */

public interface BouchonConsumer {
    /** Récupération des traits topographique du bouchon*/
    @GET(Endpoints.BOUCHON_TRAIT_TOPO_ID)
    Call<List<TraitTopographiqueBouchonDTO>> getTraitTopoFromBouchon(@Header("Authorization") String token,
                                                                     @Path("id") int id);
    /** Récupération des traits par localiation*/
    @GET(Endpoints.BOUCHON_TRAIT_TOPO_RAYON)
    Call<List<TraitTopographiqueBouchonDTO>> getTraitTopoByLocalisation(@Header("Authorization") String token,
                                                                        @Path("latitude") double latitude,
                                                                        @Path("longitude") double longitude,
                                                                        @Path("rayon") double rayon);

}
