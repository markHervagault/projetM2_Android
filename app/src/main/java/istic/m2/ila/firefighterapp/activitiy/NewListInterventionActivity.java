package istic.m2.ila.firefighterapp.activitiy;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import istic.m2.ila.firefighterapp.R;
import istic.m2.ila.firefighterapp.listIntervention.ListInterventionFragment;
import istic.m2.ila.firefighterapp.rabbitMQ.clientRabbitMqGeneric.ServiceRabbitMQIntervention;
import istic.m2.ila.firefighterapp.validationCodis.ValidationDemandeFragment;

public class NewListInterventionActivity extends AppCompatActivity {
    Boolean userCodis;

    ServiceRabbitMQIntervention serviceRabbitMQIntervention;
    private ServiceConnection serviceConnectionIntervention;
    private boolean isServiceRabbitMQInterventionBind = false;

    private void BindService() {
        bindService(new Intent(this, ServiceRabbitMQIntervention.class), serviceConnectionIntervention, Context.BIND_AUTO_CREATE);
        isServiceRabbitMQInterventionBind = true;
    }

    private void UnBindService() {
        if (isServiceRabbitMQInterventionBind) {
            unbindService(serviceConnectionIntervention);
            isServiceRabbitMQInterventionBind = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UnBindService();
    }

    private void loadFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(userCodis){
            ft.replace(R.id.list_fragment, new ListInterventionFragment());
            ft.replace(R.id.demande_fragment, new ValidationDemandeFragment());

        } else {
            ft.replace(R.id.list_fragment, new ListInterventionFragment());
        }
        ft.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Récupérer l'information de connexion Codis/Intervenant
        userCodis = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                .getBoolean("isCodis", false);

        if(userCodis){
            setContentView(R.layout.activity_new_list_intervention_codis);
        } else {
            setContentView(R.layout.activity_new_list_intervention_intervenant);
        }
        loadFragment();

        serviceConnectionIntervention = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                serviceRabbitMQIntervention = (ServiceRabbitMQIntervention) ((ServiceRabbitMQIntervention.LocalBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        BindService();

    }


}
