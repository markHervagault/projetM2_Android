package istic.m2.ila.firefighterapp.consumer;

import istic.m2.ila.firefighterapp.constantes.Endpoints;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hakima on 3/21/18.
 */

public class RestTemplate {
    private static final String TAG = "REST-TEMPLATE";
    private static RestTemplate instance;

    private RestTemplate(){

    }

    public RestTemplate getInstance(){
        if(instance == null){
            instance = new RestTemplate();
        }
        return instance;
    }

    public Consumer builConsumer(Class<? extends Consumer> clazz){
        Consumer consumer = new Retrofit.Builder()
                .baseUrl(Endpoints.BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(clazz);

        return consumer;
    }
}
