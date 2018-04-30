package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.ESinistre;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;
import istic.m2.ila.firefighterapp.fragment.map.intervention.adapter.ComposanteAdapter;


public class CreationSinistre extends Fragment implements IManipulableFragment {

    private SinistreDTO sinistre;

    private Marker marker;

    private GeoPositionDTO newGeoposition = new GeoPositionDTO();

    private Spinner typeSpinner;
    private Spinner composanteSpinner;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(marker != null){
            marker.remove();
        }
    }

    public CreationSinistre() {
        sinistre = new SinistreDTO();
    }


    public static CreationSinistre newInstance() {
        CreationSinistre fragment = new CreationSinistre();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creation_trait_topo, container, false);
        typeSpinner = view.findViewById(R.id.typeDropDown);
        composanteSpinner = view.findViewById(R.id.composanteDropDown);

        view.findViewById(R.id.createMarker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMarker();
            }
        });

        ButtonFactory.populate(this,sinistre,(LinearLayout)view.findViewById(R.id.layoutButton));

        initSpinner();
        return view;
    }

    private void initSpinner(){
        typeSpinner.setAdapter(new ArrayAdapter<ESinistre>(this.getActivity(), android.R.layout.simple_spinner_item, ESinistre.values()));
        List<TypeComposanteDTO> composantes = ((MapActivity)getActivity()).getService()
                .getTypeComposante(((MapActivity)getActivity()).getToken());
        composanteSpinner.setAdapter(new ComposanteAdapter(this.getActivity(), android.R.layout.simple_spinner_item));
    }

    @Override
    public void create() {
        SinistreDTO sinistreCreated = new SinistreDTO();
        sinistreCreated.setType((ESinistre)typeSpinner.getSelectedItem());
        sinistreCreated.setComposante((TypeComposanteDTO) composanteSpinner.getSelectedItem());
        sinistreCreated.setGeoPosition(newGeoposition);
        sinistreCreated.setInterventionId(((MapActivity)getMeActivity()).getIdIntervention());
        ((MapActivity)getMeActivity()).getService().addSinistre(((MapActivity)getMeActivity()).getToken(),sinistreCreated);

        if(marker != null){
            marker.remove();
        }
        ((MapActivity)getMeActivity()).hideSelf();
    }

    @Override
    public void update() {
        sinistre.getGeoPosition().setLatitude(marker.getPosition().latitude);
        sinistre.getGeoPosition().setLongitude(marker.getPosition().longitude);

        ((MapActivity)getMeActivity()).getService().majSinistre(((MapActivity)getMeActivity()).getToken(),sinistre);

        marker.remove();
    }

    @Override
    public void move() {

    }

    @Override
    public void delete() {

    }

    @Override
    public Activity getMeActivity() {
        return this.getActivity();
    }

    private void createMarker() {
        GoogleMap map = ((MapActivity)getActivity()).getMap();

        marker = map.addMarker(new MarkerOptions()
                .position(map.getCameraPosition().target)
                .draggable(true));

        newGeoposition.setLongitude(marker.getPosition().longitude);
        newGeoposition.setLatitude(marker.getPosition().latitude);

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                        @Override
                                        public void onMarkerDragStart(Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDrag(Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDragEnd(Marker marker) {
                                            newGeoposition.setLongitude(marker.getPosition().longitude);
                                            newGeoposition.setLatitude(marker.getPosition().latitude);
                                        }
                                    }

        );
    }
}
