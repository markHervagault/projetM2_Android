package istic.m2.ila.firefighterapp.fragment.map;

import android.content.Context;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.ItemListDroneAdapter;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.consumer.DroneConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;
import retrofit2.Response;

public class DroneListViewFragment extends Fragment {

    /**
     * Identifiant de la classe pour les logs
     */
    private static final String TAG = "DroneListView Fragment";

    /**
     * Contexte
     */
    public Context _context;

    /**
     * RecyclerView de la liste des _drones
     */
    RecyclerView _listDroneRecycler;

    /**
     * Adapter de la liste view
     */
    RecyclerView.Adapter _adapter;

    /**
     * Map de recherche de drone dans la liste pour être plus performant
     */
    private Map<Long, Integer> _dronesIndexById;

    /**
     * Liste des drones à afficher
     */
    private List<DroneDTO> _dronesList;

    /**
     * Drone sélectionné dans la liste, null si aucun drone est sélectionné
     */
    private DroneDTO _selectedDrone;

    private View view;

    public DroneListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Initialisation des collections
        _dronesIndexById = new HashMap<>();
        _dronesList = new ArrayList<>();

        //Récupération de la vue
        View view = inflater.inflate(R.layout.fragment_drone_list_view, container, false);
        if(view != null)
        {
            this.view = view;
            this._context = view.getContext();
        }
        else
        {
            Log.i(TAG, "Impossible de recuperer la vue 'R.layout.fragment_drone_list_view' -- onCreateView");
        }

        // On initialise le fragment
        InitDroneListFragment();

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // On bind la liste des _drones avec le recyclerView
        RefreshDroneList();
    }

    /**
     * Initialise le recyclerView
     */
    protected void InitDroneListFragment(){

        _listDroneRecycler = view.findViewById(R.id.recycler_list_map);
        // On peuple notre RecyclerView
        _adapter = new ItemListDroneAdapter(this._dronesList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        _listDroneRecycler.setItemAnimator(new DefaultItemAnimator());
        ((SimpleItemAnimator) _listDroneRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        _listDroneRecycler.setAdapter(_adapter);
        _listDroneRecycler.setLayoutManager(layoutManager);
    }


    /**
     * Appel le REST pour récupérer tout les _drones présents en BDD
     */
    private void RefreshDroneList(){
        AsyncTask.execute(new Runnable()
        {
            public void run()
            {
                // On vide les listes actuelles
                _dronesIndexById.clear();
                _dronesList.clear();

                // Construction de notre appel REST
                DroneConsumer droneConsumer = RestTemplate.getInstance().builConsumer(DroneConsumer.class);

                Response<List<DroneDTO>> response = null;
                try {
                    // Récupération du token
                    String token = _context.getSharedPreferences("user", _context.MODE_PRIVATE).getString("token", "null");

                    // On récupère tous les drones du serveur
                    response = droneConsumer.getListDrone(token).execute();
                    final List<DroneDTO> droneList;
                    if(response != null && response.code() == HttpURLConnection.HTTP_OK)
                        droneList = response.body();
                    else
                        droneList = new ArrayList<>();

                    //Mise a jour de la liste des drones
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            addDronesInList(droneList);
                            //Mise a jour graphique de la vue
                            _adapter.notifyDataSetChanged();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Méthode pour ajouter une liste de _drones à notre liste de _drones
     * @param drones La liste des _drones à ajouter
     */
    private void addDronesInList(List<DroneDTO> drones) {
        for(DroneDTO drone: drones){
            addDroneInList(drone);
        }
    }

    /**
     * Méthode pour ajouter un drone à la liste et mettre à jour la map "dns" droneIdPosition
     * @param drone Drone à ajouter
     */
    private void addDroneInList(DroneDTO drone)
    {
        //Drone existe déjà, return
        if (_dronesIndexById.get(drone.getId()) != null)
            return;

        //Insertion du nbouveau drone dans les listes
        _dronesIndexById.put(drone.getId(), _dronesList.size());
        _dronesList.add(drone);

        // Envoie du nouveau drone sur le bus pour mettre a jour la Map
        EventBus.getDefault().post(new DeclareDroneMessage(drone));

    }

    //region Bus Events

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onUpdateDrone(DroneInfosDTO droneInfos)
    {
        //Recherche du drone existant
        if(_dronesIndexById.get(droneInfos.id_drone)==null)
            return;

        //Récupération du drone depuis la liste
        final int index = _dronesIndexById.get(droneInfos.id_drone);
        DroneDTO drone = _dronesList.get(index);

        //Mise a jour du drone
        drone.Update(droneInfos);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _adapter.notifyItemChanged(index);
            }
        });
    }

    //endregion
}