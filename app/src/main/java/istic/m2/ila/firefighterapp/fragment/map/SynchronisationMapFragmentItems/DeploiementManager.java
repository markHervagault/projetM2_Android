package istic.m2.ila.firefighterapp.fragment.map.SynchronisationMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.MapItem;

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

    //region EventSubscribing
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onUpdateDeploiementDTOMessageEvent(DeploiementDTO message)
    {
        //Mise a jour du sinistre sur la map seulement si le drawing existe deja en BDD
        if(_deploiementById.containsKey(message.getId())) {
            _deploiementById.get(message.getId()).update(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onCreateDeploiementDTOMessageEvent(DeploiementDTO message)
    {
        // Création du sinistre
        if(!_deploiementById.containsKey(message.getId())) {
            _deploiementById.put(message.getId(), new DeploiementDrawing(message, _googleMap, _contextActivity));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onDeleteDeploiementDTOMessageEvent(DeploiementDTO message)
    {
        if(_deploiementById.containsKey(message.getId())) {
            _deploiementById.get(message.getId()).delete();
            _deploiementById.remove(message.getId());
        }
    }

    //endregion
}