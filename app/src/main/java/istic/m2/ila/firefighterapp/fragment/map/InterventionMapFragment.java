package istic.m2.ila.firefighterapp.fragment.map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;

public class InterventionMapFragment extends Fragment {

    public InterventionMapFragment() {
        // Required empty public constructor
    }

    MapView mMapView;
    private GoogleMap googleMap;

    public NewMapActivity getMeActivity(){
        return (NewMapActivity)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intervention_map, container, false);
        final Button button = view.findViewById(R.id.toggleView);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getMeActivity().toggleView();
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
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                initMap();
            }
        });

        return view;
    }

    private void initMap(){
        getMeActivity().initMap(googleMap);
        getTraitTopoBouchons();
        getTraitTopo();
        getSinistre();
        getVehicule();
    }

    private void getTraitTopoBouchons() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getActivity().getSharedPreferences("user", getMeActivity().getApplicationContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getMeActivity().getGeoPositionIntervention();
                List<TraitTopographiqueBouchonDTO> traits = getMeActivity().getService()
                        .getTraitTopoFromBouchon(token, geo.getLongitude(), geo.getLatitude(), getMeActivity().RAYON_RECHERCHE_TRAIT_TOPO);
                for(TraitTopographiqueBouchonDTO trait : traits) {
                    getMeActivity().drawTraitTopoBouchons(googleMap,trait);
                }
            }
        });
    }

    private void getTraitTopo() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getMeActivity().getGeoPositionIntervention();
                List<TraitTopoDTO> traits = getMeActivity().getService()
                        .getTraitTopo(token);
                for(TraitTopoDTO trait : traits) {
                    getMeActivity().drawTraitTopo(googleMap,trait);
                }
            }
        });
    }

    private void getSinistre() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getMeActivity().getGeoPositionIntervention();
                List<SinistreDTO> sinistres = getMeActivity().getService()
                        .getTraitFromBouchon(token);
                for(SinistreDTO sinistre : sinistres) {
                    getMeActivity().drawSinistre(googleMap, sinistre);
                }
            }
        });
    }

    private void getVehicule() {
        AsyncTask.execute(new Runnable() {
            public void run() {
                String token = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getMeActivity().getGeoPositionIntervention();
                List<DeploiementDTO> deploys = getMeActivity().getService()
                        .getDeploy(token);
                for(DeploiementDTO deploy : deploys) {
                    getMeActivity().drawVehicule(googleMap,deploy);
                }
            }
        });
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

}
