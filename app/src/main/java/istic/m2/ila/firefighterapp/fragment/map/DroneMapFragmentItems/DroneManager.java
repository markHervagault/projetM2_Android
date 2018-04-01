package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.dto.DroneDTO;

public class DroneManager extends MapItem
{
    //region Members

    private Map<Long, DroneDrawing> _dronesById;

    //endregion

    //region Constructor

    public DroneManager(GoogleMap map, Activity contextActivity)
    {
        super(map,contextActivity);

        _dronesById = new HashMap<>();
    }

    //endRegion

    //region EventSubscribing

    

    //endregion
}
