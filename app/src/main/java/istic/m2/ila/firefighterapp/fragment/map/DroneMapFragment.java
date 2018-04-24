package istic.m2.ila.firefighterapp.fragment.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DroneInfoUpdateMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PauseMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PlayMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.StopMissionMessage;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.DroneManager;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.DroneMissionDrawing;
import istic.m2.ila.firefighterapp.fragment.map.droneMapModeFragment.DroneCommandFragment;

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

    public DroneMapFragment() {
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
        Log.i(TAG, "OnCreateView");
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
                InitMenu();
            }
        });

        return _view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _MapView.onDestroy();
        EventBus.getDefault().unregister(this);
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

    //region INTERFACE

    // Interface Members
    private boolean isPathClosed;
    private boolean isAddButtonEnabled;

    /**
     * Initialise le menu de controles déroulants avec des listeners
     */
    private void InitMenu() {
        isPathClosed = false;
        isAddButtonEnabled = false;

        FloatingActionButton fabRemoveSelectedMarker = _view.findViewById(R.id.fabMenu_removeSelectedMarker);
        final FloatingActionButton fabOpenClose = _view.findViewById(R.id.fabMenu_openClosePath);
        final FloatingActionButton fabAddMarker = _view.findViewById(R.id.fabMenu_addMarker);
        FloatingActionButton fabZone = _view.findViewById(R.id.fabMenu_zone);
        FloatingActionButton fabSendMission = _view.findViewById(R.id.fab_menu2_send);

        //Remove Button Listener
        fabRemoveSelectedMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "RemoveButton Cliked");
                // Gestion de l'événement click pour le bouton flottant
                _missionDrawing.DeleteSelectedMarker();
                RefreshOpenClosePathButtonStatus();
            }
        });

        //Add Button Listener
        fabAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddButtonEnabled) // Activation du mode
                {
                    //Desactivation des boutons
                    ChangeMenuButtonsStatus(false);

                    //Activation du bouton d'ajout ?? Utile??
                    fabAddMarker.setEnabled(true);

                    // Couleurs du focus
                    fabAddMarker.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabSelectedNormal));
                    fabAddMarker.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabSelectedPressed));
                    fabAddMarker.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabSelectedRipple));

                    isAddButtonEnabled = true;
                    _missionDrawing.setEditMode(true);
                } else //Desactivation du mode
                {
                    //Reactivation des boutons
                    ChangeMenuButtonsStatus(true);

                    // Couleurs de l'unfocus
                    fabAddMarker.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabDefaultNormal));
                    fabAddMarker.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabDefaultPressed));
                    fabAddMarker.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabDefaultRipple));

                    isAddButtonEnabled = false;
                    _missionDrawing.setEditMode(false);
                }
            }
        });

        fabOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si le trajet est fermé
                if (_missionDrawing.isPathClosed()) {
                    //Changement de style du bouton
                    fabOpenClose.setColorNormal(getResources().getColor(R.color.colorMenuFabDefaultNormal));
                    fabOpenClose.setColorPressed(getResources().getColor(R.color.colorMenuFabDefaultPressed));
                    fabOpenClose.setColorRipple(getResources().getColor(R.color.colorMenuFabDefaultRipple));
                    fabOpenClose.setImageResource(R.drawable.openloop);

                    _missionDrawing.setPathClosed(false);
                } else {
                    fabOpenClose.setColorNormal(getResources().getColor(R.color.colorMenuFabSelectedNormal));
                    fabOpenClose.setColorPressed(getResources().getColor(R.color.colorMenuFabSelectedPressed));
                    fabOpenClose.setColorRipple(getResources().getColor(R.color.colorMenuFabSelectedRipple));
                    fabOpenClose.setImageResource(R.drawable.closedloop);

                    _missionDrawing.setPathClosed(true);
                }
            }
        });

        fabSendMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _missionDrawing.SendMission(1l, 1l, 0);
            }
        });

        //Ajout des boutons à la liste pour la désactivation
        floatingActionButtonList = new ArrayList<>();

        floatingActionButtonList.add(fabRemoveSelectedMarker);
        floatingActionButtonList.add(fabAddMarker);
        floatingActionButtonList.add(fabZone);

        //Abonnement aux changements de markers
        _missionDrawing.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if(evt.getPropertyName().equals("markersCount"))
                    RefreshOpenClosePathButtonStatus();
            }
        });
    }

    private List<FloatingActionButton> floatingActionButtonList;

    private void ChangeMenuButtonsStatus(Boolean enabled) {
        for (FloatingActionButton button : floatingActionButtonList)
            button.setEnabled(enabled);
    }

    private void RefreshOpenClosePathButtonStatus() {
        FloatingActionButton openCloseButton = _view.findViewById(R.id.fabMenu_openClosePath);
        if (_missionDrawing.getMarkersCount() < 3) {
            openCloseButton.setEnabled(false);
        } else {
            openCloseButton.setEnabled(true);
        }
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
        RefreshOpenClosePathButtonStatus();
    }

    //endregion

    //region Events
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onSelectedDroneChangedMessageEvent(final SelectedDroneChangedMessage message)
    {
        _droneManager.setSelectedDrone(message.Drone);
        _droneCommandFrag = new DroneCommandFragment();

        Runnable playRunnable = new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new PlayMissionMessage(message.Drone.getId()));
            }
        };
        Runnable pauseRunnable = new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new PauseMissionMessage(message.Drone.getId()));
            }
        };
        _droneCommandFrag.setActionForPlayPauseButton(playRunnable, pauseRunnable);

        Runnable stopRunnable = new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new StopMissionMessage(message.Drone.getId()));
            }
        };
        _droneCommandFrag.setActionForStopButton(stopRunnable);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onDroneChangedMessageEvent(final DroneInfosDTO droneInfosDTO)
    {
        if(_droneManager.getSelectedDrone().getId() == droneInfosDTO.id_drone){
            _droneCommandFrag.changeDroneStatut(EDroneStatut.valueOf(droneInfosDTO.status));
        }
    }
    //endregion
}
