package istic.m2.ila.firefighterapp.fragment.map;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.ItemListDroneAdapter;
import istic.m2.ila.firefighterapp.consumer.DroneConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import retrofit2.Response;

public class DroneListViewFragment extends Fragment {

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
        listDroneRecycler.setLayoutManager(mLayoutManager);
        listDroneRecycler.setItemAnimator(new DefaultItemAnimator());
        listDroneRecycler.setAdapter(mAdapter);

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

                drones.addAll(droneList);

            }
        });
    }
}
