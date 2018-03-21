package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.LoginDto;
import istic.m2.ila.firefighterapp.dto.UserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by hakima on 3/21/18.
 */

public interface LoginConsumer extends Consumer {

    @POST(Endpoints.AUTHENTICATE)
    Call<UserDto> getToken(@Body LoginDto loginDto);




}
