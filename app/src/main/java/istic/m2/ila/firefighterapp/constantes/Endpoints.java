package istic.m2.ila.firefighterapp.constantes;

/**
 * Created by hakima on 3/21/18.
 */
/* Les points d'entrées des services Rest exposés par le serveur */
public class Endpoints {

//    public static final String BASE = "http://10.0.2.2:8080/api/"; // Local
    public static final String BASE = "http://148.60.11.57:6002/api/"; // Serveur

    public static final String RABBITMQ = "amqp://guest:guest@148.60.11.57:6005/";

    //RABBITMQ CREDENTIALS
    public static final String RABBITMQ_USERNAME = "guest";
    public static final String RABBITMQ_USERPASSWORD = "guest";
    public static final String RABBITMQ_SERVERADRESS= "148.60.11.57";
    public static final int RABBITMQ_SERVERPORT = 6005;
    public static final String RABBITMQ_EXCHANGE_NAME = "hub";

    //RABBITMQ EXCHANGENAME


    public static final String AUTHENTICATE = "authenticate";

    public static final String BOUCHON_TRAIT_TOPO_ID = "bouchon/trait-topographique/{id}";
    public static final String BOUCHON_TRAIT_TOPO_RAYON = "bouchon/trait-topographique/{latitude}/{longitude}/{rayon}";

    public static final String CODE_SINISTRE = "code-sinistre";
    public static final String CODE_SINISTRE_BY_ID = "code-sinistre/{id}";

    public static final String DEPLOIEMENT_ID_ACTION = "api/deploiement/{id}/etat/action";
    public static final String DEPLOIEMENT_ID_DESENGAGE = "put /api/deploiement/{id}/etat/desengage";
    public static final String DEPLOIEMENT_ID_ENGAGE = "api/deploiement/{id}/etat/engage";
    public static final String DEPLOIEMENT_ID_VALIDE = "api/deploiement/{id}/etat/valide";
    public static final String DEPLOIEMENT_ID_REFUSE = "/api/deploiement/{id}/etat/refuse";

    public static final String DRONE = "drone";
    public static final String DRONE_DISPO = "drone/disponible";
    public static final String DRONE_ID = "drone/{id}";

    public static final String DEPLOIEMENT_DEMANDE = "deploiement/demande";
    public static final String INTERVENTION = "intervention";
    public static final String INTERVENTION_ENCOUR = "intervention/en-cours";
    public static final String INTERVENTION_ID = "intervention/{id}";
    public static final String INTERVENTION_DEMANDE = "intervention/{id}/demande";
    public static final String INTERVENTION_DEPLOIMENT = "intervention/{id}/deploiement";
    public static final String INTERVENTION_SINISTRE = "intervention/{id}/sinistre";
    public static final String INTERVENTION_TRAIT_TOPOGRAPHIQUE = "intervention/{id}/trait-topographique";

    public static final String MISSION = "mission";
    public static final String MISSION_ID = "mission/{id}";

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



}