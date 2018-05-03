package istic.m2.ila.firefighterapp.validationCodis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import istic.m2.ila.firefighterapp.R;

public class ValidationDemandeFragment extends Fragment {

    private RecyclerView recyclerDemande;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public ValidationDemandeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ValidationDemandeFragment newInstance() {
        ValidationDemandeFragment fragment = new ValidationDemandeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_validation_demande, container, false);

        this.recyclerDemande = view.findViewById(R.id.recyclerDemande);
        layoutManager = new LinearLayoutManager(this.getActivity());
        this.recyclerDemande.setLayoutManager(layoutManager);
        adapter = new DemandeAdapter(this.getActivity());
        this.recyclerDemande.setAdapter(adapter);

        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
