package istic.m2.ila.firefighterapp.validationCodis;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.VehiculeConsumer;
import retrofit2.Response;

/**
 * Created by amendes on 27/04/18.
 */

public class VehiculeDispoAdapter extends ArrayAdapter<VehiculeDTO> {
    Context context;
    TypeVehiculeDTO typeVehiculeDTO;
    ArrayList<VehiculeDTO> vehiculeDTOS = new ArrayList<>();

    public VehiculeDispoAdapter(@NonNull final Context context, int resource, final TypeVehiculeDTO typeVehiculeDTO) {
        super(context, resource);
        this.context = context;
        this.typeVehiculeDTO = typeVehiculeDTO;
        setData(this);
    }

    public void setData(final ArrayAdapter<VehiculeDTO> adapter) {
        new TestAsynchTask().execute();
    }

    public class TestAsynchTask extends AsyncTask<Void, Void, Object>{

        @Override
        protected Object doInBackground(Void... voids) {
            String token = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
            RestTemplate restTemplate = RestTemplate.getInstance();
            VehiculeConsumer vehiculeConsumer = restTemplate.builConsumer(VehiculeConsumer.class);
            try {
                Response<List<VehiculeDTO>> response = vehiculeConsumer.getListVehiculeDispoByType(token, typeVehiculeDTO.getId()).execute();
                if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                    vehiculeDTOS = (ArrayList<VehiculeDTO>) response.body();
                } else {
                    Log.e("Vehicule", "Error From Server : " + response.errorBody().string());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return vehiculeDTOS;
        }

        @Override
        protected void onPostExecute(Object response){
            populate();
        }
    }


    public void populate(){
        for (VehiculeDTO vehiculeDTO : vehiculeDTOS) {
            this.insert(vehiculeDTO, this.getCount());
        }
    }
}
