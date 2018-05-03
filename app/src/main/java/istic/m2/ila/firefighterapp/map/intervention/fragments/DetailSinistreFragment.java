package istic.m2.ila.firefighterapp.map.intervention.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.map.intervention.ButtonFactory;

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

        ((MapActivity)getMeActivity()).getService().majSinistre(((MapActivity)getMeActivity()).getToken(),sinistre);
        if (marker != null) {
            marker.remove();
        }
        ((MapActivity) getMeActivity()).hideSelf();
    }

    @Override
    public void move() {
        if(marker == null){
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(marker != null){
            marker.remove();
        }
    }

    @Override
    public void delete() {
        ((MapActivity)getMeActivity()).getService().removeSinistre(((MapActivity)getMeActivity()).getToken(),sinistre.getId());
        ((MapActivity) getMeActivity()).hideSelf();
    }

    @Override
    public Activity getMeActivity() {
        return this.getActivity();
    }
}
