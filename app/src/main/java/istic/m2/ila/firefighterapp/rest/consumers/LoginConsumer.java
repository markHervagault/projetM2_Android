package istic.m2.ila.firefighterapp.rest.consumers;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.LoginDTO;
import istic.m2.ila.firefighterapp.dto.TokenDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hakima on 3/21/18.
 */

public interface LoginConsumer {

    @POST(Endpoints.AUTHENTICATE)
    Call<TokenDTO> login(@Body LoginDTO loginDto);




}
