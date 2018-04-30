package istic.m2.ila.firefighterapp.fragment.map.Drone.Managers;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.eventbus.drone.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.PauseMissionMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.PlayMissionMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.StopMissionMessage;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;
import istic.m2.ila.firefighterapp.fragment.map.Common.MapItem;
import istic.m2.ila.firefighterapp.fragment.map.Drone.Drawings.DroneDrawing;

public class DroneManager extends MapItem
{
    //region Members

    private Map<Long, DroneDrawing> _dronesById;
    private static final String TAG = "DRONE MANAGER";


    //endregion

    //region Properties

    public static final String SELECTED_DRONE_CHANGED_EVENT = "selectedDroneChangedEvent";
    private DroneDrawing _selectedDrone;
    private synchronized void SetSelectedDrone(DroneDrawing drone)
    {
        _selectedDrone = drone;
        _followingMode = true; //Following Mode automatique a la séléction
        _propertyChangeSupport.firePropertyChange(SELECTED_DRONE_CHANGED_EVENT, null, null);
    }
    public DroneDrawing getSelectedDrone() {
        return _selectedDrone;
    }

    //endregion

    //region Constructor

    public DroneManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);
        _dronesById = new HashMap<>();
        _authorizedCameraMove = false;
        _followingMode = false;

        _propertyChangeSupport = new PropertyChangeSupport(this);
        map.setOnCameraMoveStartedListener(_onCameraMoveListener);

        EventBus.getDefault().register(this);
    }

    //endRegion

    //region Camera Drag Listener

    private boolean _authorizedCameraMove;
    private boolean _followingMode;

    private GoogleMap.OnCameraMoveStartedListener _onCameraMoveListener = new GoogleMap.OnCameraMoveStartedListener()
    {
        @Override
        public synchronized void onCameraMoveStarted(int i)
        {
            Log.i(TAG, "OnCameraMoveStarted");
            if(_authorizedCameraMove)
                _authorizedCameraMove = false;
            else
                _followingMode = false;
        }
    };

    //endregion

    //region DroneCommandes

    public void SendPlayCommand()
    {
        if (_selectedDrone.getStatus() != EDroneStatut.EN_PAUSE)
            return;

        EventBus.getDefault().post(new PlayMissionMessage(_selectedDrone.getId()));
        Log.d(TAG, "Play command sent to : " + _selectedDrone.getId());
    }

    public void SendPauseCommand()
    {
        if (!(_selectedDrone.getStatus() == EDroneStatut.EN_MISSION || _selectedDrone.getStatus() == EDroneStatut.RETOUR_BASE))
            return;

        EventBus.getDefault().post(new PauseMissionMessage(_selectedDrone.getId()));
        Log.d(TAG, "Pause command sent to : " + _selectedDrone.getId());
    }

    public void SendStopCommand()
    {
        if(_selectedDrone.getStatus() == EDroneStatut.RETOUR_BASE)
            return;

        EventBus.getDefault().post(new StopMissionMessage(_selectedDrone.getId()));
        Log.d(TAG, "Stop command sent to : " + _selectedDrone.getId());
    }

    //endregion

    //region EventSubscribing

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onDeclareDroneMessageEvent(DeclareDroneMessage message)
    {
        //Ajout du drone seulement si il n'existe pas déjà
        if(!_dronesById.containsKey(message.getDroneDTO().getId()))
            _dronesById.put(message.getDroneDTO().getId(), new DroneDrawing(message.getDroneDTO(), _googleMap, _contextActivity));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onSelectedDroneChanged(SelectedDroneChangedMessage message)
    {
        Log.d(TAG, "SelectedDroneChangedMessage");
        if(!_dronesById.containsKey(message.Drone.getId()))
            return;

        if(_selectedDrone != null && !_selectedDrone.getDrone().equals(message.Drone)){
            _selectedDrone.UnSelect();
        }

        //Evènement
        SetSelectedDrone(_dronesById.get(message.Drone.getId()));
        _selectedDrone.Select();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onDroneInfoDTOMessageEvent(DroneInfosDTO message)
    {
        //Mise a jour du drone sur la map seulement si le drone existe deja en BDD
        if(_dronesById.containsKey(message.id_drone))
             _dronesById.get(message.id_drone).Update(message);

        //Vérification du drone séléctionné
        if(_selectedDrone != null && _selectedDrone.getId() != message.id_drone)
            return;

        //Vérification du mode de suivit
        if(!_followingMode)
            return;

        //Mise a jour de la caméra
        _authorizedCameraMove = true;
        float zoom = _googleMap.getCameraPosition().zoom;
        _googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(message.position.latitude, message.position.longitude), zoom));
    }

    //endregion

    //region PropertyChanged


    private PropertyChangeSupport _propertyChangeSupport;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeSupport.removePropertyChangeListener(listener);
    }

    //endregion
}