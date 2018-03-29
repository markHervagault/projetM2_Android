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

public class DetailsInterventionActivity extends AppCompatActivity implements InterventionDetailsMoyensFragments.ActivityMoyens {

    private static String TAG = "DetailIntervention";

    private InterventionDTOParcelable interventionDTOParcelable;
    private Map<String, List<DeploiementDTO>> mapSortDeploiment;

    //Todo : return POJO details
    public InterventionDTO getIntervention() {
        return interventionDTOParcelable.getInterventionDTO();
    }

    @Override
    public Map<String, List<DeploiementDTO>> getDeploiments() {
        Log.i(TAG, "getDeploimentsTri Begin");
        String token = getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        String id = interventionDTOParcelable.getInterventionDTO().getId().toString();

        RestTemplate restTemplate = RestTemplate.getInstance();
        DeploimentConsumer deploimentConsumer = restTemplate.builConsumer(DeploimentConsumer.class);
        Response<List<DeploiementDTO>> response = null;

        mapSortDeploiment = new HashMap<>();
        List<DeploiementDTO> deploiementDTOList = null;


        try {
            response = deploimentConsumer.getListDeploimentById(token, id).execute();

            if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                deploiementDTOList = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String type = null;
        if (deploiementDTOList != null) {
            for (DeploiementDTO deploiement : deploiementDTOList) {

                if (deploiement.getVehicule() != null) {
                    type = deploiement.getVehicule().getType().getLabel();
                } else {
                    type = deploiement.getTypeDemande().getLabel();
                }
                List<DeploiementDTO> list = !mapSortDeploiment.containsKey(type) ? new ArrayList<DeploiementDTO>() : mapSortDeploiment.get(type);
                list.add(deploiement);

                mapSortDeploiment.put(type, list);
            }
        }
        Log.i(TAG, "getDeploimentsTri End");
        return mapSortDeploiment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate Begin");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();
        if (extra != null) {
            interventionDTOParcelable = extra.getParcelable("interventionDTO");
            Log.i("Fragment ", "Get Parcelable : " + interventionDTOParcelable.toString());
        } else {
            Log.i("Fragment ", "Parcelable is empty");
        }

        setContentView(R.layout.activity_intervention_details);
        Log.i(TAG, "onCreate End");
    }


}