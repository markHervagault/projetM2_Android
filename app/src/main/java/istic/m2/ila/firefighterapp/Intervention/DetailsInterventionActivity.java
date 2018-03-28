package istic.m2.ila.firefighterapp.Intervention;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.DeploimentConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionFullDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.UserDTO;
import retrofit2.Response;

/**
 * Created by markh on 20/03/2018.
 */

public class DetailsInterventionActivity extends AppCompatActivity {

    private InterventionDTOParcelable interventionDTOParcelable;
    private List<DeploiementDTO> deploiementList;

    //Todo : return POJO details
    public InterventionDTO getIntervention(){
        return interventionDTOParcelable.getInterventionDTO();
    }

    public List<String> getDatas(){

        List<String> datas = new ArrayList<>();
        datas.add("MDR");
        datas.add("LOL");
        datas.add("YOLO");
        return datas;
    }

    public List<List<DeploiementDTO>> getDeploimentsTri() throws IOException {
        //get Deploiment from server
        String token = getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        String id = "5";
        DeploiementDTO deploiementDTO = new DeploiementDTO();
        RestTemplate restTemplate = RestTemplate.getInstance();

        DeploimentConsumer deploimentConsumer = restTemplate.builConsumer(DeploimentConsumer.class);

        try {
            Response<DeploiementDTO> response = deploimentConsumer.getListDeploimentById(token,id).execute();
        } catch (Exception e){
            throw e;
        }


        ArrayList<List<DeploiementDTO>> deploimentsTri = new ArrayList<>();

        //tri (map et...)

        return deploimentsTri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("DetailsIntervention ", "onCreate Begin");

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        if(extra != null){
            interventionDTOParcelable = extra.getParcelable("interventionDTO");
            Log.i("Fragment ", "Get Parcelable : " + interventionDTOParcelable.toString());
        } else {
            Log.i("Fragment ", "Parcelable is empty");
        }

        setContentView(R.layout.activity_intervention_details);
        Log.i("DetailsIntervention ", "onCreate End");
    }
}