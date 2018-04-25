package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;

/**

 * to handle interaction events.
 * Use the {@link CreationTraitTopo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreationTraitTopo extends Fragment {

    private TraitTopoDTO traitTopo;

    private Spinner typeSpinner;
    private Spinner composanteSpinner;
    private EditText longitudeEditText;
    private EditText latitudeEditText;

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
        longitudeEditText = view.findViewById(R.id.longitude);
        latitudeEditText = view.findViewById(R.id.latitude);
        LinearLayout layoutButton = view.findViewById(R.id.layoutButton);
        for(Button btn : ButtonFactory.getButton(this.getActivity(),traitTopo)){
            layoutButton.addView(btn);
        }
        initSpinner();
        return view;
    }

    private void initSpinner(){
        typeSpinner.setAdapter(new ArrayAdapter<ETypeTraitTopo>(this.getActivity(), android.R.layout.simple_spinner_item, ETypeTraitTopo.values()));
        List<TypeComposanteDTO> composantes = ((NewMapActivity)getActivity()).getService()
                .getTypeComposante(((NewMapActivity)getActivity()).getToken());
        composanteSpinner.setAdapter(new ArrayAdapter<TypeComposanteDTO>(getActivity(), android.R.layout.simple_list_item_1, composantes));
    }

    public void createTraitTopo() {
        TraitTopoDTO traitCreated = new TraitTopoDTO();

        traitCreated.setType((ETypeTraitTopo)typeSpinner.getSelectedItem());
        traitCreated.setComposante((TypeComposanteDTO) composanteSpinner.getSelectedItem());
        GeoPositionDTO geoposition = new GeoPositionDTO();
        geoposition.setLatitude(Double.parseDouble(latitudeEditText.getText().toString()));
        geoposition.setLongitude(Double.parseDouble(longitudeEditText.getText().toString()));
        traitCreated.setPosition(geoposition);
    }

}
