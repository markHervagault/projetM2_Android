package istic.m2.ila.firefighterapp.map.Drone.Drawings;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatus;
import istic.m2.ila.firefighterapp.map.Common.DoubleArrayEvaluator;
import istic.m2.ila.firefighterapp.map.Common.MapItem;

public class DroneDrawing extends MapItem
{
    //region Members
    private static final String TAG = "DroneDrawing";
    private static final double BASE_LATITUDE = -80.618424; // Quelque part en Antarctique ...
    private static final double BASE_LONGITUDE = 40.930148; // ... se promène un drone anti-incendie

    private DroneDTO _drone;
    private EDroneStatus _oldStatus;
    private Marker _droneMarker;

    //endregion

    //region Properties

    public long getId() { return _drone.getId(); }
    public EDroneStatus getStatus() { return _drone.getStatut(); }
    public DroneDTO getDrone() { return _drone; }

    private boolean _isSelected;
    public boolean isSelected() { return _isSelected; }
    public void Select() {
        _isSelected = true;
        final Bitmap icon = getNewBitmapRenderedWithColor(R.drawable.drone, "#0e03a8");
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                _droneMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            }
        });
    }

    public void UnSelect() {
        _isSelected = false;
        final Bitmap icon = getNewBitmapRenderedWithColor(R.drawable.drone, "#040b14");
        _contextActivity.runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                _droneMarker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
            }
        });

    }

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
                .zIndex(50f)); //TODO placer les z_Index dans un fichier de conf, pour être sur des superpositions dans la istic.m2.ila.firefighterapp.map
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
        double[] startValues = new double[]{_droneMarker.getPosition().latitude, _droneMarker.getPosition().longitude};
        double[] endValues = new double[]{latitude, longitude};

        ValueAnimator droneUpdater = ValueAnimator.ofObject(new DoubleArrayEvaluator(), startValues, endValues);
        droneUpdater.setDuration(100); //100ms animation
        droneUpdater.setInterpolator(new DecelerateInterpolator());
        droneUpdater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                double[] animatedValue = (double[])valueAnimator.getAnimatedValue();
                _droneMarker.setPosition(new LatLng(animatedValue[0], animatedValue[1]));
            }
        });
        droneUpdater.start();

        //_droneMarker.setPosition(new LatLng(latitude, longitude));
    }

    //endregion
}

