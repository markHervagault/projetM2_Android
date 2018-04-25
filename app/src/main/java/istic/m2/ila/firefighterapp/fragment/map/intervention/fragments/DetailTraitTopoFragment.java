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

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.ITraitTopo;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;

public class DetailTraitTopoFragment extends Fragment implements IManipulableFragment {

    private static final String ARG = "data";
    private ITraitTopo traitTopo;

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

        LinearLayout buttonLayout = view.findViewById(R.id.buttonLayout);
        for(Button btn : ButtonFactory.getButton(this,traitTopo)){
            buttonLayout.addView(btn);
        }
        return view;
    }


    @Override
    public void create() {

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
