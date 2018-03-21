package istic.m2.ila.firefighterapp;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import istic.m2.ila.firefighterapp.adapter.CustomInfoWindowAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends FragmentActivity implements
        OnMapReadyCallback {

    private GoogleMap mMap;

    private Map<LatLng, Integer> indexMarkers; // Récupérer l'index d'un marqueur

    private List<Marker> markers; // Liste des marqueurs affichés sur la Google Map
    private List<LatLng> markersLatLng; // Liste de coordonnés (LatLng) pour dessiner le polygon

    // Quelques marqueurs de coordonnées afin de tester
    private Marker mCannes;
    private Marker mBrest;
    private Marker mLondon;

    // représente le marqueur courant sur lequel on a cliqué
    private Marker selectedMarker;

    // Marqueur de position du Drone
    private Marker mDrone;

    // Coordinates
    private static final LatLng BREST = new LatLng(48.4, -4.4833);
    private static final LatLng RENNES = new LatLng(48.0833, -1.6833);
    private static final LatLng LONDRES = new LatLng(51.5084, -0.1255);
    private static final LatLng CANNES = new LatLng(43.552849, 7.017369);
//    private static final LatLng MENTON = new LatLng(43.774483, 7.497540);
//    private static final LatLng DUNKIRK = new LatLng(51.050030, 2.397766);
//    private static final LatLng LILLE = new LatLng(50.629250, 3.057256);
//    private static final LatLng BEAUVAIS = new LatLng(49.431744, 2.089773);
//    private static final LatLng MULHOUSE = new LatLng(47.750839, 7.335888);
//    private static final LatLng BORDEAUX = new LatLng(44.836151, -0.580816);
//    private static final LatLng BOULOGNE_BILLANCOURT = new LatLng(48.843933, 2.247391);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtenir le SupportMapFragment et être notifié quand la map est prête à être utilisée.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Gestion de l'événement click pour le bouton flottant
                deleteMarker(selectedMarker);

                String fabDebugText = "Nombre de markers=" + markers.size();
                // On définit notre action spécifique
                Snackbar.make(view, fabDebugText, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Liste des marqueurs
        markers = new ArrayList<>();

        // Liste de coordonnées des marqueurs
        markersLatLng = new ArrayList<>();

        // Map pour retrouver l'index d'un marqueur à partir de ses coordonnées
        indexMarkers = new HashMap<>();
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

        // Notre InfoWindow personnalisé
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));


        // Ajout du marqueur de positon du drône aux coordonnées RENNES
        mDrone = mMap.addMarker(new MarkerOptions()
                .position(RENNES)
                .title("Drone")
                .snippet("Iris Plus qui coûte 2K€")
                .draggable(false) // Ce marqueur ne peut être déplacé
                // On change la couleur du marqueur : ROSE
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

        // Ajout d'un marqueur de positon aux coordonnées BREST
        mBrest = mMap.addMarker(new MarkerOptions()
                .position(BREST)
                .title("Marker in Brest")
                .snippet("C'est humide il paraît..")
                .draggable(true)); // Marqueur déplaçable
        indexMarkers.put(mBrest.getPosition(), markers.size());
        markers.add(mBrest);
        markersLatLng.add(BREST);

        // Ajout d'un marqueur de positon aux coordonnées CANNES
        mCannes = mMap.addMarker(new MarkerOptions()
                .position(CANNES)
                .title("Festival de Cannes")
                .snippet("Le lieu de tous les awards")
                .draggable(true)); // Marqueur déplaçable
        indexMarkers.put(mCannes.getPosition(), markers.size());
        markers.add(mCannes);
        markersLatLng.add(CANNES);

        // Ajout d'un marqueur de positon aux coordonnées LONDRES
        mLondon = mMap.addMarker(new MarkerOptions()
                .position(LONDRES)
                .title("Marker in Bordeaux")
                .snippet("La couleur ou le vin ? ...")
                .draggable(true)); // Marqueur déplaçable
        indexMarkers.put(mLondon.getPosition(), markers.size());
        markers.add(mLondon);
        markersLatLng.add(LONDRES);

        // Centre l'écran sur le Drône
        mMap.moveCamera(CameraUpdateFactory.newLatLng(RENNES));

        // On désactive la barre d'outils
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // Listener pour le Drag & Drop
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // Test - Afficher les coordonnées
                LatLng p = marker.getPosition();
                String str = "lat:" + p.latitude + " long: " + p.longitude;
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Test - Afficher les coordonnées
                LatLng p = marker.getPosition();
                String str = "lat:" + p.latitude + " long: " + p.longitude;
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            /**
             * Au clic sur la map, on ajoute un marqueur à cet endroit
             */
            public void onMapClick(LatLng latLng) {
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Selected")
                        .draggable(true));

                indexMarkers.put(latLng, markers.size());
                markers.add(newMarker);
                markersLatLng.add(latLng);

                drawPolygon();
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

        drawPolygon();
    }

    /**
     * (re) dessine le Polygône montré sur Google Map
     * suivant notre liste de marqueurs
     */
    public void drawPolygon(){

        mMap.clear();

        // Dessine le Polygône sur notre Google Maps
        PolygonOptions rectOptions = new PolygonOptions()
                .addAll( markersLatLng )
                .strokeColor(Color.parseColor("#7F616161"))
                .fillColor(Color.parseColor("#7FBDBDBD"))
                .strokeWidth(3)
                .geodesic(true);

        // Ajoute le polygône sur la map
        if (!markersLatLng.isEmpty()) {
            mMap.addPolygon(rectOptions);
        }

        // Réindexation de tous les marqueurs
        indexMarkers = new HashMap<>();

        for (Marker m : markers) {
            // Mise à jour de l'index
            indexMarkers.put(m.getPosition(), markers.indexOf(m));

            // Afficher le marqueur sur la map
            mMap.addMarker(new MarkerOptions()
                    .position(m.getPosition())
                    .title(m.getTitle())
                    .snippet(m.getSnippet())
                    .draggable(m.isDraggable()));
        }

        // Afficher le marqueur du Drone sur la map
        mMap.addMarker(new MarkerOptions()
                .position(mDrone.getPosition())
                .title(mDrone.getTitle())
                .snippet(mDrone.getSnippet())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .draggable(mDrone.isDraggable()));
    }

    /**
     * Supprime un marqueur
     * @param marker à supprimer
     */
    public void deleteMarker (Marker marker) {

        if (marker != null && marker != mDrone) {

            // on récupère l'index du marqueur
            LatLng markerPosition = marker.getPosition();
            Integer matchMarker = indexMarkers.get(markerPosition);

            if (matchMarker != null) {
                int index = matchMarker;

                // on retire le markers[index] de la map
                markers.get(index).remove();

                // on retire le markers[index] des coordonnées sauvegardées
                markersLatLng.remove(markerPosition);

                // Supprime le marker[index] de la liste des marqueurs sur la Google Map
                markers.remove(index);
                drawPolygon();
            }

            // l'ancien marqueur n'est plus sélectionné
            selectedMarker = null;
        }
    }
}
