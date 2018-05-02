package istic.m2.ila.firefighterapp.constantes;

/**
 * Created by hakima on 3/21/18.
 */
/* Les points d'entrées des services Rest exposés par le serveur */
public class Endpoints {

//    public static final String BASE = "http://10.0.2.2:8080/api/"; // Local
    public static final String BASE = "http://51.15.251.202:6002/api/"; // Serveur

    //RABBITMQ CREDENTIALS
    public static final String RABBITMQ_USERNAME = "guest";
    public static final String RABBITMQ_USERPASSWORD = "guest";
    public static final String RABBITMQ_SERVERADRESS= "51.15.251.202";
    public static final int RABBITMQ_SERVERPORT = 6005;
    public static final String RABBITMQ_EXCHANGE_NAME = "hub";
    public static final String RABBITMQ_ANDROID_UPDATE = "android.update.";
    public static final String RABBITMQ_ANDROID_DELETE = "android.delete.";
    public static final String RABBITMQ_EXCHANGE_TYPE = "topic";

    //RABBITMQ ENDPOINTS

    public static final String RABBITMQ_DRONE_COMMAND = "drone.command.";
    public static final String RABBITMQ_DRONE_INFO = "drone.info.";
    public static final String RABBITMQ_ALLDRONE_INFO = "drone.info.#";
    public static final String RABBITMQ_MISSION_DTO = "android.update.MissionDTO.";
    public static final String RABBITMQ_ALLMISSION_DTO = "android.update.MissionDTO.#";


    public static final String AUTHENTICATE = "authenticate";

    public static final String BOUCHON_TRAIT_TOPO_ID = "bouchon/trait-topographique/{id}";
    public static final String BOUCHON_TRAIT_TOPO_RAYON = "bouchon/trait-topographique/{latitude}/{longitude}/{rayon}";

    public static final String CODE_SINISTRE = "code-sinistre";
    public static final String CODE_SINISTRE_BY_ID = "code-sinistre/{id}";

    //public static final String DEPLOIEMENT_ID_ACTION = "api/deploiement/{id}/etat/SyncAction";
    public static final String DEPLOIEMENT_UPDATE = "deploiement";
    public static final String DEPLOIEMENT_ID_ACTION = "deploiement/{id}/etat/action";
    public static final String DEPLOIEMENT_ID_DESENGAGE = "deploiement/{id}/etat/desengage";
    public static final String DEPLOIEMENT_ID_ENGAGE = "deploiement/{id}/etat/engage";
    public static final String DEPLOIEMENT_ID_VALIDE = "deploiement/{id}/etat/valide";
    public static final String DEPLOIEMENT_ID_REFUSE = "deploiement/{id}/etat/refuse";

    public static final String DRONE = "drone";
    public static final String DRONE_DISPO = "drone/disponible";
    public static final String DRONE_ID = "drone/{id}";

    public static final String DEPLOIEMENT_DEMANDE = "deploiement/demande";
    public static final String INTERVENTION = "intervention";
    public static final String INTERVENTION_ENCOUR = "intervention/en-cours";
    public static final String INTERVENTION_ID = "intervention/{id}";
    public static final String INTERVENTION_DEMANDE = "deploiement";
    public static final String INTERVENTION_DEPLOIMENT = "intervention/{id}/deploiement";
    public static final String INTERVENTION_SINISTRE = "intervention/{id}/sinistre";
    public static final String INTERVENTION_TRAIT_TOPOGRAPHIQUE = "intervention/{id}/trait-topographique";

    public static final String MISSION = "mission";
    public static final String MISSION_ID = "mission/{id}";
    public static final String CURRENT_MISSION_ID = "drone/{id}/mission-active";

    public static final String SINISTRE = "sinistre";
    public static final String SINISTRE_ID = "sinistre/{id}";

    public static final String TYPE_COMPOSANTE = "type-composante";
    public static final String TYPE_COMPOSANTE_ID = "type-composante/{id}";

    public static final String TYPE_SINISTRE = "type-sinistre";

    public static final String TYPE_TRAIT_TOPO = "type-trait-topographique";

    public static final String TYPE_VEHICULE ="type-vehicule";

    public static final String VEHICULE = "vehicule";
    public static final String VEHICULE_DISPONIBLE = "vehicule/disponible";
    public static final String VEHICULE_DISPONIBLE_TYPE = "vehicule/disponible/type/{idType}";
    public static final String VEHICULE_ID = "vehicule/{id}";

    public static final String TRAIT_TOPOS ="trait-topos";
    public static final String TRAIT_TOPOS_ID = "trait-topos/{id}";
    public static final String PHOTOS = "photo";

}