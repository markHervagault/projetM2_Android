package istic.m2.ila.firefighterapp.map.Drone.Drawings;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.PointMissionDTO;
import istic.m2.ila.firefighterapp.map.Common.MapItem;

public class PathPointDrawing extends MapItem
{
    //region Members

    private Marker _marker;

    //Icon

    private BitmapDescriptor _normalIcon;
    private BitmapDescriptor _selectedNormalIcon;
    private BitmapDescriptor _photoIcon;
    private BitmapDescriptor _selectedPhotoIcon;


    //endregion

    //region Properties

    //Selected
    private boolean _isSelected;
    public boolean isSelected() { return _isSelected; }
    public void Select()
    {
        _isSelected = true;
        if(_action)
            _marker.setIcon(_selectedPhotoIcon);
        else
            _marker.setIcon(_selectedNormalIcon);
    }
    public void UnSelect()
    {
        _isSelected = false;
        if(_action)
            _marker.setIcon(_photoIcon);
        else
            _marker.setIcon(_normalIcon);
    }

    //Position
    public LatLng getPosition() { return _marker.getPosition(); }

    //Tag
    public Integer getTag() { return (Integer) _marker.getTag(); }
    public void setTag(Integer tag) { _marker.setTag(tag); }

    //Action
    private boolean _action;
    public boolean getAction() {
        return _action;
    }
    public void setAction(boolean action) {
        _action = action;
        if(action) {
            if (_isSelected)
                _marker.setIcon(_selectedPhotoIcon);
            else
                _marker.setIcon(_photoIcon);
        }
        else
        {
            if(_isSelected)
                _marker.setIcon(_selectedNormalIcon);
            else
                _marker.setIcon(_normalIcon);
        }
    }

    //PointMission
    private PointMissionDTO _poinMission;
    public PointMissionDTO getPoinMission() {
        return _poinMission;
    }
    //endregion

    //region Constructor

    public PathPointDrawing(LatLng position, boolean draggable, Integer tag, GoogleMap map, Activity activity) {
        super(map, activity);
        _action = false;

        //Normal & Selected
        Bitmap normal = getNewBitmapRenderedWithColor(R.drawable.pin_normal, "#ff6666");
        _normalIcon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(normal, 48, 48, false));
        Bitmap selectedNormal = getNewBitmapRenderedWithColor(R.drawable.pin_normal, "#79d279");
        _selectedNormalIcon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(selectedNormal, 64, 64, false));

        //Photo & Selected
        Bitmap photo = getNewBitmapRenderedWithColor(R.drawable.pin_full, "#ff6666");
        _photoIcon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(photo, 48, 48, false));
        Bitmap selectedPhoto = getNewBitmapRenderedWithColor(R.drawable.pin_full, "#79d279");
        _selectedPhotoIcon = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(selectedPhoto, 64, 64, false));

        _marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .zIndex(10)
                .title("Point de passage")
                .draggable(draggable)
                .icon(_normalIcon));

        _marker.setTag(tag);
        _isSelected = false;
        _poinMission = null;
    }

    public PathPointDrawing(PointMissionDTO point, boolean draggable, Integer tag, GoogleMap map, Activity activity)
    {
        this(new LatLng(point.getLatitude(), point.getLongitude()), draggable, tag, map, activity);
        _poinMission = point;
        setAction(point.getAction());
    }

    //endregion

    //region Methods

    public void Update(LatLng position)
    {
        _marker.setPosition(position);
    }

    public void Remove()
    {
        _marker.remove();
    }

    //endregion
}