package istic.m2.ila.firefighterapp.consumer;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.DemandeDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/23/18.
 */
/** Consumer des services rest, en relation avec le deploiment, exposé par le serveur*/
public interface DeploimentConsumer {

    /** Récupération des demandes de déploiment de l'intervention */
    @GET(Endpoints.INTERVENTION_DEMANDE)
    Call<Void> getDeploimentRequest(@Header("Authorization") String token
            , @Path("id") String id
            , @Body DemandeDTO demandeDto);

    /** Création d'une demande de déploiment*/
    @POST(Endpoints.INTERVENTION_DEMANDE)
    Call<DemandeDTO> createDeploiment(@Header("Authorization") String token
            , @Path("id") String id
            , @Body DemandeDTO demandeDto);

    /** Récuperation de la liste des déploiments de l'intervention*/
    @GET(Endpoints.INTERVENTION_DEPLOIMENT)
    Call<List<DeploiementDTO>> getListDeploimentById(@Header("Authorization") String token,
                                                     @Path("id") String id );
}
