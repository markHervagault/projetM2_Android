package istic.m2.ila.firefighterapp.fragment.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PauseMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PlayMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.StopMissionMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.DroneManager;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.DroneMissionDrawing;
import istic.m2.ila.firefighterapp.fragment.map.droneMapModeFragment.DroneCommandFragment;
import istic.m2.ila.firefighterapp.fragment.map.droneMapModeFragment.DroneEditMissionFragment;

public class DroneMapFragment extends Fragment {
    //region  INIT

    //Global Members
    private final String TAG = "DroneMap Fragment";

    // Init Members
    MapView _MapView;
    private GoogleMap _googleMap;
    private View _view;

    private DroneMissionDrawing _missionDrawing;
    private DroneManager _droneManager;

    private DroneCommandFragment _droneCommandFrag;
    private DroneEditMissionFragment _droneEditMissionFrag;

    public DroneMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.i(TAG, "OnCreateView");
        EventBus.getDefault().register(this);
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_drone_map, container, false);
        final Button button = _view.findViewById(R.id.toggleView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((NewMapActivity) getActivity()).toggleView();
            }
        });

        _MapView = (MapView) _view.findViewById(R.id.mapView);
        _MapView.onCreate(savedInstanceState);

        _MapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        _MapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                _googleMap = mMap;
                ((NewMapActivity) getActivity()).initMap(_googleMap);

                //Initialise la carte et le menu
                InitMap();
            }
        });

        return _view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        _MapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        _MapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        _MapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        _MapView.onPause();
    }

    //endregion

    //region MAP

    /**
     * Initialise la Carte avec les listeners d'évènements
     */
    private void InitMap()
    {
        _droneManager = new DroneManager(_googleMap, getActivity());
        _missionDrawing = new DroneMissionDrawing(_googleMap, getActivity());
    }

    //endregion

    //region Events
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSelectedDroneChangedMessageEvent(final SelectedDroneChangedMessage message)
    {
        DroneDTO droneSelected = message.Drone;

        _droneManager.setSelectedDrone(droneSelected);

        // si le drone est en mission, on passe en mode commande
        if( droneSelected.getStatut()!=null
                && droneSelected.getStatut()!=EDroneStatut.DECONNECTE
                && droneSelected.getStatut()!=EDroneStatut.DISPONIBLE )
        {
            _droneCommandFrag = new DroneCommandFragment();
            _droneCommandFrag.setSelectedDroneId(message.Drone.getId());

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentCalqueDrone, _droneCommandFrag)
                    .commit();
        }
        // sinon on passe en mode édition d'une mission
        else
        {
            _droneEditMissionFrag = new DroneEditMissionFragment();
            _droneEditMissionFrag.setMissionDrawing(_missionDrawing);

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentCalqueDrone, _droneEditMissionFrag)
                    .commit();
        }

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onDroneChangedMessageEvent(final DroneInfosDTO droneInfosDTO)
    {
        if(_droneManager.getSelectedDrone()!=null &&
                _droneManager.getSelectedDrone().getId() == droneInfosDTO.id_drone){
            _droneCommandFrag.changeDroneStatut(EDroneStatut.valueOf(droneInfosDTO.status));
        }
    }
    //endregion
}
