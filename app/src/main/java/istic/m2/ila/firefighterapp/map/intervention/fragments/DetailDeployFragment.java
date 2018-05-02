package istic.m2.ila.firefighterapp.map.intervention.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.map.intervention.ButtonFactory;
import istic.m2.ila.firefighterapp.map.intervention.adapter.ComposanteAdapter;


public class DetailDeployFragment extends Fragment implements IManipulableDeployFragment{
    private static final String ARG = "data";
    private DeploiementDTO deploiementDTO;
    private GeoPositionDTO newGeoposition = new GeoPositionDTO();

    private Boolean onModif = false;
    private Boolean onMove = false;

    private LinearLayout readComposante;
    private LinearLayout modifComposante;

    private Spinner composanteSpinner;

    private Marker marker;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(marker != null){
            marker.remove();
        }
    }

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_deploy, container, false);
        ((TextView) view.findViewById(R.id.status)).setText(deploiementDTO.getState().toString());
        ((TextView) view.findViewById(R.id.composante)).setText(deploiementDTO.getComposante().getDescription());

        if (deploiementDTO.getDateHeureDemande() != null) {
            ((TextView) view.findViewById(R.id.dateHeureDemande)).setText(formater.format(deploiementDTO.getDateHeureDemande()));
        }
        if (deploiementDTO.getDateHeureValidation() != null) {
            ((TextView) view.findViewById(R.id.dateHeureValidation)).setText(formater.format(deploiementDTO.getDateHeureValidation()));
        }
        if (deploiementDTO.getDateHeureEngagement() != null) {
            ((TextView) view.findViewById(R.id.dateHeureEngagement)).setText(formater.format(deploiementDTO.getDateHeureEngagement()));
        }
        if (deploiementDTO.getDateHeureDesengagement() != null) {
            ((TextView) view.findViewById(R.id.dateHeureDesengagement)).setText(formater.format(deploiementDTO.getDateHeureDesengagement()));
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

        readComposante = view.findViewById(R.id.readComposante);
        modifComposante = view.findViewById(R.id.modifComposante);

        composanteSpinner = view.findViewById(R.id.composanteSpinner);
        composanteSpinner.setAdapter(new ComposanteAdapter(this.getActivity(), android.R.layout.simple_spinner_item));

        //int selectedPos = ((ComposanteAdapter)composanteSpinner.getAdapter()).getPosition(deploiementDTO.getComposante());
        //composanteSpinner.setSelection(selectedPos);

        ButtonFactory.populate(this, deploiementDTO, (LinearLayout)view.findViewById(R.id.buttonLayout));

        return view;
    }

    @Override
    public void create() {
        //empty method
    }

    private void switchView() {
        if(onModif){
            readComposante.setVisibility(View.GONE);
            modifComposante.setVisibility(View.VISIBLE);
        } else {
            readComposante.setVisibility(View.VISIBLE);
            modifComposante.setVisibility(View.GONE);
        }
    }

    @Override
    public void update() {
        if(onMove){
            this.onMove = false;
            deploiementDTO.setPresenceCRM(false);
            deploiementDTO.setGeoPosition(newGeoposition);
            marker.remove();
            ((MapActivity)getMeActivity()).getService().deploiementToEngage(((MapActivity)getMeActivity()).getToken(), deploiementDTO.getId());
        }

        if(onModif) {
            this.onModif = false;
            deploiementDTO.setComposante((TypeComposanteDTO) composanteSpinner.getSelectedItem());
            switchView();
        }
        ((MapActivity)getMeActivity()).getService().majDeploiement(((MapActivity)getMeActivity()).getToken(), deploiementDTO);
        ((MapActivity)getMeActivity()).hideSelf();
    }

    @Override
    public void move() {
        this.onMove = true;
        GoogleMap map = ((MapActivity)getActivity()).getMap();

        marker = map.addMarker(new MarkerOptions()
                .position(map.getCameraPosition().target)
                .draggable(true));

        newGeoposition.setLongitude(marker.getPosition().longitude);
        newGeoposition.setLatitude(marker.getPosition().latitude);

        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                                        @Override
                                        public void onMarkerDragStart(Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDrag(Marker marker) {

                                        }

                                        @Override
                                        public void onMarkerDragEnd(Marker marker) {
                                            newGeoposition.setLongitude(marker.getPosition().longitude);
                                            newGeoposition.setLatitude(marker.getPosition().latitude);
                                        }
                                    }

        );
    }

    @Override
    public void delete() {
        //nothing
    }

    @Override
    public Activity getMeActivity() {
        return this.getActivity();
    }

    @Override
    public void engage() {
        ((MapActivity)getActivity()).getService().deploiementToEngage(((MapActivity)getActivity()).getToken(),deploiementDTO.getId());
        ((MapActivity)getMeActivity()).hideSelf();
    }

    @Override
    public void action() {
        ((MapActivity)getActivity()).getService().deploiementToAction(((MapActivity)getActivity()).getToken(),deploiementDTO.getId());
        ((MapActivity)getMeActivity()).hideSelf();
    }

    @Override
    public void toCrm() {
        deploiementDTO.setPresenceCRM(true);
        ((MapActivity)getMeActivity()).getService().majDeploiement(((MapActivity)getMeActivity()).getToken(), deploiementDTO);
        ((MapActivity)getActivity()).getService().deploiementToEngage(((MapActivity)getActivity()).getToken(),deploiementDTO.getId());
        ((MapActivity)getMeActivity()).hideSelf();
    }

    @Override
    public void modif() {
        this.onModif = true;
        switchView();
    }

    @Override
    public void desengage() {
        ((MapActivity)getActivity()).getService().deploiementToDesengage(((MapActivity)getActivity()).getToken(),deploiementDTO.getId());
        ((MapActivity)getMeActivity()).hideSelf();
    }
}
