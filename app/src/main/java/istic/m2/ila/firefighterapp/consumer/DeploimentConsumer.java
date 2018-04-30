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
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/23/18.
 */
/** Consumer des services rest, en relation avec le deploiment, exposé par le serveur*/
public interface DeploimentConsumer {

    /** Mise en action d'un deploiement pr l'intervenant */
    @PUT(Endpoints.DEPLOIEMENT_ID_ACTION)
    Call<DeploiementDTO> setDeploiementToAction(@Header("Authorization") String token,
                                                      @Path("id") Long id);

    /** Désengagement d'un déploiement par l'intervenant*/
    @PUT(Endpoints.DEPLOIEMENT_ID_DESENGAGE)
    Call<DeploiementDTO> setDeploiementToDesengage(@Header("Authorization")String token,
                                                   @Path("id") Long id);

    /** engagement d'un déploiement par l'intervenant*/
    @PUT(Endpoints.DEPLOIEMENT_ID_ENGAGE)
    Call<DeploiementDTO> setDeploiementToEngage(@Header("Authorization")String token,
                                                   @Path("id") Long id);

    /** Validation d'un déploiement par un operateur CODIS*/
    @PUT(Endpoints.DEPLOIEMENT_ID_VALIDE)
    Call<DeploiementDTO> setDeploiementToValide(@Header("Authorization")String token,
                                                @Path("id") Long id);

    /** Refus d'un deploiement par un operateur CODIS*/
    @PUT(Endpoints.DEPLOIEMENT_ID_REFUSE)
    Call<DeploiementDTO> setDeploiementToRefuse(@Header("Authorization")String token,
                                                @Path("id") Long id);
    /** Récupération des demandes de déploiment de l'intervention */
    @GET(Endpoints.INTERVENTION_DEMANDE)
    Call<Void> getDeploimentRequest(@Header("Authorization") String token
            , @Path("id") String id
            , @Body DemandeDTO demandeDto);

    /** Création d'une demande de déploiment*/
    @POST(Endpoints.INTERVENTION_DEMANDE)
    Call<DemandeDTO> createDeploiment(@Header("Authorization") String token
            , @Path("id") Long id
            , @Body DemandeDTO demandeDto);

    /** Récuperation de la liste des déploiments de l'intervention*/
    @GET(Endpoints.INTERVENTION_DEPLOIMENT)
    Call<List<DeploiementDTO>> getListDeploimentById(@Header("Authorization") String token,
                                               @Path("id") String id );

    /** Récuperation de la liste des déploiments de l'intervention*/
    @PUT(Endpoints.DEPLOIEMENT_UPDATE)
    Call<DeploiementDTO> updateDeploiment(@Header("Authorization") String token,
                                                     @Body DeploiementDTO deploiementDTO);

}
