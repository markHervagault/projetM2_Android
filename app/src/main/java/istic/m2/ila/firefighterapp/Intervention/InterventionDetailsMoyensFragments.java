package istic.m2.ila.firefighterapp.Intervention;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.DeploimentConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.EEtatDeploiement;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsMoyensFragments extends Fragment {

    private static String TAG = "FragmentMoyens";


    private Long idIntervention;
    private List<DeploiementDTO> listDeploiment;
    private Context context;
    private LinearLayout linLayTabMoy;

    public interface ActivityMoyens {
        Long getIdIntervention();
    }

    public InterventionDetailsMoyensFragments() {
        // Required empty public constructor
    }

    private List<DeploiementDTO> getDeploiments() {
        Log.i(TAG, "getDeploimentsTri Begin");
        String token = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        String id = this.idIntervention.toString();

        RestTemplate restTemplate = RestTemplate.getInstance();
        DeploimentConsumer deploimentConsumer = restTemplate.builConsumer(DeploimentConsumer.class);
        Response<List<DeploiementDTO>> response = null;

        List<DeploiementDTO>  listDeploimentTmp = null;


        try {
            response = deploimentConsumer.getListDeploimentById(token, id).execute();

            if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                listDeploimentTmp = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.i(TAG, "getDeploimentsTri End");
        return listDeploimentTmp;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //init pojo data
        this.context = context;
        this.idIntervention = ((ActivityMoyens) this.getActivity()).getIdIntervention();
        this.listDeploiment = getDeploiments();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_intervention_details_moyens_fragments, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.interventionDetailsMoyenRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleAdapter(recyclerView, this.listDeploiment));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //update fields
    }

    private class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {
        private static final int UNSELECTED = -1;

        private RecyclerView recyclerView;
        private int selectedItem = UNSELECTED;
        private List<DeploiementDTO> listDeploiment;


        public SimpleAdapter(RecyclerView recyclerView,List<DeploiementDTO> listDeploimentParam) {
            this.recyclerView = recyclerView;
            this.listDeploiment = listDeploimentParam;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_intervention_recycler_item_type_moyen, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(holder, position);
        }

        @Override
        public int getItemCount() {
            return listDeploiment.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ExpandableLayout expandableLayout;
            private LinearLayout expandedLinearLayout;
            private TextView tvTypeMoyen;
            private TextView tvEtatMoyen;
            private TextView tvLabelMoyen;
            private TextView tvCrm;
            private TextView tvSpacetabMoyTop;
            private TextView tvSpacetabMoyBot;

            private TextView tvHeureDemande;
            private TextView tvHeureValidationRefus;
            private TextView tvHeureEngagement;
            private TextView tvHeureLiberation;

            private int positionList;


            public ViewHolder(View itemView) {
                super(itemView);


                tvTypeMoyen = itemView.findViewById(R.id.expand_button);
                tvEtatMoyen = itemView.findViewById(R.id.tvEtatMoyen);
                tvLabelMoyen = itemView.findViewById(R.id.tvLabelMoyen);
                tvCrm = itemView.findViewById(R.id.tvCrmOrNot);
                tvSpacetabMoyTop = itemView.findViewById(R.id.spaceTabMoyTop);
                tvSpacetabMoyBot = itemView.findViewById(R.id.spaceTabMoyBottom);

                tvHeureDemande  = itemView.findViewById(R.id.tvHeureDemande);
                tvHeureValidationRefus = itemView.findViewById(R.id.tvHeureValidationRefus);
                tvHeureEngagement = itemView.findViewById(R.id.tvHeureEngagement);
                tvHeureLiberation = itemView.findViewById(R.id.tvHeureLiberation);
            }

            public void bind(ViewHolder holder, int position) {

                boolean isSelected = position == selectedItem;

                holder.tvTypeMoyen.setText(listDeploiment.get(position).getTypeDemande().getLabel());
                holder.tvTypeMoyen.setBackgroundColor(Color.parseColor(listDeploiment.get(position).getComposante().getCouleur()));
                holder.tvTypeMoyen.setTextColor(Color.WHITE);
                holder.tvSpacetabMoyTop.setBackgroundColor(Color.GRAY);

                if(position==0){
                    holder.tvSpacetabMoyTop.setBackgroundColor(Color.GRAY);
                    holder.tvSpacetabMoyTop.setVisibility(View.VISIBLE);
                }
                holder.tvSpacetabMoyBot.setBackgroundColor(Color.GRAY);

                if(null != listDeploiment.get(position).getVehicule()){
                        holder.tvLabelMoyen.setText(listDeploiment.get(position).getVehicule().getLabel());
                    } else {
                        holder.tvLabelMoyen.setText("...");
                    }

                holder.tvEtatMoyen.setText(listDeploiment.get(position).getState().toString().toUpperCase());
                    if( listDeploiment.get(position).isPresenceCRM()){
                        holder.tvCrm.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvCrm.setVisibility(View.INVISIBLE);

                    }

                holder.tvTypeMoyen.setSelected(isSelected);

                SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");


                if(listDeploiment.get(position).getDateHeureDemande()!=null) {
                    holder.tvHeureDemande.setText(formater.format(listDeploiment.get(position).getDateHeureDemande()));
                }

                if(listDeploiment.get(position).getDateHeureValidation()!=null) {
                    holder.tvHeureValidationRefus.setText(formater.format(listDeploiment.get(position).getDateHeureValidation()));
                }

                if(listDeploiment.get(position).getDateHeureEngagement()!=null) {
                    holder.tvHeureEngagement.setText(formater.format(listDeploiment.get(position).getDateHeureEngagement()));
                }

                if(listDeploiment.get(position).getDateHeureDesengagement()!=null) {
                    holder.tvHeureLiberation.setText(formater.format(listDeploiment.get(position).getDateHeureDesengagement()));
                }


                positionList = position;
                    TextView tmpText1 = new TextView(itemView.getContext());
                    tmpText1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NewMapActivity)getActivity()).displayFragmentHolder(listDeploiment.get(positionList));
                        }
                    });
            }
           }
    }
}
