package istic.m2.ila.firefighterapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.EEtatDeploiement;
import istic.m2.ila.firefighterapp.dto.ESinistre;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopographiqueBouchon;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import istic.m2.ila.firefighterapp.fragment.map.DroneListViewFragment;
import istic.m2.ila.firefighterapp.fragment.map.DroneMapFragment;
import istic.m2.ila.firefighterapp.fragment.map.InterventionListViewFragment;
import istic.m2.ila.firefighterapp.fragment.map.InterventionMapFragment;

public class NewMapActivity extends AppCompatActivity {

    private Boolean interventionView = true;

    private InterventionListViewFragment intervListFrag;
    private InterventionMapFragment intervMapFrag;

    private DroneListViewFragment droneListFrag;
    private DroneMapFragment droneMapFrag;

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
        /*map.put(EEtatDeploiement.DEMANDE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.VALIDE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.ENGAGE, R.drawable.moyen_prevu);
        map.put(EEtatDeploiement.EN_ACTION, R.drawable.moyen);*/
        return map;
    }

    private static final Map<ESinistre,Integer> referentielSinistre = createReferentielSinistre ();
    private static Map<ESinistre,Integer> createReferentielSinistre(){
        Map<ESinistre,Integer> map = new HashMap<>();
        /*map.put(ESinistre.CENTRE, R.drawable.centre_sinistre);
        map.put(ESinistre.POINT, R.drawable.ic_star_black_24dp);
        map.put(ESinistre.ZONE, R.drawable.boom70x70);*/
        return map;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_map);
        intervListFrag = new InterventionListViewFragment();
        intervMapFrag = new InterventionMapFragment();
        droneListFrag = new DroneListViewFragment();
        droneMapFrag = new DroneMapFragment();
        toggleView();
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

    public void initMap(final GoogleMap googleMap){
        googleMap.setMaxZoomPreference(20.0f);
        // Centre l'écran sur le Drône sur RENNES
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.1119800, -1.6742900), 18.0f));
        googleMap.setBuildingsEnabled(false); //2D pour améliorer les performances
    }

    public void drawTraitTopoBouchons(final GoogleMap googleMap, final TraitTopographiqueBouchonDTO traitTopo) {

        // Récupération des icônes en fonction du type (change ou change pas)
        int rIcone = referentielTraitTopoBouchon.get(traitTopo.getType());

        // Différenciation de la couleur en fonction pour les types qui changent
        Drawable drawableIcon= ContextCompat.getDrawable(getApplicationContext(), rIcone);

        // Par défaut, on récupère notre ressource sous forme de Bitmap
        Bitmap icon = BitmapFactory.decodeResource(
                getApplicationContext().getResources(), rIcone);

        String rgbNoA = traitTopo.getComposante().getCouleur().substring(0,7);

        switch (traitTopo.getType()) {
            case PDR: break;
            case PEP: break;
            case PENP:break;
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
    }

    public void drawTraitTopo(final GoogleMap googleMap, final TraitTopoDTO traitTopo) {

        int rIcone = referentielTraitTopo.get(traitTopo.getType());

        String rgbNoA = traitTopo.getComposante().getCouleur().substring(0,7);
        Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);

        // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
        LatLng pos = new LatLng(traitTopo.getPosition().getLatitude(), traitTopo.getPosition().getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(traitTopo.getComposante().getLabel())
                .snippet(traitTopo.getType().name() + " - " + traitTopo.getComposante().getDescription())
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .draggable(false));
    }

    public void drawTraitTopoSinistre(final GoogleMap googleMap, final SinistreDTO sinistre) {
        int rIcone = referentielSinistre.get(sinistre.getType());

        String rgbNoA = sinistre.getComposante().getCouleur().substring(0,7);
        Bitmap icon = getNewBitmapRenderedWithColor(rIcone, rgbNoA);

        // Ajout des icônes (marqueurs) sur la map en fonction de la localisation du trait
        LatLng pos = new LatLng(sinistre.getGeoPosition().getLatitude(), sinistre.getGeoPosition().getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(pos)
                .title(sinistre.getComposante().getLabel())
                .snippet(sinistre.getType().name() + " - " + sinistre.getComposante().getDescription())
                .icon(BitmapDescriptorFactory.fromBitmap(icon))
                .draggable(false)
        );
    }

    public void drawTraitTopoVehicule(final GoogleMap googleMap, final DeploiementDTO deploy) {
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
            googleMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(label)
                    .snippet(label + " - " + deploy.getComposante().getDescription())
                    .icon(BitmapDescriptorFactory.fromBitmap(icon))
                    .draggable(false));
        }
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
}
