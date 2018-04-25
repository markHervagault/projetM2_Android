package istic.m2.ila.firefighterapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.Intervention.InterventionDetailsMoyensFragments;
import istic.m2.ila.firefighterapp.clientRabbitMQ.ServiceRabbitMQ;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopographiqueBouchon;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.fragment.map.DroneListViewFragment;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragment;
import istic.m2.ila.firefighterapp.fragment.map.InterventionMapFragment;
import istic.m2.ila.firefighterapp.fragment.map.SynchronisationMapFragmentItems.SinistreManager;
import istic.m2.ila.firefighterapp.fragment.map.SynchronisationMapFragmentItems.TraitTopoManager;
import istic.m2.ila.firefighterapp.services.IMapService;
import istic.m2.ila.firefighterapp.services.impl.MapService;

public class NewMapActivity extends AppCompatActivity implements InterventionDetailsMoyensFragments.ActivityMoyens {

    private Boolean interventionView = true;

    private InterventionDetailsMoyensFragments intervListFrag;
    private InterventionMapFragment intervMapFrag;

    private DroneListViewFragment droneListFrag;
    private DroneMapFragment droneMapFrag;

    public final Integer RAYON_RECHERCHE_TRAIT_TOPO = 5000;

    private IMapService service = MapService.getInstance();
    public IMapService getService() {
        return service;
    }

    public GeoPositionDTO getGeoPositionIntervention() {
        GeoPositionDTO geo = new GeoPositionDTO();
        geo.setLongitude(-1.638374);
        geo.setLatitude(48.115150);
        return geo;
    }
    private InterventionDTO intervention;

    private Long idIntervention;

    public Long getIdIntervention() {
        return idIntervention;
    }

    public void setIdIntervention(Long idIntervention) {
        this.idIntervention = idIntervention;
    }

    //region ON CREATE/DESTROY
    private ServiceConnection serviceConnection;
    ServiceRabbitMQ serviceRabbitMQ;

    private boolean isServiceBound = false;

    private void BindService()
    {
        bindService(new Intent(this, ServiceRabbitMQ.class), serviceConnection, Context.BIND_AUTO_CREATE );
        isServiceBound = true;
    }

    private void UnBindService()
    {
        if(!isServiceBound)
            return;

        unbindService(serviceConnection);
        isServiceBound = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        idIntervention = ((InterventionDTO)getIntent().getSerializableExtra("intervention")).getId();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);

        serviceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                serviceRabbitMQ = ((ServiceRabbitMQ.LocalBinder)service).getService();

                intervListFrag = new InterventionDetailsMoyensFragments();
                intervMapFrag = new InterventionMapFragment();

                droneMapFrag = new DroneMapFragment(); //Map avant drone list
                droneListFrag = new DroneListViewFragment();

                toggleView();
            }

            @Override
            public void onServiceDisconnected(ComponentName name)
            {

            }
        };

        BindService();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        UnBindService();
    }

    public void toggleView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if(interventionView){
            transaction.replace(R.id.mapFragment, intervMapFrag);
            transaction.replace(R.id.listViewFragment, intervListFrag);
        } else {
            transaction.replace(R.id.mapFragment, droneMapFrag);
            transaction.replace(R.id.listViewFragment, droneListFrag);
        }
        transaction.commit();
        interventionView = !interventionView;
    }
    //endregion

    // region DessinsMap
    private TraitTopoManager _traitTopoManager;
    private SinistreManager _sinistreManager;

    public void initMap(final GoogleMap googleMap){
        googleMap.setMaxZoomPreference(20.0f);
        // Centre l'écran sur le Drône sur RENNES ISTIC
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getGeoPositionIntervention().getLatitude(), getGeoPositionIntervention().getLongitude()), 18.0f));
        googleMap.setBuildingsEnabled(false); //2D pour améliorer les performances

        _sinistreManager = new SinistreManager(googleMap, this);
        _traitTopoManager = new TraitTopoManager(googleMap, this);

        getTraitTopoBouchons(googleMap);
        getTraitTopo(googleMap);
        getSinistre(googleMap);
    }


    // region DataForDrawing
    public void getTraitTopo(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getGeoPositionIntervention();
                List<TraitTopoDTO> traits = getService()
                        .getTraitTopo(token, getIdIntervention());
                for(TraitTopoDTO trait : traits) {
                    _traitTopoManager.onCreateTraitTopoDTOMessageEvent(trait);
                }
            }
        });
    }

    public void getSinistre(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getGeoPositionIntervention();
                List<SinistreDTO> sinistres = getService()
                        .getSinistre(token, getIdIntervention());
                for(SinistreDTO sinistre : sinistres) {
                    _sinistreManager.onCreateSinistreDTOMessageEvent(sinistre);
                }
            }
        });
    }

    public void getTraitTopoBouchons(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getGeoPositionIntervention();
                List<TraitTopographiqueBouchonDTO> traits = getService()
                        .getTraitTopoFromBouchon(token, getIdIntervention(), geo.getLongitude(), geo.getLatitude(), RAYON_RECHERCHE_TRAIT_TOPO);
                for(TraitTopographiqueBouchonDTO trait : traits) {
                    drawTraitTopoBouchons(googleMap,trait);
                }
            }
        });
    }

    // endregion

    // region drawTraitTopoBouchon
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

    public void drawTraitTopoBouchons(final GoogleMap googleMap, final TraitTopographiqueBouchonDTO traitTopo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Récupération des icônes en fonction du type (change ou change pas)
                int rIcone = referentielTraitTopoBouchon.get(traitTopo.getType());

                // Par défaut, on récupère notre ressource sous forme de Bitmap
                Bitmap icon = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(), rIcone);

                String rgbNoA = traitTopo.getComposante().getCouleur().substring(0, 7);

                switch (traitTopo.getType()) {
                    case PDR:
                        break;
                    case PEP:
                        break;
                    case PENP:
                        break;
                    case DANGER:
                        icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);
                        break;
                    case SENSIBLE:
                        icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);
                        break;
                }

                // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
                LatLng pos = new LatLng(traitTopo.getGeoPosition().getLatitude(), traitTopo.getGeoPosition().getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(traitTopo.getLabel())
                        .snippet(traitTopo.getType().getDescription() + " - " + traitTopo.getComposante().getDescription())
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .draggable(false));
            }});
    }

    /**
     * Fonction pour changer la couleur d'une image
     * @param resDrawableId id de la ressource, notre image, à changer
     * @param colorRequested couleur qu'on attend
     * @return la nouvelle image avec la couleur
     */
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
    // endregion
    // endregion
}
