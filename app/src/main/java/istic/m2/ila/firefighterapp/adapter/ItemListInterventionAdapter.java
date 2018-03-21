package istic.m2.ila.firefighterapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adou.myapplication.R;

import java.util.List;
import java.util.Map;

/**
 * Created by adou on 02/02/18.
 */

public class ItemListInterventionAdapter extends RecyclerView.Adapter<ItemListInterventionAdapter.ViewHolder> {
    private List<Map<String, String>> mDataset;

    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListInterventionAdapter(List<Map<String, String>> myDataset) {
        mDataset = myDataset;
    }

    // Fournit une reference aux vues pour chaque item
    // les items complexes peuvent avoir besoin de plus d'une vue par item  et
    // vous fournir un accès aux vues pour une donnée d'item dans le view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView codeSinistreIntervention;
        public TextView dateIntervention;
        public TextView adresseIntervention;
        public TextView statutIntervention;
        public ImageView imgMap;

        public ViewHolder(View v) {
            super(v);
            dateIntervention = v.findViewById(R.id.adresse_intervention);
            codeSinistreIntervention = v.findViewById(R.id.adresse_intervention);
            adresseIntervention = v.findViewById(R.id.adresse_intervention);
            statutIntervention = v.findViewById(R.id.stat);
            imgMap = v.findViewById(R.id.img);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemListInterventionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewitem, parent, false);

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - on récupère un élément dy dataset à cette position
        // - on remplace le contenu de la vue avec cet élément
        Map<String, String> map = mDataset.get(position);
        holder.dateIntervention.setText(map.get("dateIntervention"));
        holder.codeSinistreIntervention.setText(map.get("codeSinistreIntervention"));
        holder.adresseIntervention.setText(map.get("adresseIntervention"));
        holder.statutIntervention.setText(map.get("statutIntervention"));
        holder.imgMap.setImageResource(Integer.parseInt(map.get("imgMap")));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
