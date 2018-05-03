package istic.m2.ila.firefighterapp.map.Drone.Managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.EDroneStatus;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.PointMissionDTO;
import istic.m2.ila.firefighterapp.eventbus.drone.UnSelectPathPointMessage;
import istic.m2.ila.firefighterapp.map.Common.MapItem;
import istic.m2.ila.firefighterapp.map.Drone.Drawings.PathDrawing;
import istic.m2.ila.firefighterapp.map.Drone.Drawings.PathPointDrawing;
import istic.m2.ila.firefighterapp.services.impl.MapService;

public class MissionManager extends MapItem
{
    //region Mission Mode

    public static final String MISSION_MODE_CHANGED_EVENT_NAME = "MissionMode";
    public enum MissionMode
    {
        FOLLOW,
        EDIT,
        NONE
    }
    private MissionMode _missionMode;
    public MissionMode getMissionMode()
    {
        return _missionMode;
    }
    private void setMissionMode(MissionMode mode)
    {
        MissionMode lastMode = _missionMode;
        _missionMode = mode;
        _propertyChangeSupport.firePropertyChange(MISSION_MODE_CHANGED_EVENT_NAME, lastMode, _missionMode);
    }

    //endregion

    //region Members

    private static final String TAG = "MissionManager";

    //Map Items
    private List<PathPointDrawing> _pathPoints;
    private Map<Integer, PathPointDrawing> _pathPointsByTag;

    private PathDrawing _pathDrawing;
    private DroneDTO _selectedDrone;
    private long _interventionId;

    //endregion

    //region Properties

    //Edit mode
    public static final String EDIT_MODE_CHANGED_EVENT_NAME = "EditMode";
    private boolean _editMode;
    public boolean isEditMode() { return _editMode; }
    public void setEditMode(boolean editMode)
    {
        _editMode = editMode;

        //Autorisation de rajouter des markers
        if(_editMode && _missionMode == MissionMode.EDIT) //On autorise l'édition
            _googleMap.setOnMapClickListener(onMapClickListener);
        else
            _googleMap.setOnMapClickListener(onMapClickListenerCommand);

        _propertyChangeSupport.firePropertyChange(EDIT_MODE_CHANGED_EVENT_NAME, !_editMode, _editMode);
    }

    //Send Mission
    public static final String SENDMISSION_CHANGED_EVENT_NAME = "SendMission";
    private boolean _canSendMission;
    public boolean canSendMission() { return _canSendMission; }
    private void setCanSendMission(boolean canSendMission)
    {
        _canSendMission = canSendMission;
        _propertyChangeSupport.firePropertyChange(SENDMISSION_CHANGED_EVENT_NAME, !_canSendMission, _canSendMission);
    }

    //PointCount
    public static final String POINTCOUNT_CHANGED_EVENT_NAME = "PointsCount";
    public int getPointsCount() {
        return _pathPoints.size();
    }

    //Selected Marker
    public static final String SELECTED_MARKER_CHANGED = "SelectedMarker";
    private PathPointDrawing _selectedMarker;
    public PathPointDrawing getSelectedMarker() { return _selectedMarker; }
    private void setSelectedMarker(PathPointDrawing marker)
    {
        if(_selectedMarker != null)
            _selectedMarker.UnSelect();

        _selectedMarker = marker;
        if(_selectedMarker != null)
            _selectedMarker.Select();

        _propertyChangeSupport.firePropertyChange(SELECTED_MARKER_CHANGED, null, null);
    }

    //endregion

    //region Constructor

    public MissionManager(GoogleMap map, Activity activity)
    {
        super(map, activity);

        //Members init
        _pathPoints = new ArrayList<>();
        _pathPointsByTag = new HashMap<>();
        _pathDrawing = new PathDrawing(_googleMap, _contextActivity);
        _editMode = false;
        _propertyChangeSupport = new PropertyChangeSupport(this);
        _interventionId = ((MapActivity)_contextActivity).getIdIntervention();

        //Listener Init
        _googleMap.setOnMapClickListener(null);
        _googleMap.setOnMarkerDragListener(null);
        _googleMap.setOnMarkerClickListener(onMarkerClickListener);

        //Registering EventBus
        EventBus.getDefault().register(this);
    }

    public void ResetMission()
    {
        Log.i(TAG, "Reseting Mission");

        for(PathPointDrawing point : _pathPoints)
            point.Remove();

        _pathPoints.clear();
        _pathPointsByTag.clear();
        _pathDrawing.Clear();
        _selectedMarker = null;

        setEditMode(false);

        _propertyChangeSupport.firePropertyChange(POINTCOUNT_CHANGED_EVENT_NAME, 1000,0);
        _propertyChangeSupport.firePropertyChange(SELECTED_MARKER_CHANGED, null, null);
    }

    //endregion

    //region MapListener

    //region Map Click
    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener()
    {
        @Override
        public void onMapClick(LatLng latLng)
        {
            Log.i(TAG, "Click sur la map");
            //Reset du marker selectionné
            setSelectedMarker(null);

            //Seulement en mode édition
            if(!_editMode)
                return;

            //Tag pour pouvoir retrouver le point
            Integer tag = _pathPoints.size();
            PathPointDrawing point = new PathPointDrawing(latLng, true, tag, _googleMap, _contextActivity);

            //Ajout du marker à la collection
            _pathPoints.add(point);
            _pathPointsByTag.put(tag, point);

            //Refresh du path
            _pathDrawing.Update(_pathPoints);

            //On informe du rajout d'un nouveau point
            _propertyChangeSupport.firePropertyChange(POINTCOUNT_CHANGED_EVENT_NAME, getPointsCount() - 1, getPointsCount());
        }
    };

    private GoogleMap.OnMapClickListener onMapClickListenerCommand = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            Log.i(TAG, "Click sur la map");
            //Reset du marker selectionné
            setSelectedMarker(null);
            EventBus.getDefault().post(new UnSelectPathPointMessage());
        }
    };
    //endregion

    //region Marker Click
    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker)
        {
            //Vérification du tag du marker
            /*if(marker.getTag() != null && marker.getTag() instanceof Integer && _pathPointsByTag.containsKey(marker.getTag())) {
                //Deselection du marker actuel
                if(_selectedMarker != null){
                    _selectedMarker.UnSelect();
                }

                setSelectedMarker(_pathPointsByTag.get(marker.getTag()));
            }
            else{
                setSelectedMarker(null);
            }*/

            if(marker.getTag() != null && marker.getTag() instanceof Integer && _pathPointsByTag.containsKey(marker.getTag())) {
                // Si un marqueur n'a jamais été sélectionné auparavant
                if(_selectedMarker == null){
                    setSelectedMarker(_pathPointsByTag.get(marker.getTag()));
                    EventBus.getDefault().post(_selectedMarker);
                }
                // si le marqueur sélectionné et le même que celui sélectionné auparavant
                else if (_selectedMarker.getTag().equals(marker.getTag())){
                    _selectedMarker.UnSelect();
                    setSelectedMarker(null);
                    EventBus.getDefault().post(new UnSelectPathPointMessage());
                }
                // si le marqueur n'est pas le même que celui sélectionné auparavant
                else{
                    _selectedMarker.UnSelect();
                    setSelectedMarker(_pathPointsByTag.get(marker.getTag()));
                    EventBus.getDefault().post(_selectedMarker);
                }
            }
            else{
                setSelectedMarker(null);
                EventBus.getDefault().post(new UnSelectPathPointMessage());
            }
            // TODO : Quand est ce qu'on renvoie TRUE ? Quand est ce qu'on renvoie FALSE ?
            return true;
        }
    };
    //endregion

    //region Marker Drag
    private GoogleMap.OnMarkerDragListener onMarkerDragListener = new GoogleMap.OnMarkerDragListener()
    {
        private PathPointDrawing _draggedMarker = null;

        @Override
        public void onMarkerDragStart(Marker marker)
        {
            if(marker.getTag() != null && marker.getTag() instanceof Integer && _pathPointsByTag.containsKey(marker.getTag())) {
                _draggedMarker = _pathPointsByTag.get(marker.getTag());
                _draggedMarker.Update(marker.getPosition());

                _pathDrawing.Update(_pathPoints);
            }
        }

        @Override
        public void onMarkerDrag(Marker marker)
        {
            if(_draggedMarker != null)
            {
                _draggedMarker.Update(marker.getPosition());
                _pathDrawing.Update(_pathPoints);
            }
        }

        @Override
        public void onMarkerDragEnd(Marker marker)
        {
            if(_draggedMarker != null)
            {
                _draggedMarker.Update(marker.getPosition());
                _pathDrawing.Update(_pathPoints);
            }
        }
    };

    //endregion

    //endregion

    //region Methods

    public void RemoveSelectedMarker()
    {
        if(_selectedMarker == null)
            return;

        _selectedMarker.Remove();
        _pathPoints.remove(_selectedMarker);
        setSelectedMarker(null);

        ReindexPoints();
        _pathDrawing.Update(_pathPoints);

        _propertyChangeSupport.firePropertyChange(POINTCOUNT_CHANGED_EVENT_NAME, getPointsCount() + 1, getPointsCount());
    }

    public void OpenPath()
    {
        _pathDrawing.OpenPath();
    }

    public void ClosePath()
    {
        _pathDrawing.ClosePath();
    }

    public boolean isPathCLosed()
    {
        return _pathDrawing.isPathClosed();
    }

    private void ReindexPoints()
    {
        _pathPointsByTag.clear();
        for(PathPointDrawing point : _pathPoints)
            _pathPointsByTag.put(point.getTag(), point);

    }

    public void SendMission()
    {
        Log.i(TAG, "SendMission");
        if(!canSendMission()) {
            Log.i(TAG, "Cannot send mission");
            return;
        }

        if(_selectedDrone == null) {
            Log.i(TAG, "No Selected Drone");
            return;
        }

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
                        currentMission.setInterventionId(_interventionId);
                        currentMission.setNbIteration(0);
                        currentMission.setDroneId(_selectedDrone.getId());
                        currentMission.setBoucleFermee(_pathDrawing.isPathClosed());
                        Set<PointMissionDTO> points = new HashSet<>();

                        //Génération des points de mission
                        long index = 0;
                        for(PathPointDrawing point : _pathPoints)
                        {
                            Log.i(TAG, "Ajout du point d'index "+ index + " avec le tag "+ point.getTag().intValue());
                            PointMissionDTO pointMissionDTO = new PointMissionDTO();
                            pointMissionDTO.setIndex(index);
                            pointMissionDTO.setAction(point.getAction());
                            pointMissionDTO.setLatitude(point.getPosition().latitude);
                            pointMissionDTO.setLongitude(point.getPosition().longitude);

                            points.add(pointMissionDTO);
                            index++;
                        }
                        currentMission.setDronePositions(points);
                        //Log.i(TAG, "Sending Misison : " + currentMission.toString());

                        //Envoi de la mission
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run()
                            {
                                //vérification du token
                                String token = _contextActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                                if(!token.equals("null"))
                                    MapService.getInstance().sendDroneMission(token, currentMission);
                                //TODO : Vérifier le retour d'envoi et informaer l'utilisateur
                            }
                        });
                    }
                });
            }
        });
        alertBuilder.create().show();
    }

    private void SetCurrentMission(final MissionDTO dto)
    {
        if(dto.getDronePositions() == null)
        {
            Log.e(TAG, "Send Mission : drone positions null");
            return;
        }

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
                //Passage en mode Follow
                if(getMissionMode() != MissionMode.FOLLOW)
                    setMissionMode(MissionMode.FOLLOW);

                ResetMission();

                for(PointMissionDTO point : points)
                {
                    //Tag pour pouvoir retrouver le point
                    Integer tag = _pathPoints.size();
                    PathPointDrawing pathPoint = new PathPointDrawing(point, false, tag, _googleMap, _contextActivity);

                    //Ajout du marker à la collection
                    _pathPoints.add(pathPoint);
                    _pathPointsByTag.put(tag, pathPoint);

                    //On rafraichit le path
                    _pathDrawing.Update(_pathPoints);
                    if(dto.getBoucleFermee())
                        ClosePath();
                    else
                        OpenPath();
                }
            }
        });
    }

    //endregion

    //region Events Bus

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSelectedDroneChanged(SelectedDroneChangedMessage message)
    {
        Log.i(TAG, "SelectedDroneChanged : " + message.Drone.getStatut());

        //Même drone séléctionné, on ne fait pas de resfresh
        if(_selectedDrone != null && _selectedDrone.getId().equals(message.Drone.getId()))
            return;

        _selectedDrone = message.Drone;
        ResetMission();

        switch (message.Drone.getStatut()) {
            case EN_MISSION:
            case RETOUR_BASE:
            case EN_PAUSE:
            case EN_PAUSE_RETOUR_BASE:
                setMissionMode(MissionMode.FOLLOW);
                //Recupération de la mission
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //vérification du token
                        Log.i(TAG, "Getting current mission");
                        String token = _contextActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                        if (!token.equals("null")) {
                            MissionDTO currentMission = MapService.getInstance().getCurrentDroneMission(token, _selectedDrone.getId());
                            if (currentMission != null)
                                SetCurrentMission(currentMission);
                            else
                                Log.e(TAG, "Current mission null for drone : " + _selectedDrone.getId());
                        }
                    }
                });
                break;

            case DISPONIBLE:
                setMissionMode(MissionMode.EDIT);
                setCanSendMission(true);
                _googleMap.setOnMapClickListener(onMapClickListener);
                _googleMap.setOnMarkerDragListener(onMarkerDragListener);
                break;

            case DECONNECTE:
                setMissionMode(MissionMode.EDIT);
                setCanSendMission(false);
                _googleMap.setOnMapClickListener(onMapClickListener);
                _googleMap.setOnMarkerDragListener(onMarkerDragListener);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSelectedDroneStatusChanged(final SelectedDroneStatusChangedMessage message)
    {
        switch (message.getDroneStatut()) {
            case EN_MISSION:
            case EN_PAUSE:
            case RETOUR_BASE:
            case EN_PAUSE_RETOUR_BASE:
                if(getMissionMode() != MissionMode.FOLLOW)
                {
                    ResetMission();
                    setMissionMode(MissionMode.FOLLOW);
                }
                break;

            case DISPONIBLE:
            case DECONNECTE:
                if(message.getDroneStatut() == EDroneStatus.DISPONIBLE)
                    setCanSendMission(true);
                else
                    setCanSendMission(false);

                if(getMissionMode() != MissionMode.EDIT)
                {
                    ResetMission();
                    setMissionMode(MissionMode.EDIT);
                    _googleMap.setOnMapClickListener(onMapClickListener);
                    _googleMap.setOnMarkerDragListener(onMarkerDragListener);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void OnDroneMisisonUpdate(MissionDTO currentMission)
    {
        if(currentMission == null || !currentMission.getDroneId().equals(_selectedDrone.getId())) {
            Log.i(TAG, "ReceivedCurrent mission null or not selected drone : " + currentMission.toString());
            return;
        }

        Log.i(TAG, "OnDroneMissionUpdate : " + currentMission.toString());
        SetCurrentMission(currentMission);
    }

    //endregion

    //region PropertyChanged

    private PropertyChangeSupport _propertyChangeSupport;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeSupport.removePropertyChangeListener(listener);
    }

    //endregion
}