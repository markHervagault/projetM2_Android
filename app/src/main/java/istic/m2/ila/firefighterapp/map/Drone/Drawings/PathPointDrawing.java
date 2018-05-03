package istic.m2.ila.firefighterapp.map.Drone.Drawings;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

import com.google.android.gms.maps.GoogleMap;
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

    private Bitmap _normalIcon;
    private Bitmap _photoIcon;
    private Bitmap _selectedIcon;


    //endregion

    //region Properties

    //Selected
    private boolean _isSelected;
    public boolean isSelected() { return _isSelected; }
    public void Select()
    {
        _isSelected = true;
        _marker.setIcon(BitmapDescriptorFactory.fromBitmap(_selectedIcon));
    }
    public void UnSelect()
    {
        _isSelected = false;
        if(_action)
            _marker.setIcon(BitmapDescriptorFactory.fromBitmap(_photoIcon));
        else
            _marker.setIcon(BitmapDescriptorFactory.fromBitmap(_normalIcon));
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
        if(action)
            _marker.setIcon(BitmapDescriptorFactory.fromBitmap(_photoIcon));
        else
            _marker.setIcon(BitmapDescriptorFactory.fromBitmap(_normalIcon));
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
        _normalIcon = getNewBitmapRenderedWithColor(R.drawable.map_marker, "#ff6666");
        _photoIcon = getNewBitmapRenderedWithColor(R.drawable.map_marker, "#79d279");
        _selectedIcon = getNewBitmapRenderedWithColor(R.drawable.map_marker, "#1a53ff");

        _marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .zIndex(10)
                .title("Point de passage")
                .draggable(draggable)
                .icon(BitmapDescriptorFactory.fromBitmap(_normalIcon)));

        _marker.setTag(tag);
        _isSelected = false;
        _poinMission = null;
    }

    public PathPointDrawing(PointMissionDTO point, boolean draggable, Integer tag, GoogleMap map, Activity activity)
    {
        this(new LatLng(point.getLatitude(), point.getLongitude()), draggable, tag, map, activity);
        _poinMission = point;
        if(point.getAction())
            _marker.setIcon(BitmapDescriptorFactory.fromBitmap(_photoIcon));
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