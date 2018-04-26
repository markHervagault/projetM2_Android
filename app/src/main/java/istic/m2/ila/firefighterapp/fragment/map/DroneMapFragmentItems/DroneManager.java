package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PauseMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PlayMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;

public class DroneManager extends MapItem
{
    //region Members

    private Map<Long, DroneDrawing> _dronesById;

    private static final String TAG = "DRONE MANAGER";

    //endregion

    //region Properties

    private DroneDrawing _selectedDrone;
    public DroneDrawing getSelectedDrone() {
        return _selectedDrone;
    }

    //endregion

    //region Constructor

    public DroneManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _dronesById = new HashMap<>();
        Log.i(TAG, "Subscribed to the bus");
        EventBus.getDefault().register(this);
    }

    //endRegion

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

        EventBus.getDefault().post(new PauseMissionMessage(_selectedDrone.getId()));
        Log.d(TAG, "Stop command sent to : " + _selectedDrone.getId());
    }

    //endregion

    //region EventSubscribing

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onDeclareDroneMessageEvent(DeclareDroneMessage message)
    {
        Log.i(TAG, "Recu : déclaredronemessage");
        //Ajout du drone seulement si il n'existe pas déjà
        if(!_dronesById.containsKey(message.getDroneDTO().getId()))
            _dronesById.put(message.getDroneDTO().getId(), new DroneDrawing(message.getDroneDTO(), _googleMap, _contextActivity));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onSelectedDroneChanged(SelectedDroneChangedMessage message)
    {
        if(!_dronesById.containsKey(message.Drone.getId()))
            return;

        if(_selectedDrone != null)
            _selectedDrone.UnSelect();

        _selectedDrone = _dronesById.get(message.Drone.getId());
        _selectedDrone.Select();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public synchronized void onDroneInfoDTOMessageEvent(DroneInfosDTO message)
    {
        //Mise a jour du drone sur la map seulement si le drone existe deja en BDD
        if(_dronesById.containsKey(message.id_drone))
        {
            DroneDrawing drone = _dronesById.get(message.id_drone);
            drone.Update(message);
        }
    }

    //endregion
}