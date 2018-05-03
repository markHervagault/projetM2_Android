package istic.m2.ila.firefighterapp.listIntervention;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.AddInterventionActivity;
import istic.m2.ila.firefighterapp.adapter.ItemListInterventionAdapter;

public class ListInterventionFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton floatingActionButton;

    public ListInterventionFragment() {
        // Required empty public constructor
    }

    public static ListInterventionFragment newInstance() {
        ListInterventionFragment fragment = new ListInterventionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_intervention, container, false);

        // use a linear layout manager
        mRecyclerView = view.findViewById(R.id.recycler_list_intervention);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ItemListInterventionAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        floatingActionButton = view.findViewById(R.id.fab_list_intervention_add);
        // Récupérer l'information de connexion Codis/Intervenant
        Boolean userCodis = this.getActivity().getSharedPreferences("user", this.getActivity().getApplicationContext().MODE_PRIVATE)
                .getBoolean("isCodis", false);
        if(userCodis){
            floatingActionButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    openAddInterventionActivity(view);
                }
            });
        }
        else{
            floatingActionButton.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void openAddInterventionActivity(View view) {
        startActivity(new Intent(this.getActivity(), AddInterventionActivity.class));
    }
}
