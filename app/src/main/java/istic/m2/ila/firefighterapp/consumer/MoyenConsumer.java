package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/22/18.
 */
/** Consumer des services rest en lien avec les traits topographiques,vehicule,composante, exposé par le serveur */
public interface MoyenConsumer {

    /** Ajout d'un trait topographique à une intervention*/
    @POST(Endpoints.INTERVENTION_TRAIT_TOPOGRAPHIQUE)
    Call<TraitTopoDTO> addTraitTopo(@Header("Authorization")String token,
                                    @Path("id")String id,
                                    @Body TraitTopoDTO traitTopoDto);

    /** Récuperation de la liste des traits topographiques d'une intervention*/
    @GET(Endpoints.INTERVENTION_TRAIT_TOPOGRAPHIQUE)  //a rechecker
    Call<List<TraitTopoDTO>> getTraitTopo(@Header("Authorization") String token,
                                         @Path("id")String id);

    /** Supprimer trait topographique*/
    @PUT(Endpoints.TRAIT_TOPOGRAPHIQUE)
    Call<Void> deleteTraitTopo(@Header("Authorization")String token,
                               @Path("id")String id);

    /** Récuperation de la liste des composantes*/
    @GET(Endpoints.COMPOSANTE)
    Call<List<TypeComposanteDTO>> getListComposantes(@Header("Authorization")String token);

    /** Récuperer la liste de tous les vehicules */
    @GET(Endpoints.VEHICULE)
    Call<VehiculeDTO> getAllVehicule(@Header("Authorization")String token);

    /** Récuperation de tous les vehicules disponible*/
    @GET(Endpoints.VEHICULE_DISPONIBLE)
    Call<List<VehiculeDTO>> getVehiculeDispo(@Header("Authorization")String token);

    /** Récuperation de tous les vehicules disponible par type */
    @GET(Endpoints.VEHICULE_DISPONIBLE_TYPE) // a rechecker
    Call<List<VehiculeDTO>> getVehiculeDispoByType(@Header("Authorization")String token);
}
