package istic.m2.ila.firefighterapp.activitiy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import istic.m2.ila.firefighterapp.R;

public class NewListInterventionActivity extends AppCompatActivity {
    Boolean userCodis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_list_intervention);

        // Récupérer l'information de connexion Codis/Intervenant
        userCodis = getSharedPreferences("user", getApplicationContext().MODE_PRIVATE)
                .getBoolean("isCodis", false);
    }
}
