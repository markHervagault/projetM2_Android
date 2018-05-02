package istic.m2.ila.firefighterapp.activitiy;

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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.Intervention.ActivityMoyens;
import istic.m2.ila.firefighterapp.Intervention.InterventionDetailsMoyensFragmentsTV;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopographiqueBouchon;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.IDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.map.Drone.DroneMapFragment;
import istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems.DeploiementManager;
import istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems.SinistreManager;
import istic.m2.ila.firefighterapp.map.SynchronisationMapFragmentItems.TraitTopoManager;
import istic.m2.ila.firefighterapp.map.intervention.InterventionMapFragment;
import istic.m2.ila.firefighterapp.rabbitMQ.RabbitMQDroneService;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.ServiceRabbitMQDeploiement;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.ServiceRabbitMQSinistre;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.ServiceRabbitMQTraitTopo;
import istic.m2.ila.firefighterapp.services.IMapService;
import istic.m2.ila.firefighterapp.services.impl.MapService;

public class MapActivity extends AppCompatActivity implements ActivityMoyens {

    private Boolean interventionView = true;

    //region Detail/Creation fragment
    private final int MAX_SERVICE = 4;
    private int nbServiceConnected = 0;
    private InterventionDetailsMoyensFragmentsTV intervListFrag;
    private InterventionMapFragment intervMapFrag;

    private DroneMapFragment droneMapFrag;

    public final Integer RAYON_RECHERCHE_TRAIT_TOPO = 5000;

    private IMapService service = MapService.getInstance();

    public IMapService getService() {
        return service;
    }

    public String getToken() {
        return getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                .getString("token", "null");
    }

    public GeoPositionDTO getGeoPositionIntervention() {
        return intervention.getAdresse().getGeoPosition();
    }

    private InterventionDTO intervention;

    public Long getIdIntervention() {
        return intervention.getId();
    }

    // region referentiel bitmap
    private static final Map<ETypeTraitTopographiqueBouchon, Integer> referentielTraitTopoBouchon = createReferentielTraitTopoBouchon();

    private static Map<ETypeTraitTopographiqueBouchon, Integer> createReferentielTraitTopoBouchon() {
        Map<ETypeTraitTopographiqueBouchon, Integer> map = new HashMap<>();
        map.put(ETypeTraitTopographiqueBouchon.DANGER, R.drawable.danger_24dp);
        map.put(ETypeTraitTopographiqueBouchon.SENSIBLE, R.drawable.sensible_24dp);
        map.put(ETypeTraitTopographiqueBouchon.PDR, R.drawable.pdr_24dp);
        map.put(ETypeTraitTopographiqueBouchon.PENP, R.drawable.penp_24dp);
        map.put(ETypeTraitTopographiqueBouchon.PEP, R.drawable.pep_24dp);
        return map;
    }

    //endregion

    //region ON CREATE/DESTROY
    private ServiceConnection serviceConnection;
    private ServiceConnection serviceConnectionTraitTopo;
    ServiceRabbitMQSinistre serviceRabbitMQSinistre;
    ServiceRabbitMQDeploiement serviceRabbitMQDeploiment;

    RabbitMQDroneService serviceRabbitMQ;
    ServiceRabbitMQTraitTopo serviceRabbitMQTraitTopo;
    private ServiceConnection serviceConnectionSinistre;
    private ServiceConnection serviceConnectionDeploiment;

    private boolean isServiceRabbitMQBind = false;
    private boolean isServiceRabbitMQTraitTopoBind = false;
    private boolean isServiceRabbitMQSinistreBind = false;
    private boolean isServiceRabbitMQDeploimentBind = false;

    private void BindService()
    {
        bindService(new Intent(this, RabbitMQDroneService.class), serviceConnection, Context.BIND_AUTO_CREATE );
        isServiceRabbitMQBind = true;

        bindService(new Intent(this, ServiceRabbitMQTraitTopo.class), serviceConnectionTraitTopo, Context.BIND_AUTO_CREATE );
        isServiceRabbitMQTraitTopoBind = true;

        bindService(new Intent(this, ServiceRabbitMQSinistre.class), serviceConnectionSinistre, Context.BIND_AUTO_CREATE);
        isServiceRabbitMQSinistreBind = true;

        bindService(new Intent(this, ServiceRabbitMQDeploiement.class), serviceConnectionDeploiment, Context.BIND_AUTO_CREATE);
        isServiceRabbitMQDeploimentBind = true;
    }

    private void UnBindService()
    {
        if(isServiceRabbitMQBind){
            unbindService(serviceConnection);
            isServiceRabbitMQBind = false;
        }
        if(isServiceRabbitMQTraitTopoBind){
            unbindService(serviceConnectionTraitTopo);
            isServiceRabbitMQTraitTopoBind = false;
        }
        if (isServiceRabbitMQSinistreBind) {
            unbindService(serviceConnectionSinistre);
            isServiceRabbitMQSinistreBind = false;
        }
        if (isServiceRabbitMQDeploimentBind) {
            unbindService(serviceConnectionDeploiment);
            isServiceRabbitMQDeploimentBind = false;
        }
    }

    private DeploiementManager _deploiementManager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UnBindService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intervention = getService().getIntervention(getToken(),((InterventionDTO) getIntent().getSerializableExtra("intervention")).getId());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //this.fragmentHolder = (FragmentHolder) this.getSupportFragmentManager().findFragmentById(R.id.holder_fragment);


        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceRabbitMQ = ((RabbitMQDroneService.LocalBinder) service).getService();
                initializeFragments();

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        serviceConnectionTraitTopo = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceRabbitMQTraitTopo = (ServiceRabbitMQTraitTopo) ((ServiceRabbitMQTraitTopo.LocalBinder)service).getService();
                initializeFragments();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        serviceConnectionSinistre = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceRabbitMQSinistre = (ServiceRabbitMQSinistre) ((ServiceRabbitMQSinistre.LocalBinder) service).getService();
                initializeFragments();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        serviceConnectionDeploiment = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceRabbitMQDeploiment = (ServiceRabbitMQDeploiement) ((ServiceRabbitMQDeploiement.LocalBinder) service).getService();
                initializeFragments();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        BindService();
    }

    public void toggleView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //FrameLayout frameMoyen =   findViewById(R.id.listViewFragment);


        //frameMoyen.setVisibility(View.VISIBLE);
        if (interventionView) {
            transaction.replace(R.id.mapFragment, intervMapFrag);
            //transaction.replace(R.id.listViewFragment, intervListFrag);
        } else {
            transaction.replace(R.id.mapFragment, droneMapFrag);
            //transaction.replace(R.id.listViewFragment, droneListFrag);
        }
        transaction.commit();
        interventionView = !interventionView;
    }

    //endregion
    private TraitTopoManager _traitTopoManager;
    private SinistreManager _sinistreManager;

    private void initializeFragments() {
        nbServiceConnected++;
        if (nbServiceConnected == MAX_SERVICE) {
            intervMapFrag = new InterventionMapFragment();
            droneMapFrag = new DroneMapFragment(); //Map avant drone list
            toggleView();
        }
    }

    public DeploiementManager getDeploiementManager() {
        return _deploiementManager;
    }

    public void initMap(final GoogleMap googleMap) {
        googleMap.setMaxZoomPreference(20.0f);
        // todo DONT : Centre l'écran sur le Drône sur RENNES ISTIC
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getGeoPositionIntervention().getLatitude(), getGeoPositionIntervention().getLongitude()), 18.0f));
        googleMap.setBuildingsEnabled(false); //2D pour améliorer les performances

        _sinistreManager = new SinistreManager(googleMap, this);
        _traitTopoManager = new TraitTopoManager(googleMap, this);
        _deploiementManager = new DeploiementManager(googleMap, this);

        getTraitTopoBouchons(googleMap);
        getTraitTopo(googleMap);
        getSinistre(googleMap);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Object obj = marker.getTag();
                if (obj instanceof IDTO) {
                    displayFragmentHolder((IDTO) obj);
                    getMap().moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                }
                return true;
            }

        });

    }

    public GoogleMap getMap() {
        if (interventionView) {
            return intervMapFrag.getMap();
        } else {
            return intervMapFrag.getMap();
        }
    }



    public void showFragment() {
        Log.i("Visibility", "SHOW");
        if (!this.intervMapFrag.getFragmentHolder().isVisible()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .show(intervMapFrag.getFragmentHolder())
                    .commit();
            intervMapFrag.getFragmentHolder().getView().setVisibility(View.VISIBLE);
        }
    }

    public void hideFragment() {
        Log.i("Visibility", "HIDE");
        if (intervMapFrag.getFragmentHolder().isVisible()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .hide(intervMapFrag.getFragmentHolder())
                    .commit();
            intervMapFrag.getFragmentHolder().getView().setVisibility(View.GONE);
        }
    }

    public void createMoyen() {
        hideFragment();
        intervMapFrag.getFragmentHolder().replace(new DeploiementDTO());
        showFragment();
    }

    public void hideSelf() {
        intervMapFrag.getFragmentHolder().hideSelf();
    }


    public void displayFragmentHolder(IDTO dto) {
        if (intervMapFrag.getFragmentHolder().getObjectHeld() == dto) {
            hideFragment();
            intervMapFrag.getFragmentHolder().setObjectHeld(null);
        } else if (intervMapFrag.getFragmentHolder().getObjectHeld() == null) {
            intervMapFrag.getFragmentHolder().replace(dto);
            showFragment();
        }
        else {
            hideFragment();
            intervMapFrag.getFragmentHolder().replace(dto);
            showFragment();
        }
    }

    public void createTrait() {
        hideFragment();
        intervMapFrag.getFragmentHolder().replace(new TraitTopoDTO());
        showFragment();
    }


    public void createSinistre() {
        hideFragment();
        intervMapFrag.getFragmentHolder().replace(new SinistreDTO());
        showFragment();
    }

    //endregion

    //region récupération de data

    public void getTraitTopoBouchons(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                GeoPositionDTO geo = getGeoPositionIntervention();
                List<TraitTopographiqueBouchonDTO> traits = getService()
                        .getTraitTopoFromBouchon(getToken(), getIdIntervention(), geo.getLongitude(), geo.getLatitude(), RAYON_RECHERCHE_TRAIT_TOPO);
                for (TraitTopographiqueBouchonDTO trait : traits) {
                    drawTraitTopoBouchons(googleMap, trait);
                }
            }
        });
    }

    public void getTraitTopo(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                GeoPositionDTO geo = getGeoPositionIntervention();
                List<TraitTopoDTO> traits = getService()
                        .getTraitTopo(getToken(), getIdIntervention());
                for (TraitTopoDTO trait : traits) {
                    _traitTopoManager.onCreateOrUpdateTraitTopoDTOMessageEvent(trait);
                }
            }
        });
    }

    public void getSinistre(final GoogleMap googleMap) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                List<SinistreDTO> sinistres = getService()
                        .getSinistre(getToken(), getIdIntervention());
                for (SinistreDTO sinistre : sinistres) {
                    _sinistreManager.onCreateOrUpdateSinistreDTOMessageEvent(sinistre);
                }
            }
        });
    }

    //endregion

    //region drawing

    public void drawTraitTopoBouchons(final GoogleMap googleMap, final TraitTopographiqueBouchonDTO traitTopo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Récupération des icônes en fonction du type (change ou change pas)
                int rIcone = referentielTraitTopoBouchon.get(traitTopo.getType());

                // Différenciation de la couleur en fonction pour les types qui changent
                Drawable drawableIcon = ContextCompat.getDrawable(getApplicationContext(), rIcone);

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

                // Ajout des icônes (marqueurs) sur la istic.m2.ila.firefighterapp.map en fonction de la localisation du trait
                LatLng pos = new LatLng(traitTopo.getGeoPosition().getLatitude(), traitTopo.getGeoPosition().getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(traitTopo.getLabel())
                        .snippet(traitTopo.getType().getDescription() + " - " + traitTopo.getComposante().getDescription())
                        .icon(BitmapDescriptorFactory.fromBitmap(icon))
                        .draggable(false)).setTag(traitTopo);
            }
        });
    }

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
    //endregion

}