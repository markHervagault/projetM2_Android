package istic.m2.ila.firefighterapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.adapter.ItemListInterventionAdapter;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;

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
        mRecyclerView = findViewById(R.id.recycler_list_intervention);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // On peuple notre RecyclerView
//        List<Map<String, String>> myDataset = getSampleDataToTest();
        List<InterventionDTO> myDataset = getSampleDataToTest2(20);

        mAdapter = new ItemListInterventionAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<Map<String, String>> getSampleDataToTest() {
        List<Map<String, String>> myDataset = new ArrayList<>();
        // un item
        HashMap<String, String> map = new HashMap<>();

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        map = new HashMap<>();
        map.put("dateIntervention", "22/04/2018");
        map.put("codeSinistreIntervention", "SAP");
        map.put("adresseIntervention", "Bourgogne");
        map.put("statutIntervention", "Client de courrier électronique");
        map.put("imgMapIntervention", String.valueOf(R.mipmap.ic_launcher));
        myDataset.add(map);

        return myDataset;
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

            // TODO
            // Code sinistre
            // oneInterventionDTO.set

            myDataset.add(oneInterventionDTO);
        }

        return myDataset;
    }

    /**
     * Ouvre l'activité pour ajouter une nouvelle Intervention
     * @param view
     */
    public void openAddInterventionActivity(View view) {
        startActivity(new Intent(ListInterventionActivity.this, AddInterventionActivity.class));
    }
}
