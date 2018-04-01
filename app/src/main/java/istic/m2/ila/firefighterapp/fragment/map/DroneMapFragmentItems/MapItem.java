package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;

public abstract class MapItem
{
    //region Members

    protected GoogleMap _googleMap;
    protected Activity _contextActivity;

    //endregion

    //region Constructor

    public MapItem(GoogleMap map, Activity activity)
    {
        _googleMap = map;
        _contextActivity = activity;
    }

    //endregion
}
