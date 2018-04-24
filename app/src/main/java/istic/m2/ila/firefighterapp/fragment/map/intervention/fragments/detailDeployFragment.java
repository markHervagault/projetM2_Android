package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;


public class detailDeployFragment extends Fragment {
    private static final String ARG = "data";
    private DeploiementDTO deploy;

    public detailDeployFragment() {}

    public static detailDeployFragment newInstance(DeploiementDTO dto) {
        detailDeployFragment fragment = new detailDeployFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG, dto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deploy = (DeploiementDTO) getArguments().getSerializable(ARG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_deploy, container, false);
        ((TextView)view.findViewById(R.id.status)).setText(deploy.getState().toString());
        ((TextView)view.findViewById(R.id.composante)).setText(deploy.getComposante().getDescription());
        if(deploy.getVehicule()!= null){
            ((TextView)view.findViewById(R.id.vehiculeName)).setText(deploy.getVehicule().getLabel());
            ((TextView)view.findViewById(R.id.vehiculeType)).setText(deploy.getVehicule().getType().getLabel());
        }

        return view;
    }
}
