package istic.m2.ila.firefighterapp.map.Drone.Drawings;

import android.app.Activity;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.map.Common.MapItem;

public class PathDrawing extends MapItem
{
    //region members

    private static final int STROKE_WIDTH = 3;

    private Polyline _path;
    private List<PathPointDrawing> _passingPoints;

    //endregion

    //region Properties

    private boolean _isPathClosed;
    public boolean isPathClosed() { return _isPathClosed; }
    public void  ClosePath()
    {
        if(_isPathClosed)
            return;

        _isPathClosed = true;
        RefreshPath();
    }
    public void OpenPath()
    {
        if(!_isPathClosed)
            return;

        _isPathClosed = false;
        RefreshPath();
    }

    //endregion

    //region Constructor

    public PathDrawing(GoogleMap map, Activity activity) {
        super(map, activity);

        PolylineOptions options = new PolylineOptions();
        _passingPoints = new ArrayList<>();
        _path = _googleMap.addPolyline(options);
        _isPathClosed = false;
    }

    //endregion

    //region Methods

    public void Update(List<PathPointDrawing> pathPoints)
    {
        _passingPoints = pathPoints;
        RefreshPath();
    }

    public void Clear()
    {
        _path.remove();
        _path = _googleMap.addPolyline(new PolylineOptions());

        _passingPoints.clear();
    }

    private void RefreshPath()
    {
        PolylineOptions options = new PolylineOptions();
        List<LatLng> pathPoints = new ArrayList<>();
        for(PathPointDrawing point : _passingPoints)
            pathPoints.add(point.getPosition());

        //Boucle fermée, on rajoute le premier point
        if(_isPathClosed && _passingPoints.size() > 2) {
            pathPoints.add(_passingPoints.get(0).getPosition());
            options.color(Color.GREEN);
        }
        else
            options.color(Color.BLUE);

        options.addAll(pathPoints)
                .width(STROKE_WIDTH);

        _path.remove();
        _path = _googleMap.addPolyline(options);
        _path.setZIndex(9f);
    }

    //endregion
}
