package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DroneDTO;

public class DroneDrawing extends MapItem
{
    //region Members
    private static final double BASE_LATITUDE = -80.618424; // Quelque part en Antarctique ...
    private static final double BASE_LONGITUDE = 40.930148; // ... se promène un drone anti-incendie

    private DroneDTO _drone;
    private Marker _droneMarker;

    //endregion

    //region Properties

    public long getId() { return _drone.getId(); }

    //endregion

    //region Constructor
    public DroneDrawing(DroneDTO droneDTO, GoogleMap map, Activity contextActivity)
    {
        super(map, contextActivity);
        _drone = droneDTO;

        InitDrone();
    }

    private void InitDrone()
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                _droneMarker = _googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(BASE_LATITUDE, BASE_LONGITUDE))
                .rotation(0)
                .title(_drone.getNom())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                .anchor(0.5f,0.5f) //Center on point
                .draggable(false));
            }
        });
    }
    //endregion

    //region Update Methods

    public void UpdatePosition(final double latitude, final double longitude, final double rotation)
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                _droneMarker.setPosition(new LatLng(latitude, longitude));
                _droneMarker.setRotation((float)Math.toDegrees(rotation) - _googleMap.getCameraPosition().bearing);
            }
        });
    }

    public void UpdatePosition(final double latitude, final double longitude)
    {
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                _droneMarker.setPosition(new LatLng(latitude, longitude));
            }
        });
    }

    //endregion
}
