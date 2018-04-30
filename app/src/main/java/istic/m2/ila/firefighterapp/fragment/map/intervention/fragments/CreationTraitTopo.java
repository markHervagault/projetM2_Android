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

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;
import istic.m2.ila.firefighterapp.fragment.map.intervention.adapter.ComposanteAdapter;

/**

 * to handle interaction events.
 * Use the {@link CreationTraitTopo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreationTraitTopo extends Fragment implements IManipulableFragment {

    private TraitTopoDTO traitTopo;

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

    public CreationTraitTopo() {
        traitTopo = new TraitTopoDTO();
    }

    public static CreationTraitTopo newInstance() {
        CreationTraitTopo fragment = new CreationTraitTopo();
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

        ButtonFactory.populate(this,traitTopo,(LinearLayout)view.findViewById(R.id.layoutButton));

        initSpinner();
        return view;
    }

    private void initSpinner(){
        typeSpinner.setAdapter(new ComposanteAdapter(this.getActivity(), android.R.layout.simple_spinner_item));
        typeSpinner.setAdapter(new ArrayAdapter<ETypeTraitTopo>(this.getActivity(), android.R.layout.simple_spinner_item, ETypeTraitTopo.values()));
        composanteSpinner.setAdapter(new ComposanteAdapter(this.getActivity(), android.R.layout.simple_spinner_item));
    }

    @Override
    public void create() {
        TraitTopoDTO traitCreated = new TraitTopoDTO();
        traitCreated.setType((ETypeTraitTopo)typeSpinner.getSelectedItem());
        traitCreated.setComposante((TypeComposanteDTO) composanteSpinner.getSelectedItem());
        traitCreated.setPosition(newGeoposition);
        traitCreated.setInterventionId(((MapActivity)getMeActivity()).getIdIntervention());
        ((MapActivity)getMeActivity()).getService().addTraitTopo(((MapActivity)getMeActivity()).getToken(),traitCreated);

        if(marker != null){
            marker.remove();
        }
    }

    @Override
    public void update() {
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
