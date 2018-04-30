package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/28/18.
 */

public interface TraitTopoConsumer {

    /** */
    @GET(Endpoints.TRAIT_TOPOS)
    Call<List<ETypeTraitTopo>> getAllTraitTopo(@Header("Authorization")String token);

    @POST(Endpoints.TRAIT_TOPOS)
    Call<TraitTopoDTO> createTraitTopo(@Header("Authorization") String token,
                                       @Body TraitTopoDTO traitTopoDTO);

    @PUT(Endpoints.TRAIT_TOPOS)
    Call<TraitTopoDTO> updateTraitTopo(@Header("Authorization")String token,
                                       @Body TraitTopoDTO traitTopoDTO);
    @DELETE(Endpoints.TRAIT_TOPOS_ID)
    Call<Void> deleteTraitTopo(@Header("Authorization")String token,
                               @Path("id") int id);

    @GET(Endpoints.TRAIT_TOPOS_ID)
    Call<TraitTopoDTO> getTraitTopoByID(@Header("Authorization") String token,
                                        @Path("id") int id);

}
