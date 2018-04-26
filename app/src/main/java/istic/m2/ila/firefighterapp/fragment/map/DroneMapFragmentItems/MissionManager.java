package istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneStatusChangedMessage;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.PointMissionDTO;
import istic.m2.ila.firefighterapp.services.impl.MapService;

public class MissionManager extends MapItem
{
    //region Mission Mode

    public static final String MISSIONMODE_CHANGED_EVENT_NAME = "MissionMode";
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
        _propertyChangeSupport.firePropertyChange(MISSIONMODE_CHANGED_EVENT_NAME, lastMode, _missionMode);
    }

    //endregion

    //region Members

    private static final String TAG = "MissonManager";

    //Map Items
    private List<PathPointDrawing> _pathPoints;
    private Map<Integer, PathPointDrawing> _pathPointsByTag;
    private PathPointDrawing _selectedMarker;

    private PathDrawing _pathDrawing;
    private DroneDTO _selectedDrone;

    //endregion

    //region Properties

    public static final String EDITMODE_CHANGED_EVENT_NAME = "EditMode";
    private boolean _editMode;
    public boolean isEditMode() { return _editMode; }
    public void setEditMode(boolean editMode)
    {
        _editMode = editMode;

        //Autorisation de rajouter des markers
        if(_editMode && _missionMode == MissionMode.EDIT) //On autorise l'édition
            _googleMap.setOnMapClickListener(onMapClickListener);
        else
            _googleMap.setOnMapClickListener(null);

        _propertyChangeSupport.firePropertyChange(EDITMODE_CHANGED_EVENT_NAME, !_editMode, _editMode);
    }

    public static final String SENDMISSION_CHANGED_EVENT_NAME = "SendMission";
    private boolean _canSendMission;
    public boolean canSendMission() { return _canSendMission; }
    private void setCanSendMission(boolean canSendMission)
    {
        _canSendMission = canSendMission;
        _propertyChangeSupport.firePropertyChange(SENDMISSION_CHANGED_EVENT_NAME, !_canSendMission, _canSendMission);
    }

    public static final String POINTCOUNT_CHANGED_EVENT_NAME = "PointsCount";
    public int getPointsCount() { return _pathPoints.size(); }

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

        //Listener Init
        _googleMap.setOnMapClickListener(null);
        _googleMap.setOnMarkerDragListener(null);
        _googleMap.setOnMarkerClickListener(onMarkerClickListener);

        //Registering EventBus
        EventBus.getDefault().register(this);
    }

    public void ResetMission()
    {
        for(PathPointDrawing point : _pathPoints)
            point.Remove();

        _pathPoints.clear();
        _pathPointsByTag.clear();
        _pathDrawing.Clear();
        _selectedMarker = null;

        setEditMode(false);
    }

    //endregion

    //region MapListener

    //region Map Click
    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener()
    {
        @Override
        public void onMapClick(LatLng latLng)
        {
            //Reset du marker selectionné
            _selectedMarker = null;

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
    //endregion

    //region Marker Click
    private GoogleMap.OnMarkerClickListener onMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker)
        {
            //Vérification du tag du marker
            if(marker.getTag() != null && marker.getTag() instanceof Integer && _pathPointsByTag.containsKey(marker.getTag())) {
                //Deselection du marker actuel
                if(_selectedMarker != null)
                    _selectedMarker.UnSelect();
                _selectedMarker = _pathPointsByTag.get(marker.getTag());
                _selectedMarker.Select();
            }

            return false;
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
        _pathPointsByTag.remove(_selectedMarker);

        ReindexPoints();

        _propertyChangeSupport.firePropertyChange(POINTCOUNT_CHANGED_EVENT_NAME, getPointsCount() + 1, getPointsCount());
    }

    private void ReindexPoints()
    {
        _pathPointsByTag.clear();
        for(PathPointDrawing point : _pathPoints)
            _pathPointsByTag.put(point.getTag(), point);

    }

    public void SendMission()
    {
        if(!canSendMission())
            return;

        if(_selectedDrone == null)
            return;

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
                        currentMission.setDroneId(_selectedDrone.getId());//TODO SelectedDrone
                        currentMission.setBoucleFermee(_pathDrawing.isPathClosed());
                        Set<PointMissionDTO> points = new HashSet<>();

                        //Génération des points de mission
                        long index = 0;
                        for(PathPointDrawing point : _pathPoints)
                        {
                            PointMissionDTO pointMissionDTO = new PointMissionDTO();
                            pointMissionDTO.setIndex(index);
                            pointMissionDTO.setAction(false);
                            pointMissionDTO.setLatitude(point.getPosition().latitude);
                            pointMissionDTO.setLongitude(point.getPosition().longitude);

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
                                //TODO : Vérifier le retour d'envoi et informaer l'utilisateur
                            }
                        });
                    }
                });
            }
        });
        alertBuilder.create().show();
    }

    private void SetCurrentMission(MissionDTO dto)
    {
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
                ResetMission();

                for(PointMissionDTO point : points)
                {
                    //Tag pour pouvoir retrouver le point
                    Integer tag = _pathPoints.size();
                    PathPointDrawing pathPoint = new PathPointDrawing(new LatLng(point.getLatitude(), point.getLongitude()), false, tag, _googleMap, _contextActivity);

                    //Ajout du marker à la collection
                    _pathPoints.add(pathPoint);
                    _pathPointsByTag.put(tag, pathPoint);

                    //On rafraichit le path
                    _pathDrawing.Update(_pathPoints);
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

        _selectedDrone = message.Drone;
        ResetMission();

        switch (message.Drone.getStatut()) {
            case EN_MISSION:
            case EN_PAUSE:
            case RETOUR_BASE:
                setMissionMode(MissionMode.FOLLOW);
                //Recupération de la mission
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //vérification du token
                        String token = _contextActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                        if (!token.equals("null")) {
                            MissionDTO currentMission = MapService.getInstance().getCurrentDroneMission(token, _selectedDrone.getId());
                            if (currentMission != null)
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
                _googleMap.setOnMapClickListener(onMapClickListener);
                _googleMap.setOnMarkerDragListener(onMarkerDragListener);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnSelectedDroneStatusChanged(final SelectedDroneStatusChangedMessage message)
    {
        Log.i(TAG, "SelectedDroneStatusChanged : " + message.getDroneStatut());

        ResetMission();

        switch (message.getDroneStatut()) {
            case EN_MISSION:
            case EN_PAUSE:
            case RETOUR_BASE:
                setMissionMode(MissionMode.FOLLOW);
                //Recupération de la mission
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //vérification du token
                        String token = _contextActivity.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                        if (!token.equals("null")) {
                            MissionDTO currentMission = MapService.getInstance().getCurrentDroneMission(token, message.getDroneId());
                            if (currentMission != null)
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
                _googleMap.setOnMapClickListener(onMapClickListener);
                _googleMap.setOnMarkerDragListener(onMarkerDragListener);
                break;
        }
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