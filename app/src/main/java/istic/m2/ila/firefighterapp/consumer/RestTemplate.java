package istic.m2.ila.firefighterapp.consumer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Date.class, new DateSerializer())
                .create();

        T consumer = new Retrofit.Builder()
                .baseUrl(Endpoints.BASE)
                .addConverterFactory(GsonConverterFactory.create(gson)) //GsonConverterFactory.create()
                .build()
                .create(clazz);

        return consumer;
    }

    class DateDeserializer implements JsonDeserializer {

        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context)
                throws JsonParseException {
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            if(json == null){
                return null;
            } else {
                try {
                    Date date = dateParser.parse(json.getAsString().substring(0,19));
                    return date;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    class DateSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date date, Type typeOfSrc,
                                     JsonSerializationContext context) {
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            if(date == null){
                return null;
            } else {
                String json = dateParser.format(date);
                return new JsonPrimitive(json);
            }
        }
    }
}
