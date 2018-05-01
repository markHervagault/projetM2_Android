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
import istic.m2.ila.firefighterapp.dto.ITraitTopo;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.map.intervention.ButtonFactory;

public class DetailTraitTopoFragment extends Fragment implements IManipulableFragment {

    private static final String ARG = "data";
    private ITraitTopo traitTopo;

    private Marker marker;

    private GeoPositionDTO newGeoposition = new GeoPositionDTO();

    public DetailTraitTopoFragment() {}

    public static DetailTraitTopoFragment newInstance(ITraitTopo dto) {
        DetailTraitTopoFragment fragment = new DetailTraitTopoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG, dto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            traitTopo = (ITraitTopo) getArguments().getSerializable(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_trait_topo, container, false);
        TextView textViewType = view.findViewById(R.id.typeValue);
        textViewType.setText(traitTopo.getType().toString());
        ((TextView)view.findViewById(R.id.composanteValue)).setText(traitTopo.getComposante().getDescription());

        ButtonFactory.populate(this,traitTopo,(LinearLayout)view.findViewById(R.id.buttonLayout));

        return view;
    }


    @Override
    public void create() {

    }

    @Override
    public void update() {
        traitTopo.setPosition(newGeoposition);

        ((MapActivity)getMeActivity()).getService().majTraitTopo(((MapActivity)getMeActivity()).getToken(),(TraitTopoDTO)traitTopo);

        marker.remove();

    }

    @Override
    public void move() {
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

    @Override
    public void delete() {
        ((MapActivity)getMeActivity()).getService().removeTraitTopo(((MapActivity)getMeActivity()).getToken(),traitTopo.getId());
    }

    @Override
    public Activity getMeActivity() {
        return this.getActivity();
    }
}
