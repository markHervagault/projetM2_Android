package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;

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
        typeSpinner.setAdapter(new ArrayAdapter<ETypeTraitTopo>(this.getActivity(), android.R.layout.simple_spinner_item, ETypeTraitTopo.values()));
        List<TypeComposanteDTO> composantes = ((NewMapActivity)getActivity()).getService()
                .getTypeComposante(((NewMapActivity)getActivity()).getToken());
        composanteSpinner.setAdapter(new ArrayAdapter<TypeComposanteDTO>(getActivity(), android.R.layout.simple_list_item_1, composantes));
    }

    @Override
    public void create() {
        TraitTopoDTO traitCreated = new TraitTopoDTO();
        traitCreated.setType((ETypeTraitTopo)typeSpinner.getSelectedItem());
        traitCreated.setComposante((TypeComposanteDTO) composanteSpinner.getSelectedItem());
        traitCreated.setPosition(newGeoposition);
        traitCreated.setInterventionId(((NewMapActivity)getMeActivity()).getIdIntervention());
        ((NewMapActivity)getMeActivity()).getService().addTraitTopo(((NewMapActivity)getMeActivity()).getToken(),traitCreated);

        marker.remove();
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
        GoogleMap map = ((NewMapActivity)getActivity()).getMap();

        marker = map.addMarker(new MarkerOptions()
                .position(map.getCameraPosition().target)
                .draggable(true));

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
