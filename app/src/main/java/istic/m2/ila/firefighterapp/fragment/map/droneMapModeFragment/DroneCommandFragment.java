package istic.m2.ila.firefighterapp.fragment.map.droneMapModeFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.dto.EDroneStatut;

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
    private ImageButton buttonPlayPause;

    /**
     * Bouton stop mission
     */
    private ImageButton buttonStop;

    private String isPlay = "play";
    private String isPause = "pause";

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
        buttonPlayPause.setTag(isPlay);
        buttonStop = view.findViewById(R.id.ButtonStop);

        return view;
    }

    public void setActionForStopButton(final Runnable runnable) {
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnable.run();
            }
        });
    }

    public void setActionForPlayPauseButton(final Runnable runnablePlay, final Runnable runnablePause){
        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonPlayPause.getTag().equals(isPlay)){
                    runnablePlay.run();
                }else{
                    runnablePause.run();
                }
            }
        });
    }

    public void changeDroneStatut(EDroneStatut statut){

        if(statut==null)
        {
            Log.e(TAG, "Le nouveau statut du drone est null");
            return;
        }

        switch (statut) {
            case EN_MISSION:
                buttonPlayPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.pause));
                break;
            /*case EN_PAUSE:
                buttonPlayPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
                break;
            case DISPONIBLE:
            case RETOUR_BASE:
                buttonPlayPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.pause));
                buttonStop.setVisibility(View.GONE);
                break;*/
            case CONNECTE:
            case DECONNECTE:
            default:
                break;
        }
    }

}
