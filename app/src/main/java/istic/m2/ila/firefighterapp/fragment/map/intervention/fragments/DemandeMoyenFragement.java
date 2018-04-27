package istic.m2.ila.firefighterapp.fragment.map.intervention.fragments;

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

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.DeploimentConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DemandeDTO;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.adapter.ComposanteAdapter;
import istic.m2.ila.firefighterapp.fragment.map.intervention.adapter.TypeVehiculeAdapter;
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
        editTextNumberDemand = (EditText) view.findViewById(R.id.editText_nom);
        setSpinnerData();
        Button demande = (Button) view.findViewById(R.id.Demande);
        demande.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                List<DemandeDTO> listDemande = buildListVehicule(type);
                for(int i = 0; i < listDemande.size(); i++){
                    createDeploiement(listDemande.get(i));
                }
            }
        });

        return view;
    }


    private void setSpinnerData(){
        mySpinner = (Spinner) view.findViewById(R.id.menu);
        composanteSpinner = (Spinner)view.findViewById(R.id.menuComposante);
        composanteSpinner.setAdapter(new ComposanteAdapter(this.getActivity(),android.R.layout.simple_list_item_1));
        mySpinner.setAdapter(new TypeVehiculeAdapter(this.getActivity(), android.R.layout.simple_spinner_item));
    }

    private  void createDeploiement(DemandeDTO deploiement){
        RestTemplate restTemplate = RestTemplate.getInstance();
        DeploimentConsumer consumer = restTemplate.builConsumer(DeploimentConsumer.class);
        Response<DemandeDTO> response = null;
        try {
            response = consumer.createDeploiment(((NewMapActivity)getActivity()).getToken(),((NewMapActivity)getActivity()).getIdIntervention(), deploiement).execute();
            if (response != null && response.code() == HttpURLConnection.HTTP_OK){
                Log.i(TAG, "Demande éffectué");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private List<DemandeDTO> buildListVehicule(TypeVehiculeDTO type){
        List<DemandeDTO> demandes = new ArrayList<DemandeDTO>();

        for (int i = 0; i <= Integer.decode(editTextNumberDemand.getText().toString()); i++){
            DemandeDTO demande = new DemandeDTO();
            demande.setTypeDemande((TypeVehiculeDTO)mySpinner.getSelectedItem());
            demande.setComposante((TypeComposanteDTO)composanteSpinner.getSelectedItem());
            demandes.add(demande);
        }
          return demandes;
    }



}
