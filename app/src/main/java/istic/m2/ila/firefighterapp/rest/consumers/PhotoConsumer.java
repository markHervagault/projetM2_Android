package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.PhotoDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by hakima on 4/30/18.
 */

public interface PhotoConsumer {

    @GET(Endpoints.PHOTOS)
    Call<List<PhotoDTO>> getPhoto(@Header("Authorization") String header);

}
