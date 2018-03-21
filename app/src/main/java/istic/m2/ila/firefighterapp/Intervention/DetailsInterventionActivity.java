package istic.m2.ila.firefighterapp.Intervention;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import istic.m2.ila.firefighterapp.R;

/**
 * Created by markh on 20/03/2018.
 */

public class DetailsInterventionActivity extends AppCompatActivity implements InterventionDetailsStaticFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_details);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
