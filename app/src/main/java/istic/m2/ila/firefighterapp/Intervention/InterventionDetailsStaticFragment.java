package istic.m2.ila.firefighterapp.Intervention;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InterventionDetailsStaticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterventionDetailsStaticFragment extends Fragment {

    private TextView addresseTextView;
    private TextView codeSinistreTextView;
    private TextView creatorTextView;
    private TextView heureCreationTextView;

    public InterventionDetailsStaticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InterventionDetailsStaticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterventionDetailsStaticFragment newInstance() {
        InterventionDetailsStaticFragment fragment = new InterventionDetailsStaticFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intervention_details_static, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //TODO : Initialiser les diff textview ici



        // Set values for view here
        addresseTextView = (TextView) view.findViewById(R.id.addresse_textview);

        // update view
        addresseTextView.setText("3 rue test");
    }

}
