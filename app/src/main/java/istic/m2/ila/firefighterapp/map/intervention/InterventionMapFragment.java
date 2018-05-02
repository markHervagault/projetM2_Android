package istic.m2.ila.firefighterapp.map.intervention;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.activitiy.MapActivity;
import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.DeploiementDTO;
import istic.m2.ila.firefighterapp.dto.GeoPositionDTO;

public class InterventionMapFragment extends Fragment {

    public InterventionMapFragment() {
        // Required empty public constructor
    }

    MapView mMapView;

    View mView;

    private GoogleMap googleMap;

    private FragmentHolder fragmentHolder;

    public GoogleMap getMap(){
        return googleMap;
    }


    public MapActivity getMeActivity(){
        return (MapActivity)getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_intervention_map, container, false);
        this.fragmentHolder = FragmentHolder.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.interventionDetailsFragmentLayout, fragmentHolder).commit();

        final Button button = mView.findViewById(R.id.toggleView);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getMeActivity().toggleView();
            }
        });

        final Button buttonMoy = mView.findViewById(R.id.toggleViewTabMoy);

        buttonMoy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getMeActivity().toggleReduceTabMoyens();
            }
        });
        buttonMoy.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getMeActivity().showHideMoy();
                return true;
            }
        });

        mMapView = (MapView) mView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the istic.m2.ila.firefighterapp.map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                initMap();
                initMenu();
            }
        });

        return mView;
    }

    private void initMap() {
        getMeActivity().initMap(googleMap);
        getVehicule();
    }

    // region MenuFlottant
    private List<FloatingActionButton> floatingActionButtonList;
    private boolean isTraitTopoSelected;
    private boolean isSinistreSelected;
    private boolean isDeploiementSelected;

    /**
     * Initialisation et définition des actions, du comportement visuel pour les
     * Boutons du menu flottant
     */
    private void initMenu() {
        isTraitTopoSelected = false;
        isDeploiementSelected = false;
        isSinistreSelected = false;

        final FloatingActionButton fabTraitTopographique = mView.findViewById(R.id.fabMenu_trait);
        final FloatingActionButton fabSinistre = mView.findViewById(R.id.fabMenu_sinistre);
        final FloatingActionButton fabDeploiement = mView.findViewById(R.id.fabMenu_deploiement);

        //Remove Button Listener
        fabTraitTopographique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //isTraitTopoSelected = toggleColorFloatingButton(fabTraitTopographique, isTraitTopoSelected);
                getMeActivity().createTrait();
            }
        });

        //Add Button Listener
        fabDeploiement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isDeploiementSelected = toggleColorFloatingButton(fabDeploiement, isDeploiementSelected);
                getMeActivity().createMoyen();
            }
        });

        fabSinistre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isSinistreSelected = toggleColorFloatingButton(fabSinistre, isSinistreSelected);
                getMeActivity().createSinistre();
            }
        });

        //Ajout des boutons à la liste pour la désactivation
        floatingActionButtonList = new ArrayList<>();

        floatingActionButtonList.add(fabTraitTopographique);
        floatingActionButtonList.add(fabDeploiement);
        floatingActionButtonList.add(fabSinistre);
    }

    /**
     * Fonction pour changer la couleur d'un bouton flottant suivant une condition.
     * @param fab floating action button ciblé
     * @param condition condition d'aiguillage pour le toggle
     * @return la nouvelle valeur courante (après le toggle)
     */
    private boolean toggleColorFloatingButton(FloatingActionButton fab, boolean condition) {
        if (!condition) // Activation du mode
        {
            //Desactivation des boutons
            ChangeMenuButtonsStatus(false);

            //Activation du bouton d'ajout ?? Utile??
            fab.setEnabled(true);

            // Couleurs du focus
            fab.setColorNormal(getResources().
                    getColor(R.color.colorMenuFabSelectedNormal));
            fab.setColorPressed(getResources().
                    getColor(R.color.colorMenuFabSelectedPressed));
            fab.setColorRipple(getResources().
                    getColor(R.color.colorMenuFabSelectedRipple));

            condition = true;
        } else //Desactivation du mode
        {
            //Reactivation des boutons
            ChangeMenuButtonsStatus(true);

            // Couleurs de l'unfocus
            fab.setColorNormal(getResources().
                    getColor(R.color.colorMenuFabDefaultNormal));
            fab.setColorPressed(getResources().
                    getColor(R.color.colorMenuFabDefaultPressed));
            fab.setColorRipple(getResources().
                    getColor(R.color.colorMenuFabDefaultRipple));

            condition = false;
        }
        return condition;
    }

    /**
     * Change l'état des boutons suivant la valeur passée en paramètres
     * @param enabled true pour activer, false sinon
     */
    private void ChangeMenuButtonsStatus(Boolean enabled) {
        for (FloatingActionButton button : floatingActionButtonList)
            button.setEnabled(enabled);
    }
    // endregion

    private void getVehicule() {
        /*AsyncTask.execute(new Runnable() {
            public void run() {

            }
        });*/
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String token = getActivity().getSharedPreferences("user", getContext().MODE_PRIVATE)
                        .getString("token", "null");
                GeoPositionDTO geo = getMeActivity().getGeoPositionIntervention();
                List<DeploiementDTO> deploys = getMeActivity().getService()
                        .getDeploy(token,getMeActivity().getIdIntervention());
                for(DeploiementDTO deploy : deploys) {
                    getMeActivity().drawVehicule(googleMap,deploy);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public FragmentHolder getFragmentHolder() {
        return fragmentHolder;
    }
}
