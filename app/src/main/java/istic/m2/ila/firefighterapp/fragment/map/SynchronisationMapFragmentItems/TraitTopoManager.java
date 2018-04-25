package istic.m2.ila.firefighterapp.fragment.map.SynchronisationMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.MapItem;

/**
 * Created by adou on 24/04/18.
 */

public class TraitTopoManager extends MapItem
{
    //region Members

    private Map<Long, TraitTopoDrawing> _sinistresById;

    //endregion

    //region Constructor

    public TraitTopoManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _sinistresById = new HashMap<>();
        EventBus.getDefault().register(this);
    }

    //endRegion

    //region EventSubscribing
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onUpdateTraitTopoDTOMessageEvent(TraitTopoDTO message)
    {
        //Mise a jour du sinistre sur la map seulement si le drawing existe deja en BDD
        if(_sinistresById.containsKey(message.getId())) {
            _sinistresById.get(message.getId()).update(message);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onCreateTraitTopoDTOMessageEvent(TraitTopoDTO message)
    {
        // Création du sinistre
        if(!_sinistresById.containsKey(message.getId())) {
            _sinistresById.put(message.getId(), new TraitTopoDrawing(message, _googleMap, _contextActivity));
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public synchronized void onDeleteTraitTopoDTOMessageEvent(TraitTopoDTO message)
    {
        if(_sinistresById.containsKey(message.getId())) {
            _sinistresById.get(message.getId()).delete();
            _sinistresById.remove(message.getId());
        }
    }

    //endregion
}