package istic.m2.ila.firefighterapp;

import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import com.github.clans.fab.FloatingActionButton;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import istic.m2.ila.firefighterapp.adapter.CustomInfoWindowAdapter;
import istic.m2.ila.firefighterapp.clientRabbitMQ.ServiceRabbitMQDrone;
import istic.m2.ila.firefighterapp.adapter.ItemListCrmAdapter;
import istic.m2.ila.firefighterapp.adapter.ItemListInterventionAdapter;
import istic.m2.ila.firefighterapp.consumer.DroneMissionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOError;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Response;


public class MapActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;
    private Polygon mPolygon;
    private Polyline mPolyline;

    private Map<LatLng, Integer> indexMarkers; // Récupérer l'index d'un marqueur
    private Map<String, Integer> indexMarkers2; // Récupérer l'index d'un marqueur

    private List<Marker> markers; // Liste des marqueurs affichés sur la Google Map
    private List<LatLng> markersLatLngPolygon; // Liste de coordonnés (LatLng) pour dessiner le polygon
    private List<LatLng> markersLatLngPolylines; // Liste de coordonnés (LatLng) pour dessiner les segments

    // représente le marqueur courant sur lequel on a cliqué
    private Marker selectedMarker;

    // Marqueur de position du Drone
    private Marker mDrone;

    // Coordinates
    private static final LatLng RENNES = new LatLng(48.0833, -1.6833);
    private static final LatLng UNIVERSITE_RENNES_1 = new LatLng(48.114182, -1.636238);
    private static final LatLng RENNES_ISTIC = new LatLng(48.115150, -1.638374);

    // Contrôles d'interfaces
    private boolean isEnabledButtonAddPointToVisit;
    private boolean isTrajetClosed;
    private List<FloatingActionButton> fabMenuButtons;
    private final int STROKE_WIDTH = 3;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // initialise le layout CRM
        initCRMFragment();

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
        indexMarkers = new HashMap<>();
        indexMarkers2 = new HashMap<>();

        isEnabledButtonAddPointToVisit = false;

        // Initialisation des éléments du menu
        initMenuFlottant();

        startService(new Intent(this, ServiceRabbitMQDrone.class));
    }

    private void initCRMFragment(){

        mRecyclerView = findViewById(R.id.recycler_list_crm);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // On peuple notre RecyclerView
        List<Map<String, String>> myDataset = getSampleDataToTest();
        mAdapter = new ItemListCrmAdapter(myDataset);

        mRecyclerView.setAdapter(mAdapter);
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
        FloatingActionButton fab = findViewById(R.id.fab_menu_removePointToVisit);

        // Bouton menu flottant - Ouvrir/fermer un trajet
        final FloatingActionButton fab3 = findViewById(R.id.fab_menu_trajet_open_close);

        // Bouton menu flottant - Tracer un trajet
        final FloatingActionButton fab2 = findViewById(R.id.fab_menu_addPointToVisit);

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

        FloatingActionButton fab4 = findViewById(R.id.fab_menu_tracer_zone);

        // On sauvegarde nos boutons
        fabMenuButtons = new ArrayList<>();
        fabMenuButtons.add(fab);
        fabMenuButtons.add(fab2);
//        fabMenuButtons.add(fab3);
        fabMenuButtons.add(fab4);
    }

    private void disableButtonOpenCloseTrajet() {
        // Si on a moins de 3 points on ne peut fermer le trajet
        FloatingActionButton fab3 = findViewById(R.id.fab_menu_trajet_open_close);
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
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

        // Ajout du marqueur de positon du drône aux coordonnées RENNES_ISTIC
        mDrone = mMap.addMarker(new MarkerOptions()
                .position(RENNES_ISTIC)
                .title("Drone")
                .snippet("Iris Plus qui coûte 2K€")
                .draggable(false) // Ce marqueur ne peut être déplacé
                // On change la couleur du marqueur : ROSE
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                .anchor(0.5f,0.5f)
        );


        // On désactive la barre d'outils
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Listener pour le Drag & Drop
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

                // Récupérer l'index de l'ancien marker
                String oldMarkerTitle = marker.getTitle();
                Integer matchMarker = indexMarkers2.get(oldMarkerTitle);
                if (matchMarker != null) {
                    int index = matchMarker;

                    // Ajouter le nouveau marqueur à index + 1
                    markers.add(index + 1, marker);
                    markersLatLngPolygon.add(index + 1, marker.getPosition());
                    markersLatLngPolylines.add(index + 1, marker.getPosition());

                    // Supprimer l'ancien marqueur
                    selectedMarker = markers.get(index);
                    deleteMarker(selectedMarker);
                }
            } // onMarkerDragEnd
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            /**
             * Au clic sur la map, on ajoute un marqueur à cet endroit
             */
            public void onMapClick(LatLng latLng) {
                if (isEnabledButtonAddPointToVisit) {
                    int previousSize = markers.size();
                    Marker newMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Selected ("+ (previousSize + 1) +")")
                            .draggable(true));


                    indexMarkers.put(latLng, previousSize);
                    indexMarkers2.put(newMarker.getTitle(), previousSize);
                    markers.add(newMarker);
                    markersLatLngPolygon.add(latLng);
                    markersLatLngPolylines.add(latLng);

                    disableButtonOpenCloseTrajet();

//                    drawPolygon();
                    drawLines();
                }
            }
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

        // Centre l'écran sur le Drône
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(RENNES));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(RENNES_ISTIC, 18.0f));

//        drawPolygon();
    }

    /**
     * (re) dessine le Polygône montré sur Google Map
     * suivant notre liste de marqueurs
     */
    public void drawPolygon(){

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
        }

        // Afficher le marqueur du Drone sur la map
        mDrone = mMap.addMarker(new MarkerOptions()
                .position(mDrone.getPosition())
                .title(mDrone.getTitle())
                .snippet(mDrone.getSnippet())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                .anchor(0.5f,0.5f)
                .draggable(mDrone.isDraggable()));
    }

    /**
    * (re) dessine les segments montrés sur Google Map
    * suivant notre liste de marqueurs
    */
    public void drawLines(){

         mMap.clear();
         if (mPolyline != null) {
            mPolyline.remove();
         }

         PolylineOptions lineOptions = new PolylineOptions();
         List<LatLng> listToUse = new ArrayList<>(markersLatLngPolylines);
         if (isTrajetClosed) {
             // On reajoute le dernier
             if (!markersLatLngPolylines.isEmpty()) {
                 listToUse.add(markersLatLngPolylines.get(0));
             }
             lineOptions.color(Color.GREEN);
         } else {
             lineOptions.color(Color.BLUE);
         }

        // Dessine le Polygône sur notre Google Maps
        lineOptions
            .addAll(listToUse)
            .width(STROKE_WIDTH);

        // Ajoute le polygône sur la map
        if (!markersLatLngPolylines.isEmpty()) {
            mPolyline = mMap.addPolyline(lineOptions);
        }

        // Réindexation de tous les marqueurs
        indexMarkers = new HashMap<>();
        indexMarkers2 = new HashMap<>();

        for (Marker m : markers) {
            // Mise à jour de l'index
            indexMarkers.put(m.getPosition(), markers.indexOf(m));
            indexMarkers2.put(m.getTitle(), markers.indexOf(m));

            // Afficher le marqueur sur la map
            Marker posMarker = mMap.addMarker(new MarkerOptions()
                .position(m.getPosition())
                .title(m.getTitle())
                .snippet(m.getSnippet())
                .draggable(m.isDraggable()));

            posMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_trim));
        }

        // Afficher le marqueur du Drone sur la map
        mDrone = mMap.addMarker(new MarkerOptions()
                .position(mDrone.getPosition())
                .title(mDrone.getTitle())
                .snippet(mDrone.getSnippet())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drone))
                .anchor(0.5f, 0.5f)
                .draggable(mDrone.isDraggable()));
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
                            Response<MissionDTO> response = dmc.createMission("Bearer "+token,currentMission).execute();
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

        if (marker != null && marker != mDrone) {

            // on récupère l'index du marqueur
            String markerTitle = marker.getTitle();
            LatLng markerPosition = marker.getPosition();
//            Integer matchMarker = indexMarkers.get(markerPosition);
            Integer matchMarker = indexMarkers2.get(markerTitle);

            if (matchMarker != null) {
                int index = matchMarker;

                // on retire le markers[index] de la map
                markers.get(index).remove();
                marker.remove();

                // on retire le markers[index] des coordonnées sauvegardées
                markersLatLngPolygon.remove(markerPosition);
                markersLatLngPolylines.remove(markerPosition);

                // Supprime le marker[index] de la liste des marqueurs sur la Google Map
                markers.remove(index);
//                drawPolygon();
                drawLines();
            }

            // l'ancien marqueur n'est plus sélectionné
            selectedMarker = null;

            disableButtonOpenCloseTrajet();
        }
    }
}
