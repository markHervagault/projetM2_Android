package istic.m2.ila.firefighterapp.Intervention;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.DeploimentConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionFullDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import istic.m2.ila.firefighterapp.dto.UserDTO;
import retrofit2.Response;

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