package istic.m2.ila.firefighterapp.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import istic.m2.ila.firefighterapp.Intervention.InterventionDetailsMoyensFragments;
import istic.m2.ila.firefighterapp.Intervention.InterventionDetailsStaticFragment;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;

/**
 * Created by markh on 20/03/2018.
 */

public class DetailsInterventionActivity extends AppCompatActivity implements InterventionDetailsMoyensFragments.ActivityMoyens, InterventionDetailsStaticFragment.ActivityDetails {

    private static String TAG = "DetailIntervention";

    private InterventionDTO interventionDTO;

    @Override
    public InterventionDTO getIntervention() {
        return interventionDTO;
    }

    @Override
    public Long getIdIntervention() {
        return interventionDTO.getId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate Begin");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        interventionDTO = (InterventionDTO) intent.getSerializableExtra("interventionDTO");
        if (interventionDTO != null) {
            Log.i("Fragment ", "Get Parcelable : " + interventionDTO.toString());
        } else {
            Log.i("Fragment ", "Parcelable is empty");
        }

        setContentView(R.layout.activity_intervention_details);
        Log.i(TAG, "onCreate End");
    }


}