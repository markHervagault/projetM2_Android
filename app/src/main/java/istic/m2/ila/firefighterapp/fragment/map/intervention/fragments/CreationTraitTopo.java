package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;

/**

 * to handle interaction events.
 * Use the {@link CreationTraitTopo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreationTraitTopo extends Fragment {

    private TraitTopoDTO traitTopo;

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
        Spinner spinner = view.findViewById(R.id.typeDropDown);
        return view;
    }

    public void initTypeDropDown(Spinner spinner) {
        /*ArrayAdapter<ETypeTraitTopo> adapter = ArrayAdapter;
        spinner.setAdapter(adapter);*/

    }

}
