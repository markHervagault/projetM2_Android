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

    private InterventionFullDTO interventionDTO;
    private List<DeploiementDTO> deploiementList;

    //Todo : return POJO details
    public InterventionFullDTO getIntervention(){
        return interventionDTO;
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

        Log.i("FRagment ", intent + "");
        Log.i("FRagment ", intent.getStringExtra("adresseString"));
        Log.i("FRagment ", extra.getString("adresseString"));

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            //todo parse the DTO and get the full Intervention
//            //items = extras.getParcelableArrayList(ARG_PERSON_LIST);
//        } else {
//            interventionDTO = new InterventionFullDTO();
//            AdresseDTO adresseDTO = new AdresseDTO();
//            SinistreDTO sinistreDTO = new SinistreDTO();
//            UserDTO userDTO = new UserDTO();
//
//            adresseDTO.setNumero(12L);
//            adresseDTO.setVoie("Rue de Verdun");
//            adresseDTO.setVille("Fougère");
//            adresseDTO.setCodePostal("35133 Fougères");
//
//            sinistreDTO.setType(new);
//
//            interventionDTO.setAdresse(adresseDTO);
////            interventionDTO.setDateHeureCreation();
////            interventionDTO.setDateHeureFin();
////            interventionDTO.setFini();
////            interventionDTO.setId();
//        }

    }

}
