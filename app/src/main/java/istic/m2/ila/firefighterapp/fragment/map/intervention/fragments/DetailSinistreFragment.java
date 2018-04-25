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

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.ButtonFactory;

public class DetailSinistreFragment extends Fragment implements IManipulableFragment {
    private static final String ARG = "data";
    private SinistreDTO sinistre;

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
        LinearLayout buttonLayout = view.findViewById(R.id.buttonLayout);
        for(Button btn : ButtonFactory.getButton(this,sinistre)){
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
        ((NewMapActivity)getMeActivity()).getService().removeSinistre(((NewMapActivity)getMeActivity()).getToken(),sinistre.getId());
    }

    @Override
    public Activity getMeActivity() {
        return this.getActivity();
    }
}
