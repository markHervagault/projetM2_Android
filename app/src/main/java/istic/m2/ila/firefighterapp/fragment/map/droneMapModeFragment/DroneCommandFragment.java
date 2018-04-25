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
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PauseMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.PlayMissionMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.SelectedDroneChangedMessage;
import istic.m2.ila.firefighterapp.clientRabbitMQ.messages.StopMissionMessage;
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

    private Long selectedDroneId;

    View view;

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
        View view = inflater.inflate(R.layout.fragment_commande_drone, container, false);
        this.view = view;
        this.context = view.getContext();

        buttonPlayPause = view.findViewById(R.id.ButtonPlayPause);
        buttonPlayPause.setTag(isPause);
        buttonPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonPlayPause.getTag().equals(isPlay)){
                    EventBus.getDefault().post(new PlayMissionMessage(selectedDroneId));
                    Log.d(TAG, "Envoie d'une commande play au drone d'id "+selectedDroneId);
                }else{
                    EventBus.getDefault().post(new PauseMissionMessage(selectedDroneId));
                    Log.d(TAG, "Envoie d'une commande pause au drone d'id "+selectedDroneId);
                }
            }
        });

        buttonStop = view.findViewById(R.id.ButtonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new StopMissionMessage(selectedDroneId));
                Log.d(TAG, "Envoie d'une commande stop au drone d'id "+selectedDroneId);
            }
        });

        return view;
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
                buttonStop.setVisibility(View.VISIBLE);
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonPlayPause.setTag(isPause);
                break;
            case EN_PAUSE:
                buttonPlayPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.play));
                buttonStop.setVisibility(View.VISIBLE);
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonPlayPause.setTag(isPlay);
                break;
            case RETOUR_BASE:
                buttonPlayPause.setBackgroundDrawable(getResources().getDrawable(R.drawable.pause));
                buttonPlayPause.setVisibility(View.VISIBLE);
                buttonStop.setVisibility(View.GONE);
                buttonPlayPause.setTag(isPause);
                break;
            case DISPONIBLE:
            case DECONNECTE:
            default:
                buttonStop.setVisibility(View.GONE);
                buttonPlayPause.setVisibility(View.GONE);
                break;
        }
    }

    public Long getSelectedDroneId() {
        return selectedDroneId;
    }

    public void setSelectedDroneId(Long selectedDroneId) {
        this.selectedDroneId = selectedDroneId;
    }
}
