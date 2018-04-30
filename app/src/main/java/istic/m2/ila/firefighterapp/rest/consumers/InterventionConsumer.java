package istic.m2.ila.firefighterapp.rest.consumers;

import java.util.List;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import istic.m2.ila.firefighterapp.dto.CodeSinistreDTO;
import istic.m2.ila.firefighterapp.dto.CreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.DemandeDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionFullDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hakima on 3/21/18.
 */

/**
 * Consumer des services rest, en relation avec les interventions, exposé par le serveur
 */
public interface InterventionConsumer {

    @GET(Endpoints.CODE_SINISTRE)
    Call<List<CodeSinistreDTO>> getAllCodeSinistre(@Header("Authorization") String token);

    /**
     * Récuperation de toutes les interventions
     */
    @GET(Endpoints.INTERVENTION)
    Call<List<InterventionDTO>> getIntervention(@Header("Authorization") String token);

    /**
     * Récuperation d'une intervention par id
     */
    @GET(Endpoints.INTERVENTION_ID)
    Call<InterventionDTO> getInterventionById(@Header("Authorization") String token,
                                              @Path("id") Long id);

    /**
     * Fermer l'intervention en cours
     */
    @DELETE(Endpoints.INTERVENTION_ID)
    Call<InterventionDTO> closeIntervention(@Header("Authorization") String token
            , @Path("id") String id);

    /** Récupération de toutes les demandes de déploiements de toutes interventions*/
    @GET(Endpoints.DEPLOIEMENT_DEMANDE)
    Call<List<DemandeDTO>> getListDemandeDeploiement(@Header("Authorization")String token);

    /** Ajout d'une nouvelle intervention CODIS */
    @POST(Endpoints.INTERVENTION)
    Call<InterventionDTO> createIntervention(@Header("Authorization") String token,
                                             @Body CreateInterventionDTO createInterventionDTO);

    /** Mise à jour d'une intervention*/
    @PUT(Endpoints.INTERVENTION)
    Call<InterventionDTO> updateIntervention(@Header("Authorization") String token,
                                             @Body CreateInterventionDTO createInterventionDTO);

    /** Récupérations de toutes les interventions en cours*/
    @GET(Endpoints.INTERVENTION_ENCOUR)
    Call<List<InterventionDTO>> getListInterventionEnCours(@Header("Authorization") String token);

    /** Archivage d'une intervention*/
    @DELETE(Endpoints.INTERVENTION_ID)
    Call<DemandeDTO> deleteIntervention(@Header("Authorization") String token,
                                        @Path("id") Long id);

    /** Récupération d'une intervention avec tous les attributs*/
    @GET(Endpoints.INTERVENTION_ID)
    Call<InterventionFullDTO> getFullInterventionDetails(@Header("Authorization")String token,
                                                         @Path("id") Long id);

    /** Récupération de toutes les demandes de déploiements d'une intervention*/
    @GET(Endpoints.INTERVENTION_DEMANDE)
    Call<List<DemandeDTO>> getListDemandeDEploiement(@Header("Authorization")String token,
                                                     @Path("id") Long id );
    /** Ajout d'une demande de deploiement*/
    @POST(Endpoints.INTERVENTION_DEMANDE)
    Call<DeploiementDTO> addDemandeDeploiement(@Header("Authorization")String token,
                                               @Path("id") Long id,
                                               @Body DemandeDTO demandeDTO);

    /** Récupération de tous les déploiements d'une intervention*/
    @GET(Endpoints.INTERVENTION_DEPLOIMENT)
    Call<List<DeploiementDTO>> getListDeploiement(@Header("Authorization") String token,
                                                  @Path("id") Long id);
    /** Récupération de la liste des sinistre*/
    @GET(Endpoints.INTERVENTION_SINISTRE)
    Call<List<SinistreDTO>> getListSinistre(@Header("Authorization")String token,
                                            @Path("id")Long id);

    /** Récupération de tous les traits topographique d'une intervention*/
    @GET(Endpoints.INTERVENTION_TRAIT_TOPOGRAPHIQUE)
    Call<List<TraitTopoDTO>> getListTraitTopo(@Header("Authorization") String token,
                                              @Path("id") Long id);

    /** Ajouter des traits topo a une instervention*/
    @POST(Endpoints.INTERVENTION_TRAIT_TOPOGRAPHIQUE)
    Call<InterventionDTO> addTraitTopoToIntervention(@Header("Authorization") String token,
                                                     @Path("id") Long id,
                                                     @Body TraitTopoDTO traitTopoDTO);


}
