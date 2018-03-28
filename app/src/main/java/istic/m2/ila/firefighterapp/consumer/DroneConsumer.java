package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/28/18.
 */

public interface DroneConsumer {
    /** Récuperer la liste des drones enregistré */
    @GET(Endpoints.DRONE)
    Call<List<DroneDTO>> getListDrone(@Header("Authorization") String token);

    /** Récupération de la liste des drones dispo*/
    @GET(Endpoints.DRONE_DISPO)
    Call<List<DroneDTO>> getListDroneDispo(@Header("Authorization") String token);

    /** Récupération d'un drone avec son id*/
    @GET(Endpoints.DRONE_ID)
    Call<DroneDTO> getDroneById(@Header("Authorization")String token,
                                @Path("id") int id);

}
