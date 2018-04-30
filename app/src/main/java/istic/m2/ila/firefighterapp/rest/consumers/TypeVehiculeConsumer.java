package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Created by hakima on 3/28/18.
 */

public interface TypeVehiculeConsumer {

    /** Récupération des types de véhicules et de leurs attributs.*/
    @GET(Endpoints.TYPE_VEHICULE)
    Call<List<TypeVehiculeDTO>> getListTypeVehicules(@Header("Authorization")String token);
}
