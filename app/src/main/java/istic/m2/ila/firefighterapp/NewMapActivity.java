package istic.m2.ila.firefighterapp;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

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
}
