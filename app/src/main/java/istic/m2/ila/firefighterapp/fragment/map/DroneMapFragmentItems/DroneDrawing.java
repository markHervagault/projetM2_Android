package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;

public class DroneDrawing extends MapItem
{
    //region Members
    private static final String TAG = "DroneDrawing";
    private static final double BASE_LATITUDE = -80.618424; // Quelque part en Antarctique ...
    private static final double BASE_LONGITUDE = 40.930148; // ... se promène un drone anti-incendie

    private DroneDTO _drone;
    private EDroneStatut _oldStatus;
    private Marker _droneMarker;

    //endregion

    //region Properties

    public long getId() { return _drone.getId(); }
    public EDroneStatut getStatus() { return _drone.getStatut(); }
    public DroneDTO getDrone() { return _drone; }

    private boolean _isSelected;
    public boolean isSelected() { return _isSelected; }
    public void Select() { _isSelected = true; }
    public void UnSelect() {_isSelected = false; }

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
        _isSelected = false;

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
                .draggable(false)
                .zIndex(50f)); //TODO placer les z_Index dans un fichier de conf, pour être sur des superpositions dans la map
            }
        });
    }
    //endregion

    //region Update Methods

    public void Update(final DroneInfosDTO dto)
    {
        _drone.Update(dto);
        UpdatePosition(dto.position.latitude, dto.position.longitude, dto.orientation.yaw);

        //Seulement si le drone est séléctionné
        if(_isSelected)
        {
            try {
                if (getStatus() != _oldStatus) {
                    Log.i(TAG, "SelectedDrone Status Changed");
                    EventBus.getDefault().post(new SelectedDroneStatusChangedMessage(getStatus(), dto.id_drone));
                }
            } catch (IllegalArgumentException e) {
                Log.e(TAG, e.getMessage());
            }
            finally {
                _oldStatus = _drone.getStatut();
            }
        }
        else
            _oldStatus = _drone.getStatut();
    }

    private void UpdatePosition(final double latitude, final double longitude, final double rotation)
    {
        UpdatePosition(latitude, longitude);
        _droneMarker.setRotation((float)Math.toDegrees(rotation) - _googleMap.getCameraPosition().bearing);
    }

    private void UpdatePosition(final double latitude, final double longitude)
    {
        _droneMarker.setPosition(new LatLng(latitude, longitude));
    }

    //endregion
}
