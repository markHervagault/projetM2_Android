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
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.InterventionConsumer;
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
        setContentView(R.layout.activity_new_list_intervention_codis);

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
                        //mAdapter = new ItemListInterventionAdapter(finalMyDataset);
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
        startActivity(new Intent(this, AddInterventionActivity.class));
    }
}
