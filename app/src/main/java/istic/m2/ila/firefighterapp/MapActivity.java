package istic.m2.ila.firefighterapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import com.github.clans.fab.FloatingActionButton;

import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.IBinder;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import istic.m2.ila.firefighterapp.adapter.CustomInfoWindowAdapter;
import istic.m2.ila.firefighterapp.adapter.ItemListDroneAdapter;
import istic.m2.ila.firefighterapp.clientRabbitMQ.ServiceRabbitMQ;
import istic.m2.ila.firefighterapp.adapter.ItemListCrmAdapter;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.DroneInfoUpdateMessage;
import istic.m2.ila.firefighterapp.consumer.DroneMissionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.DroneDTO;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;
import istic.m2.ila.firefighterapp.dto.EEtatDeploiement;
import istic.m2.ila.firefighterapp.dto.ESinistre;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopographiqueBouchon;
import istic.m2.ila.firefighterapp.dto.MissionDTO;
import istic.m2.ila.firefighterapp.dto.PointMissionDTO;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Console;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.services.IMapService;
import istic.m2.ila.firefighterapp.services.impl.MapService;
import retrofit2.Response;


public class MapActivity extends FragmentActivity implements
        OnMapReadyCallback {

    //////////////////////////// VARIABLES GENERALES ///////////////////////

    /**
     * Tag qui identifie la classe pour les LOGs
     */
    private static String TAG = "MapActivity => ";

    private IMapService mapService = MapService.getInstance();

    /**
     * TRUE si l'activité est en mode drone, FALSE si elle est en mode intervention
     */
    private Boolean isOnModeDrone;

    private ServiceRabbitMQ mServiceRabbitMQ;
    private Boolean serviceRabbitMQIsBound = false;

    /**
     * La map de base affichée tout le temps
     */
    private GoogleMap mMap;

    /**
     * Liste sur le côté droit de l'activity pour afficher soit la liste des drones soit la CRM
     */
    RecyclerView listDroneOrCRM;

    // Contrôles d'interfaces
    private boolean isEnabledButtonAddPointToVisit;
    private boolean isTrajetClosed;
    private List<FloatingActionButton> fabMenuButtons;
    private final int STROKE_WIDTH = 3;

    //////////////////////// VARIABLES MODE INTERVENTION ///////////////////


    /**
     * Permet d'avoir le statut du service rabbitMQ
     */
    private static final double RAYON_RECHERCHE_TRAIT_TOPO = 5000;
    private static final Map<ETypeTraitTopographiqueBouchon,Integer> referentielTraitTopoBouchon = createReferentielTraitTopoBouchon ();
    private static Map<ETypeTraitTopographiqueBouchon,Integer> createReferentielTraitTopoBouchon(){
        Map<ETypeTraitTopographiqueBouchon,Integer> map = new HashMap<>();
        map.put(ETypeTraitTopographiqueBouchon.DANGER, R.drawable.danger_24dp);
        map.put(ETypeTraitTopographiqueBouchon.SENSIBLE, R.drawable.sensible_24dp);
        map.put(ETypeTraitTopographiqueBouchon.PDR, R.drawable.pdr_24dp);
        map.put(ETypeTraitTopographiqueBouchon.PENP, R.drawable.penp_24dp);
        map.put(ETypeTraitTopographiqueBouchon.PEP, R.drawable.pep_24dp);
        return map;
    }

    private static final Map<ETypeTraitTopo,Integer> referentielTraitTopo = createReferentielTraitTopo ();
    private static Map<ETypeTraitTopo,Integer> createReferentielTraitTopo(){
        Map<ETypeTraitTopo,Integer> map = new HashMap<>();
        map.put(ETypeTraitTopo.DANGER, R.drawable.danger_24dp);
        map.put(ETypeTraitTopo.SENSIBLE, R.drawable.sensible_24dp);
        return map;
    }

    private static final Map<EEtatDeploiement,Integer> referentielMoyen = createReferentielMoyen ();
    private static Map<EEtatDeploiement,Integer> createReferentielMoyen(){
        Map<EEtatDeploiement,Integer> map = new HashMap<>();
        map.put(EEtatDeploiement.DEMANDE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.VALIDE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.ENGAGE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.EN_ACTION, R.drawable.moyen);
        return map;
    }

    private static final Map<ESinistre,Integer> referentielSinistre = createReferentielSinistre ();
    private static Map<ESinistre,Integer> createReferentielSinistre(){
        Map<ESinistre,Integer> map = new HashMap<>();
        map.put(ESinistre.CENTRE, R.drawable.centre_sinistre);
        map.put(ESinistre.POINT, R.drawable.ic_star_black_24dp);
        map.put(ESinistre.ZONE, R.drawable.boom70x70);
        return map;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceRabbitMQ = ((ServiceRabbitMQ.LocalBinder)service).getService();
            if(isOnModeDrone){
                // initialise le layout list drone
                initDroneListFragment();
                // On récupère la liste des drones
                getDronesFromBDD();
            }else{
                // initialise le layout CRM
                initCRMFragment();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mServiceRabbitMQ = null;
        }
    };

    /**
     * Lie le service RabbitMQ à l'activity
     */
    void doBindService() {
        bindService(new Intent(this, ServiceRabbitMQ.class), mConnection, Context.BIND_AUTO_CREATE);
        serviceRabbitMQIsBound = true;

    }

    /**
     * Détache le service RabbitMQ de l'activity
     */
    void doUnbindService() {
        if (serviceRabbitMQIsBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            serviceRabbitMQIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ////////////////////////////////////////////////////////
        ////////// On démarre l'activité en mode drone /////////
        isOnModeDrone = true;
        ////////////////////////////////////////////////////////

        // On s'enregistre pour écouter sur le bus
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_map);

        // Obtenir le SupportMapFragment et être notifié quand la map est prête à être utilisée.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Liste des marqueurs
        markers = new ArrayList<>();

        // Liste de coordonnées des marqueurs
        markersLatLngPolygon = new ArrayList<>();
        markersLatLngPolylines = new ArrayList<>();

        // Map pour retrouver l'index d'un marqueur à partir de ses coordonnées
        indexMarkers2 = new HashMap<>();

        isEnabledButtonAddPointToVisit = false;

        // Initialisation des éléments du menu
        initMenuFlottant();

        // partie service rabbitMQ
        doBindService();


        // init referentiel icône
        final Map<ETypeTraitTopographiqueBouchon,Integer> referentiel = new HashMap<>();

        DroneDTO droneAdd = new DroneDTO();
        droneAdd.setId((long)51515);
        droneAdd.setStatut(EDroneStatut.DECONNECTE);
        droneAdd.setNom("Le drone du turfu");
        drones.add(droneAdd);

    }

    @Subscribe
    public void onEvent(final DroneInfoUpdateMessage message)
    {
        if(droneSelected==null || droneSelected.getId()!=message.getDroneId()){
            return;
        }
        Log.d(TAG, "======================================================================== j'ai recu les infos du drone n° : " + message.getDroneId());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mDrone==null){
                    mDrone = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(message.getLatitude(), message.getLongitude()))
                            .rotation((float)Math.toDegrees((float)message.getYawOrientation()))
                            .title(droneSelected.getNom())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                            .anchor(0.5f,0.5f)
                            .draggable(false));
                }else{
            mDrone.setPosition(new LatLng(message.getLatitude(), message.getLongitude()));
                    mDrone.setRotation((float)Math.toDegrees((float)message.getYawOrientation()));
                }
            }
        });
    }

    /**
     * Appel le REST pour récupérer tout les drones présents en BDD
     */
    private void getDronesFromBDD(){
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                drones.addAll(mapService.getDrone(token));

            }
        });
    }

    public void onClickOnDrone(DroneDTO drone){
        // Afficher le marqueur du Drone sur la map
        /*if(drone.getId()==droneSelected.getId()){
            droneSelected = null;
        }*/
        droneSelected = drone;
        if(null!=mDrone){
            mDrone.remove();
            mDrone = null;
        }
        if(drone.getStatut()!=EDroneStatut.DECONNECTE){
            Toast.makeText(getApplicationContext(), "Recherche la position de "+ drone.getNom(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), drone.getNom()+ " est déconnecté, position inconnue", Toast.LENGTH_SHORT).show();
        }
    }

    protected void initCRMFragment(){

        listDroneOrCRM = findViewById(R.id.recycler_list_map);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listDroneOrCRM.setLayoutManager(mLayoutManager);
        listDroneOrCRM.setItemAnimator(new DefaultItemAnimator());

        // On peuple notre RecyclerView
        List<Map<String, String>> myDataset = getSampleDataToTest();
        RecyclerView.Adapter mAdapter = new ItemListCrmAdapter(myDataset);
        listDroneOrCRM.setAdapter(mAdapter);
    }

    protected void initDroneListFragment(){

        listDroneOrCRM = findViewById(R.id.recycler_list_map);
        // On peuple notre RecyclerView
        RecyclerView.Adapter mAdapter = new ItemListDroneAdapter(this.drones);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listDroneOrCRM.setLayoutManager(mLayoutManager);
        listDroneOrCRM.setItemAnimator(new DefaultItemAnimator());
        listDroneOrCRM.setAdapter(mAdapter);

    }

    private List<Map<String, String>> getSampleDataToTest() {
        List<Map<String, String>> myDataset = new ArrayList<>();
        // un item
        HashMap<String, String> map = new HashMap<>();

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°254");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°132");
        map.put("vehiculeTypeCrm", "SAP");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°42");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°148");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "FPT n°12");
        map.put("vehiculeTypeCrm", "FPT");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°26");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°62");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°69");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°149");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°150");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°151");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°152");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°153");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°154");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°155");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°254");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°132");
        map.put("vehiculeTypeCrm", "SAP");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°42");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°148");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "FPT n°12");
        map.put("vehiculeTypeCrm", "FPT");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°26");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°62");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSR n°69");
        map.put("vehiculeTypeCrm", "VSR");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°149");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°150");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°151");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°152");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°153");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°154");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        map = new HashMap<>();
        map.put("vehiculeIdCrm", "VSAV n°155");
        map.put("vehiculeTypeCrm", "VSAV");
        myDataset.add(map);

        return myDataset;
    }

    private void initMenuFlottant() {

        isTrajetClosed = false;

        disableButtonOpenCloseTrajet();

        // removePointToVisit
        FloatingActionButton fab = findViewById(R.id.fabMenu_removeSelectedMarker);

        // Bouton menu flottant - Ouvrir/fermer un trajet
        final FloatingActionButton fab3 = findViewById(R.id.fabMenu_openClosePath);

        // Bouton menu flottant - Tracer un trajet
        final FloatingActionButton fab2 = findViewById(R.id.fabMenu_addMarker);

        // Listener bouton - Supprimer un élément
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gestion de l'événement click pour le bouton flottant
                deleteMarker(selectedMarker);
            }
        });

        // Listener Bouton menu flottant - Tracer un trajet
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEnabledButtonAddPointToVisit) {
                    setEnabledAllFloatingButtons(false);

                    // Réactiver le BOUTON ACTUELLEMENT SELECTIONNE
                    fab2.setEnabled(true);

                    // Couleurs du focus
                    fab2.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabSelectedNormal));
                    fab2.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabSelectedPressed));
                    fab2.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabSelectedRipple));
                    isEnabledButtonAddPointToVisit = true;

                } else {
                    setEnabledAllFloatingButtons(true);

                    // Couleurs de l'unfocus
                    fab2.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabDefaultNormal));
                    fab2.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabDefaultPressed));
                    fab2.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabDefaultRipple));
                    isEnabledButtonAddPointToVisit = false;
                }
            }
        });

        // Listener Bouton menu flottant - Ouvrir/fermer un trajet
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Si le trajet est FERME
                if (isTrajetClosed) {

                    fab3.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabDefaultNormal));
                    fab3.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabDefaultPressed));
                    fab3.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabDefaultRipple));

                    // On le passe à OUVERT
//                    fab3.setLabelText(getResources().getString(R.string.map_activity_fab_menu_trajet_close));
                    fab3.setImageResource(R.drawable.openloop);
                    isTrajetClosed = false;
                }
                // Si le trajet est OUVERT
                else {

                    fab3.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabSelectedNormal));
                    fab3.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabSelectedPressed));
                    fab3.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabSelectedRipple));

                    // On le passe à FERME
//                    fab3.setLabelText(getResources().getString(R.string.map_activity_fab_menu_trajet_open));
                    fab3.setImageResource(R.drawable.closedloop);
                    isTrajetClosed = true;
                }

                drawLines();
            }
        });

        FloatingActionButton fab4 = findViewById(R.id.fabMenu_zone);

        // On sauvegarde nos boutons
        fabMenuButtons = new ArrayList<>();
        fabMenuButtons.add(fab);
        fabMenuButtons.add(fab2);
//        fabMenuButtons.add(fab3);
        fabMenuButtons.add(fab4);
    }

    private void disableButtonOpenCloseTrajet() {
        // Si on a moins de 3 points on ne peut fermer le trajet
        FloatingActionButton fab3 = findViewById(R.id.fabMenu_openClosePath);
        if (markersLatLngPolylines.size() < 3) {
            fab3.setEnabled(false);
        } else {
            fab3.setEnabled(true);
        }
    }

    /**
     * Active ou désactive tous les floating buttons du menu
     * @param isEnabled si true, false sinon
     */
    private void setEnabledAllFloatingButtons(boolean isEnabled) {
        // (Dés) activer TOUS LES boutons de menu
        FloatingActionMenu fabMenu = findViewById(R.id.menu_fab);
        int nbChilren = fabMenu.getChildCount();

        for (FloatingActionButton fab : fabMenuButtons) {
            fab.setEnabled(isEnabled);
        }
    }

    // =============================================================================================================
    // =============================================== MAP =========================================================
    // =============================================================================================================

    /**
     * Zone pour le drone
     */
    private Polygon mPolygon;
    /**
     * Trajet pour le drone
     */
    private Polyline mPolyline;
    /**
     * Marqueur qui représente le drone sélectionné
     */
    private Marker mDrone;
    /**
     * Drone sélectionné dans la liste, null si aucun drone est sélectionné
     */
    private DroneDTO droneSelected;
    private MissionDTO currentMission;
    /**
     * Liste des drones
     */
    private List<DroneDTO> drones = new ArrayList<>();

    private Map<String, Integer> indexMarkers2; // Récupérer l'index d'un marqueur

    private List<Marker> markers; // Liste des marqueurs affichés sur la Google Map
    private List<LatLng> markersLatLngPolygon; // Liste de coordonnés (LatLng) pour dessiner le polygon
    private List<LatLng> markersLatLngPolylines; // Liste de coordonnés (LatLng) pour dessiner les segments


    /**
     * Liste des couples Titre du merker / Maker
     */
    private Map<String, Integer> indexMarkerByTitle = new HashMap<>();
    /**
     *  Liste des points de passage du drone
     */
    private List<Marker> _markers = new ArrayList<>();
    /**
     * Marqueur séléctionné
     */
    private Marker selectedMarker = null;
    /**
     * Represente la liste des points qui doivent être parcourus par le drone
     */
    private List<LatLng> dronePath = new ArrayList<>();

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        mMap = googleMap;
        mPolyline = null;
        mPolygon = null;

        // Notre InfoWindow personnalisé
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));

        // On désactive le clic sur notre InfoWindow
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) { }
        });

        // On désactive la barre d'outils
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //Ajout de markers
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            /**
             * Au clic sur la map, on ajoute un marqueur à cet endroit
             */
            public void onMapClick(LatLng latLng)
            {
                //Si on est pas en mode ajout de points, on ne fait rien
                if (!isEnabledButtonAddPointToVisit)
                    return;

                //Creation du nouveau marker avec son icone associée
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Point de passage : " + (_markers.size()))
                        .draggable(true));

                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_trim));

                indexMarkerByTitle.put(marker.getTitle(),  _markers.size());
                _markers.add(marker);
                dronePath.add(latLng);

                disableButtonOpenCloseTrajet();
                drawLines();
                //drawPolygon();
            }
        });

        //Drag and Drop
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener()
        {
            private int index;

            //Mise en cache du point à modifier
            @Override
            public void onMarkerDragStart(Marker marker)
            {
                if(indexMarkerByTitle.containsKey(marker.getTitle()))
                    index = indexMarkerByTitle.get(marker.getTitle());
                else
                    index = -1;
            }

            //Pendant le mouvement, on redessine la ligne
            @Override
            public void onMarkerDrag(Marker marker)
            {
                if(index == -1)
                    return;

                dronePath.remove(index);
                dronePath.add(index , marker.getPosition());

                drawLines();
            }

            @Override
            public void onMarkerDragEnd(Marker marker)
            {
                //Déplacement d'un marker qui ne fait pas partie du path drone
                if(index == -1)
                    return;

                //Ajout du nouveau marker a la position de l'ancien
                /*_markers.add(index + 1 , marker);
                dronePath.add(index + 1 , marker.getPosition());

                //Supression de l'ancien marker
                selectedMarker = _markers.get(index);
                deleteMarker(selectedMarker);*/

                _markers.get(index).setPosition(marker.getPosition());

                drawLines();

            } // onMarkerDragEnd
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Sauvegarde le marquer sélectionné pour une action future, ex : suppression
                selectedMarker = marker;
                return false;
            }
        });

        mMap.setMaxZoomPreference(20.0f);

        // Centre l'écran sur le Drône sur RENNES ISTIC
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.115150, -1.638374), 18.0f));

//        drawPolygon();

        drawTraitTopographiquesBouchon();
        drawTraitTopo();
        drawSinistre();
        drawMoyens();
    }

    /**
     * Dessine les Véhicule de l'interv
     */
    public void drawMoyens() {

        // Récupérer les traits depuis le bouchon
        AsyncTask.execute(new Runnable() {
            public void run() {
                // Récupération du token
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");

                final List<DeploiementDTO> finalDeploy = mapService.getDeploy(token,2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (DeploiementDTO deploy: finalDeploy) {

                            if(deploy.getGeoPosition() != null) {
                                // Récupération des icônes en fonction du type (change ou change pas)
                                int rIcone = referentielMoyen.get(deploy.getState());

                                String rgbNoA = deploy.getComposante().getCouleur().substring(0, 7);
                                Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);
                                String label = "";
                                if (deploy.getState() != EEtatDeploiement.DEMANDE) {
                                    label = deploy.getVehicule().getLabel();
                                }

                                // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
                                LatLng pos = new LatLng(deploy.getGeoPosition().getLatitude(), deploy.getGeoPosition().getLongitude());
                                mMap.addMarker(new MarkerOptions()
                                        .position(pos)
                                        .title(label)
                                        .snippet(label + " - " + deploy.getComposante().getDescription())
                                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                        .draggable(false));
                            }
                        };
                    }
                });
            }
        });
    }

    /**
     * Dessine les Sinitres de l'interv
     */
    public void drawSinistre() {

        // Récupérer les traits depuis le bouchon
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");

                final List<SinistreDTO> finalSinistres = mapService.getSinistre(token,2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (SinistreDTO sinistre: finalSinistres) {

                            // Récupération des icônes en fonction du type (change ou change pas)
                            int rIcone = referentielSinistre.get(sinistre.getType());

                            String rgbNoA = sinistre.getComposante().getCouleur().substring(0,7);
                            Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);

                            // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
                            LatLng pos = new LatLng(sinistre.getGeoPosition().getLatitude(), sinistre.getGeoPosition().getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title(sinistre.getComposante().getLabel())
                                    .snippet(sinistre.getType().name() + " - " + sinistre.getComposante().getDescription())
                                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                    .draggable(false)
                            );
                        };
                    }
                });
            }
        });
    }

    /**
     * Dessine les traits topographiques de l'interv
     */
    public void drawTraitTopo() {

        // Récupérer les traits depuis le bouchon
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                final List<TraitTopoDTO> finalTraits = mapService.getTraitTopo(token,2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (TraitTopoDTO trait: finalTraits) {

                            // Récupération des icônes en fonction du type (change ou change pas)
                            int rIcone = referentielTraitTopo.get(trait.getType());

                            String rgbNoA = trait.getComposante().getCouleur().substring(0,7);
                            Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);

                            // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
                            LatLng pos = new LatLng(trait.getPosition().getLatitude(), trait.getPosition().getLongitude());
                            mMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title(trait.getComposante().getLabel())
                                    .snippet(trait.getType().name() + " - " + trait.getComposante().getDescription())
//                                    .icon(BitmapDescriptorFactory.fromResource(rIcone))
                                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                    .draggable(false));
                        };
                    }
                });
            }
        });
    }

    /**
     * Dessine les traits topographiques venant du Bouchon
     * - Ceux qui ne changent pas (PEP, PENP, PDR)
     * - Ceux qui changent ("danger", "sensibles")
     */


    private void drawTraitTopographiquesBouchon() {


        // TODO - Renseigner ces valeurs avec les coordonnées du centre de la carte actuelle
        LatLng mapCenter = mMap.getCameraPosition().target;
        final double latitude = mapCenter.latitude;
        final double longitude = mapCenter.longitude;


        // Récupérer les traits depuis le bouchon
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                final List<TraitTopographiqueBouchonDTO> finalTraits = mapService.getTraitTopoFromBouchon(token,2, longitude, latitude, RAYON_RECHERCHE_TRAIT_TOPO);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (TraitTopographiqueBouchonDTO trait: finalTraits) {

                            // Récupération des icônes en fonction du type (change ou change pas)
                            int rIcone = referentielTraitTopoBouchon.get(trait.getType());

                            // Différenciation de la couleur en fonction pour les types qui changent
                            Drawable drawableIcon= ContextCompat.getDrawable(getApplicationContext(), rIcone);

                            // Par défaut, on récupère notre ressource sous forme de Bitmap
                            Bitmap icon = BitmapFactory.decodeResource(
                                    getApplicationContext().getResources(), rIcone);

                            String rgbNoA = trait.getComposante().getCouleur().substring(0,7);

                            switch (trait.getType()) {
                                case PDR: break;
                                case PEP: break;
                                case PENP:break;
                                case DANGER:
                                    Log.i(trait.getLabel(), rgbNoA);
                                    icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);
                                    break;
                                case SENSIBLE:
                                    Log.i(trait.getLabel(), rgbNoA);
                                    icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);
                                    break;
                            }

                            // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
                            LatLng pos = new LatLng(trait.getGeoPosition().getLatitude(), trait.getGeoPosition().getLongitude());
                            Marker posMarker = mMap.addMarker(new MarkerOptions()
                                    .position(pos)
                                    .title(trait.getLabel())
                                    .snippet(trait.getType().getDescription() + " - " + trait.getComposante().getDescription())
//                                    .icon(BitmapDescriptorFactory.fromResource(rIcone))
                                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
                                    .draggable(false));
                        };
                    }
                });
            }
        });
    }

    /**
     * Helper pour convertir les Drawable en Bitmap en modifiant la couleur
     * Utile pour le changement de couleur programmatiquement
     * @param resDrawableId drawabme à utiliser
     * @param colorRequested couleur du remplissage
     * @return
     */
    @NonNull
    private Bitmap getNewBitmapRenderedWithColor(int resDrawableId, String colorRequested) {
        Bitmap icon;// Copier le bitmap et le passer en Canvas sinon on aura une exception
        icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), resDrawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(Color.parseColor(colorRequested), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);

        Canvas canvas = new Canvas(icon);
        canvas.drawBitmap(icon, 0, 0, paint);
        return icon;
    }

    /**
     * (re) dessine le Polygône montré sur Google Map
     * suivant notre liste de marqueurs
     */
    public void drawPolygon(){
/*
        mMap.clear();

        // Dessine le Polygône sur notre Google Maps
        PolygonOptions rectOptions = new PolygonOptions()
                .addAll(markersLatLngPolygon)
                .strokeColor(Color.parseColor("#7F616161"))
                .fillColor(Color.parseColor("#7FBDBDBD"))
                .strokeWidth(STROKE_WIDTH)
                .geodesic(true);

        // Ajoute le polygône sur la map
        if (!markersLatLngPolygon.isEmpty()) {
            mPolygon = mMap.addPolygon(rectOptions);
        }

        // Réindexation de tous les marqueurs
        indexMarkers = new HashMap<>();
        indexMarkers2 = new HashMap<>();

        for (Marker m : markers) {
            // Mise à jour de l'index
            indexMarkers.put(m.getPosition(), markers.indexOf(m));
            indexMarkers2.put(m.getTitle(), markers.indexOf(m));
            int previousSize = markers.size();

            // Afficher le marqueur sur la map
            Marker posMarker = mMap.addMarker(new MarkerOptions()
                    .position(m.getPosition())
                    .title(m.getTitle())
                    .title("Selected ("+ (previousSize + 1) +")")
                    .snippet(m.getSnippet())
                    .icon(BitmapDescriptorFactory.defaultMarker(R.drawable.marker_trim))
//                    .anchor(0.5f, 0.5f)
                    .draggable(m.isDraggable()));
        }*/
    }

    /**
    * (re) dessine les segments montrés sur Google Map
    * suivant notre liste de marqueurs
    */
    public void drawLines(){

         //mMap.clear();
         if (mPolyline != null) {
            mPolyline.remove();
         }

        if(dronePath.isEmpty())
            return;

         PolylineOptions lineOptions = new PolylineOptions();
         List<LatLng> listToUse = new ArrayList<>(dronePath);

         if (isTrajetClosed)
         {
             // On reajoute le dernier
             listToUse.add(dronePath.get(0));
             lineOptions.color(Color.GREEN);
         }
         else
             lineOptions.color(Color.BLUE);

        // Dessine le Polygône sur notre Google Maps
        lineOptions
            .addAll(listToUse)
            .width(STROKE_WIDTH);

        // Ajoute le polygône sur la map
        mPolyline = mMap.addPolyline(lineOptions);
    }

    /**
     * Affiche une boîte de dialogue qui permet de confirmer (ou non)
     * l'envoi des données au drone
     */
    public void confirmSendToDrone (View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MapActivity.this);

        adb.setTitle("Attention");
        adb.setMessage("Voulez-vous vraiment envoyer les données de cette mission au drône ?");

        adb.setPositiveButton("Confirm", new DialogInterface.OnClickListener()  {
            public void onClick(DialogInterface dialog, int id) {
                // TODO - envoyer les données au drône sélectionné
                Toast.makeText(getApplicationContext(), "Tmp : Le message a été envoyé", Toast.LENGTH_SHORT);

                AsyncTask.execute(new Runnable() {
                    public void run() {
                        MissionDTO currentMission = new MissionDTO();

                        // id de l'intervention
                        Long interventionId = 1l;
                        currentMission.setInterventionId(interventionId);

                        // Itération infinie
                        currentMission.setNbIteration(0);

                        // Drône sélectionné
                        Long droneId = 1l;
                        currentMission.setDroneId(droneId);

                        // Définir le type de trajet (Ouvert/Fermé)
                        currentMission.setBoucleFermee(isTrajetClosed);

                        // Liste des points à visiter
                        Set<PointMissionDTO> points = new HashSet<>();

                        // On convertit nos LatLng en PointMissionDTO
                        int size = markersLatLngPolylines.size();
                        for (int i = 0; i < size; i++) {
                            LatLng p = markersLatLngPolylines.get(i);
                            PointMissionDTO point = new PointMissionDTO();
                            point.setIndex(Long.valueOf(i));

                            // Prise de photo
                            point.setAction(false);

                            point.setLatitude(p.latitude);
                            point.setLongitude(p.longitude);

                            // Sauvegarder notre point
                            points.add(point);
                        }
                        currentMission.setDronePositions(points);

                        // Envoyer au serveur notre mission
                        RestTemplate restTemplate = RestTemplate.getInstance();
                        DroneMissionConsumer dmc = restTemplate.builConsumer(DroneMissionConsumer.class);
                        // TODO - Récupérer le token
                        String token = getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
                        Log.i("Mission", "Token : " + token);
                        try
                        {
                            Response<MissionDTO> response = dmc.createMission(token, currentMission).execute();
                            if(response.code() != HttpURLConnection.HTTP_OK)
                            {
                                Log.i("Mission", Integer.toString(response.code()));
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

//                      dmc.createMission(token, currentMission);
                    }
                });
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        adb.create().show();
    }

    /**
     * Supprime un marqueur
     * @param marker à supprimer
     */
    public void deleteMarker (Marker marker) {

        if (marker != null && marker != mDrone)
        {
            // on récupère l'index du marqueur
            String markerTitle = marker.getTitle();
            Integer matchMarker = indexMarkerByTitle.get(markerTitle);

            if (matchMarker != null)
            {
                int index = matchMarker;

                // on retire le markers[index] de la map
                marker.remove();

                // Supprime le marker[index] de la liste des marqueurs sur la Google Map
                _markers.remove(index);
                dronePath.remove(index);

                //Réindexation nécéssaire

                //drawPolygon();
                drawLines();
            }

            // l'ancien marqueur n'est plus sélectionné
            selectedMarker = null;

            disableButtonOpenCloseTrajet();
        }
    }

    @Override
    protected void onDestroy() {
        // On se désabonne du bus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        doUnbindService();
    }
}
