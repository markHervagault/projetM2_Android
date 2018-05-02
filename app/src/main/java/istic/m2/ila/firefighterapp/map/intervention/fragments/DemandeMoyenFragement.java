package istic.m2.ila.firefighterapp.map.intervention.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.DemandeDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import istic.m2.ila.firefighterapp.map.intervention.adapter.ComposanteAdapter;
import istic.m2.ila.firefighterapp.map.intervention.adapter.TypeVehiculeAdapter;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.DeploimentConsumer;
import retrofit2.Response;

/**
 * Created by hakima on 4/24/18.
 */

public class DemandeMoyenFragement extends Fragment {
    View view;
    private static final String TAG = "tag";
    Spinner mySpinner;
    Spinner composanteSpinner;
    private TypeVehiculeDTO type = new TypeVehiculeDTO();
    private EditText editTextNumberDemand;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_moyen_fragment, container, false);
        editTextNumberDemand = view.findViewById(R.id.editText_nom);
        setSpinnerData();
        Button demande = view.findViewById(R.id.Demande);
        demande.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                List<DemandeDTO> listDemande = buildListVehicule(type);
                for(int i = 0; i < listDemande.size(); i++){
                    createDeploiement(listDemande.get(i));
                }
                ((MapActivity)getActivity()).hideSelf();
            }
        });

        return view;
    }


    private void setSpinnerData(){
        mySpinner = view.findViewById(R.id.menu);
        composanteSpinner = view.findViewById(R.id.menuComposante);
        composanteSpinner.setAdapter(new ComposanteAdapter(this.getActivity(),android.R.layout.simple_list_item_1));
        mySpinner.setAdapter(new TypeVehiculeAdapter(this.getActivity(), android.R.layout.simple_spinner_item));
    }

    private  void createDeploiement(DemandeDTO deploiement){
        RestTemplate restTemplate = RestTemplate.getInstance();
        DeploimentConsumer consumer = restTemplate.builConsumer(DeploimentConsumer.class);
        Response<DemandeDTO> response = null;
        try {
            response = consumer.createDeploiment(((MapActivity)getActivity()).getToken(), deploiement).execute();

            if (response != null && response.code() == HttpURLConnection.HTTP_OK){
                Log.i(TAG, "Demande éffectué");

            } else {

            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<DemandeDTO> buildListVehicule(TypeVehiculeDTO type){
        List<DemandeDTO> demandes = new ArrayList<>();

        for (int i = 0; i < Integer.decode(editTextNumberDemand.getText().toString()); i++){
            DemandeDTO demande = new DemandeDTO();
            demande.setTypeDemande((TypeVehiculeDTO)mySpinner.getSelectedItem());
            demande.setComposante((TypeComposanteDTO)composanteSpinner.getSelectedItem());
            demande.setInterventionId(((MapActivity)getActivity()).getIdIntervention());
            demandes.add(demande);
        }
          return demandes;
    }



}
