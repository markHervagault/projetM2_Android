package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;

/**

 * to handle interaction events.
 * Use the {@link CreationTraitTopo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreationTraitTopo extends Fragment {

    public CreationTraitTopo() {
        // Required empty public constructor
    }

    public static CreationTraitTopo newInstance(TraitTopoDTO dto) {
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
        return inflater.inflate(R.layout.fragment_creation_trait_topo, container, false);
    }

    public void replace(Object obj){

    }
}
