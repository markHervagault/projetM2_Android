package istic.m2.ila.firefighterapp.fragment.map.Drone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.fragment.map.Drone.Managers.DroneManager;
import istic.m2.ila.firefighterapp.fragment.map.Drone.Managers.MissionManager;
import istic.m2.ila.firefighterapp.fragment.map.Drone.Mode.DroneCommandFragment;
import istic.m2.ila.firefighterapp.fragment.map.Drone.Mode.DroneMissionFragment;

public class DroneMapFragment extends Fragment {
    //Global Members
    private final String TAG = "DroneMap Fragment";

    // Init Members
    MapView _MapView;
    private GoogleMap _googleMap;
    private View _view;

    private MissionManager _missionManager;
    private DroneManager _droneManager;

    private DroneCommandFragment _droneCommandFrag;
    private DroneMissionFragment _droneMissionFrag;

    public DroneMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.i(TAG, "OnCreateView");
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_drone_map, container, false);

        InitUI();

        final Button button = _view.findViewById(R.id.toggleView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MapActivity) getActivity()).toggleView();
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
                ((MapActivity) getActivity()).initMap(_googleMap);

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

    //region Init

    /**
     * Initialise la Carte avec les listeners d'évènements
     */
    private void InitMap()
    {
        //Instanciation des managers
        _droneManager = new DroneManager(_googleMap, getActivity());
        _missionManager = new MissionManager(_googleMap, getActivity());

        _droneManager.addPropertyChangeListener(_droneListener);
        _missionManager.addPropertyChangeListener(_missionListener);
    }

    private void InitUI()
    {
        //Fragments UI
        _droneCommandFrag = new DroneCommandFragment();
        _droneMissionFrag = new DroneMissionFragment();

        //Inflating UI
        getFragmentManager().beginTransaction().replace(R.id.droneCommandFragmentLayout, _droneCommandFrag).commit();
        getFragmentManager().beginTransaction().replace(R.id.droneMissionFragmentLayout, _droneMissionFrag).commit();

        //No View At start
        getFragmentManager().beginTransaction().hide(_droneMissionFrag).commit();
        getFragmentManager().beginTransaction().hide(_droneCommandFrag).commit();
    }


    //DroneManager
    private PropertyChangeListener _droneListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent)
        {
            switch (propertyChangeEvent.getPropertyName())
            {
                case DroneManager.SELECTED_DRONE_CHANGED_EVENT:
                    break;
            }
        }
    };

    //Mission Manager Listener
    private PropertyChangeListener _missionListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent)
        {
            switch (propertyChangeEvent.getPropertyName())
            {
                case MissionManager.MISSIONMODE_CHANGED_EVENT_NAME: //Changement de mode de mission
                    UpdateMissionMode();
                    break;
                case MissionManager.EDITMODE_CHANGED_EVENT_NAME: //Prévenir l'UI changement de bouton
                    if(_missionManager.isEditMode())
                        _droneMissionFrag.SetAddMode();
                    else
                        _droneMissionFrag.UnSetAddMode();
                    break;
                case MissionManager.POINTCOUNT_CHANGED_EVENT_NAME: //Prévenir l'UI, chanement de bouton
                    _droneMissionFrag.RefreshOpenPathButton(_missionManager.getPointsCount());
                    break;
                case MissionManager.SENDMISSION_CHANGED_EVENT_NAME: //Prévenir l'UI, changement de bouton
                    if(_missionManager.canSendMission())
                        _droneMissionFrag.setCanSendMission();
                    else
                        _droneMissionFrag.unSetCanSendButton();

                default:
                    break;
            }
        }
    };

    //endregion

    //region UI Listeners

    //region DroneCommand Listeners

    private View.OnClickListener _onButtonPlayPauseListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(view.getTag().equals(DroneCommandFragment.PLAY_TAG))
                _droneManager.SendPlayCommand();
            else
                _droneManager.SendPauseCommand();
        }
    };

    private View.OnClickListener _onButtonStopListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            _droneManager.SendStopCommand();
        }
    };

    //endregion

    //region DroneMissionListener

    //TODO : Abonnement sur selectedMarker, et modification du bouton (enabled ou pas)

    private View.OnClickListener _onRemoveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            _missionManager.RemoveSelectedMarker();
        }
    };

    private View.OnClickListener _onOpenCLoseButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            if(_missionManager.isPathCLosed())
            {
                _missionManager.OpenPath();
                _droneMissionFrag.OpenPath();
            }
            else {
                _missionManager.ClosePath();
                _droneMissionFrag.ClosePath();
            }
        }
    };

    private View.OnClickListener _onAddModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            if(_missionManager.isEditMode())
            {
                _missionManager.setEditMode(false);
                _droneMissionFrag.UnSetAddMode();
            }
            else
            {
                _missionManager.setEditMode(true);
                _droneMissionFrag.SetAddMode();
            }
        }
    };

    private View.OnClickListener _onZoneButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            //TODO : Implémenter zone? maybe?
        }
    };

    private View.OnClickListener _onSendMissionButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            _missionManager.SendMission();
        }
    };

    //endregion

    //endregion

    //region Methods

    private void UpdateMissionMode()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (_missionManager.getMissionMode())
        {
            case FOLLOW:
                _droneCommandFrag.Reset(_droneManager.getSelectedDrone().getStatus());
                transaction.show(_droneCommandFrag);
                if(!_droneMissionFrag.isHidden())
                    transaction.hide(_droneMissionFrag);

                _droneCommandFrag.buttonPlayPause.setOnClickListener(_onButtonPlayPauseListener);
                _droneCommandFrag.buttonStop.setOnClickListener(_onButtonStopListener);

                break;

            case EDIT:
                //_droneMissionFrag.Reset(); //TODO : implémenter
                transaction.show(_droneMissionFrag);
                if(!_droneCommandFrag.isHidden())
                    transaction.hide(_droneCommandFrag);

                _droneMissionFrag._removeSelectedMarkerButton.setOnClickListener(_onRemoveButtonListener);
                _droneMissionFrag._openCloseClosePathButton.setOnClickListener(_onOpenCLoseButtonListener);
                _droneMissionFrag._addModeButton.setOnClickListener(_onAddModeButtonListener);
                _droneMissionFrag._zoneButton.setOnClickListener(_onZoneButtonListener);
                _droneMissionFrag._sendMissionButton.setOnClickListener(_onSendMissionButtonListener);

                break;

            case NONE:
                if(!_droneCommandFrag.isHidden())
                    transaction.hide(_droneCommandFrag);
                if(!_droneMissionFrag.isHidden())
                    transaction.hide((_droneMissionFrag));
                break;
        }

        transaction.commit();
    }

    //endregion
}
