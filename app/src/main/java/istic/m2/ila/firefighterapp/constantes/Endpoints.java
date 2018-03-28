package istic.m2.ila.firefighterapp.constantes;

/**
 * Created by hakima on 3/21/18.
 */
/* Les points d'entrées des services Rest exposés par le serveur */
public class Endpoints {

//    public static final String BASE = "http://10.0.2.2:8080/api/"; // Local
    public static final String BASE = "http://148.60.11.57:6002/api/"; // Serveur

    public static final String RABBITMQ = "amqp://guest:guest@148.60.11.57:6005/";

    public static final String AUTHENTICATE = "authenticate";

    public static final String MISSION = "mission";
    public static final String MISSION_ID = "mission/{id}";

    public static final String INTERVENTION = "intervention/en-cours";
    public static final String INTERVENTION_ID = "intervention/{id}";

    public static final String INTERVENTION_DEMANDE = "intervention/{id}/demande";
    public static final String INTERVENTION_DEPLOIMENT = "intervention/{id}/deploiement";
    public static final String INTERVENTION_TRAIT_TOPOGRAPHIQUE = "intervention/{id}/trait-topographique";

    public static final String SINISTRE = "sinistre";
    public static final String TYPE_SINISTRE = "type-sinistre";


    public static final String SIG_TRAIT_TOPOGRAPHIQUE = "sig/trait-topographique";
    public static final String TRAIT_TOPOGRAPHIQUE = "trait-topographique/{id}";
    public static final String COMPOSANTE = "type-composante";

    public static final String VEHICULE = "vehicule";
    public static final String VEHICULE_DISPONIBLE = "vehicule/disponible";
    public static final String VEHICULE_DISPONIBLE_TYPE = "vehicule/disponible/par_type?type={TYPE}";




}