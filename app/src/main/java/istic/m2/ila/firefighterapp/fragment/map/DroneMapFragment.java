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
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragmentItems.DroneMissionDrawing;
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

    private DroneMissionDrawing missionDrawing;

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

                //Initialise la carte et le menu
                InitMap();
                InitMenu();
            }
        });

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
                missionDrawing.DeleteSelectedMarker();
                RefreshOpenClosePathButtonStatus();
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
                    missionDrawing.setEditMode(true);
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
                    missionDrawing.setEditMode(false);
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
            }
        });

        fabSendMission.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                missionDrawing.SendMission(1l, 1l, 0);
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
        if (missionDrawing.getMarkersCount() < 3) {
            openCloseButton.setEnabled(false);
        } else {
            openCloseButton.setEnabled(true);
        }
    }

    // =================================================================== //INTERFACE

    // =================================================================== MAP

    /**
     * Initialise la Carte avec les listeners d'évènements
     */
    private void InitMap()
    {
        missionDrawing = new DroneMissionDrawing(googleMap, getActivity());
        RefreshOpenClosePathButtonStatus();
    }

    // =================================================================== //MAP

    // =================================================================== EVENT

    @Subscribe
    public void OnSelectedDroneChangedEvent(final SelectedDroneChangedMessage message)
    {

    }

    @Subscribe
    public void OnDroneInfoUpdateEvent(final DroneInfoUpdateMessage message)
    {

    }

    // =================================================================== //EVENT
}
