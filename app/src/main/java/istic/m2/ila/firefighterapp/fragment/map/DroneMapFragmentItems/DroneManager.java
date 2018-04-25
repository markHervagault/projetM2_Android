package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DeclareDroneMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;

public class DroneManager extends MapItem
{
    //region Members

    private Map<Long, DroneDrawing> _dronesById;

    private static final String TAG = "DRONE MANAGER";

    private DroneDTO selectedDrone;

    public DroneDTO getSelectedDrone() {
        return selectedDrone;
    }

    public void setSelectedDrone(DroneDTO selectedDrone) {
        this.selectedDrone = selectedDrone;
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
    public synchronized void onDroneInfoDTOMessageEvent(DroneInfosDTO message)
    {
        //Mise a jour du drone sur la map seulement si le drone existe deja en BDD
        if(_dronesById.containsKey(message.id_drone))
            _dronesById.get(message.id_drone).UpdatePosition(message.position.latitude, message.position.longitude, message.orientation.yaw);
    }

    //endregion
}