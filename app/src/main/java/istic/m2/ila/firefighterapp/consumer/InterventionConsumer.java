package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.InterventionDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by hakima on 3/21/18.
 */

public interface InterventionConsumer extends Consumer {

    @POST(Endpoints.AUTHENTICATE)
    Call<String> createIntervention(@Header("Authorization") String token, @Body InterventionDto interventionDto);
}
