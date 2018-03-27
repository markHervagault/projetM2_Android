package istic.m2.ila.firefighterapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import istic.m2.ila.firefighterapp.addintervention.FragmentFormulaire;

/**
 * Created by markh on 20/03/2018.
 */

public class AddInterventionActivity extends FragmentActivity implements FragmentFormulaire.OnFragmentInteractionListener{
    private Button validateButton;
    FragmentFormulaire fragmentFormulaire;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_intervention);

        fragmentManager = getSupportFragmentManager();
        fragmentFormulaire = (FragmentFormulaire) fragmentManager.findFragmentById(R.id.fragmentFormulaire);

        validateButton = findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                creationIntervention();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void creationIntervention(){
        //TODO PopUp de confirmation
        this.fragmentFormulaire.getBundle();
        //TODO getBundle pour les moyens
    }
}
