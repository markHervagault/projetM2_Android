package istic.m2.ila.firefighterapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.PhotoDTO;

/**
 * Created by markh on 30/04/2018.
 */

public class ItemListPictureAdapter extends RecyclerView.Adapter<ItemListPictureAdapter.ViewHolder> {

    Context context;

    private String TAG = "ItemListPictureAdapter => ";

    private List<PhotoDTO> photos;

    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListPictureAdapter(List<PhotoDTO> photos) {
        this.photos = photos;
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
            date_list_picture = v.findViewById(R.id.date_list_picture);
        }
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListPictureAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // créer une nouvelle vue
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_drone, parent, false);
        context = parent.getContext();
        ItemListPictureAdapter.ViewHolder vh= new ItemListPictureAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ItemListPictureAdapter.ViewHolder holder, final int position) {
        final PhotoDTO photo = photos.get(position);
        byte[] decodedString = Base64.decode(photo.getImgBase64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.image_list_picture.setImageBitmap(decodedByte);
        holder.date_list_picture.setText(photo.getDateHeure().toString());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(photos==null){
            return 0;
        }
        return photos.size();
    }

}
