package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;

public class DetailSinistreFragment extends Fragment implements IManipulableFragment {
    private static final String ARG = "data";
    private SinistreDTO sinistre;

    private Marker marker;


    private GeoPositionDTO newGeoposition = new GeoPositionDTO();

    public DetailSinistreFragment() {}

    public static DetailSinistreFragment newInstance(SinistreDTO dto) {
        DetailSinistreFragment fragment = new DetailSinistreFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG, dto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sinistre = (SinistreDTO) getArguments().getSerializable(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_sinistre, container, false);
        ((TextView)view.findViewById(R.id.typeValueSinistre)).setText(sinistre.getType().toString());
        ((TextView)view.findViewById(R.id.composanteValue)).setText(sinistre.getComposante().getDescription());

        ButtonFactory.populate(this,sinistre,(LinearLayout)view.findViewById(R.id.buttonLayout));

        return view;
    }

    @Override
    public void create() {

    }

    @Override
    public void update() {
        sinistre.setGeoPosition(newGeoposition);

        ((NewMapActivity)getMeActivity()).getService().majSinistre(((NewMapActivity)getMeActivity()).getToken(),sinistre);

        marker.remove();
    }

    @Override
    public void move() {
        GoogleMap map = ((NewMapActivity)getActivity()).getMap();

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

    @Override
    public void delete() {
        ((NewMapActivity)getMeActivity()).getService().removeSinistre(((NewMapActivity)getMeActivity()).getToken(),sinistre.getId());
    }

    @Override
    public Activity getMeActivity() {
        return this.getActivity();
    }
}
