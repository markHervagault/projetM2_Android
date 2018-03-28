package istic.m2.ila.firefighterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.Intervention.DetailsInterventionActivity;
import istic.m2.ila.firefighterapp.Intervention.InterventionDTOParcelable;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.CodeSinistreDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;

/**
 * Created by adou on 02/02/18.
 */

public class ItemListInterventionAdapter extends RecyclerView.Adapter<ItemListInterventionAdapter.ViewHolder> {
    private List<InterventionDTO> mDataset;
    private Context context;
    private String TAG = "ItemListInterventionAdapter";


    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListInterventionAdapter(List<InterventionDTO> myDataset) {
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
        public ImageView imgMapIntervention;
        public LinearLayout layoutItemIntervention;

        public ViewHolder(View v) {
            super(v);
            dateIntervention = v.findViewById(R.id.date_intervention);
            codeSinistreIntervention = v.findViewById(R.id.code_sinistre_intervention);
            adresseIntervention = v.findViewById(R.id.adresse_intervention);
            statutIntervention = v.findViewById(R.id.statut_intervention);
            imgMapIntervention = v.findViewById(R.id.img_map_intervention);
            layoutItemIntervention = v.findViewById(R.id.item_intervention_layout);
        }
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListInterventionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // créer une nouvelle vue
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_intervention, parent, false);

        context = parent.getContext();
        ViewHolder vh= new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - on récupère un élément dy dataset à cette position
        // - on remplace le contenu de la vue avec cet élément
        InterventionDTO intervention = mDataset.get(position);

        // Date de l'intervention
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date dateHeureCreation = new Date();
        System.out.println(); //16/11/2016 12:08:43
        holder.dateIntervention.setText(dateFormat.format(dateHeureCreation));

        // Code sinistre
        CodeSinistreDTO codeSinistreDTO = intervention.getCodeSinistre();
        holder.codeSinistreIntervention.setText(codeSinistreDTO.getCode());

        // Adresse
        AdresseDTO adresseDTO = intervention.getAdresse();
        final String adresseString = adresseDTO.getNumero() + " " +
                adresseDTO.getVoie() + " " +
                adresseDTO.getCodePostal() + " " +
                adresseDTO.getVille() + " ";
        holder.adresseIntervention.setText(adresseString);

        // Statut
        String statut = (intervention.isFini()) ? "Terminé" : "En cours";
        holder.statutIntervention.setText(statut);

        // Gestion de la miniature
//        holder.imgMapIntervention.setImageResource(Integer.parseInt(intervention.get("imgMapIntervention")));

        // TODO
        // Gestion du clic
        holder.layoutItemIntervention.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //(ListInterventionActivity).onClickIntervention();
                Log.i(TAG, "Un clic a été effectué sur l'item " + position);

                Intent intent = new Intent(context, DetailsInterventionActivity.class);
                Bundle bundle = intent.getExtras();
                if (bundle == null) {
                    bundle = new Bundle();
                }

                InterventionDTOParcelable interventionDTOParcelable = new InterventionDTOParcelable(mDataset.get(position));

                bundle.putParcelable("interventionDTO", interventionDTOParcelable);
                bundle.putString("adresseString", adresseString);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
