package istic.m2.ila.firefighterapp.services.impl;

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.consumer.BouchonConsumer;
import istic.m2.ila.firefighterapp.consumer.DroneConsumer;
import istic.m2.ila.firefighterapp.consumer.InterventionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
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
    public List<SinistreDTO> getTraitFromBouchon(final String token) {

        // Nos traits
        List<SinistreDTO> sinistre = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<SinistreDTO>> response = null;
        try {
            //TODO VDS idInterv
            response = consumer.getListSinistre(token,2).execute();

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
    public List<TraitTopoDTO> getTraitTopo(String token) {
        List<TraitTopoDTO> traits = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<TraitTopoDTO>> response = null;
        try {


            //TODO VDS idInterv
            response = consumer.getListTraitTopo(token,2).execute();

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
    public List<TraitTopographiqueBouchonDTO> getTraitTopoFromBouchon(final String token, final double longitude, final double latitude, final double rayon) {
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
    public List<DeploiementDTO> getDeploy(String token) {
        // Nos traits
        List<DeploiementDTO> deploy = new ArrayList<>();

        // Construction de notre appel REST
        RestTemplate restTemplate = RestTemplate.getInstance();
        InterventionConsumer consumer = restTemplate.builConsumer(InterventionConsumer.class);

        Response<List<DeploiementDTO>> response = null;
        try {


            //TODO VDS idInterv
            response = consumer.getListDeploiement(token,2).execute();

            if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                deploy = response.body();
                Log.i("MapActivity",  "Traits topo récupérés=" + deploy.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deploy;
    }

}
