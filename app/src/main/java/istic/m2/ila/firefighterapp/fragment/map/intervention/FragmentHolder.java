package istic.m2.ila.firefighterapp.fragment.map.intervention;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.CreationSinistre;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.CreationTraitTopo;

public class FragmentHolder extends Fragment {

    private Fragment fragmentToDisplay;

    public FragmentHolder() {
        // Required empty public constructor
    }

    //region lifeCycle

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHolder.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentHolder newInstance(String param1, String param2) {
        FragmentHolder fragment = new FragmentHolder();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_fragment_holder, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //endregion lifeCycle
    public Fragment getFragmentToDisplay() {
        return this.getFragmentToDisplay();
    }

    public void setFragmentToDisplay(Fragment fragment) {
        this.insertFragment(fragment);
        this.fragmentToDisplay = fragment;
    }

    private void insertFragment(Fragment fragment) {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragment);
        ft.commit();
    }

    public void replace(DeploiementDTO deploiementDTO) {
        if (deploiementDTO.getId() != null) {
            //set(new FragmentDeploiementDétail)
        } else {
            //set(new FragmentDeploiementCreation)
        }
    }

    public void replace(TraitTopoDTO traitTopoDTO) {
        if (traitTopoDTO.getId() != null) {
            this.setFragmentToDisplay(new CreationTraitTopo());
        } else {
            this.setFragmentToDisplay(new CreationTraitTopo());
        }
    }

    public void replace(SinistreDTO sinistreDTO) {
        if (sinistreDTO.getId() != null) {
            this.setFragmentToDisplay(new CreationSinistre());
        } else {
            this.setFragmentToDisplay(new CreationSinistre());
        }
    }
}
