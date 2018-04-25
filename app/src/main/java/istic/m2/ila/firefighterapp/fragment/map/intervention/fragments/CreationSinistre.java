package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.ESinistre;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;


public class CreationSinistre extends Fragment implements IManipulableFragment {

    private SinistreDTO sinistre;

    private Spinner typeSpinner;
    private Spinner composanteSpinner;
    private EditText longitudeEditText;
    private EditText latitudeEditText;

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
        longitudeEditText = view.findViewById(R.id.longitude);
        latitudeEditText = view.findViewById(R.id.latitude);
        LinearLayout layoutButton = view.findViewById(R.id.layoutButton);
        for(Button btn : ButtonFactory.getButton(this,sinistre)){
            layoutButton.addView(btn);
        }
        initSpinner();
        return view;
    }

    private void initSpinner(){
        typeSpinner.setAdapter(new ArrayAdapter<ESinistre>(this.getActivity(), android.R.layout.simple_spinner_item, ESinistre.values()));
        List<TypeComposanteDTO> composantes = ((NewMapActivity)getActivity()).getService()
                .getTypeComposante(((NewMapActivity)getActivity()).getToken());
        composanteSpinner.setAdapter(new ArrayAdapter<TypeComposanteDTO>(getActivity(), android.R.layout.simple_list_item_1, composantes));
    }

    @Override
    public void create() {
        SinistreDTO sinistreCreated = new SinistreDTO();
        sinistreCreated.setType((ESinistre)typeSpinner.getSelectedItem());
        sinistreCreated.setComposante((TypeComposanteDTO) composanteSpinner.getSelectedItem());
        GeoPositionDTO geoposition = new GeoPositionDTO();
        geoposition.setLatitude(Double.parseDouble(latitudeEditText.getText().toString()));
        geoposition.setLongitude(Double.parseDouble(longitudeEditText.getText().toString()));
        sinistreCreated.setGeoPosition(geoposition);
        sinistreCreated.setInterventionId(((NewMapActivity)getMeActivity()).getIdIntervention());
        ((NewMapActivity)getMeActivity()).getService().addSinistre(((NewMapActivity)getMeActivity()).getToken(),sinistreCreated);
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
}
