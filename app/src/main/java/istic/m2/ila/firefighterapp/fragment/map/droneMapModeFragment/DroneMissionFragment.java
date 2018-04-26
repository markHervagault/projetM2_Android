package istic.m2.ila.firefighterapp.fragment.map.droneMapModeFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import istic.m2.ila.firefighterapp.R;

/**
 * Created by markh on 25/04/2018.
 */

public class DroneMissionFragment extends Fragment {

    /**
     * Identifiant de la classe pour les logs
     */
    private String TAG = "DroneMissionFragment => ";

    /**
     * Contexte
     */
    public Context context;

    private boolean isPathClosed;
    private boolean isAddButtonEnabled;

    //private DroneMissionDrawing _missionDrawing;

    View _view;

    public DroneMissionFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_mission_drone, container, false);
        this._view = view;
        this.context = view.getContext();

        //InitMenu();

        return view;
    }

    /**
     * Initialise le menu de controles déroulants avec des listeners
     */
    /*private void InitMenu() {
        isPathClosed = false;
        isAddButtonEnabled = false;

        FloatingActionButton fabRemoveSelectedMarker = _view.findViewById(R.id.fabMenu_removeSelectedMarker);
        final FloatingActionButton fabOpenClose = _view.findViewById(R.id.fabMenu_openClosePath);
        final FloatingActionButton fabAddMarker = _view.findViewById(R.id.fabMenu_addMarker);
        FloatingActionButton fabZone = _view.findViewById(R.id.fabMenu_zone);
        FloatingActionButton fabSendMission = _view.findViewById(R.id.fab_menu2_send);

        //Remove Button Listener
        fabRemoveSelectedMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "RemoveButton Cliked");
                // Gestion de l'événement click pour le bouton flottant
                _missionDrawing.DeleteSelectedMarker();
                RefreshOpenClosePathButtonStatus();
            }
        });

        //Add Button Listener
        fabAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAddButtonEnabled) // Activation du mode
                {
                    //Desactivation des boutons
                    ChangeMenuButtonsStatus(false);

                    //Activation du bouton d'ajout ?? Utile??
                    fabAddMarker.setEnabled(true);

                    // Couleurs du focus
                    fabAddMarker.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabSelectedNormal));
                    fabAddMarker.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabSelectedPressed));
                    fabAddMarker.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabSelectedRipple));

                    isAddButtonEnabled = true;
                    _missionDrawing.setAddMode(true);
                } else //Desactivation du mode
                {
                    //Reactivation des boutons
                    ChangeMenuButtonsStatus(true);

                    // Couleurs de l'unfocus
                    fabAddMarker.setColorNormal(getResources().
                            getColor(R.color.colorMenuFabDefaultNormal));
                    fabAddMarker.setColorPressed(getResources().
                            getColor(R.color.colorMenuFabDefaultPressed));
                    fabAddMarker.setColorRipple(getResources().
                            getColor(R.color.colorMenuFabDefaultRipple));

                    isAddButtonEnabled = false;
                    _missionDrawing.setAddMode(false);
                }
            }
        });

        fabOpenClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si le trajet est fermé
                if (_missionDrawing.isPathClosed()) {
                    //Changement de style du bouton
                    fabOpenClose.setColorNormal(getResources().getColor(R.color.colorMenuFabDefaultNormal));
                    fabOpenClose.setColorPressed(getResources().getColor(R.color.colorMenuFabDefaultPressed));
                    fabOpenClose.setColorRipple(getResources().getColor(R.color.colorMenuFabDefaultRipple));
                    fabOpenClose.setImageResource(R.drawable.openloop);

                    _missionDrawing.setPathClosed(false);
                } else {
                    fabOpenClose.setColorNormal(getResources().getColor(R.color.colorMenuFabSelectedNormal));
                    fabOpenClose.setColorPressed(getResources().getColor(R.color.colorMenuFabSelectedPressed));
                    fabOpenClose.setColorRipple(getResources().getColor(R.color.colorMenuFabSelectedRipple));
                    fabOpenClose.setImageResource(R.drawable.closedloop);

                    _missionDrawing.setPathClosed(true);
                }
            }
        });

        fabSendMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _missionDrawing.SendMission(1l, 1l, 0);
            }
        });

        //Ajout des boutons à la liste pour la désactivation
        floatingActionButtonList = new ArrayList<>();

        floatingActionButtonList.add(fabRemoveSelectedMarker);
        floatingActionButtonList.add(fabAddMarker);
        floatingActionButtonList.add(fabZone);

        //Abonnement aux changements de markers
        _missionDrawing.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                if(evt.getPropertyName().equals("markersCount"))
                    RefreshOpenClosePathButtonStatus();
            }
        });
    }

    private List<FloatingActionButton> floatingActionButtonList;

    private void ChangeMenuButtonsStatus(Boolean enabled) {
        for (FloatingActionButton button : floatingActionButtonList)
            button.setEnabled(enabled);
    }

    private void RefreshOpenClosePathButtonStatus() {
        FloatingActionButton openCloseButton = _view.findViewById(R.id.fabMenu_openClosePath);
        if (_missionDrawing.getMarkersCount() < 3) {
            openCloseButton.setEnabled(false);
        } else {
            openCloseButton.setEnabled(true);
        }
    }

    public DroneMissionDrawing getMissionDrawing() {
        return _missionDrawing;
    }

    public void setMissionDrawing(DroneMissionDrawing _missionDrawing) {
        this._missionDrawing = _missionDrawing;
    }*/
}
