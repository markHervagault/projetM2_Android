package istic.m2.ila.firefighterapp.map.intervention;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.IDTO;
import istic.m2.ila.firefighterapp.dto.SinistreDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopoDTO;
import istic.m2.ila.firefighterapp.dto.TraitTopographiqueBouchonDTO;

public class FragmentHolder extends Fragment {

    private Fragment fragmentToDisplay;
    private Object objectHeld;
    private ImageButton dropdownButton;
    private TextView titleView;
    public FragmentHolder() {
        // Required empty public constructor
    }

    //region lifeCycle
    public static FragmentHolder newInstance() {
        FragmentHolder fragment = new FragmentHolder();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_holder, container, false);
        view.setVisibility(View.GONE);
        this.titleView = view.findViewById(R.id.holder_fragment_title);
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
        setObjectHeld(null);
        setFragmentToDisplay(null);
        ((MapActivity) this.getActivity()).hideFragment();

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
        if(fragment == null) {
            ft.remove(fragmentToDisplay);
        } else {
            ft.replace(R.id.fragment_placeholder, fragment);
        }
        ft.commit();
    }

    public void replace(IDTO dto) {
        this.setObjectHeld(dto);
        this.changeTitle(dto.menuTitle(),dto.menuColor());
        this.setFragmentToDisplay(getFragment(dto));
    }

    public Fragment getFragment(IDTO dto) {
        if (dto instanceof TraitTopoDTO) {
            return FragmentFactory.getFragment((TraitTopoDTO) dto);
        } else if (dto instanceof SinistreDTO) {
            return FragmentFactory.getFragment((SinistreDTO) dto);
        } else if (dto instanceof DeploiementDTO) {
            return FragmentFactory.getFragment((DeploiementDTO) dto);
        } else if (dto instanceof TraitTopographiqueBouchonDTO) {
            return FragmentFactory.getFragment((TraitTopographiqueBouchonDTO) dto);
        }
        return null;
    }

    public void changeTitle(String title, String color) {
        this.titleView.setBackgroundColor(Color.parseColor(color));
        this.titleView.setText(title);
    }
}
