package istic.m2.ila.firefighterapp.fragment.map.SynchronisationMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.fragment.map.Common.MapItem;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.MessageGeneric;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;

/**
 * Created by adou on 24/04/18.
 */

public class InterventionManager extends MapItem
{
    //region Members

    private Map<Long, InterventionDTO> _interventionById;

    //endregion

    //region Constructor

    public InterventionManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _interventionById = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    //endRegion

    //region Actions

    public synchronized void onCreateOrUpdateInterventionDTOMessageEvent(InterventionDTO message)
    {
        _interventionById.put(message.getId(), message);
    }

    public synchronized void onDeleteInterventionDTOMessageEvent(InterventionDTO message)
    {
        if(_interventionById.containsKey(message.getId())) {
            _interventionById.remove(message.getId());
        }
    }
    //endregion

    //region EventSuscribing

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onInterventionDTOMessageEvent(MessageGeneric<InterventionDTO> message){
        if(message != null){
            if(message.getSyncAction() == SyncAction.UPDATE){
                onCreateOrUpdateInterventionDTOMessageEvent(message.getDto());
            }else if (message.getSyncAction() == SyncAction.DELETE){
                onDeleteInterventionDTOMessageEvent(message.getDto());
            }
        }
    }

    //endregion

}