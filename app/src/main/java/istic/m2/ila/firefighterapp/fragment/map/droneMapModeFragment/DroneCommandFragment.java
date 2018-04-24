package istic.m2.ila.firefighterapp.fragment.map.droneMapModeFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

import istic.m2.ila.firefighterapp.R;

/**
 * Created by markh on 24/04/2018.
 */

public class DroneCommandFragment extends Fragment {

    /**
     * Identifiant de la classe pour les logs
     */
    private String TAG = "DroneCommandFragment => ";

    /**
     * Contexte
     */
    public Context context;

    /**
     * Bouton play/pause mission
     */
    public ImageButton buttonPlayPause;

    /**
     * Bouton stop mission
     */
    public ImageButton buttonStop;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commande_drone, container, false);
        this.view = view;
        this.context = view.getContext();

        buttonPlayPause = view.findViewById(R.id.ButtonPlayPause);
        buttonStop = view.findViewById(R.id.ButtonStop);

        return view;
    }
}
