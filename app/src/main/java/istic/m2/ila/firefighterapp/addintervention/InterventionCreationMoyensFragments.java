package istic.m2.ila.firefighterapp.addintervention;


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
import android.widget.LinearLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.consumer.RestTemplate;
import istic.m2.ila.firefighterapp.consumer.VehiculeConsumer;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionCreationMoyensFragments extends Fragment {

    private static String TAG = "CreationInterventionMoyens";

    private Map<String, List<VehiculeDTO>> mapVehiculesDisponibles;
    private Context context;

    public InterventionCreationMoyensFragments() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //init pojo data
        this.context = context;
        //fill items
        this.mapVehiculesDisponibles = getVehicules();
    }

    private Map<String, List<VehiculeDTO>>  getVehicules(){
        Log.i(TAG, "getVehiculeDispo Begin");
        String token = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");

        RestTemplate restTemplate = RestTemplate.getInstance();
        VehiculeConsumer vehiculeConsumer = restTemplate.builConsumer(VehiculeConsumer.class);
        Response<List<VehiculeDTO>> response = null;

        Map<String, List<VehiculeDTO>> mapSorted = new HashMap<>();
        List<VehiculeDTO> vehiculeDTOList = null;


        try {
            response = vehiculeConsumer.getListVehiculeDispo(token).execute();

            if (response != null && response.code() == HttpURLConnection.HTTP_OK) {
                vehiculeDTOList = response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String type = null;
        if (vehiculeDTOList != null) {
            for (VehiculeDTO vehicule  : vehiculeDTOList) {


                type = vehicule.getType().getLabel();

                List<VehiculeDTO> list = !mapSorted.containsKey(type) ? new ArrayList<VehiculeDTO>() : mapSorted.get(type);
                list.add(vehicule);

                mapSorted.put(type, list);
            }
        }
        Log.i(TAG, "getVehiculeDispo End");
        return mapSorted;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_intervention_details_moyens_fragments, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.interventionDetailsMoyenRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SimpleAdapter(recyclerView, mapVehiculesDisponibles));

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
        private Map<String, List<VehiculeDTO>> mapSorted;


        public SimpleAdapter(RecyclerView recyclerView, Map<String, List<VehiculeDTO>> mapSorted) {
            this.recyclerView = recyclerView;
            this.mapSorted = mapSorted;
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
            return mapSorted.size();
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
                ArrayList<String> keys = new ArrayList<>(mapSorted.keySet());

                List<VehiculeDTO> content = mapSorted.get(keys.get(position));

                boolean isSelected = position == selectedItem;

                expandButton.setText(keys.get(position));
                expandButton.setSelected(isSelected);
                expandableLayout.setExpanded(isSelected, false);

                for (VehiculeDTO vehicule : content) {
                    TextView tmpText1 = new TextView(itemView.getContext());
//                    Button dtn = new Button();
//                    dtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });
                    tmpText1.setText(vehicule.getLabel());
                    tmpText1.setTextColor(Color.WHITE);
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
