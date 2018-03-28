package istic.m2.ila.firefighterapp.consumer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hakima on 3/21/18.
 */
/** */
public class RestTemplate {
    private static final String TAG = "REST-TEMPLATE";
    private static RestTemplate instance;

    private RestTemplate(){

    }

    public static RestTemplate getInstance(){
        if(instance == null){
            instance = new RestTemplate();
        }
        return instance;
    }


    public <T> T builConsumer(Class<T> clazz){

        Log.i("TAG", "building object" + Endpoints.BASE);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        T consumer = new Retrofit.Builder()
                .baseUrl(Endpoints.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson)) //GsonConverterFactory.create()
                .build()
                .create(clazz);

        return consumer;
    }
}
