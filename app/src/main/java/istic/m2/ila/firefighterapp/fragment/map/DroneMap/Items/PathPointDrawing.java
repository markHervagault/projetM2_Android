package istic.m2.ila.firefighterapp.fragment.map.DroneMap.Items;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.fragment.map.Common.MapItem;

public class PathPointDrawing extends MapItem
{
    //region Members

    private Marker _marker;

    //endregion

    //region Properties

    //Selected
    private boolean _isSelected;
    public boolean isSelected() { return _isSelected; }
    public void Select() { _isSelected = true; }
    public void UnSelect() { _isSelected = false; }

    //Position
    public LatLng getPosition() { return _marker.getPosition(); }

    //Tag
    public Integer getTag() { return (Integer) _marker.getTag(); }
    public void setTag(Integer tag) { _marker.setTag(tag); }

    //endregion

    //region Constructor

    public PathPointDrawing(LatLng position, boolean draggable, Integer tag, GoogleMap map, Activity activity) {
        super(map, activity);

        _marker = _googleMap.addMarker(new MarkerOptions()
                .position(position)
                .zIndex(10)
                .title("Point de passage")
                .draggable(draggable)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));

        _marker.setTag(tag);
        _isSelected = false;
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