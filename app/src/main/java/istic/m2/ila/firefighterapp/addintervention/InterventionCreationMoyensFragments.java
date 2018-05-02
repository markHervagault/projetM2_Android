package istic.m2.ila.firefighterapp.addintervention;


import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.rest.RestTemplate;
import istic.m2.ila.firefighterapp.rest.consumers.VehiculeConsumer;
import istic.m2.ila.firefighterapp.dto.DeploiementCreateInterventionDTO;
import istic.m2.ila.firefighterapp.dto.VehiculeDTO;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterventionCreationMoyensFragments extends Fragment {

    private static String TAG = "CreationInterventionMoyens";

    private Map<String, List<VehiculeDTO>> mapVehiculesDisponibles;
    private Context context;
    private List<VehiculeDTO> vehiculeSelected;

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
        this.vehiculeSelected = new ArrayList<>();
    }

    private Map<String, List<VehiculeDTO>>  getVehicules(){

        Map<String, List<VehiculeDTO>> mapSorted = new HashMap<>();

        Log.i(TAG, "getVehiculeDispo Begin");
        String token = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("token", "null");

        getVehiculesFromServer(mapSorted, token);
        return mapSorted;
    }

    private void getVehiculesFromServer(final Map<String, List<VehiculeDTO>> mapSorted, final String token) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                RestTemplate restTemplate = RestTemplate.getInstance();
                VehiculeConsumer vehiculeConsumer = restTemplate.builConsumer(VehiculeConsumer.class);
                Response<List<VehiculeDTO>> response = null;

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
            }
        });
    }

    public Set<DeploiementCreateInterventionDTO> getVehiculesSelected(){
        Set<DeploiementCreateInterventionDTO> deploimentSet = new HashSet<>();
        for (VehiculeDTO vehiculeDTO: vehiculeSelected) {
            DeploiementCreateInterventionDTO deploiement = new DeploiementCreateInterventionDTO();
            deploiement.setIdVehicule(vehiculeDTO.getId());
            deploiement.setIdTypeComposante(vehiculeDTO.getType().getId());
            deploimentSet.add(deploiement);
        }
        return deploimentSet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_moyens_creation_intervention_fragment, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_add_moyen_fragment);
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
                    .inflate(R.layout.add_moyens_creation_intervention_fragment, parent, false);
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

                expandableLayout = itemView.findViewById(R.id.expandable_layout_add_moyen_fragment);
                expandableLayout.setInterpolator(new OvershootInterpolator());
                expandableLayout.setOnExpansionUpdateListener(this);
                expandButton = itemView.findViewById(R.id.expand_button_add_moyen_fragment);
                expandButton.setOnClickListener(this);
                expandedLinearLayout = itemView.findViewById(R.id.list_layout_moyen_add_moyen_fragment);
            }

            public void bind() {
                int position = getAdapterPosition();
                ArrayList<String> keys = new ArrayList<>(mapSorted.keySet());

                final List<VehiculeDTO> content = mapSorted.get(keys.get(position));

                boolean isSelected = position == selectedItem;

                expandButton.setText(keys.get(position));
                expandButton.setSelected(isSelected);
                expandableLayout.setExpanded(isSelected, false);
//                expandableLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                for (final VehiculeDTO vehicule : content) {
                    Switch aSwitch = new Switch(itemView.getContext());

                    aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                vehiculeSelected.add(vehicule);
                            } else {
                                vehiculeSelected.remove(vehicule);
                            }
                        }
                    });
                    aSwitch.setText(vehicule.getLabel());
                    aSwitch.setTextColor(Color.WHITE);
                    expandedLinearLayout.addView(aSwitch);
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
