package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.InterventionDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by hakima on 3/21/18.
 */

public interface InterventionConsumer extends Consumer {

    @POST(Endpoints.INTERVENTION_ID)
    Call<String> createIntervention(@Header("Authorization") String token
                 , @Body InterventionDto interventionDto);

    @PUT(Endpoints.INTERVENTION_ID)
    Call<String> updateIntervention(@Header("Authorization") String token
                 , @Body InterventionDto interventionDto);

}
