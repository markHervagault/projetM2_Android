package istic.m2.ila.firefighterapp.adapter;

/**
 * Created by markh on 26/03/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;

public class ItemListCrmAdapter extends RecyclerView.Adapter<ItemListCrmAdapter.ViewHolder> {
    private List<Map<String, String>> mDataset;

    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListCrmAdapter(List<Map<String, String>> myDataset) {
        mDataset = myDataset;
    }

    // Fournit une reference aux vues pour chaque item
    // les items complexes peuvent avoir besoin de plus d'une vue par item  et
    // vous fournir un accès aux vues pour une donnée d'item dans le view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vehicule_id_crm;
        public TextView vehicule_type_crm;

        public ViewHolder(View v) {
            super(v);
            vehicule_id_crm = v.findViewById(R.id.vehicule_id_crm);
            vehicule_type_crm = v.findViewById(R.id.vehicule_type_crm);
        }
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListCrmAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // créer une nouvelle vue
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_crm, parent, false);

        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - on récupère un élément dy dataset à cette position
        // - on remplace le contenu de la vue avec cet élément
        Map<String, String> map = mDataset.get(position);
        holder.vehicule_id_crm.setText(map.get("vehiculeIdCrm"));
        holder.vehicule_type_crm.setText(map.get("vehiculeTypeCrm"));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
