package istic.m2.ila.firefighterapp.Intervention;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsStaticFragment extends Fragment {

    private TextView addresseTextView;
    private TextView codeSinistreTextView;
    private TextView creatorTextView;
    private TextView heureCreationTextView;

    //private pojo item
    private String data;

    public InterventionDetailsStaticFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        //init pojo data
        data = ((DetailsInterventionActivity)this.getActivity()).getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intervention_details_static, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        //update fields
        addresseTextView = getActivity().findViewById(R.id.addresse_textview);
        addresseTextView.setText(data);
    }

}
