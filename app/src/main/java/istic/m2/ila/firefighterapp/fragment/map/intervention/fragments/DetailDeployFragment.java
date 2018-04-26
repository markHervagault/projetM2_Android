package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;


public class DetailDeployFragment extends Fragment implements IManipulableFragment{
    private static final String ARG = "data";
    private DeploiementDTO deploiementDTO;
    private GeoPositionDTO newGeoPositionDTO;
    //TODO boolean pour l'edition

    private Marker marker;
    public DetailDeployFragment() {
    }

    public static DetailDeployFragment newInstance(DeploiementDTO dto) {
        DetailDeployFragment fragment = new DetailDeployFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG, dto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deploiementDTO = (DeploiementDTO) getArguments().getSerializable(ARG);
            newGeoPositionDTO = deploiementDTO.getGeoPosition();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_deploy, container, false);
        ((TextView) view.findViewById(R.id.status)).setText(deploiementDTO.getState().toString());
        ((TextView) view.findViewById(R.id.composante)).setText(deploiementDTO.getComposante().getDescription());
        ((TextView) view.findViewById(R.id.etat)).setText(deploiementDTO.getState().name());

        if (deploiementDTO.getDateHeureDemande() != null) {
            ((TextView) view.findViewById(R.id.dateHeureDemande)).setText(deploiementDTO.getDateHeureDemande().toString());
        }
        if (deploiementDTO.getDateHeureValidation() != null) {
            ((TextView) view.findViewById(R.id.dateHeureValidation)).setText(deploiementDTO.getDateHeureValidation().toString());
        }
        if (deploiementDTO.getDateHeureEngagement() != null) {
            ((TextView) view.findViewById(R.id.dateHeureEngagement)).setText(deploiementDTO.getDateHeureEngagement().toString());
        }
        if (deploiementDTO.getDateHeureDesengagement() != null) {
            ((TextView) view.findViewById(R.id.dateHeureDesengagement)).setText(deploiementDTO.getDateHeureDesengagement().toString());
        }
        if (deploiementDTO.getVehicule() != null) {
            ((TextView) view.findViewById(R.id.vehiculeName)).setText(deploiementDTO.getVehicule().getLabel());
            ((TextView) view.findViewById(R.id.vehiculeType)).setText(deploiementDTO.getVehicule().getType().getLabel());
        }
        if(deploiementDTO.isPresenceCRM()){
            ((TextView) view.findViewById(R.id.crm)).setText("OK");
        } else{
            ((TextView) view.findViewById(R.id.crm)).setText("FAUX");
        }
        return view;
    }

    @Override
    public void create() {
        //empty method
    }

    @Override
    public void update() {
        deploiementDTO.setGeoPosition(newGeoPositionDTO);
        //deploiementDTO.setComposante();
        ((NewMapActivity)getMeActivity()).getService().majDeploiement(((NewMapActivity)getMeActivity()).getToken(), deploiementDTO);

        if(marker != null){
            marker.remove();
        }
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
