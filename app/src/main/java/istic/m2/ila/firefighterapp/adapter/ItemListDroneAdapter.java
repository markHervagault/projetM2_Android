package istic.m2.ila.firefighterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.Intervention.DetailsInterventionActivity;
import istic.m2.ila.firefighterapp.Intervention.InterventionDTOParcelable;
import istic.m2.ila.firefighterapp.MapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.NewDroneMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.constantes.IHMLabels;
import istic.m2.ila.firefighterapp.dto.DroneDTO;

/**
 * Created by markh on 28/03/2018.
 */
public class ItemListDroneAdapter extends RecyclerView.Adapter<ItemListDroneAdapter.ViewHolder> {

    Context context;

    private String TAG = "ItemListDroneAdapter => ";

    private List<DroneDTO> drones;

    public int indexSelected = -1;

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
        public ImageView image_battery_listDrone;
        public LinearLayout layoutItemDroneList;

        public ViewHolder(View v) {
            super(v);
            drone_name_listDrone = v.findViewById(R.id.drone_name_listDrone);
            statut_listDrone = v.findViewById(R.id.statut_listDrone);
            image_statut_listDrone = v.findViewById(R.id.image_statut_listDrone);
            image_battery_listDrone = v.findViewById(R.id.image_battery_listDrone);
            layoutItemDroneList = v.findViewById(R.id.item_Drone_List_layout);
        }
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListDroneAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // créer une nouvelle vue
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_drone, parent, false);
        context = parent.getContext();
        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - on récupère un élément dy dataset à cette position
        // - on remplace le contenu de la vue avec cet élément
        final DroneDTO drone = drones.get(position);
        holder.drone_name_listDrone.setText(drone.getNom());
        String status;
        switch (drone.getStatut()){
            case CONNECTE:
                status = IHMLabels.DRONE_STATUT_CONNECTE;
                holder.image_statut_listDrone.setImageResource(R.drawable.connected);
                holder.image_battery_listDrone.setVisibility(View.VISIBLE);
                break;
            case DECONNECTE:
                status = IHMLabels.DRONE_STATUT_DECONNECTE;
                holder.image_statut_listDrone.setImageResource(R.drawable.disconnected);
                holder.image_battery_listDrone.setVisibility(View.GONE);
                break;
            case EN_MISSION:
                status = IHMLabels.DRONE_STATUT_EN_MISSION;
                holder.image_statut_listDrone.setImageResource(R.drawable.droneenmission);
                holder.image_battery_listDrone.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d(TAG, "Statut du drone inconnu");
                holder.image_statut_listDrone.setImageResource(R.drawable.rond_gris_croix);
                holder.image_battery_listDrone.setVisibility(View.GONE);
                status = IHMLabels.DRONE_STATUT_INCONNU;
        }

        int battery = drone.getBattery();

        if(battery>70)
        {
            holder.image_battery_listDrone.setImageResource(R.drawable.fullbattery);
        }
        else if (battery > 45 )
        {
            holder.image_battery_listDrone.setImageResource(R.drawable.midbattery);
        }
        else if (battery > 20)
        {
            holder.image_battery_listDrone.setImageResource(R.drawable.criticalbattery);
        }
        else {
            holder.image_battery_listDrone.setImageResource(R.drawable.emptybattery);
        }

        holder.statut_listDrone.setText(status);


        if(indexSelected==position){
            holder.layoutItemDroneList.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_for_list_drone_selected));
        }else{
            holder.layoutItemDroneList.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border_for_list_drone_unselected));
        }

        // Gestion du clic
        holder.layoutItemDroneList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new SelectedDroneChangedMessage(drone));
                if(indexSelected != position){
                    indexSelected = position;
                    notifyDataSetChanged();
                }
            }
        });

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
