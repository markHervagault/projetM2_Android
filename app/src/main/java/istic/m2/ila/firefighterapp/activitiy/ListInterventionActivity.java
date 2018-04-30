package istic.m2.ila.firefighterapp.activitiy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.adapter.ItemListInterventionAdapter;
import istic.m2.ila.firefighterapp.rest.consumers.InterventionConsumer;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import retrofit2.Response;

/**
 * Created by markh on 20/03/2018.
 */

public class ListInterventionActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intervention);

        // use a linear layout manager
        mRecyclerView = findViewById(R.id.recycler_list_intervention);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Récupérer l'information de connexion Codis/Intervenant
        boolean isCodis = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                .getBoolean("isCodis", false);

        // Masquer afficher la possibilité de créer une intervention suivant le cas
        if (isCodis) {
            findViewById(R.id.layout_list_activity_fab).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layout_list_activity_fab).setVisibility(View.GONE);
        }

        AsyncTask.execute(new Runnable() {
            public void run() {

                // On peuple notre RecyclerView
                List<InterventionDTO> myDataset = new ArrayList<>();

                // Construction de notre appel REST
                RestTemplate restTemplate = RestTemplate.getInstance();
                InterventionConsumer interventionConsumer = restTemplate.builConsumer(InterventionConsumer.class);

                Response<List<InterventionDTO>> response = null;
                try {
                    // Récupération du token
                    String token = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                            .getString("token", "null");

                    // On récupère toutes les interventions du Serveur
                    response = interventionConsumer.getListInterventionEnCours(token).execute();
                    if(response != null && response.code() == HttpURLConnection.HTTP_OK) {
                        myDataset = response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final List<InterventionDTO> finalMyDataset = myDataset;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ItemListInterventionAdapter(finalMyDataset);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
            }
        });
    }

    /**
     * Ouvre l'activité pour ajouter une nouvelle Intervention
     * @param view
     */
    public void openAddInterventionActivity(View view) {
        startActivity(new Intent(ListInterventionActivity.this, AddInterventionActivity.class));
    }

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_intervention);
        mRecyclerView = findViewById(R.id.recycler_list_intervention);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // On peuple notre RecyclerView
        // List<Map<String, String>> myDataset = getSampleDataToTest();
        List<InterventionDTO> myDataset = getSampleDataToTest2(20);

        mAdapter = new ItemListInterventionAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<InterventionDTO> getSampleDataToTest2(int max) {
        List<InterventionDTO> myDataset = new ArrayList<>();
        // un item
        InterventionDTO oneInterventionDTO;

        for (int i = 0; i< max; i++) {
            oneInterventionDTO = new InterventionDTO();

            // Adresse
            AdresseDTO adresseDTO = new AdresseDTO();
            adresseDTO.setNumero(Long.valueOf(33 + i));
            adresseDTO.setVoie("Rue Lescot " + i);
            adresseDTO.setCodePostal("35000");
            adresseDTO.setVille("Rennes");

            oneInterventionDTO.setAdresse(adresseDTO);

            // Date de création
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date dateHeureCreation = new Date();
            System.out.println(dateFormat.format(dateHeureCreation)); //2016/11/16 12:08:43
            oneInterventionDTO.setDateHeureCreation(dateHeureCreation);

            // Statut
            boolean statut = (i%2 == 0) ? true : false;
            oneInterventionDTO.setFini(statut);

            // Code sinistre
            CodeSinistreDTO codeSinistreDTO = new CodeSinistreDTO();
            codeSinistreDTO.setCode("COD" + i);
            codeSinistreDTO.setIntitule("Test de l'intitule " + i);
            oneInterventionDTO.setCodeSinistre(codeSinistreDTO);

            myDataset.add(oneInterventionDTO);
        }

        return myDataset;
    }
    */
}
