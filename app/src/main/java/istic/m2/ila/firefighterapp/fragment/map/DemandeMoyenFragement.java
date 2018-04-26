package istic.m2.ila.firefighterapp.fragment.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import istic.m2.ila.firefighterapp.consumer.TypeVehiculeConsumer;
import istic.m2.ila.firefighterapp.dto.DemandeDTO;
import istic.m2.ila.firefighterapp.dto.ETypeTraitTopo;
import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import istic.m2.ila.firefighterapp.services.IMapService;
import istic.m2.ila.firefighterapp.services.impl.MapService;
import retrofit2.Response;

/**
 * Created by hakima on 4/24/18.
 */

public class DemandeMoyenFragement extends Fragment {
    View view;
    private static final String TAG = "tag";
    Spinner mySpinner;
    Spinner composanteSpinner;
    private String number;
    private TypeVehiculeDTO type = new TypeVehiculeDTO();
    private List<String> items = new ArrayList<String>();
    private List<TypeVehiculeDTO> listeType = new ArrayList<TypeVehiculeDTO>();
    private List<TypeComposanteDTO> listeComposante = new ArrayList<TypeComposanteDTO>();
    private IMapService mapServie = MapService.getInstance();
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

        listeType = getTypeVehicules();
        listeComposante = mapServie.getTypeComposante(((NewMapActivity)getActivity()).getToken());
        Log.i(TAG,"composante:" + listeComposante.get(1));
        composanteSpinner.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,listeComposante));
        mySpinner.setAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,listeType));


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

    private List<TypeVehiculeDTO> getTypeVehicules(){
        RestTemplate restTemplate = RestTemplate.getInstance();
        TypeVehiculeConsumer consumer = restTemplate.builConsumer(TypeVehiculeConsumer.class);
        Response<List<TypeVehiculeDTO>> response = null;
        try{
            response = consumer.getListTypeVehicules(((NewMapActivity)getActivity()).getToken()).execute();
            if (response != null && response.code() == HttpURLConnection.HTTP_OK){
                Log.i(TAG, "type véhicule récupéré");
                return response.body();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<>();
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
