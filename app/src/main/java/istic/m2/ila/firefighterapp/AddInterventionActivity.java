package istic.m2.ila.firefighterapp;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

import istic.m2.ila.firefighterapp.addintervention.FragmentFormulaire;
import istic.m2.ila.firefighterapp.addintervention.InterventionCreationMoyensFragments;
import istic.m2.ila.firefighterapp.consumer.InterventionConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.CreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by markh on 20/03/2018.
 */

public class AddInterventionActivity extends FragmentActivity implements FragmentFormulaire.OnFragmentInteractionListener {
    private Button validateButton;
    FragmentFormulaire fragmentFormulaire;
    InterventionCreationMoyensFragments fragmentMoyen;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_intervention);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        fragmentManager = getSupportFragmentManager();
        fragmentFormulaire = (FragmentFormulaire) fragmentManager.findFragmentById(R.id.fragment);
        fragmentMoyen = (InterventionCreationMoyensFragments) fragmentManager.findFragmentById(R.id.fragment_moyens);

        validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(creationIntervention()){
                    redirectToList();
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public Boolean creationIntervention() {
        Bundle bundleFormulaire = this.fragmentFormulaire.getBundle();
        //TODO PopUp de confirmation


        if(bundleFormulaire==null){
            Toast toast = Toast.makeText(getApplicationContext(), "Champs invalide(s)", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else{
            CreateInterventionDTO createInterventionDTO = new CreateInterventionDTO();
            AdresseDTO adresseDTO = new AdresseDTO();
            List<VehiculeDTO> vehiculeList;

            /*Valeur localisation*/
            adresseDTO.setVille(bundleFormulaire.getString("ville"));
            adresseDTO.setCodePostal(bundleFormulaire.getString("cp"));
            adresseDTO.setVoie(bundleFormulaire.getString("rue"));
            adresseDTO.setNumero(bundleFormulaire.getLong("numero"));

            GeoPositionDTO geoPositionDTO = new GeoPositionDTO();
            geoPositionDTO.setLatitude(bundleFormulaire.getDouble("latitude"));
            geoPositionDTO.setLongitude(bundleFormulaire.getDouble("longitude"));
            adresseDTO.setGeoPosition(geoPositionDTO);
            /*Valeur localisation*/


            vehiculeList = fragmentMoyen.getVehiculesSelected();
            //todo uncomment when DTOs are up to date
            //createInterventionDTO.setVehicules(vehiculeList);

            createInterventionDTO.setAdresse(adresseDTO);
            createInterventionDTO.setNom(bundleFormulaire.getString("nom"));
            createInterventionDTO.setIdCodeSinistre(bundleFormulaire.getLong("codeSinistreId"));

            RestTemplate restTemplate = RestTemplate.getInstance();
            InterventionConsumer interventionConsumer = restTemplate.builConsumer(InterventionConsumer.class);

            String token = getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
            Call<InterventionDTO> interventionDTO = interventionConsumer.createIntervention(token, createInterventionDTO);

            try {
                Log.i("Rest", "Rest call");
                Response<InterventionDTO> response = interventionDTO.execute();
                if(response != null && response.code() == HttpURLConnection.HTTP_CREATED) {
                    Log.i("Rest", "Good response");
                    return true;
                }
                else{
                    Log.i("Rest", "Bad response");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
        //TODO getBundle pour les moyens
    }

    public void redirectToList(){
        Intent homepage = new Intent(this.getApplicationContext(), ListInterventionActivity.class);
        startActivity(homepage);
    }
}
