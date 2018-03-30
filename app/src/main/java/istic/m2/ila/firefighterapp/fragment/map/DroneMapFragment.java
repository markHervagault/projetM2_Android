package istic.m2.ila.firefighterapp.fragment.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.CustomInfoWindowAdapter;
import istic.m2.ila.firefighterapp.clientRabbitMQ.ServiceRabbitMQ;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DroneInfoUpdateMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.consumer.DroneMissionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.DroneInfosDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.PointMissionDTO;
import istic.m2.ila.firefighterapp.services.impl.MapService;
import okhttp3.Response;

public class DroneMapFragment extends Fragment
{
    // =================================================================== INIT

    //Global Members
    private final String TAG = "DroneMap Fragment";

    // Init Members
    MapView mMapView;
    private GoogleMap googleMap;
    private View view;

    public DroneMapFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_drone_map, container, false);
        final Button button = view.findViewById(R.id.toggleView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((NewMapActivity)getActivity()).toggleView();
            }
        });

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap)
            {
                googleMap = mMap;
                ((NewMapActivity)getActivity()).initMap(googleMap);

                //Initialise la carte
                InitMap();
            }
        });

        //Init Floating Menu
        InitMenu();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // =================================================================== //INIT

    // =================================================================== INTERFACE

    // Interface Members
    private boolean isPathClosed;
    private boolean isAddButtonEnabled;

    /**
     * Initialise le menu de controles déroulants avec des listeners
     */
    private void InitMenu()
    {
        isPathClosed = false;
        isAddButtonEnabled = false;

        FloatingActionButton fabRemoveSelectedMarker = view.findViewById(R.id.fabMenu_removeSelectedMarker);
        final FloatingActionButton fabOpenClose = view.findViewById(R.id.fabMenu_openClosePath);
        final FloatingActionButton fabAddMarker = view.findViewById(R.id.fabMenu_addMarker);
        FloatingActionButton fabZone = view.findViewById(R.id.fabMenu_zone);
        FloatingActionButton fabSendMission = view.findViewById(R.id.fab_menu2_send);

        //Remove Button Listener
        fabRemoveSelectedMarker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.i(TAG, "RemoveButton Cliked");
                // Gestion de l'événement click pour le bouton flottant
                DeleteMarker(selectedMarker);
            }
        });

        //Add Button Listener
        fabAddMarker.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isAddButtonEnabled) // Activation du mode
                {
                    //Desactivation des boutons
                    ChangeMenuButtonsStatus(false);

                    //Activation du bouton d'ajout ?? Utile??
                    fabAddMarker.setEnabled(true);

                    // Couleurs du focus
                    fabAddMarker.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabSelectedNormal));
                    fabAddMarker.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabSelectedPressed));
                    fabAddMarker.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabSelectedRipple));

                    isAddButtonEnabled = true;
                }
                else //Desactivation du mode
                {
                    //Reactivation des boutons
                    ChangeMenuButtonsStatus(true);

                    // Couleurs de l'unfocus
                    fabAddMarker.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabDefaultNormal));
                    fabAddMarker.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabDefaultPressed));
                    fabAddMarker.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabDefaultRipple));

                    isAddButtonEnabled = false;
                }
            }
        });

        fabOpenClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Si le trajet est fermé
                if(isPathClosed)
                {
                    //Changement de style du bouton
                    fabOpenClose.setColorNormal(getResources().getColor(R.color.colorMenuFabDefaultNormal));
                    fabOpenClose.setColorPressed(getResources().getColor(R.color.colorMenuFabDefaultPressed));
                    fabOpenClose.setColorRipple(getResources().getColor(R.color.colorMenuFabDefaultRipple));
                    fabOpenClose.setImageResource(R.drawable.openloop);

                    isPathClosed = false;
                }
                else
                {
                    fabOpenClose.setColorNormal(getResources().getColor(R.color.colorMenuFabSelectedNormal));
                    fabOpenClose.setColorPressed(getResources().getColor(R.color.colorMenuFabSelectedPressed));
                    fabOpenClose.setColorRipple(getResources().getColor(R.color.colorMenuFabSelectedRipple));
                    fabOpenClose.setImageResource(R.drawable.closedloop);

                    isPathClosed = true;
                }

                RefreshDronePath();
            }
        });

        fabSendMission.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendMission();
            }
        });

        //Ajout des boutons à la liste pour la désactivation
        floatingActionButtonList = new ArrayList<>();

        floatingActionButtonList.add(fabRemoveSelectedMarker);
        floatingActionButtonList.add(fabAddMarker);
        floatingActionButtonList.add(fabZone);
    }

    private List<FloatingActionButton> floatingActionButtonList;
    private void ChangeMenuButtonsStatus(Boolean enabled)
    {
        for(FloatingActionButton button : floatingActionButtonList)
            button.setEnabled(enabled);
    }

    private void RefreshOpenClosePathButtonStatus()
    {
        FloatingActionButton openCloseButton = view.findViewById(R.id.fabMenu_openClosePath);
        if (markers.size() < 3) {
            openCloseButton.setEnabled(false);
        } else {
            openCloseButton.setEnabled(true);
        }
    }

    // =================================================================== //INTERFACE

    // =================================================================== MAP

    //Style
    private final int STROKE_WIDTH = 3;

    //Selected Drone
    private DroneDTO selectedDrone;

    //Drone List
    private List<DroneInfoUpdateMessage> drones;
    private Map<Long, Marker> droneMarkersById;

    //Mission
    private MissionDTO currentMission;

    //Selected
    private Marker selectedMarker;

    //Path Markers
    private List<Marker> markers;
    private Map<String, Integer> markersIndexByName;
    private List<LatLng> markersPositions;

    //Path Line
    private Polyline polyline;

    /**
     * Initialise la Carte avec les listeners d'évènements
     */
    private void InitMap()
    {
        //Initialisation des collections
        markers = new ArrayList<>();
        markersIndexByName = new HashMap<>();
        markersPositions = new ArrayList<>();

        drones = new ArrayList<>();
        droneMarkersById = new HashMap<>();

        //InfoWindow Custom?
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getContext()));

        //Desactivation du click sur l'infowindow
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });

        //Desactivation de la barre d'outils
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        //Ajout des markers
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng)
            {
                //Si on est pas en mode ajout de points, on ne fait rien
                if(!isAddButtonEnabled)
                    return;

                //Creation du nouveau marker avec son icone associée
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Point de passage : " + (markers.size()))
                        .draggable(true));
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_trim));

                markersIndexByName.put(marker.getTitle(), markers.size());
                markers.add(marker);
                markersPositions.add(latLng);

                RefreshOpenClosePathButtonStatus();
                RefreshDronePath();
            }
        });

        //Drag and drop
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            private int index; // -1 si Marker non présent dans les points

            @Override
            public void onMarkerDragStart(Marker marker) {
                if(markersIndexByName.containsKey(marker.getTitle()))
                    index = markersIndexByName.get(marker.getTitle());
                else
                    index = -1;
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                if(index == -1)
                    return;

                markersPositions.remove(index);
                markersPositions.add(index , marker.getPosition());

                RefreshDronePath();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                //Déplacement d'un marker qui ne fait pas partie du path drone
                if(index == -1)
                    return;

                //Déplacement du marker vers la nouvelle position
                markers.get(index).setPosition(marker.getPosition());

                RefreshDronePath();
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedMarker = marker;
                return false;
            }
        });

        RefreshOpenClosePathButtonStatus();

        //Dessin des traits topographiques a faire faire par l'activité
    }

    /**
     * Met a jour le chemin du drone en suivant les markers positionnés sur la carte
     */
    public void RefreshDronePath()
    {
        if (polyline != null) {
            polyline.remove();
        }

        if(markersPositions.isEmpty())
            return;

        PolylineOptions lineOptions = new PolylineOptions();
        List<LatLng> listToUse = new ArrayList<>(markersPositions);

        if (isPathClosed)
        {
            // On reajoute le dernier
            listToUse.add(markersPositions.get(0));
            lineOptions.color(Color.GREEN);
        }
        else
            lineOptions.color(Color.BLUE);

        // Dessine le Polygône sur notre Google Maps
        lineOptions
                .addAll(listToUse)
                .width(STROKE_WIDTH);

        // Ajoute le polygône sur la map
        polyline = googleMap.addPolyline(lineOptions);
    }

    /**
     * Supprime le marker passé en paramètres de la carte et des collections
     */
    public void DeleteMarker(Marker marker)
    {
        if (marker == null)
            return;

        // on récupère l'index du marqueur
        String markerTitle = marker.getTitle();
        Integer matchMarker = markersIndexByName.get(markerTitle);

        if (matchMarker != null) {
            int index = matchMarker;

            // on retire le markers[index] de la map
            marker.remove();

            // Supprime le marker[index] de la liste des marqueurs sur la Google Map
            markers.remove(marker);
            markersIndexByName.remove(markerTitle);
            markersPositions.remove(index);

            //Réindexation nécéssaire

            //drawPolygon();
            RefreshDronePath();
        }

        // l'ancien marqueur n'est plus sélectionné
        selectedMarker = null;

        RefreshOpenClosePathButtonStatus();
    }

    // =================================================================== //MAP


    // =================================================================== MISSION

    private void SendMission()
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("Attention");
        alertBuilder.setMessage("Voulez-vous vraiment envoyer les données de cette mission au drône ?");
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Toast.makeText(getContext(), "Tmp : Le message a été envoyé", Toast.LENGTH_SHORT);
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        currentMission = new MissionDTO();
                        currentMission.setInterventionId(1l);
                        currentMission.setNbIteration(0);
                        currentMission.setDroneId(1l);
                        currentMission.setBoucleFermee(isPathClosed);
                        Set<PointMissionDTO> points = new HashSet<>();

                        long index = 0;
                        for (Marker marker : markers) {
                            PointMissionDTO point = new PointMissionDTO();
                            point.setIndex(index);
                            point.setAction(false);
                            point.setLatitude(marker.getPosition().latitude);
                            point.setLongitude(marker.getPosition().longitude);
                            points.add(point);
                            index++;
                        }
                        currentMission.setDronePositions(points);

                        //Lancement de la mission via Rest
                        AsyncTask.execute(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                RestTemplate restTemplate = RestTemplate.getInstance();
                                DroneMissionConsumer dmc = restTemplate.builConsumer(DroneMissionConsumer.class);
                                String token = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", null);
                                try
                                {
                                    retrofit2.Response<MissionDTO> response = dmc.createMission(token, currentMission).execute();
                                    if(response.code() != HttpURLConnection.HTTP_OK)
                                    {
                                        Log.i("Mission", Integer.toString(response.code()));
                                    }
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        alertBuilder.create().show();
    }

    private void UpdateCurrentMission()
    {
        String token = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        if(token.equals("null"))
        {
            return;
        }

        MissionDTO dto = MapService.getInstance().getCurrentDroneMission(token, selectedDrone.getId());
        ClearActualMission();
        if(dto.getId() == null)//Pas de mission en cours, DTO vide
            return;

        //DTO contient une mission, on la met a jour
        SetActualMission(dto);
    }

    private void ClearActualMission()
    {
        for(Marker marker : markers)
            DeleteMarker(marker);

        markers.clear();
        markersIndexByName.clear();
        selectedMarker = null;
    }

    private void SetActualMission(MissionDTO mission)
    {
        final List<PointMissionDTO> points = new ArrayList<>(mission.getDronePositions());
        Collections.sort(points, new Comparator<PointMissionDTO>()
        {
            public int compare(PointMissionDTO item1, PointMissionDTO item2)
            {
                return item1.getIndex().compareTo(item2.getIndex());
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                //Creation des markers pour chacun des points
                int index = 0;
                for (PointMissionDTO point : points)
                {
                    Marker marker = googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(point.getLatitude(), point.getLongitude()))
                            .title("Point de passage : " + (index))
                            .draggable(true));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_trim));

                    markersIndexByName.put(marker.getTitle(), index);
                    markers.add(marker);
                }

                RefreshOpenClosePathButtonStatus();
                RefreshDronePath();
            }
        });
    }

    // =================================================================== //MISSION

    // =================================================================== EVENT

    @Subscribe
    public void OnSelectedDroneChangedEvent(final SelectedDroneChangedMessage message)
    {
        if(selectedDrone == null) //Vérification de la première selection
            selectedDrone = message.Drone;

        else if(selectedDrone.getId().equals(message.Drone.getId())) //Si c'est le même drone, on ne fait rien
                return;

        //Sinon, mise a jour du drone Selectionné et récupération de la mission en cours
        selectedDrone = message.Drone;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Marker drone = droneMarkersById.get(message.Drone.getId());
                if(drone != null)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(drone.getPosition(), 18.0f));

                UpdateCurrentMission();
            }
        });
    }

    @Subscribe
    public void OnDroneInfoUpdateEvent(final DroneInfoUpdateMessage message)
    {
        //Si le drone n'existe pas, on l'ajoute
        if(!DroneAlreadyExist(message.getDroneId()))
        {
            drones.add(message);
            AddNewDroneOnMap(message);
        }
        else //Sinon, mise a jour du drone
            UpdateDroneOnMap(message);
    }

    private boolean DroneAlreadyExist(long droneId)
    {
        if(selectedDrone != null && selectedDrone.getId().equals(droneId))
            return true;

        for(DroneInfoUpdateMessage droneInfo : drones)
        {
            if(droneInfo.getDroneId() == droneId)
                return true;
        }
        return false;
    }

    private void UpdateDroneOnMap(final DroneInfoUpdateMessage message)
    {
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Marker droneMarker = droneMarkersById.get(message.getDroneId());
                droneMarker.setPosition(new LatLng(message.getLatitude(), message.getLongitude()));
                droneMarker.setRotation((float)Math.toDegrees(message.getYawOrientation()) - googleMap.getCameraPosition().bearing);
            }
        });
    }

    private void AddNewDroneOnMap(final DroneInfoUpdateMessage message)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                Marker droneMarker = googleMap.addMarker(new MarkerOptions().
                        position(new LatLng(message.getLatitude(), message.getLongitude()))
                        .rotation((float)Math.toDegrees((float)message.getYawOrientation()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                        .anchor(0.5f,0.5f)
                        .draggable(false));

                droneMarkersById.put(message.getDroneId(), droneMarker);
            }
        });
    }

    // =================================================================== //EVENT
}
