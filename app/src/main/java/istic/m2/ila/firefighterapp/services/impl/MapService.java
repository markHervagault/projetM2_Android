package istic.m2.ila.firefighterapp.services.impl;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.consumer.BouchonConsumer;
import istic.m2.ila.firefighterapp.consumer.DroneConsumer;
import istic.m2.ila.firefighterapp.consumer.DroneMissionConsumer;
import istic.m2.ila.firefighterapp.consumer.InterventionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.consumer.SinistreConsumer;
import istic.m2.ila.firefighterapp.consumer.TraitTopoConsumer;
import istic.m2.ila.firefighterapp.consumer.TypeComposanteConsumer;
import istic.m2.ila.firefighterapp.dto.CreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.services.IMapService;
import retrofit2.Response;

/**
 * Created by hakima on 3/29/18.
 */

public class MapService implements IMapService {
    private static final String TAG = "IMAP-SERVICE";
    private static IMapService instance;

    private MapService(){

    }

    public static IMapService getInstance() {
        if(instance == null) {
            instance = new MapService();
        }
        return instance;
    }

    @Override
    public List<SinistreDTO> getSinistre(final String token, Long id) {

        // Nos traits
        List<SinistreDTO> sinistre = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<SinistreDTO>> response = null;
        try {
            //TODO VDS idInterv
            response = consumer.getListSinistre(token, id).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                sinistre = response.body();
                Log.i(TAG,  "Traits topo récupérés=" + sinistre.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sinistre;
    }

    @Override
    public List<TraitTopoDTO> getTraitTopo(String token, Long id) {
        List<TraitTopoDTO> traits = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<TraitTopoDTO>> response = null;
        try {

            response = consumer.getListTraitTopo(token,id).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                traits = response.body();
                Log.i(TAG,  "Traits topo récupérés=" + traits.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return traits;
    }

    @Override
    public List<TraitTopographiqueBouchonDTO> getTraitTopoFromBouchon(final String token, Long id, final double longitude, final double latitude, final double rayon) {
        // Nos traits
        List<TraitTopographiqueBouchonDTO> traits = null;
        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        BouchonConsumer bouchonConsumer = restTemplate.builConsumer(BouchonConsumer.class);

        Response<List<TraitTopographiqueBouchonDTO>> response = null;
        try {
            // Récupération du token
            response = bouchonConsumer.getTraitTopoByLocalisation(
                    token, latitude, longitude, rayon).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                traits = response.body();
                Log.i(TAG,  "Traits topo récupérés=" + traits.size());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return traits;
    }

    @Override
    public List<DroneDTO> getDrone(String token) {
        // On peuple notre RecyclerView
        List<DroneDTO> droneList = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        DroneConsumer droneConsumer = restTemplate.builConsumer(DroneConsumer.class);

        Response<List<DroneDTO>> response = null;
        try {
            // Récupération du token


            // On récupère toutes les interventions du Serveur
            response = droneConsumer.getListDrone(token).execute();
            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                droneList = response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return droneList;
    }

    @Override
    public List<DeploiementDTO> getDeploy(String token, Long id) {
        // Nos traits
        List<DeploiementDTO> deploy = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<DeploiementDTO>> response = null;
        try {


            //TODO VDS idInterv
            response = consumer.getListDeploiement(token, id).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                deploy = response.body();
                Log.i("MapActivity",  "Traits topo récupérés=" + deploy.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deploy;
    }

    @Override
    public InterventionDTO addIntervention(final String token, CreateInterventionDTO createInterventionDTO) {

        // Nos intervention
        InterventionDTO intervention = new InterventionDTO();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<InterventionDTO> response = null;
        try {
            response = consumer.createIntervention(token,createInterventionDTO).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                intervention = response.body();
                Log.i(TAG,  "intervention crée" + intervention.getNom());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return intervention;
    }

    @Override
    public List<InterventionDTO> getInterventions(final String token) {

        // Nos intervention
        List<InterventionDTO> interventions = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<InterventionDTO>> response = null;
        try {
            response = consumer.getListInterventionEnCours(token).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                interventions = response.body();
                Log.i(TAG,  "Intervention récupéré : " + interventions.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return interventions;

    }

    @Override
    public TraitTopoDTO addTraitTopo(final String token, TraitTopoDTO traitTopoDTO) {

        // Nos traits topo
        TraitTopoDTO resultTraitTopo = new TraitTopoDTO();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        TraitTopoConsumer consumer = restTemplate.builConsumer(TraitTopoConsumer.class);

        Response<TraitTopoDTO> response = null;
        try {
            response = consumer.createTraitTopo(token,traitTopoDTO).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                resultTraitTopo = response.body();
                Log.i(TAG,  "intervention crée" + resultTraitTopo.getInterventionId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultTraitTopo;
    }

    @Override
    public void removeTraitTopo(String token,  Long id) {
        RestTemplate restTemplate = RestTemplate.getInstance();
        TraitTopoConsumer consumer = restTemplate.builConsumer(TraitTopoConsumer.class);

        Response<Void> response = null;
        try {
            response = consumer.deleteTraitTopo(token,id).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG,  "trait topo delete" + id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSinistre(String token,  Long id) {
        RestTemplate restTemplate = RestTemplate.getInstance();
        SinistreConsumer consumer = restTemplate.builConsumer(SinistreConsumer.class);

        Response<Void> response = null;
        try {
            response = consumer.deleteSinistre(token,id).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG,  "sinistre delete" + id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SinistreDTO addSinistre(final String token, SinistreDTO sinistre) {

        // Nos traits topo
        SinistreDTO sinistreDTO = new SinistreDTO();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        SinistreConsumer consumer = restTemplate.builConsumer(SinistreConsumer.class);

        Response<SinistreDTO> response = null;
        try {
            response = consumer.createSinistre(token,sinistre).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                sinistreDTO = response.body();
                Log.i(TAG,  "intervention crée" + sinistreDTO.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sinistreDTO;
    }

    @Override
    public void sendDroneMission(final String token, MissionDTO missionDTO)
    {
        RestTemplate restTemplate = RestTemplate.getInstance();
        DroneMissionConsumer dmc = restTemplate.builConsumer(DroneMissionConsumer.class);
        try
        {
            retrofit2.Response<MissionDTO> response = dmc.createMission(token, missionDTO).execute();
            if(response.code() != HttpURLConnection.HTTP_OK)
            {
                Log.i("Mission", Integer.toString(response.code()));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public MissionDTO getCurrentDroneMission(final String token, long droneId)
    {
        RestTemplate restTemplate = RestTemplate.getInstance();
        DroneMissionConsumer consumer = restTemplate.builConsumer(DroneMissionConsumer.class);

        MissionDTO currentMission = new MissionDTO();
        Response<MissionDTO> response = null;
        try{
            response = consumer.getMission(token, droneId).execute();
            if(response != null && response.code() == HttpURLConnection.HTTP_OK)
            {
                currentMission = response.body();
                Log.i(TAG,  "Mission récupérée pour le drone : " + droneId);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return currentMission;
    }

    @Override
    public List<TypeComposanteDTO> getTypeComposante(final String token)
    {
        RestTemplate restTemplate = RestTemplate.getInstance();
        TypeComposanteConsumer consumer = restTemplate.builConsumer(TypeComposanteConsumer.class);

        List<TypeComposanteDTO> composantes = new ArrayList<>();
        Response<List<TypeComposanteDTO>> response = null;
        try{
            response = consumer.getListTypeTraitTopo(token).execute();
            if(response != null && response.code() == HttpURLConnection.HTTP_OK)
            {
                composantes = response.body();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return composantes;
    }


}
