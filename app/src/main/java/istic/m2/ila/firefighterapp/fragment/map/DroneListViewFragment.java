package istic.m2.ila.firefighterapp.fragment.map;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.ItemListDroneAdapter;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DroneInfoUpdateMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;
import istic.m2.ila.firefighterapp.consumer.DroneConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import retrofit2.Response;

public class DroneListViewFragment extends Fragment {

    /**
     * Identifiant de la classe pour les logs
     */
    private String TAG = "DroneListViewFragment => ";

    /**
     * Contexte
     */
    public Context context;

    /**
     * RecyclerView de la liste des drones
     */
    RecyclerView listDroneRecycler;

    /**
     * Adapter de la liste view
     */
    RecyclerView.Adapter mAdapter;

    /**
     * Liste des drones
     */
    private List<DroneDTO> drones = new ArrayList<DroneDTO>();

    /**
     * Map de recherche de drone dans la liste pour être plus performant
     */
    private Map<Long, Integer> dronedIdPosition = new HashMap<Long, Integer>();

    /**
     * Drone sélectionné dans la liste, null si aucun drone est sélectionné
     */
    private DroneDTO droneSelected;

    View view;

    public DroneListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drone_list_view, container, false);
        this.view = view;
        this.context = view.getContext();

        // On initialise le fragment
        initDroneListFragment();

        // On bind la liste des drones avec le recyclerView
        getDronesFromBDD();

        return view;
    }

    /**
     * Initialise le recyclerView
     */
    protected void initDroneListFragment(){

        listDroneRecycler = view.findViewById(R.id.recycler_list_map);
        // On peuple notre RecyclerView
        mAdapter = new ItemListDroneAdapter(this.drones);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        listDroneRecycler.setItemAnimator(new DefaultItemAnimator());
        ((SimpleItemAnimator)listDroneRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        listDroneRecycler.setAdapter(mAdapter);
        listDroneRecycler.setLayoutManager(mLayoutManager);

    }


    /**
     * Appel le REST pour récupérer tout les drones présents en BDD
     */
    private void getDronesFromBDD(){
        AsyncTask.execute(new Runnable() {
            public void run() {

                // On peuple notre RecyclerView
                List<DroneDTO> droneList = new ArrayList<>();

                // Construction de notre appel REST
                RestTemplate restTemplate = RestTemplate.getInstance();
                DroneConsumer droneConsumer = restTemplate.builConsumer(DroneConsumer.class);

                Response<List<DroneDTO>> response = null;
                try {
                    // Récupération du token
                    String token = context.getSharedPreferences("user", context.MODE_PRIVATE)
                            .getString("token", "null");

                    // On récupère toutes les interventions du Serveur
                    response = droneConsumer.getListDrone(token).execute();
                    if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                        droneList = response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                addDronesInList(droneList);

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onUpdateDrone(DroneInfosDTO droneInfos) {
        if(dronedIdPosition.get(droneInfos.id_drone)!=null){
            final int positionInList = dronedIdPosition.get(droneInfos.id_drone);
            DroneDTO drone = drones.get(positionInList);
            drone.setBattery(droneInfos.battery_level);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyItemChanged(positionInList);
                }
            });
        }


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Méthode pour ajouter un drone à la liste et mettre à jour la map "dns" droneIdPosition
     * @param drone
     *      Drone à ajouter
     */
    private void addDroneInList(DroneDTO drone){
        if ( dronedIdPosition.get(drone.getId()) == null ) {
            dronedIdPosition.put(drone.getId(), drones.size());
            drones.add(drone);

            // Envoie du nouveau drone sur le bus
            NewDroneMessage message = new NewDroneMessage(drone.getId());
            Log.d(TAG, "================================================================ Envoi d'une donnee sur le bus : "+drone.getNom());
            EventBus.getDefault().post(message);
        }
    }

    /**
     * Méthode pour ajouter une liste de drones à notre liste de drones
     * @param drones
     *      Laa liste des drones à ajouter
     */
    private void addDronesInList(List<DroneDTO> drones) {
        for(DroneDTO drone: drones){
            addDroneInList(drone);
        }
    }
}
