package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;

public class DetailSinistreFragment extends Fragment {
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
        ((TextView)this.getActivity().findViewById(R.id.typeValueSinistre)).setText(sinistre.getType().toString());
        ((TextView)this.getActivity().findViewById(R.id.composanteValue)).setText(sinistre.getComposante().getDescription());
        this.getActivity().findViewById(R.id.supprimer).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSinistre();
            }
        });
        return view;
    }

    private void deleteSinistre(){};
}
