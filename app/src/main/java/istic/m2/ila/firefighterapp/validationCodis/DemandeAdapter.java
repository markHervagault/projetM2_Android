package istic.m2.ila.firefighterapp.validationCodis;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DemandeDTO;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.InterventionConsumer;
import retrofit2.Response;

/**
 * Created by amendes on 27/04/18.
 */

public class DemandeAdapter extends RecyclerView.Adapter<DemandeAdapter.DemandeViewHolder> {
    Context context;
    ArrayList<DemandeDTO> demandeDTOList = new ArrayList<>();

    public DemandeAdapter(Context context) {
        this.context = context;
        setData(context);
    }

    public void setData(final Context context) {
        AsyncTask.execute(new Runnable() {
            public void run() {
                // On peuple notre RecyclerView
                ArrayList<DemandeDTO> myDataset = new ArrayList<>();

                // Construction de notre appel REST
                RestTemplate restTemplate = RestTemplate.getInstance();
                InterventionConsumer interventionConsumer = restTemplate.builConsumer(InterventionConsumer.class);

                Response<List<DemandeDTO>> response = null;
                try {
                    // Récupération du token
                    String token = context.getSharedPreferences("user", context.getApplicationContext().MODE_PRIVATE)
                            .getString("token", "null");
                    // On récupère toutes les interventions du Serveur
                    response = interventionConsumer.getListDemandeDeploiement(token).execute();
                    if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                        myDataset = (ArrayList<DemandeDTO>) response.body();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                demandeDTOList = myDataset;
            }
        });
    }

    public static class DemandeViewHolder extends RecyclerView.ViewHolder {
        public TextView intervention;
        public TextView date;
        public TextView typeVehicule;
        public Spinner vehiculeDisponible;
        public Button buttonValidate;
        public Button buttonDismiss;
        Context context;

        public DemandeViewHolder(View v, Context context) {
            super(v);
            intervention = (TextView) v.findViewById(R.id.interventionDemande);
            date = (TextView) v.findViewById(R.id.dateDemandeIntervention);
            typeVehicule = (TextView) v.findViewById(R.id.typeVehiculeDemande);
            vehiculeDisponible = (Spinner) v.findViewById(R.id.vehiculeDisponible);
            buttonDismiss = (Button) v.findViewById(R.id.refuserDemande);
            buttonValidate = (Button) v.findViewById(R.id.validateButton);
            this.context = context;
        }

        public void bindDemande(DemandeDTO demandeDTO) {
            this.intervention.setText(demandeDTO.getId().toString());
            this.date.setText(demandeDTO.getDateHeureDemande().toString());
            this.typeVehicule.setText(demandeDTO.getTypeDemande().getLabel());
            this.vehiculeDisponible.setAdapter(
                    new VehiculeDispoAdapter(this.context,
                            android.R.layout.simple_spinner_item,
                            demandeDTO.getTypeDemande()));
        }
    }

    @Override
    public DemandeAdapter.DemandeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_demande, parent, false);
        DemandeViewHolder viewHolder = new DemandeViewHolder(view, this.context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DemandeViewHolder holder, int position) {
        holder.bindDemande(demandeDTOList.get(position));
    }

    @Override
    public int getItemCount() {
        return demandeDTOList.size();
    }
}
