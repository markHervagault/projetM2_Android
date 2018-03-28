package istic.m2.ila.firefighterapp;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;

import istic.m2.ila.firefighterapp.addintervention.FragmentFormulaire;
import istic.m2.ila.firefighterapp.consumer.InterventionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.CreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by markh on 20/03/2018.
 */

public class AddInterventionActivity extends FragmentActivity implements FragmentFormulaire.OnFragmentInteractionListener {
    private Button validateButton;
    FragmentFormulaire fragmentFormulaire;
    FragmentManager fragmentManager;

    RestTemplate restTemplate;
    InterventionConsumer interventionConsumer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_intervention);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fragmentManager = getSupportFragmentManager();
        fragmentFormulaire = (FragmentFormulaire) fragmentManager.findFragmentById(R.id.fragment);

        validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                creationIntervention();
            }
        });

        restTemplate = RestTemplate.getInstance();
        interventionConsumer = restTemplate.builConsumer(InterventionConsumer.class);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void creationIntervention() {
        Bundle bundleFormulaire = this.fragmentFormulaire.getBundle();
        //TODO PopUp de confirmation


        if(bundleFormulaire==null){
            Toast toast = Toast.makeText(getApplicationContext(), "Champs invalide(s)", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            CreateInterventionDTO createInterventionDTO = new CreateInterventionDTO();
            AdresseDTO adresseDTO = new AdresseDTO();

            adresseDTO.setVille("TMP-Rennes-TMP");
            adresseDTO.setCodePostal("TMP-35-TMP");

            createInterventionDTO.setAdresse(adresseDTO);
            createInterventionDTO.setNom("TMP");
            String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTUyMjI0OTk3Mn0.0pNWxo1ax-YYMMZ8KgHCjM56qGKwPg-tYS-L5CU1s0PN7wGB7z73ovrEwkJLwbyVfkPUGTW9_AflJmawowb_ZA";

            Log.i("OnCreate", "TOKEN ======>"+ token);

            Call<InterventionDTO> interventionDTO = interventionConsumer.createIntervention(token, createInterventionDTO);
            try {
                Log.i("Rest", "Rest call");
                Response<InterventionDTO> response = interventionDTO.execute();
                if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                    Log.i("Rest", "Good response"+response.body().getId().toString());
                }
                else{
                    Log.i("Rest", "Bad response ====>" + response.errorBody().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //TODO getBundle pour les moyens
    }
}
