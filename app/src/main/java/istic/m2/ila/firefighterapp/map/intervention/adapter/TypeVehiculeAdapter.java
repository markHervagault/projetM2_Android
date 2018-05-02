package istic.m2.ila.firefighterapp.map.intervention.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.TypeVehiculeConsumer;
import retrofit2.Response;

/**
 * Created by amendes on 27/04/18.
 */

public class TypeVehiculeAdapter extends ArrayAdapter<TypeVehiculeDTO> {
    public TypeVehiculeAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        String token = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        RestTemplate restTemplate = RestTemplate.getInstance();
        TypeVehiculeConsumer consumer = restTemplate.builConsumer(TypeVehiculeConsumer.class);
        Response<List<TypeVehiculeDTO>> response = null;
        try{
            response = consumer.getListTypeVehicules(token).execute();
            if (response != null && response.code() == HttpURLConnection.HTTP_OK){
                Log.i("TypeVehicule", "type véhicule récupéré");
                for (TypeVehiculeDTO typeVehiculeDTO : response.body()) {
                    this.insert(typeVehiculeDTO, this.getCount());
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
