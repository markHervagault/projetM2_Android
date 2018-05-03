package istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.map.Common.MapItem;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages.DeploiementMessage;

/**
 * Created by adou on 24/04/18.
 */

public class DeploiementManager extends MapItem
{
    //region Members

    private Map<Long, DeploiementDrawing> _deploiementById;

    //endregion

    //region Constructor

    public DeploiementManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _deploiementById = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    //endRegion

    //region Actions
    public synchronized void onCreateOrUpdateDeploiementDTOMessageEvent(DeploiementDTO message)
    {
        // Création du déploiement
       if(_deploiementById.containsKey(message.getId())) {
           _deploiementById.get(message.getId()).update(message);
        } else {
            _deploiementById.put(message.getId(), new DeploiementDrawing(message, _googleMap, _contextActivity));
        }
    }


    public synchronized void onDeleteDeploiementDTOMessageEvent(DeploiementDTO message)
    {
        if(_deploiementById.containsKey(message.getId())) {
            _deploiementById.get(message.getId()).delete();
            _deploiementById.remove(message.getId());
        }
    }

    //endregion

    //region EventSuscribing

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onDeploiementDTOMessageEvent(DeploiementMessage message) {
        if (message != null) {
            if (message.getSyncAction() == SyncAction.UPDATE) {
                onCreateOrUpdateDeploiementDTOMessageEvent(message.getDto());
            } else if (message.getSyncAction() == SyncAction.DELETE) {
                onDeleteDeploiementDTOMessageEvent(message.getDto());
            }
        }
    }

    //endregion
}