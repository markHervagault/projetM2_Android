package istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.MessageGeneric;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.SyncAction;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.map.Common.MapItem;

/**
 * Created by adou on 24/04/18.
 */

public class TraitTopoManager extends MapItem
{
    //region Members

    private Map<Long, TraitTopoDrawing> _traitTopoById;

    //endregion

    //region Constructor

    public TraitTopoManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _traitTopoById = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    //endRegion

    //region Actions

    public synchronized void onCreateOrUpdateTraitTopoDTOMessageEvent(TraitTopoDTO message)
    {
        if(_traitTopoById.containsKey(message.getId())) {
            _traitTopoById.get(message.getId()).update(message);
        } else {
            _traitTopoById.put(message.getId(), new TraitTopoDrawing(message, _googleMap, _contextActivity));
        }
    }

    public synchronized void onDeleteTraitTopoDTOMessageEvent(TraitTopoDTO message)
    {
        if(_traitTopoById.containsKey(message.getId())) {
            _traitTopoById.get(message.getId()).delete();
            _traitTopoById.remove(message.getId());
        }
    }
    //endregion

    //region EventSuscribing

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onTraitTopoDTOMessageEvent(MessageGeneric<TraitTopoDTO> message){
        if(message != null){
            if(message.getSyncAction() == SyncAction.UPDATE){
                onCreateOrUpdateTraitTopoDTOMessageEvent(message.getDto());
            }else if (message.getSyncAction() == SyncAction.DELETE){
                onDeleteTraitTopoDTOMessageEvent(message.getDto());
            }
        }
    }

    //endregion

}