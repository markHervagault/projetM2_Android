package istic.m2.ila.firefighterapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.constantes.PhotoTest;
import istic.m2.ila.firefighterapp.dto.PhotoDTO;
import istic.m2.ila.firefighterapp.dto.PhotoSansPhotoDTO;
import istic.m2.ila.firefighterapp.services.PhotoService;

/**
 * Created by markh on 30/04/2018.
 */

public class ItemListPictureAdapter extends RecyclerView.Adapter<ItemListPictureAdapter.ViewHolder> {

    Context context;

    private String TAG = "ItemListPictureAdapter => ";

    private List<PhotoSansPhotoDTO> photos;

    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListPictureAdapter(List<PhotoSansPhotoDTO> photos) {
        this.photos = photos;
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListPictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        // créer une nouvelle vue
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_picture, parent, false);
        context = parent.getContext();
        ItemListPictureAdapter.ViewHolder vh = new ItemListPictureAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemListPictureAdapter.ViewHolder holder, final int position) {
        final PhotoSansPhotoDTO photo = photos.get(position);
        Log.i(TAG, "Actualisation de la liste des images, récupération de la photo n° "+position+ " dans la liste.");
        // On récupère la photo sur le serveur
        PhotoService photoService = new PhotoService();
        String token = context.getSharedPreferences("user", context.MODE_PRIVATE).getString("token", "null");
        PhotoDTO photoWithPhoto = photoService.getPhotoById(token, photo.getId());
        if(photoWithPhoto==null){
            Log.e(TAG, "Impossible de récupérer l'image sur le serveur, photoWithPhoto = null");
            return;
        }else{
            Log.i(TAG, "Une image a bien été récupérée");
        }

        // on decode l'image en base64
        String base64Image = photoWithPhoto.getImgBase64().split(",")[1];
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);

        /*String base64String = PhotoTest.getPhotoEn64();
        String base64Image = base64String.split(",")[1];*/

        if(decodedString == null){
            Log.e(TAG, "Problème de décodage de l'image, result = null");
            return;
        }else if (decodedString.length==0){
            Log.e(TAG, "Problème de décodage de l'image, taille = 0");
            return;
        }
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if(decodedByte == null){
            Log.e(TAG, "Problème de conversion string to bitmap, result = null");
            return;
        }
        // On rempli l'item de la liste
        if (holder.image_list_picture==null){
            Log.e(TAG, "holder.image_list_picture = null");
            return;
        }
        holder.image_list_picture.setImageBitmap(decodedByte);
        holder.date_list_picture.setText(photo.getDateHeure().toString());
        //holder.date_list_picture.setText("01/01/1970");
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(photos==null){
            Log.i(TAG, "La liste est null");
            return 0;
        }
        Log.i(TAG, "Taille de la liste retournée : "+photos.size());
        return photos.size();
    }

    // Fournit une reference aux vues pour chaque item
    // les items complexes peuvent avoir besoin de plus d'une vue par item  et
    // vous fournir un accès aux vues pour une donnée d'item dans le view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_list_picture;
        public TextView date_list_picture;

        public ViewHolder(View v) {
            super(v);
            image_list_picture = v.findViewById(R.id.image_list_picture);
            if(image_list_picture==null){
                Log.e("ViewHolder =>", "image_list_picture = null");
            }
            date_list_picture = v.findViewById(R.id.date_list_picture);
            if(date_list_picture==null){
                Log.e("ViewHolder =>", "date_list_picture = null");
            }
        }
    }

}
