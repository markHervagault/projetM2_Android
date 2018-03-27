package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.LoginDTO;
import istic.m2.ila.firefighterapp.dto.TokenDTO;
import istic.m2.ila.firefighterapp.dto.UserDTO;
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
