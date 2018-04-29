package istic.m2.ila.firefighterapp.Intervention;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import istic.m2.ila.firefighterapp.MapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsStaticFragment extends Fragment implements View.OnClickListener {

    private TextView statusTextView;
    private TextView addresseTextView;
    private TextView codeSinistreTextView;
    private TextView villeTextView;
    private TextView creatorTextView;
    private TextView heureCreationTextView;
    private TextView heureFinTextView;
    private Button openMap;

    //private pojo item
    private InterventionDTO interventionDTO;

    public interface ActivityDetails {
        InterventionDTO getIntervention();
    }

    public InterventionDetailsStaticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //fetch data from activity
        interventionDTO = ((ActivityDetails) getActivity()).getIntervention();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intervention_details_static, container, false);
    }

    @Override
    public void onClick(View v){
        Intent redirectIntent = new Intent(getActivity(), MapActivity.class);

        redirectIntent.putExtra("intervention", interventionDTO);
        startActivity(redirectIntent);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Link Button
        openMap = getActivity().findViewById(R.id.button_map);
        openMap.setOnClickListener(this);

        //Link fields
        statusTextView = getActivity().findViewById(R.id.status_textview);
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);
        codeSinistreTextView = getActivity().findViewById(R.id.code_sinistre_textview);
        villeTextView = getActivity().findViewById(R.id.ville_textview);
        creatorTextView = getActivity().findViewById(R.id.creator_textview);
        heureCreationTextView = getActivity().findViewById(R.id.heure_creation_textview);
        heureFinTextView = getActivity().findViewById(R.id.heure_fin_textview);

        String statut = (interventionDTO.isFini()) ?
                getResources().getString(R.string.intervention_detail_fragment_static_button_fini) :
                getResources().getString(R.string.intervention_detail_fragment_static_status_encours);

        statusTextView.setText(statut);

        if(interventionDTO.getAdresse() != null) {
            String address = interventionDTO.getAdresse().getNumero().toString()
                    + " " + interventionDTO.getAdresse().getVoie();
            addresseTextView.setText(address);
            villeTextView.setText(interventionDTO.getAdresse().getVille());
        }
        if(interventionDTO.getCodeSinistre() != null) {
        String codeSinistre = interventionDTO.getCodeSinistre().getCode()
                + " : "
                + interventionDTO.getCodeSinistre().getIntitule();
            codeSinistreTextView.setText(codeSinistre);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss d MMMM yyyy", Locale.FRANCE);
        if(interventionDTO.getDateHeureCreation() !=null ){
            String heureCreation = dateFormat.format(interventionDTO.getDateHeureCreation());
            heureCreationTextView.setText(heureCreation);
        }
        if(interventionDTO.getDateHeureFin() !=null ){
            String heureFin = dateFormat.format(interventionDTO.getDateHeureCreation());
            heureFinTextView.setText(heureFin);
        }
        // todo add creator
//        if(interventionDTO.getCreator() != null){
//            codeSinistreTextView.setText(interventionDTO.getCreator().toString());
//        }

    }

}
