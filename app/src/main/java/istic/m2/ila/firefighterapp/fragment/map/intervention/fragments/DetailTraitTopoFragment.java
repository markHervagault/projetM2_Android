package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;

public class DetailTraitTopoFragment extends Fragment {

    private static final String ARG = "data";
    private TraitTopoDTO traitTopo;

    public DetailTraitTopoFragment() {}

    public static DetailTraitTopoFragment newInstance(TraitTopoDTO dto) {
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
            traitTopo = (TraitTopoDTO) getArguments().getSerializable(ARG);
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
        view.findViewById(R.id.supprimer).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTraitTopo();
            }
        });
        return view;

    }

    private void deleteTraitTopo() {

    }


}
