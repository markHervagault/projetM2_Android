package istic.m2.ila.firefighterapp.services;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.dto.PhotoDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.PhotoConsumer;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by hakima on 4/30/18.
 */

public class PhotoService {
    private String TAG = "photoService";
    public List<File> getAllPhotos(String token, Context context){
        List<File> images = new ArrayList<>();
        RestTemplate restTemplate = RestTemplate.getInstance();
        PhotoConsumer consumer = restTemplate.builConsumer(PhotoConsumer.class);
        Response<List<PhotoDTO>> response;
        try {
            response = consumer.getAllPhotos(token).execute();
            if(response != null && response.code() == HttpURLConnection.HTTP_OK){
                for(PhotoDTO dto : response.body()) {
                    images.add(decodeAndSave(dto, context));
                }
            }
        }catch (IOException e) {
            Log.e(TAG, "An unexpected error occured while trying to open the file (see the exception for more details): ", e);
        }
        return images;
    }

    public List<PhotoDTO> getPhotosForPointWithoutPhoto(String token, Context context, int pointIndex){
        RestTemplate restTemplate = RestTemplate.getInstance();
        PhotoConsumer consumer = restTemplate.builConsumer(PhotoConsumer.class);
        Response<List<PhotoDTO>> response;
        try {
            response = consumer.getPhotosForPointWithoutPhoto(token, pointIndex).execute();
            if(response != null && response.code() == HttpURLConnection.HTTP_OK){
                return response.body();
            }
        }catch (IOException e) {
            Log.e(TAG, "An unexpected error occured while trying to open the file (see the exception for more details): ", e);
        }
        return null;
    }



    public File decodeAndSave(PhotoDTO dto, Context context) throws IOException {
        byte [] rawData = Base64.decode(dto.getImgBase64(), Base64.DEFAULT);
        File file = new File(context.getFilesDir(), dto.getNomFichier());
        if(!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(rawData);
        fos.flush();
        fos.close();
        return file;
    }
}
