package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.PointMissionDTO;
import istic.m2.ila.firefighterapp.services.impl.MapService;

public class DroneMissionDrawing extends MapItem
{
    public enum MissionMode
    {
        FOLLOW,
        EDIT,
        NONE
    }

    //region Members

    private static final int STROKE_WIDTH = 3;
    private static final String TAG = "MissionDrawing";

    private List<Marker> _pathPositions;
    private HashMap<Integer,Marker> _markersByTag;
    private Polyline _pathDrawing;
    private Marker _selectedMarker;
    private DroneDTO _selectedDrone;

    private PropertyChangeSupport _propertyChangeSupport;

    //endregion

    //region Properties

    private MissionMode _missionMode;
    public MissionMode getMissionMode() { return _missionMode; }
    private void setMissionMode(MissionMode mode)
    {
        MissionMode lastMode = _missionMode;
        _missionMode = mode;
        _propertyChangeSupport.firePropertyChange("MissionMode", lastMode, mode);
    }

    //Edit Mode
    private boolean _addMode;
    public boolean isAddMode() { return _addMode; }
    public void setAddMode(boolean editMode)
    {
        _addMode = editMode;
        //Signalement d'un ajout de marker, pour mettre a jour l'UI
        _propertyChangeSupport.firePropertyChange("editMode", !editMode, editMode);
    }

    //Path Closed
    private boolean _pathClosed;
    public boolean isPathClosed() { return _pathClosed; }
    public void setPathClosed(boolean pathClosed)
    {
        _pathClosed = pathClosed;
        RefreshPath();
    }

    //Markers count
    public int getMarkersCount() { return _pathPositions.size(); }

    //endregion

    //region Constructor

    /**
     * Initilise une mission sur une googlemap
     * Récupère le contexte depuis l'acitivé liée
     * @param map
     * @param activity
     */
    public DroneMissionDrawing(GoogleMap map, Activity activity)
    {
        super(map,activity);
        InitMission();
    }

    private void InitMission()
    {
        //Initializing members
        _pathPositions = new ArrayList<>();
        _markersByTag = new HashMap<>();
        _pathDrawing = _googleMap.addPolyline(new PolylineOptions()); // Permet d'éviter la vérification constante dans RefreshPath
        _addMode = false;
        _missionMode = MissionMode.NONE;
        _propertyChangeSupport = new PropertyChangeSupport(this);

        //Setting Map Listeners
        _googleMap.setOnMapClickListener(null);
        _googleMap.setOnMarkerClickListener(onMarkerClickListener); // On garde toujours la séléction de markers
        _googleMap.setOnMarkerDragListener(null);

        //Inscription aux évènements
        EventBus.getDefault().register(this);;
    }
    
    private void ResetMission()
    {

        //Supression des éléments de la mission
        for (Marker marker : _pathPositions) {
            marker.remove();
        }
        _pathDrawing.remove();

        _selectedMarker = null;
        _pathPositions.clear();
        _markersByTag.clear();
        _addMode = false;

    }

    //endregion

    //region GoogleMap Listener

    //region onMapClickListener
    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener()
    {
        @Override
        public void onMapClick(LatLng latLng)
        {
            //Reset du marker selectionné
            _selectedMarker = null;

            //Seulement en mode édition
            if(!_addMode)
                return;

            //Ajout d'un nouveau marker sur la map
            Marker marker = _googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("Point de passage")
                    .draggable(true)
                    .zIndex(10f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));

            //Tag pour pouvoir retrouver le point
            Integer tag = _pathPositions.size();
            marker.setTag(tag);

            //Ajout du marker à la collection
            _pathPositions.add(marker);
            _markersByTag.put(tag, marker);

            //Signalement d'un ajout de marker, pour mettre a jour l'UI
            _propertyChangeSupport.firePropertyChange("markersCount", getMarkersCount() - 1, getMarkersCount());

            //Refresh du path
            RefreshPath();
        }
    };
    //endregion

    //region onMarkerClickListener
    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker)
        {
            //Vérification du tag du marker
            if(marker.getTag() != null && marker.getTag() instanceof Integer && _markersByTag.containsKey(marker.getTag()))
                _selectedMarker = _markersByTag.get(marker.getTag());

            return false;
        }
    };
    //endregion

    //region onMarkerDragListener
    private GoogleMap.OnMarkerDragListener onMarkerDragListener = new GoogleMap.OnMarkerDragListener()
    {
        private Marker _draggedMarker = null;

        @Override
        public void onMarkerDragStart(Marker marker)
        {
            if(marker.getTag() != null && marker.getTag() instanceof Integer && _markersByTag.containsKey(marker.getTag())) {
                _draggedMarker = _markersByTag.get(marker.getTag());
                _draggedMarker.setPosition(marker.getPosition());
                RefreshPath();
            }
        }

        @Override
        public void onMarkerDrag(Marker marker)
        {
            if(_draggedMarker != null)
            {
                _draggedMarker.setPosition(marker.getPosition());
                RefreshPath();
            }
        }

        @Override
        public void onMarkerDragEnd(Marker marker)
        {
            if(_draggedMarker != null)
            {
                _draggedMarker.setPosition(marker.getPosition());
                RefreshPath();
            }
        }
    };
    //endregion

    //endregion

    //region Methods

    private void RefreshPath()
    {
        if(_pathPositions.isEmpty())
            return;

        PolylineOptions options = new PolylineOptions();
        List<LatLng> positions = new ArrayList<>();
        for (Marker marker : _pathPositions)
            positions.add(marker.getPosition());

        if(_pathClosed)
        {
            positions.add(_pathPositions.get(0).getPosition());
            options.color(Color.GREEN);
        }
        else
            options.color(Color.BLUE);

        options.addAll(positions)
                .width(STROKE_WIDTH);

        _pathDrawing.remove();
        _pathDrawing = _googleMap.addPolyline(options);
        _pathDrawing.setZIndex(9f);
    }

    public void DeleteSelectedMarker()
    {
        //Seulement si un marker esr selectionné
        if(_selectedMarker == null)
            return;

        _selectedMarker.remove(); //Supression du marker de la map
        _pathPositions.remove(_selectedMarker); //Supression du marker de la mission
        ReIndexMarkers(); //Reindexation des markers restants

        //Signalement d'une suppression de marker, pour mettre a jour l'UI
        _propertyChangeSupport.firePropertyChange("markersCount", getMarkersCount() + 1, getMarkersCount());
    }

    private void ReIndexMarkers()
    {
        _markersByTag.clear();
        for(int i = 0; i < _pathPositions.size(); i++)
        {
            Integer index = i;
            _pathPositions.get(i).setTag(index);
            _markersByTag.put(index, _pathPositions.get(i));
        }
        RefreshPath();
    }

    public void SendMission(long interventionId, long droneId, int iterations)
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(_contextActivity);
        alertBuilder.setTitle("Attention");
        alertBuilder.setMessage("Voulez-vous vraiment envoyer les données de cette mission au drône ?");
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertBuilder.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                _contextActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        //Génération de la mission
                        final MissionDTO currentMission = new MissionDTO();
                        currentMission.setInterventionId(1l); //TODO SelectedDroneMission
                        currentMission.setNbIteration(0);
                        currentMission.setDroneId(1l);//TODO SelectedDrone
                        currentMission.setBoucleFermee(_pathClosed);
                        Set<PointMissionDTO> points = new HashSet<>();

                        //Génération des points de mission
                        long index = 0;
                        for(Marker marker : _pathPositions)
                        {
                            PointMissionDTO pointMissionDTO = new PointMissionDTO();
                            pointMissionDTO.setIndex(index);
                            pointMissionDTO.setAction(false);
                            pointMissionDTO.setLatitude(marker.getPosition().latitude);
                            pointMissionDTO.setLongitude(marker.getPosition().longitude);

                            points.add(pointMissionDTO);
                            index++;
                        }
                        currentMission.setDronePositions(points);

                        //Envoi de la mission
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run()
                            {
                                //vérification du token
                                String token = _contextActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                                if(!token.equals("null"))
                                    MapService.getInstance().sendDroneMission(token, currentMission);
                            }
                        });
                    }
                });
            }
        });
        alertBuilder.create().show();
    }

    public void SetCurrentMission(MissionDTO dto)
    {
        //Nettoyage de la Map
        ResetMission();

        //Tri des points du drone par index
        final List<PointMissionDTO> points = new ArrayList<>(dto.getDronePositions());
        Collections.sort(points, new Comparator<PointMissionDTO>()
        {
            public int compare(PointMissionDTO item1, PointMissionDTO item2)
            {
                return item1.getIndex().compareTo(item2.getIndex());
            }
        });

        //Mise a jour de la carte avec les nouveaux points
        _contextActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                for(PointMissionDTO point : points)
                {
                    Marker marker = _googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(point.getLatitude(), point.getLongitude()))
                            .title("Point de passage")
                            .draggable(false) //Mission en cours donc pas de modification des points
                            .zIndex(10f)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));

                    //Tag pour pouvoir retrouver le point
                    Integer tag = _pathPositions.size();
                    marker.setTag(tag);

                    //Ajout du marker à la collection
                    _pathPositions.add(marker);
                    _markersByTag.put(tag, marker);

                    //On rafraichit le path
                    RefreshPath();
                }
            }
        });
    }

    //endregion

    //region Bus Events

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSelectedDroneChanged(SelectedDroneChangedMessage message)
    {
        Log.i(TAG, "SelectedDroneCHanged : " + message.Drone.getStatut());

        _selectedDrone = message.Drone;
        ResetMission();

        switch (message.Drone.getStatut())
        {
            case EN_MISSION:
            case EN_PAUSE:
            case RETOUR_BASE:
                setMissionMode(MissionMode.FOLLOW);
                //Recupération de la mission
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run()
                    {
                        //vérification du token
                        String token = _contextActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                        if(!token.equals("null")) {
                            MissionDTO currentMission = MapService.getInstance().getCurrentDroneMission(token, _selectedDrone.getId());
                            if(currentMission != null)
                                SetCurrentMission(currentMission);
                        }
                    }
                });
                break;

            case DISPONIBLE:
                setMissionMode(MissionMode.EDIT);
                _googleMap.setOnMapClickListener(onMapClickListener);
                _googleMap.setOnMarkerDragListener(onMarkerDragListener);
                break;

            case DECONNECTE:
                setMissionMode(MissionMode.NONE);
                break;
        }

        //On enlève les listeners
        if(getMissionMode()  != MissionMode.EDIT)
        {
            _googleMap.setOnMapClickListener(null);
            _googleMap.setOnMarkerDragListener(null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSelectedDroneStatusChanged(SelectedDroneStatusChangedMessage message)
    {

    }

    //endregion

    //region PropertyChanged

    public void
    addPropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void
    removePropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeSupport.removePropertyChangeListener(listener);
    }

    //endregion
}