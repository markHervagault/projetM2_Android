package istic.m2.ila.firefighterapp.Intervention;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.MapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionFullDTO;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsStaticFragment extends Fragment implements View.OnClickListener {

    private TextView addresseTextView;
    private TextView codeSinistreTextView;
    private TextView villeTextView;
    private TextView creatorTextView;
    private TextView heureCreationTextView;
    private Button openMap;

    //private pojo item
    private InterventionFullDTO interventionDTO;

    public InterventionDetailsStaticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //init pojo data
        interventionDTO = ((DetailsInterventionActivity)getActivity()).getIntervention();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intervention_details_static, container, false);
    }

    @Override
    public void onClick(View v){
        startActivity(new Intent(getActivity(), MapActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        openMap = getActivity().findViewById(R.id.button_map);
        openMap.setOnClickListener(this);

        //update fields
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);


        String Address = "Random address";
        addresseTextView.setText(Address);

    }

}
