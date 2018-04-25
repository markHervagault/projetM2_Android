package istic.m2.ila.firefighterapp.fragment.map.intervention;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import istic.m2.ila.firefighterapp.NewMapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;
import istic.m2.ila.firefighterapp.dto.iDTO;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.DetailDeployFragment;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.DetailSinistreFragment;
import istic.m2.ila.firefighterapp.fragment.map.intervention.fragments.DetailTraitTopoFragment;

public class FragmentHolder extends Fragment {

    private Fragment fragmentToDisplay;
    private Object objectHeld;
    private ImageButton dropdownButton;

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
        View view = inflater.inflate(R.layout.fragment_fragment_holder, container, false);
        view.setVisibility(View.GONE);
        this.dropdownButton = view.findViewById(R.id.dropdownButton);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.dropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                hideSelf();
            }
        });
    }

    public void hideSelf() {
        ((NewMapActivity) this.getActivity()).hideFragment();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //endregion lifeCycle
    public Fragment getFragmentToDisplay() {
        return this.getFragmentToDisplay();
    }

    public Object getObjectHeld() {
        return objectHeld;
    }

    public void setObjectHeld(Object objectHeld) {
        this.objectHeld = objectHeld;
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

    public void replace(iDTO dto) {
        this.setObjectHeld(dto);
        this.setFragmentToDisplay(getFragment(dto));
    }

    public Fragment getFragment(iDTO dto) {
        if (dto instanceof TraitTopoDTO) {
            return FragmentFactory.getFragment((TraitTopoDTO) dto);
        } else if (dto instanceof SinistreDTO) {
            return FragmentFactory.getFragment((SinistreDTO) dto);
        } else if (dto instanceof DeploiementDTO) {
            //return FragmentFactory.getFragment((DeploiementDTO) dto);
        } else if (dto instanceof TraitTopographiqueBouchonDTO) {
            return FragmentFactory.getFragment((TraitTopographiqueBouchonDTO) dto);
        }
        return null;
    }
}
