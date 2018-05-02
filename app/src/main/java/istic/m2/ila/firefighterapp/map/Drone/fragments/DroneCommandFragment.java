package istic.m2.ila.firefighterapp.map.Drone.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.dto.EDroneStatus;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.eventbus.drone.SelectedDroneStatusChangedMessage;

/**
 * Created by markh on 24/04/2018.
 */

public class DroneCommandFragment extends Fragment {

    /**
     * Identifiant de la classe pour les logs
     */
    private String TAG = "DroneCommandFragment";

    /**
     * Contexte
     */
    public Context context;

    /**
     * Bouton play/pause mission
     */
    public FloatingActionButton buttonPlayPause;

    /**
     * Bouton stop mission
     */
    public FloatingActionButton buttonStop;

    public static final String PLAY_TAG = "play";
    public static final String PAUSE_TAG = "pause";

    View view;

    //region Constructor

    public DroneCommandFragment ()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drone_command, container, false);
        this.view = view;
        this.context = view.getContext();

        buttonPlayPause = view.findViewById(R.id.ButtonPlayPause);
        buttonStop = view.findViewById(R.id.ButtonStop);

        EventBus.getDefault().register(this);
        return view;
    }

    //endregion

    //region Methods

    public void Reset(final EDroneStatus status)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RefreshUI(status);
            }
        });
    }

    public void RefreshUI(EDroneStatus status)
    {
        //Mise a jour de l'UI en fonction de l'état du drone
        if(status == null)
        {
            Log.e(TAG, "Le nouveau statut du drone est null");
            return;
        }

        Log.e(TAG, "RefreshUI : " + status.toString());

        switch (status) {
            case EN_MISSION:
                buttonPlayPause.setImageResource(R.drawable.pause_black);
                buttonStop.setVisibility(View.VISIBLE);
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonPlayPause.setTag(PAUSE_TAG);
                break;

            case RETOUR_BASE:
                buttonPlayPause.setImageResource(R.drawable.pause_black);
                buttonStop.setVisibility(View.GONE);
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonPlayPause.setTag(PAUSE_TAG);
                break;

            case EN_PAUSE:
                buttonStop.setVisibility(View.VISIBLE);
                buttonPlayPause.setImageResource(R.drawable.play_black);
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonPlayPause.setTag(PLAY_TAG);
                break;

            case PAUSE_RETOUR_BASE:
                buttonStop.setVisibility(View.GONE);
                buttonPlayPause.setImageResource(R.drawable.play_black);
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonPlayPause.setTag(PLAY_TAG);
                break;

            case DISPONIBLE:
            case DECONNECTE:
            default:
                buttonStop.setVisibility(View.GONE);
                buttonPlayPause.setVisibility(View.GONE);
                break;
        }
    }

    //endregion

    //region Event bus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedDroneStatusChanged(SelectedDroneStatusChangedMessage message)
    {
        RefreshUI(message.getDroneStatut());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedDroneChanged(SelectedDroneChangedMessage message)
    {
        RefreshUI(message.Drone.getStatut());
    }


    //endregion
}
