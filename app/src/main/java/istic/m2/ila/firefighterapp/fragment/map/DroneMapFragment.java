package istic.m2.ila.firefighterapp.fragment.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.CustomInfoWindowAdapter;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.MissionDTO;

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
                //SendMission
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

    //Drone
    private DroneDTO selectedDrone;
    private Marker droneMarker;

    //Mission
    private MissionDTO currentMission;

    //Selected
    private Marker selectedMarker;

    //Path Markers
    private List<Marker> markers;
    private Map<String, Integer> markersIndexByName;

    //Path Line
    private List<LatLng> markersPositions;
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
        if (marker != null && marker != droneMarker) {
            // on récupère l'index du marqueur
            String markerTitle = marker.getTitle();
            Integer matchMarker = markersIndexByName.get(markerTitle);

            if (matchMarker != null) {
                int index = matchMarker;

                // on retire le markers[index] de la map
                marker.remove();

                // Supprime le marker[index] de la liste des marqueurs sur la Google Map
                markers.remove(index);
                markersPositions.remove(index);

                //Réindexation nécéssaire

                //drawPolygon();
                RefreshDronePath();
            }

            // l'ancien marqueur n'est plus sélectionné
            selectedMarker = null;

            RefreshOpenClosePathButtonStatus();
        }
    }

    // =================================================================== //MAP

    // =================================================================== EVENT



    // =================================================================== //EVENT
}
