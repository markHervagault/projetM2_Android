package istic.m2.ila.firefighterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.DetailsInterventionActivity;
import istic.m2.ila.firefighterapp.dto.AdresseDTO;
import istic.m2.ila.firefighterapp.dto.CodeSinistreDTO;
import istic.m2.ila.firefighterapp.dto.InterventionDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.InterventionConsumer;
import retrofit2.Response;

/**
 * Created by adou on 02/02/18.
 */

public class ItemListInterventionAdapter extends RecyclerView.Adapter<ItemListInterventionAdapter.ViewHolder> {
    private List<InterventionDTO> mDataset = new ArrayList<>();
    private Context context;
    private String TAG = "ItemListInterventionAdapter";


    // On fournit un constructeur adéquat (dépendant de notre jeu de données)
    public ItemListInterventionAdapter(Context context) {
        this.context = context;
        setData(context);
    }

    public void setData(final Context context) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                // On peuple notre RecyclerView
                List<InterventionDTO> myDataset = new ArrayList<>();

                // Construction de notre appel REST
                RestTemplate restTemplate = RestTemplate.getInstance();
                InterventionConsumer interventionConsumer = restTemplate.builConsumer(InterventionConsumer.class);

                Response<List<InterventionDTO>> response = null;
                try {
                    // Récupération du token
                    String token = context.getSharedPreferences("user", context.getApplicationContext().MODE_PRIVATE)
                            .getString("token", "null");
                    // On récupère toutes les interventions du Serveur
                    response = interventionConsumer.getListInterventionEnCours(token).execute();
                    if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                        myDataset = response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDataset = myDataset;
            }
        });
    }

    // Fournit une reference aux vues pour chaque item
    // les items complexes peuvent avoir besoin de plus d'une vue par item  et
    // vous fournir un accès aux vues pour une donnée d'item dans le view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView idIntervention;
        public TextView codeSinistreIntervention;
        public TextView dateIntervention;
        public TextView adresseIntervention;
        public TextView statutIntervention;
        public LinearLayout layoutItemIntervention;

        public ViewHolder(View v) {
            super(v);
            idIntervention = v.findViewById(R.id.id_intervention);
            dateIntervention = v.findViewById(R.id.date_intervention);
            codeSinistreIntervention = v.findViewById(R.id.code_sinistre_intervention);
            adresseIntervention = v.findViewById(R.id.adresse_intervention);
            statutIntervention = v.findViewById(R.id.statut_intervention);
            layoutItemIntervention = v.findViewById(R.id.item_intervention_layout);
        }
    }

    // Créer de nouvelles vues (invoqué par le layout manager)
    @Override
    public ItemListInterventionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // créer une nouvelle vue
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_intervention, parent, false);

        context = parent.getContext();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - on récupère un élément dy dataset à cette position
        // - on remplace le contenu de la vue avec cet élément
        final InterventionDTO intervention = mDataset.get(position);

        // ID de l'intervention
        holder.idIntervention.setText(intervention.getId().toString());

        // Date de l'intervention
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);
        Date dateHeureCreation = new Date();
        System.out.println(); //16/11/2016 12:08:43
        holder.dateIntervention.setText(dateFormat.format(dateHeureCreation));

        // Code sinistre
        CodeSinistreDTO codeSinistreDTO = intervention.getCodeSinistre();
        String codeSinistreLabel = codeSinistreDTO.getCode() + " - " + codeSinistreDTO.getIntitule();
        holder.codeSinistreIntervention.setText(codeSinistreLabel);

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

        // Gestion du clic
        holder.layoutItemIntervention.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //(ListInterventionActivity).onClickIntervention();
                Log.i(TAG, "Un clic a été effectué sur l'item " + position);

                Intent intent = new Intent(context, DetailsInterventionActivity.class);
                intent.putExtra("interventionDTO", intervention);
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
