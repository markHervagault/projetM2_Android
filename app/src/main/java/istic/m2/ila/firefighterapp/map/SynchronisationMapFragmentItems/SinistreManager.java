package istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.map.Common.MapItem;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.messages.SinistreMessage;

/**
 * Created by adou on 24/04/18.
 */

public class SinistreManager extends MapItem
{
    //region Members

    private Map<Long, SinistreDrawing> _sinistresById;

    //endregion

    //region Constructor

    public SinistreManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _sinistresById = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    //endRegion

    //region Actions
    public synchronized void onCreateOrUpdateSinistreDTOMessageEvent(SinistreDTO message)
    {
        // Création du sinistre
        if (_sinistresById.containsKey(message.getId())) {
            _sinistresById.get(message.getId()).update(message);
        } else {
            _sinistresById.put(message.getId(), new SinistreDrawing(message, _googleMap, _contextActivity));
        }
    }

    public synchronized void onDeleteSinistreDTOMessageEvent(SinistreDTO message)
    {
        if(_sinistresById.containsKey(message.getId())) {
            _sinistresById.get(message.getId()).delete();
            _sinistresById.remove(message.getId());
        }
    }
    //endregion


    //region EventSuscribing

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onSinistreMessageEvent(SinistreMessage message) {
        if (message != null) {
            if (message.getSyncAction() == SyncAction.UPDATE) {
                onCreateOrUpdateSinistreDTOMessageEvent(message.getDto());
            } else if (message.getSyncAction() == SyncAction.DELETE) {
                onDeleteSinistreDTOMessageEvent(message.getDto());
            }
        }
    }

    //endregion

}