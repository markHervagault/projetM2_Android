package istic.m2.ila.firefighterapp.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;
import istic.m2.ila.firefighterapp.constantes.IHMLabels;
import istic.m2.ila.firefighterapp.dto.DroneDTO;

/**
 * Created by markh on 28/03/2018.
 */
public class ItemListDroneAdapter extends RecyclerView.Adapter<ItemListDroneAdapter.ViewHolder> {

    private String TAG = "ItemListDroneAdapter => ";

    private List<DroneDTO> drones;

    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListDroneAdapter(List<DroneDTO> drones) {
        this.drones = drones;
    }

    // Fournit une reference aux vues pour chaque item
    // les items complexes peuvent avoir besoin de plus d'une vue par item  et
    // vous fournir un accès aux vues pour une donnée d'item dans le view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView drone_name_listDrone;
        public TextView statut_listDrone;
        public ImageView image_statut_listDrone;

        public ViewHolder(View v) {
            super(v);
            drone_name_listDrone = v.findViewById(R.id.drone_name_listDrone);
            statut_listDrone = v.findViewById(R.id.statut_listDrone);
            image_statut_listDrone = v.findViewById(R.id.image_statut_listDrone);
        }
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListDroneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // créer une nouvelle vue
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_drone, parent, false);

        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - on récupère un élément dy dataset à cette position
        // - on remplace le contenu de la vue avec cet élément
        DroneDTO drone = drones.get(position);
        holder.drone_name_listDrone.setText(drone.getNom());
        String status;
        switch (drone.getStatut()){
            case CONNECTE:
                status = IHMLabels.DRONE_STATUT_CONNECTE;
                holder.image_statut_listDrone.setImageResource(R.drawable.rond_vert);
                break;
            case DECONNECTE:
                status = IHMLabels.DRONE_STATUT_DECONNECTE;
                holder.image_statut_listDrone.setImageResource(R.drawable.rond_gris);
                break;
            case EN_MISSION:
                status = IHMLabels.DRONE_STATUT_EN_MISSION;
                holder.image_statut_listDrone.setImageResource(R.drawable.rond_rouge);
                break;
            default:
                Log.d(TAG, "Statut du drone inconnu");
                holder.image_statut_listDrone.setImageResource(R.drawable.rond_gris_croix);
                status = IHMLabels.DRONE_STATUT_INCONNU;

        }
        holder.statut_listDrone.setText(status);

        // Envoie du nouveau drone sur le bus
        NewDroneMessage message = new NewDroneMessage(drone.getId());
        Log.d(TAG, "================================================================ Envoi d'une donnee sur le bus : "+drone.getNom());
        EventBus.getDefault().post(message);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return drones.size();
    }
}
