package istic.m2.ila.firefighterapp.Intervention;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.DeploimentConsumer;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.TypeVehiculeDTO;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionDetailsMoyensFragments extends Fragment {

    private static String TAG = "FragmentMoyens";


    private Long idIntervention;
    private Map<String, List<DeploiementDTO>> mapSortDeploiment;
    private Context context;

    public interface ActivityMoyens {
        Long getIdIntervention();
    }

    public InterventionDetailsMoyensFragments() {
        // Required empty public constructor
    }

    private Map<String, List<DeploiementDTO>> getDeploiments() {
        Log.i(TAG, "getDeploimentsTri Begin");
        String token = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");
        String id = this.idIntervention.toString();

        RestTemplate restTemplate = RestTemplate.getInstance();
        DeploimentConsumer deploimentConsumer = restTemplate.builConsumer(DeploimentConsumer.class);
        Response<List<DeploiementDTO>> response = null;

        mapSortDeploiment = new HashMap<>();
        List<DeploiementDTO> deploiementDTOList = null;


        try {
            response = deploimentConsumer.getListDeploimentById(token, id).execute();

            if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                deploiementDTOList = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String type = null;
        if (deploiementDTOList != null) {
            for (DeploiementDTO deploiement : deploiementDTOList) {

                if (deploiement.getVehicule() != null) {
                    type = deploiement.getVehicule().getType().getLabel();
                } else {
                    type = deploiement.getTypeDemande().getLabel();
                }
                List<DeploiementDTO> list = !mapSortDeploiment.containsKey(type) ? new ArrayList<DeploiementDTO>() : mapSortDeploiment.get(type);
                list.add(deploiement);

                mapSortDeploiment.put(type, list);
            }
        }
        Log.i(TAG, "getDeploimentsTri End");
        return mapSortDeploiment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //init pojo data
        this.context = context;
        this.idIntervention = ((ActivityMoyens) this.getActivity()).getIdIntervention();
        this.mapSortDeploiment = getDeploiments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intervention_details_moyens_fragments, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.interventionDetailsMoyenRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleAdapter(recyclerView, mapSortDeploiment));

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
        private Map<String, List<DeploiementDTO>> mapSortDeploiment;


        public SimpleAdapter(RecyclerView recyclerView, Map<String, List<DeploiementDTO>> mapSortDeploiment) {
            this.recyclerView = recyclerView;
            this.mapSortDeploiment = mapSortDeploiment;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_intervention_recycler_item_type_moyen, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return mapSortDeploiment.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
            private ExpandableLayout expandableLayout;
            private LinearLayout expandedLinearLayout;
            private TextView expandButton;

            public ViewHolder(View itemView) {
                super(itemView);

                expandableLayout = itemView.findViewById(R.id.expandable_layout);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                expandableLayout.setOnExpansionUpdateListener(this);
                expandButton = itemView.findViewById(R.id.expand_button);
                expandButton.setOnClickListener(this);
                expandedLinearLayout = itemView.findViewById(R.id.list_layout_moyen);
            }

            public void bind() {
                int position = getAdapterPosition();
                ArrayList<String> keys = new ArrayList<>(mapSortDeploiment.keySet());

                List<DeploiementDTO> content = mapSortDeploiment.get(keys.get(position));

                boolean isSelected = position == selectedItem;

                expandButton.setText(keys.get(position));
                expandButton.setSelected(isSelected);
                expandableLayout.setExpanded(isSelected, false);

                for (DeploiementDTO deploiment : content) {
                    TextView tmpText1 = new TextView(itemView.getContext());
//                    Button dtn = new Button();
//                    dtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
                    if (deploiment.getVehicule() != null) {
                        tmpText1.setText(deploiment.getVehicule().getLabel());
                        tmpText1.setTextColor(Color.WHITE);

                    } else {
                        String text = deploiment.getTypeDemande().getLabel()
                                + " "
                                + getResources().getString(R.string.intervention_detail_fragment_moyens_status_demande);
                        tmpText1.setText(text);
                        tmpText1.setTextColor(Color.YELLOW);
                    }

                    expandedLinearLayout.addView(tmpText1);
                }


            }

            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);
                if (holder != null) {
                    holder.expandButton.setSelected(false);
                    holder.expandableLayout.collapse();
                }

                int position = getAdapterPosition();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                } else {
                    expandButton.setSelected(true);
                    expandableLayout.expand();
                    selectedItem = position;
                }
            }

            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("ExpandableLayout", "State: " + state);
                if (state == ExpandableLayout.State.EXPANDING) {
                    recyclerView.smoothScrollToPosition(getAdapterPosition());
                }
            }
        }
    }
}
