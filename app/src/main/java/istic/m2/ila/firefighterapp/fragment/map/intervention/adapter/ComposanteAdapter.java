package istic.m2.ila.firefighterapp.fragment.map.intervention.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import istic.m2.ila.firefighterapp.dto.TypeComposanteDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.TypeComposanteConsumer;
import retrofit2.Response;

/**
 * Created by amendes on 26/04/18.
 */

public class ComposanteAdapter extends ArrayAdapter<TypeComposanteDTO> {
    private Context mContext;
    public ComposanteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        String token = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        RestTemplate restTemplate = RestTemplate.getInstance();
        TypeComposanteConsumer typeComposanteConsumer = restTemplate.builConsumer(TypeComposanteConsumer.class);
        mContext = context;
        getListTypeComposante(this, token, typeComposanteConsumer);
    }

    private void getListTypeComposante(final ComposanteAdapter context, final String token, final TypeComposanteConsumer typeComposanteConsumer) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                try {
                    Response<List<TypeComposanteDTO>> response = typeComposanteConsumer.getListTypeComposante(token).execute();
                    if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                        for (TypeComposanteDTO typeComposanteDTO : response.body()) {
                            context.insert(typeComposanteDTO, context.getCount());
                        }
                    } else {
                        Log.e("TypeComposante", "Error From Server : " + response.errorBody().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
