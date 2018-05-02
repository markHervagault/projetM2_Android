package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.PhotoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by hakima on 4/30/18.
 */

public interface PhotoConsumer {

    @GET(Endpoints.ALLPHOTOS)
    Call<List<PhotoDTO>> getAllPhotos(@Header("Authorization") String header);

    @GET(Endpoints.PHOTOSWITHOUTPHOTOFORPOINT)
    Call<List<PhotoDTO>> getPhotosForPointWithoutPhoto(@Header("Authorization") String header,
                                           @Path("id") int index);

}
