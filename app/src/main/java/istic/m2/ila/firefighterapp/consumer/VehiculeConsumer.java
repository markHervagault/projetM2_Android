package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/28/18.
 */

public interface VehiculeConsumer {

    /** Récupération de tous les véhicules.*/
    @GET(Endpoints.VEHICULE)
    Call<List<VehiculeDTO>>  getListVehicule(@Header("Authorization") String token);

    /** Création d'un nouveau véhicule.*/
    @POST(Endpoints.VEHICULE)
    Call<VehiculeDTO> createVehicule(@Header("Authorization") String token,
                                     @Body VehiculeDTO vehiculeDTO);

    /** Mise à jour d'un véhicule existant. Si le véhicule transmis n'est pas déjà présent en base alors il est créé*/
    @PUT(Endpoints.VEHICULE)
    Call<VehiculeDTO> updateVehicule(@Header("Authorization") String token,
                                     @Body VehiculeDTO vehiculeDTO);

    /** Récupération de tous les véhicules disponibles.*/
    @GET(Endpoints.VEHICULE_DISPONIBLE)
    Call<List<VehiculeDTO>> getListVehiculeDispo(@Header("Authorization") String token);

    /** Récupération de tous les véhicules disponibles par type. Le filtrage par type est géré avec l'identifiant technique du type*/
    @GET(Endpoints.VEHICULE_DISPONIBLE_TYPE)
    Call<List<VehiculeDTO>> getListVehiculeDispoByType(@Header("Authorization")String token,
                                                       @Path("idType") int idType);

    /** Récupère un véhicule par id*/
    @GET(Endpoints.VEHICULE_ID)
    Call<VehiculeDTO> getVehiculeByID(@Header("Authorization") String token,
                                      @Path("id") int id);

}
