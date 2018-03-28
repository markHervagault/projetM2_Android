package istic.m2.ila.firefighterapp.Intervention;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionFullDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.UserDTO;

/**
 * Created by markh on 20/03/2018.
 */

public class DetailsInterventionActivity extends AppCompatActivity {

    private InterventionDTOParcelable interventionDTOParcelable;
    private InterventionFullDTO interventionFullDTO;
    private List<DeploiementDTO> deploiementList;

    //Todo : return POJO details
    public InterventionFullDTO getIntervention(){
        return interventionFullDTO;
    }

    public List<String> getDatas(){

        List<String> datas = new ArrayList<>();
        datas.add("MDR");
        datas.add("LOL");
        datas.add("YOLO");
        return datas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention_details);

        Intent intent = getIntent();
        Bundle extra = intent.getExtras();

        interventionDTOParcelable = extra.getParcelable("interventionDTO");

        Log.i("Fragment ", intent + "");
        Log.i("Fragment ", intent.getStringExtra("adresseString"));
        Log.i("Fragment ", extra.getString("adresseString"));

        String test = interventionDTOParcelable.getInterventionDTO().getAdresse().getNumero().toString();

        Log.i("Fragment ", "Id Intervention : " + test);

    }
}
